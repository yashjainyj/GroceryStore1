package com.example.grocerystore.Main;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.grocerystore.Admin.Item_data_model;
import com.example.grocerystore.Admin.Show_Item;
import com.example.grocerystore.Admin.Show_Item_Adapter;
import com.example.grocerystore.MainActivity;
import com.example.grocerystore.R;
import com.facebook.shimmer.ShimmerFrameLayout;
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
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ShowItemMain extends AppCompatActivity {
    TextInputEditText editText;
    TextInputLayout textInputLayout;
    RecyclerView recyclerView;
    ShimmerFrameLayout shimmerFrameLayout;
    RelativeLayout relativeLayout;
    private CollectionReference collectionReference ;
    String cat;
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    ArrayList<Item_data_model> arrayList;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shops_items);
        shimmerFrameLayout = findViewById(R.id.shimmer);
        relativeLayout = findViewById(R.id.rel1);
        setTitle("");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        editText = findViewById(R.id.add_item);
        editText.setVisibility(View.GONE);
        textInputLayout = findViewById(R.id.add_address1);
        textInputLayout.setVisibility(View.GONE);
        recyclerView = findViewById(R.id.recyclerview);

        Intent intent = getIntent();
        cat = intent.getStringExtra("cat");
        if(cat!=null)
        {
            if(cat.equalsIgnoreCase("All"))
                getData();
            else
                getFilterData(cat);
        }
        else
        {
            getData();
        }

    }


    public void getFilterData(String cat1){
        arrayList = new ArrayList<>();
        recyclerView.setHasFixedSize(true);
        collectionReference = firebaseFirestore.collection("Items");
        collectionReference.whereEqualTo("category",cat1).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                arrayList = new ArrayList<>();
                for(QueryDocumentSnapshot queryDocumentSnapshots1 : queryDocumentSnapshots)
                {
                    Item_data_model item_data_model = queryDocumentSnapshots1.toObject(Item_data_model.class);
                    arrayList.add(item_data_model);
                  //  Toast.makeText(ShowItemMain.this, item_data_model.getItemName(), Toast.LENGTH_SHORT).show();
                }
                RecyclerView.LayoutManager layoutManager = new GridLayoutManager(ShowItemMain.this,2);
                Show_Item_Adapter show_item_adapter = new Show_Item_Adapter(ShowItemMain.this,arrayList);
                recyclerView.setAdapter(show_item_adapter);
                recyclerView.setLayoutManager(layoutManager);
                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.GONE);
                relativeLayout.setVisibility(View.VISIBLE);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ShowItemMain.this, "Error", Toast.LENGTH_SHORT).show();
               // Log.i("msl;fdmslf", "onFailure: ----------------------------- Fail");
            }
        });
    }


    public void getData() {
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
                    //Toast.makeText(ShowItemMain.this, item_data_model.getItemName(), Toast.LENGTH_SHORT).show();
                }
                RecyclerView.LayoutManager layoutManager = new GridLayoutManager(ShowItemMain.this,2);
                Show_Item_Adapter show_item_adapter = new Show_Item_Adapter(ShowItemMain.this,arrayList);
                recyclerView.setAdapter(show_item_adapter);
                recyclerView.setLayoutManager(layoutManager);
                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.GONE);
                relativeLayout.setVisibility(View.VISIBLE);
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

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finishAffinity();
    }


    void getSearchedData(String s)
    {
        if(cat!=null)
        {
            if(cat.equalsIgnoreCase("All"))
                getDataSearched(s);
            else
                getFilterDataSearched(cat,s);
        }
        else
        {
            getData();
        }

    }

    private void getFilterDataSearched(String cat, String s) {

            arrayList = new ArrayList<>();
            recyclerView.setHasFixedSize(true);
            collectionReference = firebaseFirestore.collection("Items");
            collectionReference.whereEqualTo("category",cat).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    arrayList = new ArrayList<>();
                    for(QueryDocumentSnapshot queryDocumentSnapshots1 : queryDocumentSnapshots)
                    {
                        Item_data_model item_data_model = queryDocumentSnapshots1.toObject(Item_data_model.class);
                        if(item_data_model.getItemName().toLowerCase().contains(s.toLowerCase()))
                        arrayList.add(item_data_model);
                        //Toast.makeText(ShowItemMain.this, item_data_model.getItemName(), Toast.LENGTH_SHORT).show();
                    }
                    RecyclerView.LayoutManager layoutManager = new GridLayoutManager(ShowItemMain.this,2);
                    Show_Item_Adapter show_item_adapter = new Show_Item_Adapter(ShowItemMain.this,arrayList);
                    recyclerView.setAdapter(show_item_adapter);
                    recyclerView.setLayoutManager(layoutManager);
                    shimmerFrameLayout.stopShimmer();
                    shimmerFrameLayout.setVisibility(View.GONE);
                    relativeLayout.setVisibility(View.VISIBLE);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ShowItemMain.this, "Error", Toast.LENGTH_SHORT).show();
                    Log.i("msl;fdmslf", "onFailure: ----------------------------- Fail");
                }
            });
        }



    private void getDataSearched(String s) {
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
                    if(item_data_model.getItemName().toLowerCase().contains(s.toLowerCase()))
                    arrayList.add(item_data_model);
                    //Toast.makeText(ShowItemMain.this, item_data_model.getItemName(), Toast.LENGTH_SHORT).show();
                }
                RecyclerView.LayoutManager layoutManager = new GridLayoutManager(ShowItemMain.this,2);
                Show_Item_Adapter show_item_adapter = new Show_Item_Adapter(ShowItemMain.this,arrayList);
                recyclerView.setAdapter(show_item_adapter);
                recyclerView.setLayoutManager(layoutManager);
                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.GONE);
                relativeLayout.setVisibility(View.VISIBLE);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);

        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (android.widget.SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                getSearchedData(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
              getSearchedData(newText);
                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}

