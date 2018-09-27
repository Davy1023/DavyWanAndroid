package com.davy.davy_wanandroid.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.davy.davy_wanandroid.R;
import com.davy.davy_wanandroid.ui.main.MainActivity;

public class SplashActivity extends AppCompatActivity {
    private Handler handler = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
       handler.postDelayed(mRunnable,2000);
    }


 Runnable mRunnable =new Runnable() {
     @Override
     public void run() {
        if(!isFinishing()){
            goMain();
            finish();
        }
     }
 };

    private void goMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}