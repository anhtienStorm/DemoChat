package com.bkav.demochat;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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

import static com.bkav.demochat.HomeActivity.MSG_LOAD_LIST_USER_ONLINE;

public class ListUserFragment extends ListFragment {

    private Handler mHandler;
    private String mJsonData;
    private boolean mLoop;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mItemResource = R.layout.user_online_item_view;
        mHandler = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch (msg.what){
                    case MSG_LOAD_LIST_USER_ONLINE:
                        try {
                            ArrayList<User> users = new ArrayList<>();
                            JSONObject Jobject = new JSONObject(mJsonData);
                            JSONArray jsonArray = Jobject.getJSONArray("content");
                            for (int i=0;i<jsonArray.length();i++){
                                JSONObject Jobject1 =jsonArray.getJSONObject(i);
                                int id = Jobject1.getInt("id_account");
                                String username = Jobject1.getString("name_account");
                                String lastmessenger = "";
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
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()){
                    mJsonData = response.body().string();
                    mHandler.sendEmptyMessage(MSG_LOAD_LIST_USER_ONLINE);
                }
            }
        };
        String path = "/getaccountactive";
        RequestToServer.get( path, callback);
    }

    @Override
    public void onStop() {
        super.onStop();
        mLoop = false;
    }
}
