package com.example.uk.kisanmarket;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FragmentDryVegetable extends Fragment {

    private static final String P_SUB_CATEGORY = "Dry Vegetable";

    public List<Product> productArrayList;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    View view;
    public FragmentDryVegetable() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_dry_vegetable, container, false);
        downloadProduct();
        return view;
    }

    private void downloadProduct() {
        class ProductDownloader extends AsyncTask<Void, Void, String> {
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
                params.put("p_sub_category", P_SUB_CATEGORY);
                //returing the response
                String s = requestHandler.sendPostRequest(URLs.URL_GETPRODUCT, params);
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
                        // Toast.makeText(getActivity(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                        //getting the user from the response
                        JSONArray jsonArray = obj.getJSONArray("products");
                        productArrayList = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String p_image_url = jsonObject.getString("p_image");
                            int p_id = jsonObject.getInt("p_id");
                            String p_name = jsonObject.getString("p_name");
                            String p_category = jsonObject.getString("p_category");
                            String p_sub_category = jsonObject.getString("p_sub_category");
                            int p_quantity = jsonObject.getInt("p_quantity");
                            int p_price = jsonObject.getInt("p_price");
                            String p_owner = jsonObject.getString("p_owner");
                            // calling product constructor
                            Product product = new Product(p_image_url, p_id, p_name, p_category, p_sub_category, p_quantity, p_price, p_owner);
                            productArrayList.add(product);
                        }
                        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewDryVegetable);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                        adapter = new RecyclerViewAdapter(getContext(), productArrayList);
                        recyclerView.setAdapter(adapter);

                    }else{
                        // Toast.makeText(getActivity(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        ProductDownloader productDownloader= new ProductDownloader();
        productDownloader.execute();
    }
}
