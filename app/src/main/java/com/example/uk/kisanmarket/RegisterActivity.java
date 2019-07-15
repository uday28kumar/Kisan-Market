package com.example.uk.kisanmarket;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    EditText editTextName, editTextUsername, editTextEmail, editTextPassword, editTextCPassword, editTextPhone, editTextAddress;
    RadioGroup radioGroupType;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //adding toolbar
        toolbar = (Toolbar) findViewById(R.id.toolBar_registration);
        setSupportActionBar(toolbar);

        //if the user is already logged in we will directly start the profile activity
        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, ProfileActivity.class));
            return;
        }
        editTextName=(EditText) findViewById(R.id.editTextName);
        editTextUsername = (EditText) findViewById(R.id.editTextUsername);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        editTextCPassword=(EditText) findViewById(R.id.editTextCPassword);
        editTextPhone=(EditText) findViewById(R.id.editTextPhone);
        editTextAddress=(EditText) findViewById(R.id.editTextAddress);
        radioGroupType = (RadioGroup) findViewById(R.id.redioGroupType);


        findViewById(R.id.buttonRegister).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if user pressed on button register
                //here we will register the user to server
                registerUser();
            }
        });

        findViewById(R.id.textViewLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if user pressed on login
                //we will open the login screen
                finish();
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });

    }

    private void registerUser() {
        final String name=editTextName.getText().toString().trim();
        final String username = editTextUsername.getText().toString().trim();
        final String email = editTextEmail.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();
        final String cpassword=editTextCPassword.getText().toString().trim();
        final String phone=editTextPhone.getText().toString().trim();
        final String address=editTextAddress.getText().toString().trim();
        final String type = ((RadioButton) findViewById(radioGroupType.getCheckedRadioButtonId())).getText().toString();

        //first we will do the validations
        if(TextUtils.isEmpty(name)){
            editTextName.setError("Please enter full name");
            editTextName.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(username)) {
            editTextUsername.setError("Please enter username");
            editTextUsername.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(email)) {
            editTextEmail.setError("Please enter your email");
            editTextEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Enter a valid email");
            editTextEmail.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            editTextPassword.setError("Enter a password");
            editTextPassword.requestFocus();
            return;
        }

        if(!password.equals(cpassword)){
            editTextCPassword.setError("Password not matched");
            editTextCPassword.requestFocus();
            return;
        }

        if(TextUtils.isEmpty(phone)){
            editTextPhone.setError("Please enter your Mobile number");
            editTextPhone.requestFocus();
            return;
        }

        if(!Patterns.PHONE.matcher(phone).matches()){
            editTextPhone.setError("Enter a valid Mobile number");
            editTextPhone.requestFocus();
            return;
        }

        if(TextUtils.isEmpty(address)){
            editTextAddress.setError("Please enter your address");
            editTextAddress.requestFocus();
            return;
        }
        //if it passes all the validations

        class RegisterUser extends AsyncTask<Void, Void, String> {

            private ProgressBar progressBar;

            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();

                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                params.put("name", name);
                params.put("username", username);
                params.put("email", email);
                params.put("password", password);
                params.put("phone", phone);
                params.put("address", address);
                params.put("type", type);

                //returing the response
                return requestHandler.sendPostRequest(URLs.URL_REGISTER, params);
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //displaying the progress bar while user registers on the server
                progressBar = (ProgressBar) findViewById(R.id.progressBar);
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                //hiding the progressbar after completion
                progressBar.setVisibility(View.GONE);

                try {
                    //converting response to json object
                    JSONObject obj = new JSONObject(s);

                    //if no error in response
                    if (!obj.getBoolean("error")) {
                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();

                        //getting the user from the response
                        JSONObject userJson = obj.getJSONObject("user");

                        //creating a new user object
                        User user = new User(
                                userJson.getString("name"),
                                userJson.getString("username"),
                                userJson.getString("email"),
                                userJson.getString("phone"),
                                userJson.getString("address"),
                                userJson.getString("type")
                        );

                        //storing the user in shared preferences
                        SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);

                        //starting the profile activity
                        finish();
                        //startActivity(new Intent(RegistrationActivity.this, ProfileActivity.class));
                    } else {
                        Toast.makeText(getApplicationContext(), "Something went wrong!", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        //executing the async task
        RegisterUser ru = new RegisterUser();
        ru.execute();
    }

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
