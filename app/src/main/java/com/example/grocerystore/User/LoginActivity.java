package com.example.grocerystore.User;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.grocerystore.MainActivity;
import com.example.grocerystore.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.truecaller.android.sdk.ITrueCallback;
import com.truecaller.android.sdk.TrueError;
import com.truecaller.android.sdk.TrueProfile;
import com.truecaller.android.sdk.TrueSDK;
import com.truecaller.android.sdk.TrueSdkScope;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import static android.content.ContentValues.TAG;

public class LoginActivity extends AppCompatActivity {
    EditText email;
    Button submit,mobile;
    FirebaseAuth mAuth;
    TrueSdkScope trueScope;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        FirebaseApp.initializeApp(LoginActivity.this);
        email = findViewById(R.id.number);
        submit= findViewById(R.id.submit);
        mobile = findViewById(R.id.phone);
        mAuth = FirebaseAuth.getInstance();
        trueScope = new TrueSdkScope.Builder(this, sdkCallback)
                .consentMode(TrueSdkScope.SDK_CONSENT_TITLE_LOG_IN)
                .consentTitleOption(TrueSdkScope.SDK_CONSENT_TITLE_VERIFY)
                .footerType(TrueSdkScope.FOOTER_TYPE_SKIP)
                .build();
        TrueSDK.init(trueScope);
        if(!TrueSDK.getInstance().isUsable())
        {
            mobile.setVisibility(View.INVISIBLE);
        }
        mobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TrueSDK.getInstance().isUsable()) {
                    Locale locale = new Locale("en");
                    TrueSDK.getInstance().setLocale(locale);
                    TrueSDK.getInstance().getUserProfile(LoginActivity.this);
                } else {
                    Toast.makeText(LoginActivity.this, "TrueCaller is not installed in your Phone", Toast.LENGTH_SHORT).show();
                }

            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String phone = "+91" + email.getText().toString();
                Intent intent = new Intent(LoginActivity.this,LoginVerified.class);
                intent.putExtra("phone",phone);
                startActivity(intent);
                finish();
                //
//                mAuth.createUserWithEmailAndPassword(email.getText().toString(),pass.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
//                    @Override
//                    public void onSuccess(AuthResult authResult) {
//                        Toast.makeText(LoginActivity.this, "User Created", Toast.LENGTH_SHORT).show();
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(LoginActivity.this, "No", Toast.LENGTH_SHORT).show();
//                    }
//                });
            }
        });
    }
    private final ITrueCallback sdkCallback = new ITrueCallback() {
        @Override
        public void onSuccessProfileShared(@NonNull TrueProfile trueProfile) {
             mAuth.createUserWithEmailAndPassword(trueProfile.email,trueProfile.phoneNumber)
                     .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                         @Override
                         public void onComplete(@NonNull Task<AuthResult> task) {
                             if (task.isSuccessful())
                             {
                                 Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                                 startActivity(intent);
                                 finishAffinity();
                             }
                             else
                             {
                                 mAuth.signInWithEmailAndPassword(trueProfile.email,trueProfile.phoneNumber)
                                         .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                             @Override
                                             public void onComplete(@NonNull Task<AuthResult> task) {
                                                 Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                                                 startActivity(intent);
                                                 finishAffinity();
                                             }
                                         }).addOnFailureListener(new OnFailureListener() {
                                     @Override
                                     public void onFailure(@NonNull Exception e) {
                                         Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                     }
                                 });
                             }
                         }
                     }).addOnFailureListener(new OnFailureListener() {
                 @Override
                 public void onFailure(@NonNull Exception e) {

                 }
             });
        }

        @Override
        public void onFailureProfileShared(@NonNull TrueError trueError) {
            Log.d(TAG, "---------------------------------onFailureProfileShared: " + trueError.getErrorType());
        }

        @Override
        public void onVerificationRequired() {

        }
    };



    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        TrueSDK.getInstance().onActivityResultObtained( LoginActivity.this,resultCode, data);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser()!=null)
        {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finishAffinity();
        }
    }
}