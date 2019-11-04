package com.example.grocerystore.Location;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.grocerystore.MainActivity;
import com.example.grocerystore.MyUtility;
import com.example.grocerystore.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class GetLocation extends AppCompatActivity {
    private static final String TAG = "GetLocation";
    private static final int ERROR_DIALOG_REQUEST_ = 9001;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 101;
    public static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    public static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    public boolean mLoactionPermissionGranted = false;
    private FusedLocationProviderClient mfusedLocationProviderClient;
    Button getCurrentLocation,manuallyLocation;
    Geocoder geocoder;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.get_location);
        getCurrentLocation = findViewById(R.id.getCurrentLocation);
        manuallyLocation = findViewById(R.id.manuallylocation);
        getLocationPermission();
        getCurrentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocationPermission();
                getDeviceLocation();
            }
        });
        manuallyLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GetLocation.this,SearchLocation.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void getDeviceLocation(){
        Log.d(TAG,"Get Location");
        mfusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        try {
            if (mLoactionPermissionGranted)
            {
                Task location = mfusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful())
                        {
                            Location currentLocation = (Location) task.getResult();
                            //Log.d(TAG, currentLocation.toString() + currentLocation.getLatitude() + "     " + currentLocation.getLongitude());
                            geocoder = new Geocoder(GetLocation.this, Locale.getDefault());
                            List<Address> list = new ArrayList<>();
                            try {
                                list = geocoder.getFromLocation(currentLocation.getLatitude(),currentLocation.getLongitude(),1);
                                Log.d(TAG,  list.get(0).getAddressLine(0) + "\n" + list.get(0).getLocality() + "\n" + list.get(0).getAdminArea() + "\n" + list.get(0).getCountryName() + "\n"+ list.get(0).getPostalCode() + "\n"+ list .get(0).getFeatureName()+"\n" + list.get(0).getSubLocality()+"\n"+list.get(0).getSubAdminArea()+"\n"+list.get(0).getPremises() + "\n"+ list.get(0).getPhone()+"\n"+list.get(0).getThoroughfare()+ "\n"+list.get(0).getSubThoroughfare());
                                MyUtility.location=list.get(0).getLocality();                                Intent intent = new Intent(GetLocation.this, MainActivity.class);
                                Toast.makeText(GetLocation.this, list.get(0).getSubAdminArea(), Toast.LENGTH_SHORT).show();
                                startActivity(intent);
                                finish();

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
            }
            else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }

        }catch (SecurityException e)
        {
            Log.d(TAG,e.toString());
        }
    }


    private void getLocationPermission(){
        String permission[] = {Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION};
        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),FINE_LOCATION)== PackageManager.PERMISSION_GRANTED)
        {
            if(ContextCompat.checkSelfPermission(this.getApplicationContext(),COARSE_LOCATION)== PackageManager.PERMISSION_GRANTED)
            {
                mLoactionPermissionGranted = true;
            }
            else {
                ActivityCompat.requestPermissions(GetLocation.this,permission,LOCATION_PERMISSION_REQUEST_CODE);
            }
        }
        else
            ActivityCompat.requestPermissions(GetLocation.this,permission,LOCATION_PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mLoactionPermissionGranted=false;
        switch (requestCode)
        {
            case LOCATION_PERMISSION_REQUEST_CODE:
//                if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
//                {
//                    mLoactionPermissionGranted=true;
//                }

                if(grantResults.length>0)
                {
                    for(int i=0;i<grantResults.length;i++)
                    {
                        if(grantResults[i]==PackageManager.PERMISSION_GRANTED)
                        {
                            mLoactionPermissionGranted=false;
                            return;
                        }
                    }
                    mLoactionPermissionGranted = true;
                }
        }

    }

    public boolean isServiceOK(){
        Log.d(TAG,"is Service ok : Checking version");
        int available  = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(GetLocation.this);
        if(available== ConnectionResult.SUCCESS)
        {
            Log.d(TAG,"Every thing is OK");
            return true;
        }
        else if(GoogleApiAvailability.getInstance().isUserResolvableError(available))
        {
            Log.d(TAG,"Error occured but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(GetLocation.this,available,ERROR_DIALOG_REQUEST_);
            dialog.show();
        }
        else
        {
            Log.d(TAG,"Can not resolve error");
        }
        return  false;
    }
}
