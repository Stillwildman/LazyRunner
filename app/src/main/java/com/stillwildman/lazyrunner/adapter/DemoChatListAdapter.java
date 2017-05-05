package com.stillwildman.lazyrunner.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.stillwildman.lazyrunner.R;
import com.stillwildman.lazyrunner.model.ItemsFireChats;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by vincent.chang on 2017/5/4.
 */

public class DemoChatListAdapter extends RecyclerView.Adapter {

    private final static int CHATS_TYPE_SELF = 0;
    private final static int CHATS_TYPE_OTHERS = 1;

    private Context context;
    private ArrayList<ItemsFireChats> chatsList;
    private final String myUid;
    private HashMap<String, Boolean> myMessageKeys;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.TAIWAN);

    public DemoChatListAdapter(Context context, ArrayList<ItemsFireChats> chatsList, String myUid) {
        this.context = context;
        this.chatsList = chatsList;
        this.myUid = myUid;
        myMessageKeys = new HashMap<>();
    }

    public void addChatsItem(ItemsFireChats chatsItem, String key) {
        ItemsFireChats item = new ItemsFireChats(chatsItem.uid, chatsItem.name, chatsItem.message, chatsItem.photo_url, 0, key);
        chatsList.add(item);
        myMessageKeys.put(key, true);
        notifyItemInserted(chatsList.size() - 1);
    }

    public void addChatsItem(ItemsFireChats chatsItem) {
        if (!myMessageKeys.containsKey(chatsItem.key)) {
            chatsList.add(chatsItem);
            myMessageKeys.put(chatsItem.key, true);
            notifyItemInserted(chatsList.size() - 1);
        }
    }

    public void updateTimestamp(long timestamp, String key) {
        for (int i = 0; i < getItemCount(); i++) {
            if (chatsList.get(i).key.equals(key)) {
                chatsList.get(i).setTimestamp(timestamp);
                notifyItemChanged(i);
                break;
            }
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

        if (viewType == CHATS_TYPE_SELF) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.inflate_demo_chat_list_self, parent, false);
            return new MyHolderForSelf(view);
        }
        else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.inflate_demo_chat_list_others, parent, false);
            return new MyHolderForOthers(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MyHolderForSelf) {
            ((MyHolderForSelf) holder).text_chatMessage.setText(chatsList.get(position).message);
            if (chatsList.get(position).timestamp != 0)
                ((MyHolderForSelf) holder).text_timestamp.setText(dateFormat.format(new Date(chatsList.get(position).timestamp)));
        }
        else {
            MyHolderForOthers othersHolder = (MyHolderForOthers) holder;

            if (!chatsList.get(position).photo_url.isEmpty())
                setHeadPhoto(othersHolder.chatPhoto, chatsList.get(position).photo_url);

            othersHolder.text_chatName.setText(chatsList.get(position).name);
            othersHolder.text_chatMessage.setText(chatsList.get(position).message);
            othersHolder.text_timestamp.setText(dateFormat.format(new Date(chatsList.get(position).timestamp)));
        }
    }

    private void setHeadPhoto(ImageView imageView, String url) {
        Glide.with(context)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(imageView);
    }

    @Override
    public int getItemViewType(int position) {
        return chatsList.get(position).uid.equals(myUid) ? CHATS_TYPE_SELF : CHATS_TYPE_OTHERS;
    }

    private class MyHolderForSelf extends RecyclerView.ViewHolder {

        TextView text_chatMessage;
        TextView text_timestamp;

        MyHolderForSelf(View itemView) {
            super(itemView);
            text_chatMessage = (TextView) itemView.findViewById(R.id.text_chatMessage);
            text_timestamp = (TextView) itemView.findViewById(R.id.text_timestamp);
        }
    }

    private class MyHolderForOthers extends RecyclerView.ViewHolder {

        ImageView chatPhoto;
        TextView text_chatName;
        TextView text_chatMessage;
        TextView text_timestamp;

        MyHolderForOthers(View itemView) {
            super(itemView);
            chatPhoto = (ImageView) itemView.findViewById(R.id.image_chatHead);
            text_chatName = (TextView) itemView.findViewById(R.id.text_chatName);
            text_chatMessage = (TextView) itemView.findViewById(R.id.text_chatMessage);
            text_timestamp = (TextView) itemView.findViewById(R.id.text_timestamp);
        }
    }

    @Override
    public int getItemCount() {
        return chatsList.size();
    }

    public int getLastPosition() {
        return getItemCount() - 1;
    }
}
