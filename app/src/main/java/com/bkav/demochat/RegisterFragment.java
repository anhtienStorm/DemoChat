package com.bkav.demochat;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class RegisterFragment extends Fragment{

    EditText mUsername, mPassword, mName, mREPassword, mEmail;
    Button mBTRegister;
    private Handler mHandler;
    private String mJsonData;
    private static final int MSG_REGISTER = 6;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.register_fragment, container, false);
        initView(root);

        mHandler =  new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch (msg.what){
                    case MSG_REGISTER:
                         getActivity().onBackPressed();
                        Toast.makeText(getContext(), "Đăng ký thành công ",Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };

        mBTRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pass = mPassword.getText().toString();
                String repass = mREPassword.getText().toString();
                String username = mUsername.getText().toString();
                String email = mEmail.getText().toString();
                String name = mName.getText().toString();
                if(pass.equals(repass)){
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
                                    if(mJsonData.equals("True"))
                                       mHandler.sendEmptyMessage(MSG_REGISTER);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    };
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("name", name);
                        jsonObject.put("email", email);
                        jsonObject.put("username", username);
                        jsonObject.put("password",pass);
                        String path ="register";
                        RequestToServer.post(path, jsonObject, callback);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        return root;
    }

    public  void  initView(View v){
        mUsername = v.findViewById(R.id.username_register);
        mPassword = v.findViewById(R.id.password_register);
        mEmail = v.findViewById(R.id.email);
        mREPassword = v.findViewById(R.id.re_password_register);
        mName = v.findViewById(R.id.name_register);
        mBTRegister = v.findViewById(R.id.register_confirm);
    }


}
