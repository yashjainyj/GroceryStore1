package com.example.grocerystore.Admin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.grocerystore.MainActivity;
import com.example.grocerystore.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;

public class Add_Shop extends AppCompatActivity {
    private CircleImageView circleImageView;
    ImageView camera;
    FirebaseAuth mAuth;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    Button add;
    Uri profileUrl;
    private Uri uriProfileImage;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_shop);
        setTitle("Add Shop");
        circleImageView = findViewById(R.id.circleImageView);
        storageReference = FirebaseStorage.getInstance().getReference();
        TextInputEditText[] textInputEditTexts = {findViewById(R.id.shop_name),findViewById(R.id.shop_address),findViewById(R.id.min),findViewById(R.id.raiting),findViewById(R.id.shop_phone)};
        add= findViewById(R.id.submit);
        camera= findViewById(R.id.camera);
        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImageChooser();
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean checked=false;
                for (int i=0;i<5;i++)
                {
                    if(textInputEditTexts[i].getText().toString().equalsIgnoreCase(""))
                    {
                        textInputEditTexts[i].setError("Required");
                        checked =false;
                    }
                    else
                    {
                        if(uriProfileImage!=null)
                            checked=true;
                        else
                            Toast.makeText(Add_Shop.this, "Select Photo", Toast.LENGTH_SHORT).show();
                    }

                }
                if (checked)
                {
                    progressDialog.setTitle("Please wait a while");
                    progressDialog.show();
                    StorageReference d = FirebaseStorage.getInstance().getReference().child("images/Shopes/"+textInputEditTexts[4].getText().toString()+".jpg");
                    d.putFile(uriProfileImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> uri = taskSnapshot.getStorage().getDownloadUrl();
                            while(!uri.isComplete());
                            profileUrl = uri.getResult();
                            //profileUrl =task.getResult().getDownloadUrl();
                            //profileUrl = taskSnapshot.getStorage().getDownloadUrl().getResult();
                            databaseReference = FirebaseDatabase.getInstance().getReference().child("Shopes");
                            DatabaseReference databaseReference1 = databaseReference.push();
                            String s1 = databaseReference1.getKey();
                            databaseReference = FirebaseDatabase.getInstance().getReference().child("Shopes").child(s1);
                            Shop_Detais_Modal shop_detais_modal1 = new Shop_Detais_Modal(s1,textInputEditTexts[0].getText().toString(),textInputEditTexts[1].getText().toString(),textInputEditTexts[3].getText().toString(),textInputEditTexts[2].getText().toString(),profileUrl.toString(),textInputEditTexts[4].getText().toString());
                            databaseReference1.setValue(shop_detais_modal1);
                            Toast.makeText(Add_Shop.this, "Shop Added", Toast.LENGTH_SHORT).show();
                            progressDialog.cancel();
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Add_Shop.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.i("Fail", "onFailure:------------------> " + e.getMessage());
                        }
                    });


                }

            }
        });

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
        CropImage.activity().start(Add_Shop.this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent  = new Intent(this, Shops_Main.class);
        startActivity(intent);
        finish();
    }
}
