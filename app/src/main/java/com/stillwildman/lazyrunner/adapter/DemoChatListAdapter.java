package com.stillwildman.lazyrunner.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.stillwildman.lazyrunner.R;
import com.stillwildman.lazyrunner.model.ItemsMessage;

import java.util.ArrayList;

/**
 * Created by vincent.chang on 2017/5/4.
 */

public class DemoChatListAdapter extends RecyclerView.Adapter {

    private ArrayList<ItemsMessage> msgList;
    private String myUid;

    public DemoChatListAdapter(ArrayList<ItemsMessage> msgList, String myUid) {
        this.msgList = msgList;
        this.myUid = myUid;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    private class MyHolderForSelf extends RecyclerView.ViewHolder {

        TextView text_myMessage;
        TextView text_timestamp;

        public MyHolderForSelf(View itemView) {
            super(itemView);
            text_myMessage = (TextView) itemView.findViewById(R.id.text_chatMessage);
            text_timestamp = (TextView) itemView.findViewById(R.id.text_timestamp);
        }
    }

    private class MyHolderForOthers extends RecyclerView.ViewHolder {

        ImageView chatPhoto;
        TextView text_chatName;
        TextView text_chatMessage;
        TextView text_timestamp;

        public MyHolderForOthers(View itemView) {
            super(itemView);
            chatPhoto = (ImageView) itemView.findViewById(R.id.image_chatHead);
            text_chatName = (TextView) itemView.findViewById(R.id.text_chatName);
            text_chatMessage = (TextView) itemView.findViewById(R.id.text_chatMessage);
            text_timestamp = (TextView) itemView.findViewById(R.id.text_timestamp);
        }
    }

    @Override
    public int getItemCount() {
        return msgList.size();
    }
}
