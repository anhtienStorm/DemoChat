package com.bkav.demochat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {

    private Button mLogout;
    private TextView mEmail, mName;
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
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
    }
}
