package com.example.uk.kisanmarket;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class ProductViewActivity extends AppCompatActivity {

    Toolbar toolbar;
    private TextView productId, productName, productPrice, productOwner, productQuantity;
    private ImageView productImage;
    private Button buttonBuy;
    private EditText enteredQuantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_view);

        //adding toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbarProductView);
        setSupportActionBar(toolbar);

        productImage = (ImageView) findViewById(R.id.imageViewProductImage);
        productId = (TextView) findViewById(R.id.textViewProductId);
        productName = (TextView) findViewById(R.id.textViewProductName);
        productPrice = (TextView) findViewById(R.id.textViewProductPrice);
        productOwner = (TextView) findViewById(R.id.textViewProductOwner);
        productQuantity = (TextView) findViewById(R.id.textViewQuantity);
        enteredQuantity = (EditText) findViewById(R.id.editTextQuantity);

        Intent intent = getIntent();
        final String P_image_url = intent.getExtras().getString("productImage");
        final String P_id = intent.getExtras().getString("productId");
        final String P_name = intent.getExtras().getString("productName");
        final String P_price = intent.getExtras().getString("productPrice");
        final String P_quantity = intent.getExtras().getString("productQuantity");
        final String P_owner = intent.getExtras().getString("productOwner");

        Picasso.get()
                .load(P_image_url)
                .resize(600, 400)
                .centerCrop()
                .into(productImage);

        productId.setText(P_id);
        productName.setText(P_name);
        productPrice.setText("Rs."+P_price+"/Unit*");
        productOwner.setText(P_owner);
        productQuantity.setText("< "+P_quantity);

        final User user = SharedPrefManager.getInstance(this).getUser();
        buttonBuy = (Button) findViewById(R.id.buttonBuy);
        buttonBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String quantity = enteredQuantity.getText().toString().trim();
                if(TextUtils.isEmpty(quantity) || Integer.parseInt(quantity) > Integer.parseInt(P_quantity)){
                    Toast.makeText(ProductViewActivity.this, "Invalid quantity!", Toast.LENGTH_SHORT).show();
                    enteredQuantity.setText("");
                    enteredQuantity.requestFocus();
                    return;
                }
                finish();
                Intent intent = new Intent(ProductViewActivity.this, PlaceOrderActivity.class);
                intent.putExtra("name", user.getName());
                intent.putExtra("address", user.getAddress());
                intent.putExtra("phone", user.getPhone());
                intent.putExtra("p_image_url", P_image_url);
                intent.putExtra("p_id", P_id);
                intent.putExtra("p_name", P_name);
                intent.putExtra("p_price", P_price);
                intent.putExtra("p_owner", P_owner);
                intent.putExtra("quantity", quantity);
                startActivity(intent);
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
