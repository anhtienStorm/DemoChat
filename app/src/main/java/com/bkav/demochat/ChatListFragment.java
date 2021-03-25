package com.bkav.demochat;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ChatListFragment extends ListFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mItemResource = R.layout.user_item_view;
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        initUserList();
        mChatListAdapter.updateChatList(mUserList);
    }

    private void initUserList(){
        mUserList.clear();
        Callback callback = new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.isSuccessful()){
                            try {
                                String jsonData = response.body().string();
                                JSONObject Jobject = new JSONObject(jsonData);
                                JSONArray jsonArray = Jobject.getJSONArray("content");
                                for (int i=0;i<jsonArray.length();i++){
                                    JSONObject Jobject1 =jsonArray.getJSONObject(i);
                                    int id = Jobject1.getInt("id");
                                    String username = Jobject1.getString("name");
                                    String lastmessenger = Jobject1.getString("content");
                                    Log.d("TienNVh", "run: "+Jobject1.getInt("status"));
                                    boolean status = /*Jobject1.getInt("status") == 1 ? true:*/ false;
                                    User user = new User(id,username,lastmessenger, status );
                                    mUserList.add(user);
                                }
                                mChatListAdapter.updateChatList(mUserList);
                            } catch (JSONException | IOException e) {
                                Toast.makeText(getContext(), e +"//", Toast.LENGTH_LONG).show();
                                e.printStackTrace();
                            }


                        }
                    }
                });
            }
        };
        SharedPreferences sharedPref = getContext().getSharedPreferences(HomeActivity.SHAREPREFENCE, getContext().MODE_PRIVATE);
        String path = "/getlistaccountrecently/"+sharedPref.getString("id",null);
        RequestToServer.get( path, callback);
    }
}
