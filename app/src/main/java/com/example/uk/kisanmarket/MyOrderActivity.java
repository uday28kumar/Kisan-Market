package com.example.uk.kisanmarket;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyOrderActivity extends AppCompatActivity {

    private String username;
    public List<Order> ordersArrayList;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);

        //adding toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbarMyOrder);
        setSupportActionBar(toolbar);

        User user = SharedPrefManager.getInstance(this).getUser();
        username = user.getUsername();
        downloadOrder();
        ordersArrayList = new ArrayList<>();
       /* for(int i=0;i<5;i++){
            Order order = new Order("123","image_url", 2, "name", 5, 37, "12/3", "status" );
            ordersArrayList.add(order);
        }*/

    }

    private void downloadOrder() {
        class OrderDownloader extends AsyncTask<Void, Void, String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();

                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                params.put("username", username);
                //returing the response
                String s = requestHandler.sendPostRequest(URLs.URL_GETORDER, params);
                return s;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                try {
                    //converting response to json object
                    JSONObject obj = new JSONObject(s);
                    //if no error in response
                    if (!obj.getBoolean("error")) {
                        //getting the user from the response
                        JSONArray jsonArray = obj.getJSONArray("orders");
                        //Toast.makeText(MyOrderActivity.this, obj.getString("message"), Toast.LENGTH_SHORT).show();
                        ordersArrayList = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String order_id = jsonObject.getString("order_id");
                            int p_id = jsonObject.getInt("p_id");
                            String p_name = jsonObject.getString("p_name");
                            String p_image_url = jsonObject.getString("p_image");
                            String address = jsonObject.getString("address");
                            int p_quantity = jsonObject.getInt("quantity");
                            int p_price = jsonObject.getInt("amount");
                            String order_date = jsonObject.getString("order_date");
                            String status = jsonObject.getString("status");
                            // calling order constructor
                            Order order = new Order(order_id, p_image_url, p_id, p_name, p_quantity, p_price, order_date, status, address);
                            ordersArrayList.add(order);
                        }

                        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewMyOrder);
                        recyclerView.setLayoutManager(new LinearLayoutManager(MyOrderActivity.this));
                        adapter = new RecyclerViewAdapterMyOrder(MyOrderActivity.this, ordersArrayList);
                        recyclerView.setAdapter(adapter);
                    }else{
                         Toast.makeText(MyOrderActivity.this, obj.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        OrderDownloader orderDownloader= new OrderDownloader();
        orderDownloader.execute();
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
