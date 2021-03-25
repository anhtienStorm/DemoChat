package com.bkav.demochat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class HomeActivity extends AppCompatActivity {

    public static String SHAREPREFENCE = "INFOR_ACCOUNT";
    public static final int MSG_LOAD_CHAT_LIST = 1;
    public static final int MSG_LOAD_LIST_USER_ONLINE = 2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);

        BottomNavigationView navView = findViewById(R.id.navigation_bar);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(R.id.navigation_chat,
                R.id.navigation_list_user)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        androidx.appcompat.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(actionBar.getDisplayOptions()
                | androidx.appcompat.app.ActionBar.DISPLAY_SHOW_CUSTOM);
        ImageView imageView = new ImageView(actionBar.getThemedContext());
        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        imageView.setImageResource(R.drawable.ic_avatar);
        androidx.appcompat.app.ActionBar.LayoutParams layoutParams = new androidx.appcompat.app.ActionBar.LayoutParams(
                androidx.appcompat.app.ActionBar.LayoutParams.WRAP_CONTENT,
                androidx.appcompat.app.ActionBar.LayoutParams.WRAP_CONTENT, Gravity.RIGHT
                | Gravity.CENTER_VERTICAL);
        layoutParams.rightMargin = 40;
        layoutParams.width = 100;
        layoutParams.height = 100;
        imageView.setLayoutParams(layoutParams);
        actionBar.setCustomView(imageView);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(intent);
            }
        });
        Intent intent = getIntent();
        String ID = intent.getStringExtra("ID");
        String name = intent.getStringExtra("NAME");
        String email = intent.getStringExtra("EMAIL");
        SharedPreferences sharedPref =  getSharedPreferences(SHAREPREFENCE,  MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("id", ID);
        editor.putString("name", name);
        editor.putString("email", email);
        editor.commit();

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
                   String mJsonData = response.body().string();
                   Log.d("TienNVh", "onDestroy: "+mJsonData);;
                }
            }
        };
        SharedPreferences sharedPref = getBaseContext().getSharedPreferences(HomeActivity.SHAREPREFENCE, getApplication().MODE_PRIVATE);
        String path = "/destroyapp/"+sharedPref.getString("id",null);
        RequestToServer.get( path, callback);

    }
}
