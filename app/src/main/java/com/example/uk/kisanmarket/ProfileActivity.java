package com.example.uk.kisanmarket;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class ProfileActivity extends AppCompatActivity {

    TextView textViewFullName, textViewUsername, textViewEmail, textViewPhone, textViewAddress, textViewType;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //if the user is not logged in
        //starting the login activity
        if (!SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        //adding toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbarProfile);
        setSupportActionBar(toolbar);

        textViewFullName = (TextView) findViewById(R.id.textViewUserFullName);
        textViewUsername = (TextView) findViewById(R.id.textViewUsername);
        textViewEmail = (TextView) findViewById(R.id.textViewUserEmail);
        textViewPhone = (TextView) findViewById(R.id.textViewUserPhone);
        textViewAddress = (TextView) findViewById(R.id.textViewUserAddress);
        textViewType = (TextView) findViewById(R.id.textViewUserType);

        //getting the current user
        User user = SharedPrefManager.getInstance(this).getUser();

        //setting the values to the textviews
        textViewFullName.setText(user.getName());
        textViewUsername.setText(user.getUsername());
        textViewEmail.setText(user.getEmail());
        textViewPhone.setText(user.getPhone());
        textViewAddress.setText(user.getAddress());
        textViewType.setText(user.getType());

        //when the user presses logout button
        //calling the logout method
        findViewById(R.id.buttonLogout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                SharedPrefManager.getInstance(getApplicationContext()).logout();
            }
        });
    }

    //Adding Option menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.activity_close) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
