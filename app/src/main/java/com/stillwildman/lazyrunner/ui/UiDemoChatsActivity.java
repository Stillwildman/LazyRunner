package com.stillwildman.lazyrunner.ui;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.stillwildman.lazyrunner.R;
import com.stillwildman.lazyrunner.adapter.DemoChatListAdapter;
import com.stillwildman.lazyrunner.model.ItemsFireChats;
import com.stillwildman.lazyrunner.model.ItemsFireUser;
import com.stillwildman.lazyrunner.utilities.DialogHelper;
import com.stillwildman.lazyrunner.utilities.Utility;

import java.util.ArrayList;

/**
 * Created by vincent.chang on 2017/5/3.
 */

public class UiDemoChatsActivity extends BaseFireActivity {

    private RecyclerView recycler;
    private EditText edit_input;
    private ImageButton btn_send;

    private FirebaseUser user;
    private DemoChatListAdapter chatsAdapter;

    private ChildEventListener eventListener;

    private boolean isInitialAdded;
    private Handler initialHandler;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_demo;
    }

    @Override
    protected void init() {
        super.init();
        DialogHelper.showLoadingDialog(this);
        // TODO For the convenience of demonstrate
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    protected void findViews() {
        edit_input = (EditText) findViewById(R.id.edit_inputMessage);
        btn_send = (ImageButton) findViewById(R.id.btn_sendMessage);
    }

    @Override
    protected void setListener() {
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!edit_input.getText().toString().isEmpty())
                    sendMessageToFirebase(edit_input.getText().toString());
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    protected void onUserSignedIn(FirebaseUser user) {
        this.user = user;
        checkUserExists();
    }

    @Override
    protected void onUserSignedOut() {

    }

    private void checkUserExists() {
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i(TAG, "onDataChanged! UserReference size: " + dataSnapshot.getChildrenCount());

                if (!dataSnapshot.hasChild(user.getUid()))
                    setUserData(user);
                else {
                    initChatsList();
                    Log.i(TAG, "User exists! " + user.getDisplayName() + "\n" + user.getUid());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "EventOnCancelled:\n", databaseError.toException());
            }
        };

        getUserReference().addListenerForSingleValueEvent(eventListener);
    }

    private void setUserData(FirebaseUser user) {
        String photoUrl = "";
        if (user.getPhotoUrl() != null)
            photoUrl = user.getPhotoUrl().toString();

        ItemsFireUser userItem = new ItemsFireUser(user.getDisplayName(), user.getEmail(), photoUrl);

        getUserReference().child(user.getUid()).setValue(userItem).addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Utility.toastShort("User added!");
                    initChatsList();
                    Log.i(TAG, "User Added!");
                }
                else
                    Log.e(TAG, "User Add failed!");
            }
        });
    }

    private void initChatsList() {
        if (recycler == null) {
            recycler = (RecyclerView) findViewById(R.id.recycler_demo);
            recycler.setLayoutManager(new LinearLayoutManager(this));
        }

        if (chatsAdapter == null) {
            chatsAdapter = new DemoChatListAdapter(this, new ArrayList<ItemsFireChats>(), user.getUid());

            setChatsEventListener();

            recycler.setAdapter(chatsAdapter);

            recycler.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                @Override
                public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                    if (bottom < oldBottom) {
                        recycler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                recycler.smoothScrollToPosition(chatsAdapter.getLastPosition());
                            }
                        }, 100);
                    }
                }
            });
        }
    }

    private void setChatsEventListener() {
        if (eventListener == null) {
            eventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                    ItemsFireChats chatsItem = dataSnapshot.getValue(ItemsFireChats.class);
                    Log.i(TAG, "onChildAdded!\nName: " + chatsItem.name + "\nMessage: " + chatsItem.message);

                    onNewChatsAdded(chatsItem);
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                    ItemsFireChats chatsItem = dataSnapshot.getValue(ItemsFireChats.class);
                    Log.i(TAG, "onChildChanged!\nName: " + chatsItem.name + "\nMessage: " + chatsItem.message);
                    chatsAdapter.updateContent(chatsItem.key, chatsItem.message);
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    ItemsFireChats chatsItem = dataSnapshot.getValue(ItemsFireChats.class);
                    Log.i(TAG, "onChildRemoved!\nMessage: " + chatsItem.message + "\nKey: " + chatsItem.key);
                    chatsAdapter.removeItem(chatsItem.key);
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.w(TAG, "EventOnCancelled:\n", databaseError.toException());
                }
            };
            isInitialAdded = true;
            getChatReference().addChildEventListener(eventListener);
        }
    }

    private void onNewChatsAdded(ItemsFireChats chatsItem) {
        chatsAdapter.addChatsItem(chatsItem);

        int lastVisible = ((LinearLayoutManager) recycler.getLayoutManager()).findLastCompletelyVisibleItemPosition();

        if (isInitialAdded) {
            firstAdded();
        }
        else if (!chatsItem.uid.equals(user.getUid())) {
            if (chatsAdapter.getLastPosition() - 1 > lastVisible)
                Utility.toastShort(chatsItem.name + " : " + chatsItem.message);
            else
                recycler.smoothScrollToPosition(chatsAdapter.getLastPosition());
        }
    }

    private void firstAdded() {
        if (initialHandler == null) {
            initialHandler = new Handler();
            initialHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    isInitialAdded = false;
                    btn_send.setClickable(true);
                    DialogHelper.dismissDialog();
                    recycler.scrollToPosition(chatsAdapter.getLastPosition());
                    initialHandler = null;
                }
            }, 1000);
        }
    }

    private void sendMessageToFirebase(String message) {
        edit_input.setText("");

        String photoUrl = "";
        if (user.getPhotoUrl() != null)
            photoUrl = user.getPhotoUrl().toString();

        final String key = getChatReference().push().getKey();

        ItemsFireChats chatsItem = new ItemsFireChats(user.getUid(), user.getDisplayName(), message, photoUrl, System.currentTimeMillis(), key);

        chatsAdapter.addChatsItem(chatsItem, key);
        recycler.smoothScrollToPosition(chatsAdapter.getLastPosition());

        getChatReference().child(key).setValue(chatsItem).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                    chatsAdapter.updateTimestamp(System.currentTimeMillis(), key);
            }
        });
    }

    public void removeMessageFromFirebase(String key) {
        getChatReference().child(key).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Utility.toastShort(getString(R.string.delete_completed));
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (eventListener != null)
            getChatReference().removeEventListener(eventListener);
    }
}
