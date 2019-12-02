package com.example.grocerystore.Admin;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.grocerystore.R;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Show_Item extends AppCompatActivity {
    TextInputEditText add;
    RecyclerView recyclerView;
    private CollectionReference collectionReference ;
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    ArrayList<Item_data_model> arrayList;
    ShimmerFrameLayout shimmerFrameLayout;
    RelativeLayout relativeLayout;

    String shopId;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shops_items);
        Window window = this.getWindow();
       // setTitle("Items");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        toolbar = findViewById(R.id.toolbar);
//        getSupportActionBar();
//        toolbar.setTitle("Items");
//        setSupportActionBar(toolbar);
        shimmerFrameLayout = findViewById(R.id.shimmer);
        relativeLayout = findViewById(R.id.rel1);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.colorPrimaryDark));
        Intent intent = getIntent();
        shopId= intent.getStringExtra("shopId");
        Toast.makeText(this, shopId, Toast.LENGTH_SHORT).show();
        add = findViewById(R.id.add_item);
        recyclerView = findViewById(R.id.recyclerview);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Show_Item.this,Add_Item.class);
                intent1.putExtra("shopId",shopId);
                startActivity(intent1);
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        arrayList = new ArrayList<>();
        recyclerView.setHasFixedSize(true);
        collectionReference = firebaseFirestore.collection("Items");
        collectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                arrayList = new ArrayList<>();
                for(QueryDocumentSnapshot queryDocumentSnapshots1 : queryDocumentSnapshots)
                {
                    Item_data_model item_data_model = queryDocumentSnapshots1.toObject(Item_data_model.class);
                    if(item_data_model.getShopId().equalsIgnoreCase(shopId))
                    {
                            arrayList.add(item_data_model);
                        //Toast.makeText(Show_Item.this, item_data_model.getItemName(), Toast.LENGTH_SHORT).show();
                    }
                }
                RecyclerView.LayoutManager layoutManager = new GridLayoutManager(Show_Item.this,2);
                Show_Item_Adapter show_item_adapter = new Show_Item_Adapter(Show_Item.this,arrayList);
                recyclerView.setAdapter(show_item_adapter);
                recyclerView.setLayoutManager(layoutManager);
                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.GONE);
                relativeLayout.setVisibility(View.VISIBLE);
              }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Show_Item.this, "Error", Toast.LENGTH_SHORT).show();
                Log.i("msl;fdmslf", "onFailure: ----------------------------- Fail");
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
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this,Shops_Main.class);
        startActivity(intent);
        finish();
    }

}
