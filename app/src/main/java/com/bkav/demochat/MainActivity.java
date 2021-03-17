package com.bkav.demochat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    Fragment mLoginFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLoginFragment = new LoginFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container_fragment, mLoginFragment).commit();
    }
}