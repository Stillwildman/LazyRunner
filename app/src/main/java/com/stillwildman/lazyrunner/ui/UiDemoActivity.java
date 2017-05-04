package com.stillwildman.lazyrunner.ui;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.stillwildman.lazyrunner.R;
import com.stillwildman.lazyrunner.model.ItemsFireUser;
import com.stillwildman.lazyrunner.utilities.Utility;

/**
 * Created by vincent.chang on 2017/5/3.
 */

public class UiDemoActivity extends BaseFireActivity {

    private RecyclerView recycler;
    private EditText edit_input;
    private ImageButton btn_send;

    private ValueEventListener eventListener;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_demo;
    }

    @Override
    protected void findViews() {
        recycler = (RecyclerView) findViewById(R.id.recycler_demo);
        edit_input = (EditText) findViewById(R.id.edit_inputMessage);
        btn_send = (ImageButton) findViewById(R.id.btn_sendMessage);
    }

    @Override
    protected void setListener() {

    }

    @Override
    public void onStop() {
        super.onStop();
        if (eventListener != null)
            getUserReference().removeEventListener(eventListener);
    }

    @Override
    protected void onUserSignedIn(FirebaseUser user) {
        setUserEventListener(user);
    }

    @Override
    protected void onUserSignedOut() {

    }

    private void setUserEventListener(final FirebaseUser user) {
        if (eventListener == null) {
            eventListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Log.i(TAG, "onDataChanged! dataSnapshotSize: " + dataSnapshot.getChildrenCount());

                    if (!dataSnapshot.hasChild(user.getUid()))
                        setUserData(user);
                    else
                        Log.i(TAG, "User exists! " + user.getDisplayName() + "\n" + user.getUid());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.w(TAG, "EventOnCancelled:\n", databaseError.toException());
                }
            };
        }

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
                    Log.i(TAG, "User Added!");
                }
            }
        });
    }
}
