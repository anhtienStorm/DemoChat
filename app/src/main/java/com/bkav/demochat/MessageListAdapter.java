package com.bkav.demochat;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MessageListAdapter extends RecyclerView.Adapter<MessageListViewHolder>{

    Context mContext;
    ArrayList<Message> mMessageList;
    int mIdReceiver;
    int mPositionClick = -1;

    public MessageListAdapter(Context context, int idReceivier){
        mContext = context;
        mIdReceiver = idReceivier;
    }

    public void updateMessageList(ArrayList<Message> messages){
        mMessageList = messages;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MessageListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View messageItemView = inflater.inflate(R.layout.message_item_view, parent, false);
        return new MessageListViewHolder(messageItemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageListViewHolder holder, int position) {
        if (mMessageList.get(position).getIdReceiver() == mIdReceiver){
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) holder.mItemLayout.getLayoutParams();
            params.gravity = Gravity.END;
            holder.mItemLayout.setLayoutParams(params);

            params = (LinearLayout.LayoutParams) holder.mContentMessage.getLayoutParams();
            params.gravity = Gravity.END;
            holder.mContentMessage.setLayoutParams(params);

            params = (LinearLayout.LayoutParams) holder.mTimeMessage.getLayoutParams();
            params.gravity = Gravity.END;
            holder.mTimeMessage.setLayoutParams(params);
        } else {
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) holder.mItemLayout.getLayoutParams();
            params.gravity = Gravity.START;
            holder.mItemLayout.setLayoutParams(params);

            params = (LinearLayout.LayoutParams) holder.mContentMessage.getLayoutParams();
            params.gravity = Gravity.START;
            holder.mContentMessage.setLayoutParams(params);

            params = (LinearLayout.LayoutParams) holder.mTimeMessage.getLayoutParams();
            params.gravity = Gravity.START;
            holder.mTimeMessage.setLayoutParams(params);

            holder.mContentMessage.setBackground(mContext.getResources().getDrawable(R.drawable.background_message_item_receiver));
        }

        holder.bindView(mMessageList.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPositionClick = position;
                holder.mTimeMessage.setVisibility(holder.mTimeMessage.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }
}

class MessageListViewHolder extends RecyclerView.ViewHolder{

    public LinearLayout mItemLayout;
    public TextView mContentMessage, mTimeMessage;

    public MessageListViewHolder(@NonNull View itemView) {
        super(itemView);
        mItemLayout = itemView.findViewById(R.id.item_layout);
        mContentMessage = itemView.findViewById(R.id.content_message);
        mTimeMessage = itemView.findViewById(R.id.time_message);
    }

    public void bindView(Message message){
        mContentMessage.setText(message.getContent());
        mTimeMessage.setText(message.getDate());
    }
}
