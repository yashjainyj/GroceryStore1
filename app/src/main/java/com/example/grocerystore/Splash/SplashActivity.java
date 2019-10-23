package com.example.grocerystore.Splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.grocerystore.Location.GetLocation;
import com.example.grocerystore.MainActivity;
import com.example.grocerystore.R;

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i=new Intent(SplashActivity.this, GetLocation.class);
                startActivity(i);
                finish();
            }
        }, 2500);
    }
}
