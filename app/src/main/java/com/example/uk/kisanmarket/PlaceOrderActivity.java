package com.example.uk.kisanmarket;

/* Developed by Uday Kumar (205116027)
    CA Department, MCA, NIT Trichy
    11/08/2018
 */

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class PlaceOrderActivity extends AppCompatActivity {

    private EditText userName, userAddress, userPhone;
    private TextView productId, productName, productPrice, productOwner, productQuantity;
    private ImageView productImage;
    private TextView totalPrice, deliveryCharge, finalPrice;
    private Button buttonChangeAddress, buttonPlaceOrder;
    private String p_id, p_name, p_price, p_owner, quantity, p_image_url;
    private int total, final_price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_order);

        Intent intent = getIntent();
        final String name = intent.getExtras().getString("name");
        final String address = intent.getExtras().getString("address");
        final String phone = intent.getExtras().getString("phone");
        p_image_url = intent.getExtras().getString("p_image_url");
        p_id = intent.getExtras().getString("p_id");
        p_name = intent.getExtras().getString("p_name");
        p_price = intent.getExtras().getString("p_price");
        p_owner = intent.getExtras().getString("p_owner");
        quantity = intent.getExtras().getString("quantity");


        //getting and setting address
        userName = (EditText) findViewById(R.id.editTextName);
        userAddress = (EditText) findViewById(R.id.editTextAddress);
        userPhone = (EditText) findViewById(R.id.editTextPhone);
        buttonChangeAddress = (Button) findViewById(R.id.buttonChangeAddress);
        userName.setText(name);
        userAddress.setText(address);
        userPhone.setText(phone);
        buttonChangeAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userName.setText("");
                userAddress.setText("");
                userPhone.setText("");
                userName.requestFocus();
            }
        });

        //getting and setting product details
        productId = (TextView) findViewById(R.id.textViewProductId);
        productName = (TextView) findViewById(R.id.textViewProductName);
        productPrice = (TextView) findViewById(R.id.textViewProductPrice);
        productOwner = (TextView) findViewById(R.id.textViewProductOwner);
        productImage = (ImageView) findViewById(R.id.imageViewProductImage);
        productId.setText("Product id: "+p_id);
        productName.setText("Product Name: "+p_name);
        productPrice.setText("Product Price: Rs. "+p_price);
        productOwner.setText("Product Owner: "+p_owner);
        Picasso.get()
                .load(p_image_url)
                .resize(150, 300)
                .centerCrop()
                .into(productImage);

        //getting and setting price details
        productQuantity = (TextView) findViewById(R.id.textViewBuyQuantity);
        totalPrice = (TextView) findViewById(R.id.textViewTotalPrice);
        deliveryCharge = (TextView) findViewById(R.id.textViewDeliveryCharge);

        productQuantity.setText("Quantity: "+quantity);
        int q = Integer.parseInt(quantity);
        int p = Integer.parseInt(p_price);
        total = q * p;
        final_price = (total + (q*2));
        totalPrice.setText("Total Amount: Rs." + total);
        deliveryCharge.setText("Delivery Charge: " + q * 2);

        //setting final price
        finalPrice = (TextView) findViewById(R.id.textViewFinalPrice);
        finalPrice.setText("Rs. " + final_price + "/-");

        //place order action
        buttonPlaceOrder = (Button) findViewById(R.id.buttonPlaceOrder);
        buttonPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                placeOrder();
            }
        });
    }

    private void placeOrder() {
        final String user_FullName = userName.getText().toString().trim();
        final String user_Address = userAddress.getText().toString().trim();
        final String user_Phone = userPhone.getText().toString().trim();
        final String product_id = p_id;
        final String product_name = p_name;
        final String product_image = p_image_url;

        final User user = SharedPrefManager.getInstance(this).getUser();

        //first we will do the validations
        if(TextUtils.isEmpty(user_FullName)){
            userName.setError("Please enter full name");
            userName.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(user_Address)) {
            userAddress.setError("Please enter username");
            userAddress.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(user_Phone)) {
            userPhone.setError("Please enter your email");
            userPhone.requestFocus();
            return;
        }

        final String consumer_address = user_FullName+", "+user_Address+", "+user_Phone;

        //if it passes all the validations

        class PlaceOrder extends AsyncTask<Void, Void, String> {

            private ProgressBar progressBar;

            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();

                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                params.put("username", user.getUsername());
                params.put("p_id", product_id);
                params.put("p_name", product_name);
                params.put("p_image", product_image);
                params.put("address", consumer_address);
                params.put("quantity", quantity);
                params.put("amount", Integer.toString(final_price));
                //returing the response
                return requestHandler.sendPostRequest(URLs.URL_PLACEORDER, params);
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
        PlaceOrder placeOrder = new PlaceOrder();
        placeOrder.execute();
    }
}
