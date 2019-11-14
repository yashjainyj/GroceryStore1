package com.example.grocerystore.User;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.grocerystore.Admin.Item_data_model;
import com.example.grocerystore.Main.OrderModel;
import com.example.grocerystore.MainActivity;
import com.example.grocerystore.MyUtility;
import com.example.grocerystore.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class Payment extends AppCompatActivity {
    TextInputEditText name, upi, amount, message;
    Button submit;
    final int UPI_PAYMENT = 0;
DatabaseReference databaseReference;
    String am="";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_option);
        name = findViewById(R.id.name);
        upi = findViewById(R.id.upi);
        amount = findViewById(R.id.amount);
        message = findViewById(R.id.tm);
        submit=findViewById(R.id.submit);
        Intent intent = getIntent();
        String amount1 = intent.getStringExtra("amount");
        amount.setText(amount1);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(name.getText().toString().trim())) {
                    name.setError("Required");
                } else if (TextUtils.isEmpty(upi.getText().toString().trim())) {
                    upi.setError("Required");
                } else if (TextUtils.isEmpty(amount.getText().toString().trim())) {
                    amount.setError("Required");
                } else if (TextUtils.isEmpty(message.getText().toString().trim())) {
                    message.setError("Required");
                } else {
                    payUsingUpi("Yash", "jainyash031@okicici", message.getText().toString(), amount.getText().toString());
                }
            }

            private void payUsingUpi(String name, String upi, String message, String amount) {

                String GOOGLE_PAY_PACKAGE_NAME = "com.google.android.apps.nbu.paisa.user";
                int GOOGLE_PAY_REQUEST_CODE = 123;

                Uri uri =
                        new Uri.Builder()
                                .scheme("upi")
                                .authority("pay")
                                .appendQueryParameter("pa", upi)
                                .appendQueryParameter("pn", name)
//                                .appendQueryParameter("mc", "your-merchant-code")
                                .appendQueryParameter("tn", message)
                                .appendQueryParameter("am", amount)
                                .appendQueryParameter("cu", "INR")
                                //  .appendQueryParameter("url", "your-transaction-url")
                                .build();
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(uri);
//                intent.setPackage(GOOGLE_PAY_PACKAGE_NAME);
//                startActivityForResult(intent, GOOGLE_PAY_REQUEST_CODE);
                Intent chooser = Intent.createChooser(intent, "Pay With");
                if (null != chooser.resolveActivity(getPackageManager())) {
                    startActivityForResult(chooser, UPI_PAYMENT);
                } else {
                    Toast.makeText(Payment.this, "No UPI app Found", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case UPI_PAYMENT:
                if ((resultCode == RESULT_OK) || resultCode == 11) {
                    if (data != null) {
                        String text = data.getStringExtra("response");
                        ArrayList<String> list = new ArrayList<>();
                        list.add(text);
                        upiPaymentDataOperation(list);
                    } else {
                        ArrayList<String> list = new ArrayList<>();
                        list.add("Nothing");
                        upiPaymentDataOperation(list);
                    }

                } else {

                    ArrayList<String> list = new ArrayList<>();
                    list.add("Nothing");
                    upiPaymentDataOperation(list);
                }
                break;
        }
    }

    private void upiPaymentDataOperation(ArrayList<String> data) {
        if (isConnectionAvailable(Payment.this)) {
            String str = data.get(0);
            Log.e("UPIPAY", "upiPaymentDataOperation: " + str);
            String paymentCancel = "";
            if (str == null) str = "discard";
            String status = "";
            String approvalRefNo = "";
            String response[] = str.split("&");
            for(int i = 0; i < response.length; i++) {
                String equalStr[] = response[i].split("=");
                if (equalStr.length >= 2) {
                    if (equalStr[0].toLowerCase().equals("Status".toLowerCase())) {
                        status = equalStr[1].toLowerCase();
                    } else if (equalStr[0].toLowerCase().equals("ApprovalRefNo".toLowerCase()) || equalStr[0].toLowerCase().equals("txnRef".toLowerCase())) {
                        approvalRefNo = equalStr[1];
                    }
                } else {
                    paymentCancel = "Payment cancelled by user.";
                }
            }
            if (status.equals("success")) {
                //Code to handle successful transaction here.
                Toast.makeText(Payment.this, "Transaction successful.", Toast.LENGTH_SHORT).show();
                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                databaseReference = FirebaseDatabase.getInstance().getReference().child(firebaseAuth.getCurrentUser().getUid()).child("Cart");
                List<String> list = new ArrayList<>();
                String finalApprovalRefNo = approvalRefNo;
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        list.clear();
                        for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                        {
                            String s = dataSnapshot1.getKey();
                            list.add(s);
                        }
                        databaseReference = FirebaseDatabase.getInstance().getReference();
                        for (String itemId : list)
                        {

                            DocumentReference documentReference;
                            FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
                            documentReference = firebaseFirestore.collection("Items").document(itemId);
                            documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    Item_data_model item_data_model = documentSnapshot.toObject(Item_data_model.class);
                                    am = item_data_model.getItemPrice();
                                }
                            });
                            databaseReference = FirebaseDatabase.getInstance().getReference().child(firebaseAuth.getCurrentUser().getUid()).child("MyOrder");
                            DatabaseReference databaseReference1 = databaseReference.push();
                            String s1 = databaseReference1.getKey();
                            databaseReference = FirebaseDatabase.getInstance().getReference().child(firebaseAuth.getCurrentUser().getUid()).child("MyOrder").child(s1);
                            Date c = Calendar.getInstance().getTime();
                            SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                            String formattedDate = df.format(c);
                            Toast.makeText(Payment.this, am, Toast.LENGTH_SHORT).show();
                            OrderModel orderModel = new OrderModel(s1,itemId,formattedDate,finalApprovalRefNo,am,"");
                            databaseReference1.setValue(orderModel);
                            AlertDialog.Builder builder = new AlertDialog.Builder(Payment.this);
                            builder.setTitle("Ordered Confirmed");
                            builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(Payment.this, MainActivity.class);
                                    startActivity(intent);
                                    finishAffinity();
                                    finish();
                                }
                            });
                            builder.create().show();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                Log.e("UPI", "payment successfull: " + approvalRefNo);
            } else if ("Payment cancelled by user.".equals(paymentCancel)) {
                Toast.makeText(Payment.this, "Payment cancelled by user.", Toast.LENGTH_SHORT).show();
                Log.e("UPI", "Cancelled by user: " + approvalRefNo);
            } else {
                Toast.makeText(Payment.this, "Transaction failed.Please try again", Toast.LENGTH_SHORT).show();
                Log.e("UPI", "failed payment: " + approvalRefNo);
            }
        } else {
            Log.e("UPI", "Internet issue: ");
            Toast.makeText(Payment.this, "Internet connection is not available. Please check and try again", Toast.LENGTH_SHORT).show();
        }
    }

    public static boolean isConnectionAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()
                    && netInfo.isConnectedOrConnecting()
                    && netInfo.isAvailable()) {
                return true;
            }
        }
        return false;

    }
}