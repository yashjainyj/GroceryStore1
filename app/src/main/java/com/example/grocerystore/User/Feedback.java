package com.example.grocerystore.User;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import com.example.grocerystore.MainActivity;
import com.example.grocerystore.R;
import com.google.android.material.textfield.TextInputEditText;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Feedback extends AppCompatActivity {
    Button send,call;
     RatingBar ratingBar;
     TextInputEditText textInputEditText;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedback);
        send = findViewById(R.id.btnSubmit);
        call = findViewById(R.id.calll);
        textInputEditText = findViewById(R.id.feedback);
        ratingBar = findViewById(R.id.ratingBar);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_SEND);
                intent.setData(Uri.parse("mailto:"));
                intent.setType("message/rfc822");
                intent.putExtra(Intent.EXTRA_EMAIL,new String[]{"asifofficial10@gmail.com"});
                intent.putExtra(Intent.EXTRA_SUBJECT,"Query");
                intent.putExtra(Intent.EXTRA_TEXT,textInputEditText.getText().toString() + "\nRaiting " +  ratingBar.getRating());
                startActivity(intent.createChooser(intent,"Click One of this to Send"));
            }
        });
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:8368149754"));
                startActivity(intent);
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Feedback.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
