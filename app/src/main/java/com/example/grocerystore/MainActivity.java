package com.example.grocerystore;

import android.animation.ArgbEvaluator;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.grocerystore.Admin.Shops_Main;
import com.example.grocerystore.Main.MyOrder;
import com.example.grocerystore.Main.ShowItemMain;
import com.example.grocerystore.Model.ViewPagerAdapter;
import com.example.grocerystore.Model.ViewPager_Model;
import com.example.grocerystore.User.Add_Address;
import com.example.grocerystore.User.Address_Detail;
import com.example.grocerystore.User.Cart_Main;
import com.example.grocerystore.User.LoginActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    TextView textView;
    FirebaseAuth firebaseAuth;
    NavigationView navigationView;
    Button order;
    ViewPager viewPager;
    ViewPagerAdapter adapter;
    Integer[] color = null;
    List<ViewPager_Model> list;
    ArgbEvaluator argbEvaluator =new ArgbEvaluator();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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
        viewPager.setPadding(130,0,130,0);
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
                }
                else
                {
                    viewPager.setBackgroundColor(color[color.length-1]);
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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

         navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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
        if (firebaseAuth.getCurrentUser()!=null)
        {

            View view = getLayoutInflater().inflate(R.layout.nav_header_main,null);
            TextView name = view.findViewById(R.id.welcome);
            TextView number = view.findViewById(R.id.number);
            number.setText(firebaseAuth.getCurrentUser().getPhoneNumber());
            navigationView.getHeaderView(0).setVisibility(View.GONE);
            navigationView.addHeaderView(view);
            navigationView.setNavigationItemSelectedListener(this);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();



        if (id == R.id.location) {

        } else if (id == R.id.userLogin) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);

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

        } else if (id == R.id.nav_send) {

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
