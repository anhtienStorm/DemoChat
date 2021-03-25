package com.bkav.demochat;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListViewHolder> {

    private static final String NAME_USER_CHAT = "name_user_chat";
    private static final String ID_RECEIVER = "id_receiver";

    Context mContext;
    ArrayList<User> mUserList;
    int mItemResource;

    public ChatListAdapter(Context context, int itemResource){
        mContext = context;
        mItemResource = itemResource;
    }

    public void updateChatList(ArrayList<User> users){
        mUserList = users;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ChatListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View userItemView = inflater.inflate(mItemResource, parent, false);
        return new ChatListViewHolder(userItemView, mContext);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatListViewHolder holder, int position) {
        holder.bindView(mUserList.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ChatActivity.class);
                intent.putExtra(NAME_USER_CHAT, mUserList.get(position).getUsername());
                intent.putExtra(ID_RECEIVER, mUserList.get(position).getId());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mUserList != null ? mUserList.size() : 0;
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
