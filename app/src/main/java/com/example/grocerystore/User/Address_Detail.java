package com.example.grocerystore.User;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.grocerystore.MainActivity;
import com.example.grocerystore.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.Address;

public class Address_Detail extends AppCompatActivity {
    TextInputEditText addAddress;
    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    FirebaseAuth auth;
    List<Address_DataModel> list = new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.adderss_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Address");
        auth = FirebaseAuth.getInstance();
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.colorPrimaryDark));
        addAddress = findViewById(R.id.add_address);
        addAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Address_Detail.this, Add_Address.class);
                startActivity(intent);
            }
        });


//        list.add(new Address_DataModel("Mr","Yash Jain","Lovely Professional University","Bh6","Phagwara","home"));
//        list.add(new Address_DataModel("Mr","Ishu","Lovely Professional University","Bh6","Phagwara","home"));
//        list.add(new Address_DataModel("Mr","Sagar","Lovely Professional University","Bh6","Phagwara","home"));
//        list.add(new Address_DataModel("Mr","Himanshu","Lovely Professional University","Bh6","Phagwara","home"));
//        list.add(new Address_DataModel("Mr","Vaibhav","Lovely Professional University","Bh6","Phagwara","home"));
//
       recyclerView = findViewById(R.id.recyclerview);

    }

    @Override
    protected void onStart() {
        super.onStart();
        list.clear();
        databaseReference = FirebaseDatabase.getInstance().getReference().child(auth.getCurrentUser().getUid()).child("Address");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    //  Address_DataModel address_dataModel = dataSnapshot.getValue(Address_DataModel.class);
                    for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                        list.add(dsp.getValue(Address_DataModel.class));

                    }
                    recyclerView.setHasFixedSize(true);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(Address_Detail.this);
                    recyclerView.setLayoutManager(layoutManager);
                    Address_Adapter address_adapter = new Address_Adapter(Address_Detail.this,list);
                    //address_adapter.notifyDataSetChanged();
                    recyclerView.setAdapter(address_adapter);
                    //list.add(address_dataModel);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent  = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}

