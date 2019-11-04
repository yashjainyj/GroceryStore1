package com.example.grocerystore.Main;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.grocerystore.Admin.Item_data_model;
import com.example.grocerystore.Admin.Show_Item;
import com.example.grocerystore.Admin.Show_Item_Adapter;
import com.example.grocerystore.MainActivity;
import com.example.grocerystore.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ShowItemMain extends AppCompatActivity {
    TextInputEditText editText;
    TextInputLayout textInputLayout;
    RecyclerView recyclerView;
    private CollectionReference collectionReference ;
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    ArrayList<Item_data_model> arrayList;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shops_items);
        editText = findViewById(R.id.add_item);
        editText.setVisibility(View.GONE);
        textInputLayout = findViewById(R.id.add_address1);
        textInputLayout.setVisibility(View.GONE);
        recyclerView = findViewById(R.id.recyclerview);

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
                        arrayList.add(item_data_model);
                        Toast.makeText(ShowItemMain.this, item_data_model.getItemName(), Toast.LENGTH_SHORT).show();
                }
                RecyclerView.LayoutManager layoutManager = new GridLayoutManager(ShowItemMain.this,2);
                Show_Item_Adapter show_item_adapter = new Show_Item_Adapter(ShowItemMain.this,arrayList);
                recyclerView.setAdapter(show_item_adapter);
                recyclerView.setLayoutManager(layoutManager);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ShowItemMain.this, "Error", Toast.LENGTH_SHORT).show();
                Log.i("msl;fdmslf", "onFailure: ----------------------------- Fail");
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finishAffinity();
    }
}

