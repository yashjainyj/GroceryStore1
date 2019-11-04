package com.example.grocerystore.Admin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.grocerystore.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;

public class Add_Item extends AppCompatActivity {
    CircleImageView circleImageView;
    Button add;
    ImageView camera;
    private CollectionReference collectionReference ;
    FirebaseAuth mAuth;
    DatabaseReference databaseReference;
    Uri profileUrl;
    private Uri uriProfileImage;
    String shopId,selected_category="";
    ProgressDialog progressDialog;
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_items);
        setTitle("Add Items");
        circleImageView = findViewById(R.id.circleImageView);
        add = findViewById(R.id.submit);
        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImageChooser();
            }
        });
        Intent intent = getIntent();
        shopId = intent.getStringExtra("shopId");
        camera= findViewById(R.id.camera);
        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        TextInputEditText category  = findViewById(R.id.category);
        category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(Add_Item.this,category);
                popup.getMenuInflater()
                        .inflate(R.menu.category, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
//                                Toast.makeText(AddBook.this,"You Clicked : " + item.getTitle(),Toast.LENGTH_SHORT).show();
                        selected_category = String.valueOf(item.getTitle());
                        category.setText(selected_category);
                        return true;
                    }
                });
                popup.show();

            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selected_category.equalsIgnoreCase(""))
                {
                    category.setError("Required");
                }else
                {

                    TextInputEditText editText[]= {findViewById(R.id.item_name),findViewById(R.id.weight),findViewById(R.id.quantity),findViewById(R.id.price),findViewById(R.id.description)};

                    boolean checked=false;
                    for (int i=0;i<5;i++) {
                        if (editText[i].getText().toString().equalsIgnoreCase("")) {
                            editText[i].setError("Required");
                            checked = false;
                        } else {
                            if (uriProfileImage != null)
                                checked = true;
                            else
                                Toast.makeText(Add_Item.this, "Select Photo", Toast.LENGTH_SHORT).show();
                        }
                    }
                    if (checked)
                    {
                        progressDialog.setTitle("Please wait a while");
                        progressDialog.show();


                        StorageReference d = FirebaseStorage.getInstance().getReference().child("images/Items/"+editText[0].getText().toString()+".jpg");
                        d.putFile(uriProfileImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                Task<Uri> uri = taskSnapshot.getStorage().getDownloadUrl();
                                while(!uri.isComplete());
                                profileUrl = uri.getResult();
                                //profileUrl =task.getResult().getDownloadUrl();
                                //Toast.makeText(Add_Shop.this, "image uploaded" + profileUrl.toString(), Toast.LENGTH_SHORT).show();
                                progressDialog.cancel();


                                collectionReference = firebaseFirestore.collection("Items");
                                Item_data_model item_data_model = new Item_data_model(shopId,"",editText[0].getText().toString(),editText[1].getText().toString(),editText[2].getText().toString(),editText[3].getText().toString(),editText[4].getText().toString(), selected_category, profileUrl.toString());
                                collectionReference.add(item_data_model).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        String id = documentReference.getId();
                                        Item_data_model item_data_model = new Item_data_model(shopId,id,editText[0].getText().toString(),editText[1].getText().toString(),editText[2].getText().toString(),editText[3].getText().toString(),editText[4].getText().toString(), selected_category, profileUrl.toString());
                                        collectionReference.document(id).set(item_data_model);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Add_Item.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                Log.i("Fail", "onFailure:------------------> " + e.getMessage());
                            }
                        });
                    }



                }
            }
        });

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this,Show_Item.class);
        intent.putExtra("shopId",shopId);
        startActivity(intent);
        finish();
    }
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        CropImage.ActivityResult result = CropImage.getActivityResult(data);
        if (resultCode == RESULT_OK) {
            Uri resultUri = result.getUri();
            uriProfileImage = result.getUri();
            circleImageView.setImageURI(resultUri);
            camera.setVisibility(View.GONE);
        } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
            Exception error = result.getError();
        }
    }
    private void showImageChooser() {
        CropImage.activity().start(Add_Item.this);
    }

}
