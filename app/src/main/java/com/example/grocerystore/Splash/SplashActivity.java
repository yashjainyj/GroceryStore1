package com.example.grocerystore.Splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.grocerystore.Location.GetLocation;
import com.example.grocerystore.MainActivity;
import com.example.grocerystore.MyUtility;
import com.example.grocerystore.R;
import com.example.grocerystore.User.LoginActivity;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i=new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(i);
                    finish();
                }
        }, 2500);
    }
}
