package com.bkav.demochat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

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
                    insertChatServer(mContentMessageSend.getText().toString());
                    mContentMessageSend.setText("");
                }
            }
        });
            new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i=1;;i++){
                    try {
                        mMessageList.clear();
                        initMessageList();
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public  void insertChatServer(String contnet){
        Callback callback = new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.isSuccessful()){
                            try {
                                String jsonData = response.body().string();
                                Log.d("TienNVh", "run: "+jsonData);
                            } catch (IOException e) {
                                Toast.makeText(getBaseContext(), e +"//", Toast.LENGTH_LONG).show();
                                e.printStackTrace();
                            }

                        }
                    }
                });
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
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.isSuccessful()){
                            Log.d("TienNVh", "hello: ");
                            try {
                                String jsonData = response.body().string();
                                JSONObject Jobject = new JSONObject(jsonData);
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
                            } catch (JSONException | IOException e) {
                                Toast.makeText(getBaseContext(), e +"//", Toast.LENGTH_LONG).show();
                                e.printStackTrace();
                            }


                        }
                    }
                });
            }
        };
        JSONObject jsonObject = new JSONObject();
        try {
            SharedPreferences sharedPref = getBaseContext().getSharedPreferences(HomeActivity.SHAREPREFENCE, getBaseContext().MODE_PRIVATE);
            jsonObject.put("id_sender", 2405);
            jsonObject.put("id_reciver", 2413);
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
}
