package com.bkav.demochat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
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
import java.net.InetAddress;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;
import static com.bkav.demochat.HomeActivity.SHAREPREFENCE;

public class LoginFragment extends Fragment {

    private static final int MSG_LOGIN = 1;

    private Button mRegisterButton, mLoginButton;
    private EditText mUsername, mPassword;
    private Fragment mRegisterFragment;
    private RequestToServer mRequestToServer;
    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    private Handler mHandler;
    private String mJsonData;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.login_fragment, container, false);

        mLoginButton = root.findViewById(R.id.login);
        mRegisterButton = root.findViewById(R.id.register);
        mUsername = root.findViewById(R.id.username_login);
        mPassword = root.findViewById(R.id.password_login);

        mHandler =  new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch (msg.what){
                    case MSG_LOGIN:
                        try {
                            JSONObject Jobject = new JSONObject(mJsonData);
                            Intent intent = new Intent(getContext(), HomeActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK/* | Intent.FLAG_ACTIVITY_CLEAR_TASK*/);

                            String ID = String.valueOf(Jobject.getInt("id"));
                            String name = Jobject.getString("name");
                            String token = Jobject.getString("token");
                            String email = Jobject.getString("email");
                            String dateCreate = Jobject.getString("date_create");
                            SharedPreferences sharedPref =  getContext().getSharedPreferences(SHAREPREFENCE,  MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putString("id", ID);
                            editor.putString("name", name);
                            editor.putString("token", token);
                            editor.putString("email", email);
                            editor.putString("date_create", dateCreate);
                            editor.commit();

                            startActivity(intent);
                            Toast.makeText(getContext(), "Đăng nhập thành công :"+Jobject.getInt("id"),Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                }
            }
        };


        mRequestToServer = new RequestToServer(getActivity());

        Callback callback = new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()){
                    mJsonData = response.body().string();
                    mHandler.sendEmptyMessage(MSG_LOGIN);
                }
            }
        };

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isNetworkConnected()){
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("username", mUsername.getText().toString());
                        jsonObject.put("password",mPassword.getText().toString());
                        String path ="loginaccount";
                        RequestToServer.post(path, jsonObject, callback);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getContext(), "Not Connect Internet", Toast.LENGTH_SHORT).show();
                }

            }
        });
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRegisterFragment = new RegisterFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.slide_in, R.anim.fade_out, R.anim.fade_in, R.anim.slide_out)
                        .addToBackStack(null)
                        .replace(R.id.container_fragment, mRegisterFragment).commit();
            }
        });

        return root;
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    public boolean isInternetAvailable() {
        try {
            InetAddress ipAddr = InetAddress.getByName("google.com");
            //You can replace it with your name
            return !ipAddr.equals("");

        } catch (Exception e) {
            return false;
        }
    }
}
