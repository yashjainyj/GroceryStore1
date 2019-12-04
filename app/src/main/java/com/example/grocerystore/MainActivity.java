package com.example.grocerystore;

import android.animation.ArgbEvaluator;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.grocerystore.Admin.Shops_Main;
import com.example.grocerystore.Location.GetLocation;
import com.example.grocerystore.Main.MyOrder;
import com.example.grocerystore.Main.ShowItemMain;
import com.example.grocerystore.Model.RecyclerViewAdapter;
import com.example.grocerystore.Model.ViewPagerAdapter;
import com.example.grocerystore.Model.ViewPager_Model;
import com.example.grocerystore.Splash.SplashActivity;
import com.example.grocerystore.User.Add_Address;
import com.example.grocerystore.User.Address_Detail;
import com.example.grocerystore.User.Cart_Main;
import com.example.grocerystore.User.Feedback;
import com.example.grocerystore.User.LoginActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    TextView textView;
    FirebaseAuth firebaseAuth;
    NavigationView navigationView;
    Button order;
    RecyclerView recyclerView;
    ViewPager viewPager;
    ViewPagerAdapter adapter;
    Integer[] color = null;
    List<ViewPager_Model> list;
    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mImageUrls = new ArrayList<>();
    ArgbEvaluator argbEvaluator =new ArgbEvaluator();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        recyclerView = findViewById(R.id.recyclerview);
        TextView toolbarTitle = (TextView) findViewById(R.id.toolbarTitle);
        toolbarTitle.setText("Home");
        toolbarTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, getTitle(), Toast.LENGTH_SHORT).show();
            }
        });
        order=findViewById(R.id.order);
        order.setText("Show Items");
        list = new ArrayList<>();
        list.add(new ViewPager_Model(R.drawable.brochure,"Upto 50% Offer","New users can now avail up to 50% OFF + Flat Rs 200 cashback"));
        list.add(new ViewPager_Model(R.drawable.sticker,"Super Value Offer","On Grocery & Everyday Essentials Listed On The Offer Page."));
        list.add(new ViewPager_Model(R.drawable.poster,"Dhamaka Offer","Buy products from Up to 50% off_ Dhamaka Sale at best prices "));
        list.add(new ViewPager_Model(R.drawable.namecard,"Free Membership","Dhamaka Sale - Save 50% on 6 Months Membership Plan "));
        adapter = new ViewPagerAdapter(list,this);
        viewPager = findViewById(R.id.viewpager);
        viewPager.setAdapter(adapter);
        viewPager.setPadding(60,0,130,0);
        Integer[] color_temp = {
                getResources().getColor(R.color.color1),
                getResources().getColor(R.color.color2),
                getResources().getColor(R.color.color3),
                getResources().getColor(R.color.color4),
        };
        color = color_temp;
        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ShowItemMain.class);
                startActivity(intent);
                finish();
            }
        });

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if(position<(adapter.getCount()-1) && position<(color.length)-1 ){
                    viewPager.setBackgroundColor((Integer) argbEvaluator.evaluate(positionOffset,color[position],color[position]+1));
                    recyclerView.setBackgroundColor((Integer) argbEvaluator.evaluate(positionOffset,color[position],color[position]+1));
                }
                else
                {
                    viewPager.setBackgroundColor(color[color.length-1]);
                    recyclerView.setBackgroundColor(color[color.length-1]);
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        firebaseAuth = FirebaseAuth.getInstance();
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "SignOut Successfull", Toast.LENGTH_SHORT).show();
                firebaseAuth.signOut();
            }
        });




        getImages();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        recyclerView.setLayoutManager(layoutManager);
       RecyclerViewAdapter adapter = new RecyclerViewAdapter(MainActivity.this, mNames, mImageUrls);
        recyclerView.setAdapter(adapter);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

         navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        if (firebaseAuth.getCurrentUser()!=null)
        {
            Menu nav_Menu = navigationView.getMenu();
            if(firebaseAuth.getCurrentUser().getEmail().equalsIgnoreCase("asifofficial10@gmail.com") || firebaseAuth.getCurrentUser().getEmail().equalsIgnoreCase("jainyash031@gmail.com") || firebaseAuth.getCurrentUser().getEmail().equalsIgnoreCase(""))
            {
                nav_Menu.findItem(R.id.admin).setVisible(true);
            }
            else
            {
                nav_Menu.findItem(R.id.admin).setVisible(false);
            }

            nav_Menu.findItem(R.id.userLogin).setTitle("Logout");
            View view = getLayoutInflater().inflate(R.layout.nav_header_main,null);
            TextView name = view.findViewById(R.id.welcome);
            TextView number = view.findViewById(R.id.number);
            if(firebaseAuth.getCurrentUser().getPhoneNumber()!=null)
                number.setText(firebaseAuth.getCurrentUser().getPhoneNumber());
            else
                number.setText(firebaseAuth.getCurrentUser().getEmail());
            navigationView.getHeaderView(0).setVisibility(View.GONE);
            navigationView.addHeaderView(view);
            navigationView.setNavigationItemSelectedListener(this);
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child(firebaseAuth.getCurrentUser().getUid()).child("CurrentLocation");
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists())
                    {
                        String locality = dataSnapshot.getValue().toString();
                        String s[] = locality.split("=");
                        nav_Menu.findItem(R.id.location).setTitle(s[1].substring(0,s[1].length()-1));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        else
        {

            Menu nav_Menu = navigationView.getMenu();
            nav_Menu.findItem(R.id.address).setVisible(false);
            nav_Menu.findItem(R.id.admin).setVisible(false);
            nav_Menu.findItem(R.id.address).setVisible(false);
            nav_Menu.findItem(R.id.order).setVisible(false);
            nav_Menu.findItem(R.id.cart).setVisible(false);

        }
    }


    private void getImages(){
        mImageUrls.add("https://gmispace.com/thumbs/Vf7xhl1rq+7WF4w6VUiu8hGWujPlAm34EJJBEet5P0A=assorted-grocery-items.jpg");
        mNames.add("All");

        mImageUrls.add("https://rukminim1.flixcart.com/image/704/704/jdoubgw0/container/k/z/b/ih-9004-ideal-home-original-imaf2jshg4hzvjfm.jpeg?q=70");
        mNames.add("Kitchen");

        mImageUrls.add("https://c8.alamy.com/comp/D0MWK9/boxes-of-washing-powder-on-sale-in-a-uk-supermarket-D0MWK9.jpg");
        mNames.add("Washing");

        mImageUrls.add("https://cdn.chaldal.net/_mpimage/dettol-soap-original-30-gm?src=https%3A%2F%2Feggyolk.chaldal.com%2Fapi%2FPicture%2FRaw%3FpictureId%3D42107&q=low&v=1");
        mNames.add("Bathing");



    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.location) {
            Intent i=new Intent(MainActivity.this, GetLocation.class);
            startActivity(i);
            finish();

        } else if (id == R.id.userLogin) {
            firebaseAuth.signOut();
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.address) {
            Intent intent = new Intent(MainActivity.this, Address_Detail.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.order) {
            Intent intent = new Intent(MainActivity.this, MyOrder.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.cart) {
            Intent intent = new Intent(MainActivity.this, Cart_Main.class);
            startActivity(intent);
            finish();

        }else if (id == R.id.nav_share) {

            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Grocery Store");
            String shareMessage= "\nLet me recommend you this application\n\n";
            shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +"\n\n";
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            startActivity(Intent.createChooser(shareIntent, "choose one"));

        } else if (id == R.id.nav_send) {
            Intent intent = new Intent(MainActivity.this, Feedback.class);
            startActivity(intent);
            finish();

        }
        else if(id == R.id.admin)
        {


                Intent intent = new Intent(MainActivity.this, Shops_Main.class);
                startActivity(intent);
                finish();



        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
