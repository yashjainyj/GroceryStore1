package com.example.grocerystore.Location;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.grocerystore.MainActivity;
import com.example.grocerystore.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;


import java.io.IOException;
import java.security.Key;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class SearchLocation extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    AutoCompleteTextView search;
    Button getCurrentLocation;
    public boolean mLoactionPermissionGranted = false;
    private static final String TAG = "GetLocation";
    private static final int ERROR_DIALOG_REQUEST_ = 9001;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 101;
    public static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    public static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private FusedLocationProviderClient mfusedLocationProviderClient;
    Geocoder geocoder;
    PlaceAutocompleteAdapter placeAutocompleteAdapter;
    private GoogleApiClient mGoogleApiClient;
    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(
            new LatLng(-40, -168), new LatLng(71, 136));

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_location);
        setTitle("");
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.colorPrimaryDark));
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        search = findViewById(R.id.search);
        getCurrentLocation = findViewById(R.id.getCurrentLocation);
        getCurrentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocationPermission();
                getDeviceLocation();
            }
        });
        init();
    }


    private void getDeviceLocation(){
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
                            geocoder = new Geocoder(SearchLocation.this, Locale.getDefault());
                            List<Address> list = new ArrayList<>();
                            try {
                                list = geocoder.getFromLocation(currentLocation.getLatitude(),currentLocation.getLongitude(),1);
                                Intent intent = new Intent(SearchLocation.this, MainActivity.class);
                                Toast.makeText(SearchLocation.this, list.get(0).getSubAdminArea(), Toast.LENGTH_SHORT).show();
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
            e.printStackTrace();
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
                ActivityCompat.requestPermissions(SearchLocation.this,permission,LOCATION_PERMISSION_REQUEST_CODE);
            }
        }
        else
            ActivityCompat.requestPermissions(SearchLocation.this,permission,LOCATION_PERMISSION_REQUEST_CODE);
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


    private void init(){
                    mGoogleApiClient = new GoogleApiClient
                    .Builder(this)
                    .addApi(Places.GEO_DATA_API)
                    .addApi(Places.PLACE_DETECTION_API)
                    .enableAutoManage(this, this)
                    .build();

            placeAutocompleteAdapter = new PlaceAutocompleteAdapter(this, mGoogleApiClient,
                    LAT_LNG_BOUNDS, null);

           search.setAdapter(placeAutocompleteAdapter);

        
            search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if(actionId== EditorInfo.IME_ACTION_SEARCH
                            || actionId == EditorInfo.IME_ACTION_DONE
                            || event.getAction() == KeyEvent.ACTION_DOWN
                            || event.getAction() == KeyEvent.KEYCODE_ENTER
                    )
                    {
                        getLocate();
                    }
                    else
                        Toast.makeText(SearchLocation.this, "yehh", Toast.LENGTH_SHORT).show();
                    return false;
                }
            });
    }
    private  void getLocate(){
        String searchString = search.getText().toString();
        Geocoder geocoder = new Geocoder(this);
        List<Address> list = new ArrayList<>();
        try {
            list = geocoder.getFromLocationName(searchString,1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (list.size()>0)
        {
            Address address = list.get(0);
            Toast.makeText(this, address.toString(), Toast.LENGTH_SHORT).show();
            Log.d("d",address.toString());
        }
        else
            Toast.makeText(this, "Not a Valid City", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater menuInflater = getMenuInflater();
//        menuInflater.inflate(R.menu.search_menu, menu);
//        MenuItem search = menu.findItem(R.id.search);
//        final android.support.v7.widget.SearchView searchView = (android.support.v7.widget.SearchView) search.getActionView();
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                return true;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                return true;
//            }
//        });
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId())
//        {
//            case R.id.search :
//
//                break;
//        }
//        return true;
//    }
}
