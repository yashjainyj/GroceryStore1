package com.example.grocerystore.User;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.grocerystore.Admin.Item_data_model;
import com.example.grocerystore.Admin.Shops_Main;
import com.example.grocerystore.MainActivity;
import com.example.grocerystore.MyUtility;
import com.example.grocerystore.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Cart_Main extends AppCompatActivity {
    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    DocumentReference documentReference;
    TextView subtotal,price;
    Button buy;
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart_main);
        subtotal = findViewById(R.id.total);
        price = findViewById(R.id.price);
        price.setText("Rs.0");
        recyclerView = findViewById(R.id.recyclerview);
        buy = findViewById(R.id.buy_now);
        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Cart_Main.this,Payment.class);
                intent.putExtra("amount",price.getText());
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child(firebaseAuth.getCurrentUser().getUid()).child("Cart");
        List<String> list = new ArrayList<>();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                {
                    String s = dataSnapshot1.getKey();
                    list.add(s);
                }
                Log.d("List", "onStart: " + list);
                ArrayList<Item_data_model> list1 = new ArrayList<>();
                for(String id : list)
                {
                    documentReference = firebaseFirestore.collection("Items").document(id);
                    documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            Item_data_model item_data_model = documentSnapshot.toObject(Item_data_model.class);
                            list1.add(item_data_model);
                            RecyclerView.LayoutManager layoutManager = new GridLayoutManager(Cart_Main.this,2);
                            Cart_Adapter cart_adapter = new Cart_Adapter(Cart_Main.this,list1);
                            recyclerView.setHasFixedSize(true);
                            recyclerView.setLayoutManager(layoutManager);
                            recyclerView.setAdapter(cart_adapter);
                            int price1= 0;
                            price.setText("0");
                            Set<String> s = MyUtility.m.keySet();
                            for(String sq : s)
                            {
                                Log.d("Set " , sq);
                                for(Item_data_model item_data_model1 : list1){
                                    if(sq.equalsIgnoreCase(item_data_model1.getItemId()))
                                    {
                                        int p = Integer.parseInt(MyUtility.m.get(item_data_model1.getItemId()));
                                        Log.d("Quantity ", "onSuccess: " + p);
                                        int pe = p*Integer.parseInt(item_data_model1.getItemPrice());
                                        Log.d("Price", "onSuccess: " + pe);
                                        price1 +=pe ;
                                        Log.d("Total Price", String.valueOf(price1));
                                    }
                                }
                            }
                            price.setText("Rs "+price1+"");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}