package com.example.grocerystore.Main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.grocerystore.Admin.Item_data_model;
import com.example.grocerystore.Admin.Shop_Detais_Modal;
import com.example.grocerystore.MyUtility;
import com.example.grocerystore.R;
import com.example.grocerystore.User.Cart_Main;
import com.example.grocerystore.User.LoginActivity;
import com.example.grocerystore.User.Payment;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Product_Detail extends AppCompatActivity {
    ImageView product_image;
    TextView product_name,product_price,product_raiting,product_delivery,desc,category,seller_name;
    Button addToCart,buyNow;
    DocumentReference documentReference;
    ShimmerFrameLayout shimmerFrameLayout;
    ScrollView relativeLayout;

    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    String item_id,shopId;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_info_layout);
        shimmerFrameLayout = findViewById(R.id.shimmer);
        relativeLayout = findViewById(R.id.rel1);
        Intent intent = getIntent();
        item_id = intent.getStringExtra("item_id");
        init();
        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCart(v);

            }
        });
        buyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCart(v);
                Intent intent = new Intent(Product_Detail.this, Cart_Main.class);
                startActivity(intent);
                finish();
            }
        });
    }


  public  void   addCart(View v)
    {
        databaseReference = FirebaseDatabase.getInstance().getReference();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser()!=null)
        {
            databaseReference = databaseReference.child(firebaseAuth.getCurrentUser().getUid()).child("Cart").child(item_id);
            Map<String,String> m = new HashMap<>();
            m.put("itemId",item_id);
            MyUtility.m.put(item_id,"1");
            databaseReference.setValue(m);
            Snackbar.make(v, "Item added to cart", Snackbar.LENGTH_LONG).show();
        }
        else
        {
            Toast.makeText(Product_Detail.this, "Please Login First", Toast.LENGTH_LONG).show();
            Intent intent1 = new Intent(Product_Detail.this, LoginActivity.class);
            startActivity(intent1);
            finish();
        }


    }
    private void init() {
        product_image = findViewById(R.id.product_image);
        product_name = findViewById(R.id.product_name);
        product_price = findViewById(R.id.product_price);
        product_raiting = findViewById(R.id.raiting);
        product_delivery = findViewById(R.id.min);
        desc = findViewById(R.id.desc);
        seller_name = findViewById(R.id.seller_name);
        category = findViewById(R.id.category);
        addToCart = findViewById(R.id.cart);
        buyNow = findViewById(R.id.buy_now);
    }

    @Override
    protected void onStart() {
        super.onStart();

        documentReference = firebaseFirestore.collection("Items").document(item_id);
        documentReference.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Item_data_model item_data_model  = documentSnapshot.toObject(Item_data_model.class);
                        product_name.setText(item_data_model.getItemName());
                        product_price.setText("Rs."+item_data_model.getItemPrice());
                        category.setText(item_data_model.getCategory());
                        product_delivery.setText(item_data_model.getWeight()+"g");
                        desc.setText(item_data_model.getDescription());
                        shopId = item_data_model.getShopId();
                        Glide.with(Product_Detail.this).load(item_data_model.getImageUrl()).into(product_image);
                        databaseReference = FirebaseDatabase.getInstance().getReference();
                        databaseReference.child("Shopes").child(shopId).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Shop_Detais_Modal shop_detais_modal = dataSnapshot.getValue(Shop_Detais_Modal.class);
                                seller_name.setText(shop_detais_modal.getShop_Name());
                                product_raiting.setText(shop_detais_modal.getShop_rating());
                                shimmerFrameLayout.stopShimmer();
                                shimmerFrameLayout.setVisibility(View.GONE);
                                relativeLayout.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Toast.makeText(Product_Detail.this, "Failed", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Product_Detail.this, "Failed", Toast.LENGTH_SHORT).show();
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

}