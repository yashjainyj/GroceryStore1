package com.example.grocerystore.Main;

import android.os.Bundle;
import android.view.View;

import com.example.grocerystore.R;
import com.example.grocerystore.User.Address_DataModel;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MyOrder extends AppCompatActivity {
    TextInputEditText textInputEditText;
    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    List<OrderModel> list;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shops_items);
        textInputEditText = findViewById(R.id.add_item);
        textInputEditText.setVisibility(View.GONE);
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        list = new ArrayList<>();
    }

    @Override
    protected void onStart() {
        super.onStart();
        list.clear();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child(firebaseAuth.getCurrentUser().getUid()).child("MyOrder");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    list.add(dsp.getValue(OrderModel.class));
                }

                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MyOrder.this);
                recyclerView.setLayoutManager(layoutManager);
                MyOrderAdapter myOrderAdapter = new MyOrderAdapter(MyOrder.this,list);
                recyclerView.setAdapter(myOrderAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
