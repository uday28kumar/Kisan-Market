package com.example.uk.kisanmarket;
/* Developed by Uday Kumar (205116027)
    CA Department, MCA, NIT Trichy
    11/08/2018
 */
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.text.Layout;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private User user;

    private TextView textViewFullName, textViewEmail;
    private ViewPager viewPager;
    private TextView userFullName;
    private TextView briefAbout;
    private TextView viewProfile, viewOrder;
    private ImageView register, addProduct, onlineSupport, viewBalance, about, notification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        //adding slider
        viewPager = (ViewPager) findViewById(R.id.viewPagerMainSlider);
        ViewPageAdapterMainSlider viewPageAdapterMainSlider = new ViewPageAdapterMainSlider(this);
        viewPager.setAdapter(viewPageAdapterMainSlider);

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new SliderTimer(), 2000, 4000);

        //setting hi user name
        //getting the current user
        user = SharedPrefManager.getInstance(this).getUser();
        userFullName = (TextView) findViewById(R.id.textViewUserFullName);
        userFullName.setText("Hi "+user.getName());

        //view profile and view order
        viewProfile = (TextView) findViewById(R.id.textViewUserProfile);
        viewOrder = (TextView) findViewById(R.id.textViewUserOrder);

        viewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });

        viewOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MyOrderActivity.class);
                startActivity(intent);
            }
        });

        //register button
        register = (ImageView) findViewById(R.id.imageViewAddUser);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        //add product
        addProduct = (ImageView) findViewById(R.id.imageViewAddProduct);
        addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!SharedPrefManager.getInstance(MainActivity.this).isLoggedIn()){
                    Toast.makeText(MainActivity.this, "Please login!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!user.getType().equals("Farmer")){
                    Toast.makeText(MainActivity.this, "Please login as a Farmer!", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(MainActivity.this, UploadProductActivity.class);
                startActivity(intent);
            }
        });

        //online support
        onlineSupport = (ImageView) findViewById(R.id.imageViewOnlineSupport);
        onlineSupport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(MainActivity.this, OnlineSupportActivity.class);
                startActivity(intent);
            }
        });

        //view balance
        viewBalance = (ImageView) findViewById(R.id.imageViewBalance);
        viewBalance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(MainActivity.this, ViewBalanceActivity.class);
                startActivity(intent);
            }
        });

        //about
        about = (ImageView) findViewById(R.id.imageViewAbout);
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(MainActivity.this, AboutActivity.class);
                startActivity(intent);
            }
        });

        //notification
        notification = (ImageView) findViewById(R.id.imageViewNotification);
        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(MainActivity.this, NotificationActivity.class);
                startActivity(intent);
            }
        });

        briefAbout = (TextView) findViewById(R.id.textViewBriefAbout);
        briefAbout.setText("Kisan Market is an online platform for farmers and all other agriculture stake holders. Farmers can sell/buy crops, natural manures, cattle etc using this e-Trading Platform. Farm Produce Buyers can search for crops listed by farmers.");


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //setting username and email to nav_header
        textViewFullName = (TextView) navigationView.getHeaderView(0).findViewById(R.id.textViewUserFullName);
        textViewEmail = (TextView) navigationView.getHeaderView(0).findViewById(R.id.textViewUserEmail);

        textViewFullName.setText(user.getName());
        textViewEmail.setText(user.getEmail());
    }

    public class SliderTimer extends TimerTask{


        @Override
        public void run() {
            MainActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(viewPager.getCurrentItem() == 0){
                        viewPager.setCurrentItem(1);
                    }else if(viewPager.getCurrentItem() == 1){
                        viewPager.setCurrentItem(2);
                    }else if(viewPager.getCurrentItem() == 2){
                        viewPager.setCurrentItem(3);
                    }else if(viewPager.getCurrentItem() == 3){
                        viewPager.setCurrentItem(4);
                    }else if(viewPager.getCurrentItem() == 4){
                        viewPager.setCurrentItem(5);
                    }else{
                        viewPager.setCurrentItem(0);
                    }
                }
            });
        }
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

        if(id ==  R.id.nav_home){
            Intent intent =  new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        if (id == R.id.nav_cereals) {
            Intent intent=new Intent(this, CerealActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_vegetables) {
            Intent intent =  new Intent(this, VegetableActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_seeds) {
            Intent intent =  new Intent(this, SeedActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_fruits) {
            Intent intent =  new Intent(this, FruitActivity.class);
            startActivity(intent);
        }else if(id ==  R.id.nav_order){
            Intent intent =  new Intent(this, MyOrderActivity.class);
            startActivity(intent);
        }else if (id == R.id.nav_account) {
            Intent intent=new Intent(this, LoginActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
