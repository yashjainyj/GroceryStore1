package com.example.grocerystore.User;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.grocerystore.MainActivity;
import com.example.grocerystore.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class LoginVerified extends AppCompatActivity {
    EditText code;
    private String verficationId;
    FirebaseAuth mAuth;
    ProgressBar progressBar;
    TextView textView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_verified);
        Intent intent = getIntent();
        String number = intent.getStringExtra("phone");
        sendVerificationCode(number);
        code = findViewById(R.id.code);
        mAuth = FirebaseAuth.getInstance();
        textView = findViewById(R.id.message);
        textView.setText(textView.getText() + " "+number);
        progressBar = findViewById(R.id.progress_bar);
        findViewById(R.id.codesubmit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code1 = code.getText().toString();
                verifyCode(code1);
                progressBar.setVisibility(View.GONE);
            }
        });

    }

    private void verifyCode(String code){
      PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(verficationId,code);
      signInWithCredential(phoneAuthCredential);
    }

    private void signInWithCredential(PhoneAuthCredential phoneAuthCredential) {
        mAuth.signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    Intent intent = new Intent(LoginVerified.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    progressBar.setVisibility(View.GONE);
                }else
                {
                    Toast.makeText(LoginVerified.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    Log.d("complete", "onComplete: ----------------->" + task.getException().getMessage());
                }
            }
        });
    }

    private void sendVerificationCode(String number){
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,
                60,
                TimeUnit.SECONDS,
                LoginVerified.this,
                mCallbacks);
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verficationId =s;
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String code1 = phoneAuthCredential.getSmsCode();
            if (code1!=null)
            {
                code.setText(code1);
                verifyCode(code1);
                //signInWithCredential(phoneAuthCredential);
               progressBar.setVisibility(View.GONE);

            }

        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(LoginVerified.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.d("complete", "onComplete: ----------------->" + e.getMessage());
        }
    };
}
