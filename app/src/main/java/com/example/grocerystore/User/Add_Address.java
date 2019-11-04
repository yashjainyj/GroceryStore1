package com.example.grocerystore.User;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.grocerystore.MainActivity;
import com.example.grocerystore.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

public class Add_Address extends AppCompatActivity {
    private TextInputEditText name,flat,street,locality;
    Button home,office,add;
    RadioGroup radioGroup;
    RadioButton radioButton;
    DatabaseReference databaseReference;
    FirebaseAuth mAuth;
    int flag=0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_address_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Add Address");
        databaseReference = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.colorPrimaryDark));
        name = findViewById(R.id.name);
        flat = findViewById(R.id.flat);
        street = findViewById(R.id.street);
        locality = findViewById(R.id.locality);
        home = findViewById(R.id.home);
        office = findViewById(R.id.office);
        radioGroup= findViewById(R.id.radiogroup);
        add = findViewById(R.id.submit);
        Intent intent = getIntent();
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                office.setBackground(getResources().getDrawable(R.drawable.button_design_change));
                home.setTextColor(Color.WHITE);
                home.setBackground(getResources().getDrawable(R.drawable.button_design));
                office.setTextColor(Color.GRAY);
                if (flag==1)
                    flag=0;
            }
        });
        office.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flag==0)
                    flag=1;
                home.setBackground(getResources().getDrawable(R.drawable.button_design_change));
                office.setTextColor(Color.WHITE);
                home.setTextColor(Color.GRAY);
                office.setBackground(getResources().getDrawable(R.drawable.button_design));
            }
        });
        String s = intent.getStringExtra("s");
        if(s!=null)
        {
            editAddress(s);
        }
        else
        {
            addAddress();
        }

    }

    private void editAddress(String s) {
        databaseReference = FirebaseDatabase.getInstance().getReference().child(mAuth.getCurrentUser().getUid()).child("Address").child(s);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                      Address_DataModel address_dataModel = dataSnapshot.getValue(Address_DataModel.class);
                      name.setText(address_dataModel.getName());
                            flat.setText(address_dataModel.getFlat());
                            street.setText(address_dataModel.getStreet());
                            locality.setText(address_dataModel.getLocality());
                            if(address_dataModel.getNickname().equalsIgnoreCase("Home"))
                            {
                                flag=0;
                                office.setBackground(getResources().getDrawable(R.drawable.button_design_change));
                                home.setTextColor(Color.WHITE);
                                home.setBackground(getResources().getDrawable(R.drawable.button_design));
                                office.setTextColor(Color.GRAY);
                            }
                            else
                            {
                                flag=1;
                                home.setBackground(getResources().getDrawable(R.drawable.button_design_change));
                                office.setTextColor(Color.WHITE);
                                home.setTextColor(Color.GRAY);
                                office.setBackground(getResources().getDrawable(R.drawable.button_design));
                            }


                            add.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    int selectedId=radioGroup.getCheckedRadioButtonId();
                                    radioButton = findViewById(selectedId);
                                    if(radioButton!=null)
                                    {
                                        if (!name.getText().toString().equalsIgnoreCase(""))
                                        {
                                            if(!flat.getText().toString().equalsIgnoreCase(""))
                                            {
                                                if(!street.getText().toString().equalsIgnoreCase(""))
                                                {
                                                    if(!locality.getText().toString().equalsIgnoreCase(""))
                                                    {
                                                        String s1="";

                                                        if(flag==0)
                                                            s1="Home";
                                                        else
                                                            s1="Work";

                                                        Address_DataModel address_dataModel = new Address_DataModel(s,radioButton.getText().toString(),name.getText().toString(),flat.getText().toString(),street.getText().toString(),locality.getText().toString(),s1);
                                                        databaseReference = FirebaseDatabase.getInstance().getReference().child(mAuth.getCurrentUser().getUid()).child("Address").child(s);
                                                        databaseReference.setValue(address_dataModel);

                                                        Toast.makeText(Add_Address.this, "Address Updated", Toast.LENGTH_SHORT).show();
                                                        Intent intent = new Intent(Add_Address.this,Address_Detail.class);
                                                        startActivity(intent);
                                                        finish();
                                                    }
                                                    else {
                                                        locality.setFocusable(true);
                                                        locality.setError("Required");
                                                        Toast.makeText(Add_Address.this, "Locality is Required", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                                else
                                                {
                                                    street.setFocusable(true);
                                                    street.setError("Required");
                                                    Toast.makeText(Add_Address.this, "Street/Society/Office Name is Required", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                            else
                                            {
                                                flat.setFocusable(true);
                                                flat.setError("Required");
                                                Toast.makeText(Add_Address.this, "Flat/House/Office No. is Required", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                        else
                                        {
                                            name.setFocusable(true);
                                            name.setError("Required");
                                            Toast.makeText(Add_Address.this, "Name is Required", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    else
                                    {
                                        Toast.makeText(Add_Address.this, "Field is required", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                    //list.add(address_dataModel);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        add.setText("Update");

    }


    void addAddress()
    {
        home.setBackground(getResources().getDrawable(R.drawable.button_design));
        home.setTextColor(Color.WHITE);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId=radioGroup.getCheckedRadioButtonId();
                radioButton = findViewById(selectedId);
                if(radioButton!=null)
                {
                    if (!name.getText().toString().equalsIgnoreCase(""))
                    {
                        if(!flat.getText().toString().equalsIgnoreCase(""))
                        {
                            if(!street.getText().toString().equalsIgnoreCase(""))
                            {
                                if(!locality.getText().toString().equalsIgnoreCase(""))
                                {
                                    String s="";
                                    if(flag==0)
                                        s="Home";
                                    else
                                        s="Work";

                                    Address_DataModel address_dataModel = new Address_DataModel(radioButton.getText().toString(),name.getText().toString(),flat.getText().toString(),street.getText().toString(),locality.getText().toString(),s);
                                    databaseReference = FirebaseDatabase.getInstance().getReference().child(mAuth.getCurrentUser().getUid()).child("Address");
                                    DatabaseReference databaseReference1 = databaseReference.push();
                                    String s1 = databaseReference1.getKey();
                                    databaseReference = FirebaseDatabase.getInstance().getReference().child(mAuth.getCurrentUser().getUid()).child("Address").child(s1);
                                    Address_DataModel address_dataModel1 = new Address_DataModel(s1,radioButton.getText().toString(),name.getText().toString(),flat.getText().toString(),street.getText().toString(),locality.getText().toString(),s);
                                    databaseReference1.setValue(address_dataModel1);
                                    Toast.makeText(Add_Address.this, "Address Added", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(Add_Address.this,Address_Detail.class);
                                    startActivity(intent);
                                    finish();
                                }
                                else {
                                    locality.setFocusable(true);
                                    locality.setError("Required");
                                    Toast.makeText(Add_Address.this, "Locality is Required", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else
                            {
                                street.setFocusable(true);
                                street.setError("Required");
                                Toast.makeText(Add_Address.this, "Street/Society/Office Name is Required", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                        {
                            flat.setFocusable(true);
                            flat.setError("Required");
                            Toast.makeText(Add_Address.this, "Flat/House/Office No. is Required", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        name.setFocusable(true);
                        name.setError("Required");
                        Toast.makeText(Add_Address.this, "Name is Required", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(Add_Address.this, "Field is required", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent  = new Intent(this, Address_Detail.class);
        startActivity(intent);
        finish();
    }
}
