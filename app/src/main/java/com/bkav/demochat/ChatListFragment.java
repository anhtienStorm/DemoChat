package com.bkav.demochat;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.bkav.demochat.HomeActivity.MSG_LOAD_CHAT_LIST;

public class ChatListFragment extends ListFragment {

    private Handler mHandler;
    private String mJsonData;
    private boolean mLoop;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mItemResource = R.layout.user_item_view;

        mHandler = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch (msg.what){
                    case MSG_LOAD_CHAT_LIST:
                        try {
                            ArrayList<User> users = new ArrayList<>();
                            JSONObject Jobject = new JSONObject(mJsonData);
                            JSONArray jsonArray = Jobject.getJSONArray("content");
                            for (int i=0;i<jsonArray.length();i++){
                                JSONObject Jobject1 =jsonArray.getJSONObject(i);
                                int id = Jobject1.getInt("id");
                                String username = Jobject1.getString("name");
                                String lastmessenger = Jobject1.getString("content");
                                boolean status = Jobject1.getInt("status")==1 ? true: false;
                                User user = new User(id,username,lastmessenger, status );
                                users.add(user);
                            }
                            mUserList = users;
                            mChatListAdapter.updateChatList(mUserList);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                }
            }
        };

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        mLoop = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (mLoop){
                    try {
                        initUserList();
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    private void initUserList(){
        Callback callback = new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                if (response.isSuccessful()){
                    try {
                        mJsonData = response.body().string();
                        mHandler.sendEmptyMessage(MSG_LOAD_CHAT_LIST);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        SharedPreferences sharedPref = getContext().getSharedPreferences(HomeActivity.SHAREPREFENCE, getContext().MODE_PRIVATE);
        String path = "/getlistaccountrecently/"+sharedPref.getString("id",null);
        RequestToServer.get( path, callback);
    }

    @Override
    public void onStop() {
        super.onStop();
        mLoop = false;
    }
}
