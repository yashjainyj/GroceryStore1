package com.example.grocerystore.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.grocerystore.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Shops_Main extends AppCompatActivity {
    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    FloatingActionButton add;
    List<Shop_Detais_Modal> list = new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shops_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Admin");
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.colorPrimaryDark));
        recyclerView = findViewById(R.id.recyclerview);
        add = findViewById(R.id.add_shop);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Shops_Main.this,Add_Shop.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        list.clear();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Shopes");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                    {
                        list.add(dataSnapshot1.getValue(Shop_Detais_Modal.class));
                    }
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(Shops_Main.this);
                    recyclerView.setLayoutManager(layoutManager);
                    Shop_Main_Adapter shop_main_adapter = new Shop_Main_Adapter(Shops_Main.this,list);
                    recyclerView.setAdapter(shop_main_adapter);
                    recyclerView.setHasFixedSize(true);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
