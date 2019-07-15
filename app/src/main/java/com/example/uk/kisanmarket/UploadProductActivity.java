package com.example.uk.kisanmarket;

/* Developed by Uday Kumar (205116027)
    CA Department, MCA, NIT Trichy
    11/08/2018
 */

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

public class UploadProductActivity extends AppCompatActivity implements View.OnClickListener {

    private int PICK_IMAGE_REQUEST = 1;

    private ImageView imageViewProductImage;
    private Button buttonChoose;
    private EditText editTextProductName;
    private Spinner spinnerProductCategory;
    private Spinner spinnerProductSubCategory;
    private EditText editTextProductQuantity;
    private EditText editTextProductPrice;
    private TextView textViewProductOwner;
    private Button buttonUploadProduct;

    Toolbar toolbar;

    private Bitmap bitmap;
    private Uri filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_product);


        buttonChoose = (Button) findViewById(R.id.buttonChoose);
        imageViewProductImage = (ImageView) findViewById(R.id.imageViewProductImage);
        editTextProductName = (EditText) findViewById(R.id.editTextProductName);
        spinnerProductCategory = (Spinner) findViewById(R.id.spinnerProductCategory);
        spinnerProductSubCategory = (Spinner) findViewById(R.id.spinnerProductSubCategory);
        editTextProductQuantity = (EditText) findViewById(R.id.editTextProductQuantity);
        editTextProductPrice = (EditText) findViewById(R.id.editTextProductPrice);
        textViewProductOwner = (TextView) findViewById(R.id.textViewProductOwner);
        buttonUploadProduct = (Button) findViewById(R.id.buttonUploadProduct);

        User user = SharedPrefManager.getInstance(this).getUser();
        textViewProductOwner.setText(user.getUsername());

        //adding toolbar
        toolbar = (Toolbar) findViewById(R.id.toolBar_registration);
        setSupportActionBar(toolbar);

        buttonChoose.setOnClickListener(this);
        buttonUploadProduct.setOnClickListener(this);
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageViewProductImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void uploadImage(){
        final String productImage = getStringImage(bitmap);
        final String productName = editTextProductName.getText().toString().trim();
        final String productCategory = spinnerProductCategory.getSelectedItem().toString().trim();
        final String productSubCategory = spinnerProductSubCategory.getSelectedItem().toString().trim();
        final String productQuantity = editTextProductQuantity.getText().toString().trim();
        final String productPrice = editTextProductPrice.getText().toString().trim();
        final String productOwner =  textViewProductOwner.getText().toString().trim();

        // validation
        if(productImage.equals("")){
            Toast.makeText(this, "Please select image!", Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(productName)){
            editTextProductName.setError("Enter product name! ");
            editTextProductName.requestFocus();
            return;
        }

        if(TextUtils.isEmpty(productQuantity)){
            editTextProductQuantity.setError("Enter product quantity! ");
            editTextProductQuantity.requestFocus();
            return;
        }

        if(TextUtils.isEmpty(productPrice)){
            editTextProductPrice.setError("Enter product price");
            editTextProductPrice.requestFocus();
            return;
        }

        class UploadImage extends AsyncTask<Void,Void,String> {

            private ProgressBar progressBar;

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
                        //starting current activity
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Error in registration!", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                RequestHandler requestHandler = new RequestHandler();

                HashMap<String,String> params = new HashMap<>();
                params.put("p_image", productImage);
                params.put("p_name", productName);
                params.put("p_category", productCategory);
                params.put("p_sub_category", productSubCategory);
                params.put("p_quantity", productQuantity);
                params.put("p_price", productPrice);
                params.put("p_owner", productOwner);
                String result = requestHandler.sendPostRequest(URLs.URL_UPLOAD,params);
                return result;
            }
        }

        UploadImage ui = new UploadImage();
        ui.execute();
    }

    @Override
    public void onClick(View v) {
        if (v == buttonChoose) {
            showFileChooser();
        }

        if(v == buttonUploadProduct){
            if(imageViewProductImage.getDrawable()==null){
                Toast.makeText(this, "Please select product image! ", Toast.LENGTH_SHORT).show();
                imageViewProductImage.requestFocus();
                return;
            }
            uploadImage();
        }
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
