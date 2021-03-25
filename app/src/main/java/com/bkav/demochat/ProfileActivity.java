package com.bkav.demochat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.bkav.demochat.HomeActivity.MSG_LOAD_LIST_USER_ONLINE;

public class ProfileActivity extends AppCompatActivity {

    private Button mLogout;
    private TextView mEmail, mName;
    private Handler mHandler;
    private String mJsonData;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_layout);
        mEmail = findViewById(R.id.content_email);
        mName = findViewById(R.id.name_user);
        mLogout = findViewById(R.id.logout);
        SharedPreferences sharedPref = getBaseContext().getSharedPreferences(HomeActivity.SHAREPREFENCE, getBaseContext().MODE_PRIVATE);
        mName.setText(sharedPref.getString("name", "name"));
        mEmail.setText(sharedPref.getString("email","Email"));
        mLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mHandler = new Handler(){
                    @Override
                    public void handleMessage(@NonNull Message msg) {
                        switch (msg.what){
                            case MSG_LOAD_LIST_USER_ONLINE:
                                SharedPreferences mySPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                                SharedPreferences.Editor editor = mySPrefs.edit();
                                editor.remove("id");
                                editor.remove("name");
                                editor.remove("email");
                                editor.apply();
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                break;
                        }
                    }
                };
                Callback callback = new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        if (response.isSuccessful()){
                            mJsonData = response.body().string();
                            if(mJsonData.equals("True"))
                                 mHandler.sendEmptyMessage(MSG_LOAD_LIST_USER_ONLINE);
                        }
                    }
                };
                SharedPreferences sharedPref = getBaseContext().getSharedPreferences(HomeActivity.SHAREPREFENCE, getApplication().MODE_PRIVATE);
                String path = "/logout/"+sharedPref.getString("id",null);
                RequestToServer.get( path, callback);

            }
        });
    }
}
