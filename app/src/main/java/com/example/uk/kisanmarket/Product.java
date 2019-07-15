package com.example.uk.kisanmarket;

public class Product {
    private final String server ="https://kisanmarket.000webhostapp.com/";
    private String p_image_url;
    private int p_id;
    private String p_name, p_category, p_sub_category;
    private int p_quantity, p_price;
    private String p_owner;

    public Product(String p_image_url, int p_id, String p_name, String p_category, String p_sub_category, int p_quantity, int p_price, String p_owner){
        this.p_image_url = p_image_url;
        this.p_id = p_id;
        this.p_name = p_name;
        this.p_category = p_category;
        this.p_sub_category = p_sub_category;
        this.p_quantity = p_quantity;
        this.p_price = p_price;
        this.p_owner = p_owner;
    }

    public String getP_image_url() { return server+this.p_image_url; }

    public String getP_id() { return Integer.toString(this.p_id); }

    public String getP_name() { return this.p_name; }

    public String getP_category() { return this.p_category; }

    public String getP_sub_category() { return this.p_sub_category; }

    public String getP_quantity() { return Integer.toString(this.p_quantity); }

    public String getP_price() { return Integer.toString(this.p_price); }

    public String getP_owner() { return this.p_owner; }
}
