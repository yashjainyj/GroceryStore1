package com.example.grocerystore.User;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.grocerystore.Admin.Item_data_model;
import com.example.grocerystore.Admin.Shops_Main;
import com.example.grocerystore.MainActivity;
import com.example.grocerystore.MyUtility;
import com.example.grocerystore.R;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Cart_Main extends AppCompatActivity {
    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    DocumentReference documentReference;
    TextView subtotal,price;
    ShimmerFrameLayout shimmerFrameLayout;
    Button buy;
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart_main);
        subtotal = findViewById(R.id.total);
        price = findViewById(R.id.price);
        shimmerFrameLayout = findViewById(R.id.shimmer);
        price.setText("Rs.0");
        recyclerView = findViewById(R.id.recyclerview);
        buy = findViewById(R.id.buy_now);
        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectAddress();


            }
        });
    }

    private void selectAddress() {
        ArrayList<Address_DataModel>list=new ArrayList<>();
        list.clear();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child(firebaseAuth.getCurrentUser().getUid()).child("Address");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    // Address_DataModel address_dataModel = dataSnapshot.getValue(Address_DataModel.class);
                    for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                        list.add(dsp.getValue(Address_DataModel.class));
                    }
                    ArrayList<String> s1 = new ArrayList<>();
                    for(Address_DataModel address_dataModel :list)
                    {
                        String s ="";
                        s=s+" "+ address_dataModel.getSalutation()+address_dataModel.getName()+"\n";
                        s = s+address_dataModel.getFlat() +"\n";
                        s=s+ address_dataModel.getLocality()+"\n";
                        s=s+address_dataModel.getStreet()+"\n";
                        s=s+address_dataModel.getNickname()+"\n";
                        s1.add(s);
                    }
                    AlertDialog.Builder builder = new AlertDialog.Builder(Cart_Main.this);
                    builder.setTitle("Select Address");
                    View view = getLayoutInflater().inflate(R.layout.select_address_listview,null);
                    ListView listView = view.findViewById(R.id.list_item);
                    TextInputEditText addaddress;
                    addaddress = view.findViewById(R.id.add_address);
                    addaddress.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(Cart_Main.this,Add_Address.class);
                            intent.putExtra("or","1");
                            startActivity(intent);
                            finish();
                        }
                    });
                    ArrayAdapter<String> address_dataModelArrayAdapter = new ArrayAdapter<>(Cart_Main.this,android.R.layout.simple_list_item_1,s1);
                    listView.setAdapter(address_dataModelArrayAdapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            Intent intent = new Intent(Cart_Main.this,Payment.class);
                            intent.putExtra("amount",price.getText().toString());
                            intent.putExtra("address_id",list.get(position).getKey());
                            Log.d("", "onItemClick: "+ list.get(position).getKey());
                            startActivity(intent);
                            finish();
                        }
                    });
                    builder.setView(view);
                    if(!((Activity) Cart_Main.this).isFinishing())
                    {
                        //show dialog
                        builder.show();
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }
    @Override
    public void onResume() {
        super.onResume();
        shimmerFrameLayout.startShimmer();
    }

    @Override
    public void onPause() {
        super.onPause();
        shimmerFrameLayout.stopShimmer();
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
                if(!list.isEmpty()) {
                    ArrayList<Item_data_model> list1 = new ArrayList<>();
                    for (String id : list) {
                        documentReference = firebaseFirestore.collection("Items").document(id);
                        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                Item_data_model item_data_model = documentSnapshot.toObject(Item_data_model.class);
                                list1.add(item_data_model);
                                RecyclerView.LayoutManager layoutManager = new GridLayoutManager(Cart_Main.this, 2);
                                Cart_Adapter cart_adapter = new Cart_Adapter(Cart_Main.this, list1);
                                recyclerView.setHasFixedSize(true);
                                recyclerView.setLayoutManager(layoutManager);
                                recyclerView.setAdapter(cart_adapter);
                                int price1 = 0;
                                price.setText("0");
                                Set<String> s = MyUtility.m.keySet();
                                for (String sq : s) {
                                    Log.d("Set ", sq);
                                    for (Item_data_model item_data_model1 : list1) {
                                        if (sq.equalsIgnoreCase(item_data_model1.getItemId())) {
                                            int p = Integer.parseInt(MyUtility.m.get(item_data_model1.getItemId()));
                                            Log.d("Quantity ", "onSuccess: " + p);
                                            int pe = p * Integer.parseInt(item_data_model1.getItemPrice());
                                            Log.d("Price", "onSuccess: " + pe);
                                            price1 += pe;
                                            Log.d("Total Price", String.valueOf(price1));
                                        }
                                    }
                                }
                                price.setText("Rs " + price1 + "");
                                shimmerFrameLayout.stopShimmer();
                                shimmerFrameLayout.setVisibility(View.GONE);
                                recyclerView.setVisibility(View.VISIBLE);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });
                    }
                }
                else
                {
                    shimmerFrameLayout.stopShimmer();
                    shimmerFrameLayout.setVisibility(View.GONE);
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