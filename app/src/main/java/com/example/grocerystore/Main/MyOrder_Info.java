package com.example.grocerystore.Main;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.grocerystore.Admin.Item_data_model;
import com.example.grocerystore.Admin.Shop_Detais_Modal;
import com.example.grocerystore.R;
import com.example.grocerystore.User.Address_DataModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MyOrder_Info extends AppCompatActivity {
    DatabaseReference databaseReference;
    String orderId,itemId,addressId,shopId;
    TextView date,refer,total,name,price,s_address,b_address,item,finaltotal,shop_name;
    ImageView imageView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_info);
        Intent intent = getIntent();
        date = findViewById(R.id.order_date);
        refer = findViewById(R.id.order_ref);
        total = findViewById(R.id.order_tot);
        name = findViewById(R.id.name);
        price = findViewById(R.id.price);
        s_address = findViewById(R.id.address);
        b_address = findViewById(R.id.add);
        item = findViewById(R.id.price1);
        imageView = findViewById(R.id.image);
        finaltotal = findViewById(R.id.price11);
        shop_name = findViewById(R.id.shop_name);
        orderId = intent.getStringExtra("orderId");
        databaseReference = FirebaseDatabase.getInstance().getReference();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child(firebaseAuth.getCurrentUser().getUid()).child("MyOrder").child(orderId);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    OrderModel orderModel = dataSnapshot.getValue(OrderModel.class);
                    itemId = orderModel.getItemId();
                    addressId = orderModel.getAddressId();
                    date.setText(orderModel.getOrderDate());
                    refer.setText(orderModel.getReferenceNo());
                    total.setText("Rs."+orderModel.getAmount());

                databaseReference = FirebaseDatabase.getInstance().getReference().child(firebaseAuth.getCurrentUser().getUid()).child("Address").child(addressId);
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Address_DataModel address_dataModel = dataSnapshot.getValue(Address_DataModel.class);
                        String s="";

                        s=s+"<b> " + address_dataModel.getSalutation()+address_dataModel.getName()+"</b>"+"<br>";
                        s = s+address_dataModel.getFlat() +"<br>";
                        s=s+ address_dataModel.getLocality()+"<br>";
                        s=s+address_dataModel.getStreet()+"<br>";
                        s=s+address_dataModel.getNickname()+"<br>";
                        s_address.setText(Html.fromHtml(s));
                        b_address.setText(Html.fromHtml(s));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                DocumentReference documentReference;
                FirebaseFirestore firebaseFirestore =FirebaseFirestore.getInstance();
                documentReference = firebaseFirestore.collection("Items").document(itemId);
                documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Item_data_model item_data_model = documentSnapshot.toObject(Item_data_model.class);
                        name.setText(item_data_model.getItemName());
                        price.setText("Rs." + item_data_model.getItemPrice());
                        item.setText("Rs." + item_data_model.getItemPrice());
                        finaltotal.setText("Rs." + item_data_model.getItemPrice());
                        shopId = item_data_model.getShopId();
                        Glide.with(MyOrder_Info.this).load(item_data_model.getImageUrl()).into(imageView);


                        databaseReference = FirebaseDatabase.getInstance().getReference().child("Shopes").child(shopId);
                        databaseReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Shop_Detais_Modal shop_detais_modal = dataSnapshot.getValue(Shop_Detais_Modal.class);
                                shop_name.setText(shop_detais_modal.getShop_Name());

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                });

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
