package com.bkav.demochat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ChatActivity extends AppCompatActivity {

    private static final String NAME_USER_CHAT = "name_user_chat";
    private static final String ID_RECEIVER = "id_receiver";
    private static final int MSG_MESSAGE = 5;

    private EditText mContentMessageSend;
    private ImageView mButtonSend;
    private RecyclerView mMessageRecyclerView;
    private MessageListAdapter mMessageListAdapter;
    private ArrayList<Message> mMessageList;
    private int mIdReceiver;
    private Handler mHandler;
    private String mJsonData;

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
                    insertChatServer(mContentMessageSend.getText().toString());
                    mContentMessageSend.setText("");
                }
            }
        });

        mHandler = new Handler(){
            @Override
            public void handleMessage(@NonNull android.os.Message msg) {
                switch (msg.what){
                    case MSG_MESSAGE:
                        try {
                            JSONObject Jobject = new JSONObject(mJsonData);
                            JSONArray jsonArray = Jobject.getJSONArray("content");
                            for (int i=0;i<jsonArray.length();i++){
                                JSONObject Jobject1 =jsonArray.getJSONObject(i);
                                int idSender =Integer.parseInt(Jobject1.getString("id_sender"));
                                int idRevicer =Integer.parseInt(Jobject1.getString("id_reciver"));
                                String content = Jobject1.getString("content");
                                String date = Jobject1.getString("date");
                                Message message = new Message(idSender, idRevicer, content, date);
                                mMessageList.add(message);
                            }
                            mMessageListAdapter.updateMessageList(mMessageList);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                }
            }
        };
    }

    public  void insertChatServer(String contnet){
        Callback callback = new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()){
                    try {
                        String jsonData = response.body().string();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        JSONObject jsonObject = new JSONObject();
        try {
            SharedPreferences sharedPref = getBaseContext().getSharedPreferences(HomeActivity.SHAREPREFENCE, getBaseContext().MODE_PRIVATE);
            jsonObject.put("id_sender", sharedPref.getString("id",null));
            jsonObject.put("id_reciver", mIdReceiver);
            jsonObject.put("content",contnet);
            String path ="chat";
            RequestToServer.post(path, jsonObject, callback);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void initMessageList(){
        Callback callback = new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()){
                    try {
                        mJsonData = response.body().string();
                        mHandler.sendEmptyMessage(MSG_MESSAGE);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        JSONObject jsonObject = new JSONObject();
        try {
            SharedPreferences sharedPref = getBaseContext().getSharedPreferences(HomeActivity.SHAREPREFENCE, getBaseContext().MODE_PRIVATE);
            jsonObject.put("id_sender", sharedPref.getString("id", null));
            jsonObject.put("id_reciver", mIdReceiver);
            String path ="listchat";
            RequestToServer.post(path, jsonObject, callback);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getDateTimeCurrent(){
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm dd/MM/yyyy");
        return sdf.format(new Date());
    }

    @Override
    protected void onStop() {
        super.onStop();
        Callback callback = new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()){
                    mJsonData = response.body().string();
                    Log.d("TienNVh", "onStop: "+mJsonData);;
                }
            }
        };
        SharedPreferences sharedPref = getBaseContext().getSharedPreferences(HomeActivity.SHAREPREFENCE, getApplication().MODE_PRIVATE);
        String path = "/destroyapp/"+sharedPref.getString("id",null);
        RequestToServer.get( path, callback);

    }
}
