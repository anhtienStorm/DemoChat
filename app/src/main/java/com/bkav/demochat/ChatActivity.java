package com.bkav.demochat;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ChatActivity extends AppCompatActivity {

    private static final String NAME_USER_CHAT = "name_user_chat";
    private static final String ID_RECEIVER = "id_receiver";

    private EditText mContentMessageSend;
    private ImageView mButtonSend;
    private RecyclerView mMessageRecyclerView;
    private MessageListAdapter mMessageListAdapter;
    private ArrayList<Message> mMessageList;
    private int mIdReceiver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_activity);

        Intent intent = getIntent();
        setTitle(intent.getStringExtra(NAME_USER_CHAT));
        mIdReceiver = intent.getIntExtra(ID_RECEIVER, -1);

        mMessageList = new ArrayList<>();
        mMessageRecyclerView = findViewById(R.id.list_message);
        mContentMessageSend = findViewById(R.id.input_message);
        mButtonSend = findViewById(R.id.send_message);

        initMessageList();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        mMessageListAdapter = new MessageListAdapter(getApplicationContext(), mIdReceiver);
        mMessageRecyclerView.setLayoutManager(linearLayoutManager);
        mMessageRecyclerView.setAdapter(mMessageListAdapter);
        mMessageListAdapter.updateMessageList(mMessageList);
        mMessageRecyclerView.smoothScrollToPosition(mMessageList.size());

        mButtonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(mContentMessageSend.getText())){
                    Message message = new Message(1, mIdReceiver, mContentMessageSend.getText().toString(), getDateTimeCurrent());
                    mMessageList.add(message);
                    mMessageListAdapter.updateMessageList(mMessageList);
                    mMessageRecyclerView.smoothScrollToPosition(mMessageList.size());
                    mContentMessageSend.setText("");
                }
            }
        });
    }

    public void initMessageList(){
        Message m1, m2, m3;
        m1 = new Message(1,2,"abc","19:30 18/03/2021");
        m2 = new Message(2,1,"aawgawegasdgawegwgawagbc","20:10 18/03/2021");
        m3 = new Message(1,2,"aagwegasdgwagwgewbc","22:45 18/03/2021");

        mMessageList.add(m1);
        mMessageList.add(m2);
        mMessageList.add(m3);

    }

    public String getDateTimeCurrent(){
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm dd/MM/yyyy");
        return sdf.format(new Date());
    }
}
