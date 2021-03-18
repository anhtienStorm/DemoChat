package com.bkav.demochat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListViewHolder> {

    Context mContext;
    ArrayList<User> mUserList;
    ClickChatItemView mClickChatItemView;

    public ChatListAdapter(Context context){
        mContext = context;
    }

    public void updateChatList(ArrayList<User> users){
        mUserList = users;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ChatListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View userItemView = inflater.inflate(R.layout.user_item_view, parent, false);
        return new ChatListViewHolder(userItemView, mContext);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatListViewHolder holder, int position) {
        holder.bindView(mUserList.get(position));
    }

    @Override
    public int getItemCount() {
        return mUserList.size();
    }

    public void setClickChatItemView(){

    }

    public interface ClickChatItemView{
        void onClick();
    }
}

class ChatListViewHolder extends RecyclerView.ViewHolder {

    Context mContext;
    TextView mName, mLastmessage;
    ImageView mTick;

    public ChatListViewHolder(@NonNull View itemView, Context context) {
        super(itemView);
        mContext = context;
        mName = itemView.findViewById(R.id.name_user);
        mLastmessage = itemView.findViewById(R.id.last_message);
        mTick = itemView.findViewById(R.id.status);
    }

    public void bindView(User user){
        mName.setText(user.getUsername());
        mLastmessage.setText(user.getLastMessage());
        mTick.setVisibility(user.getStatus() ? View.VISIBLE : View.GONE);
    }
}