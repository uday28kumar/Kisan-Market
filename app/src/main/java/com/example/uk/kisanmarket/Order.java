package com.example.uk.kisanmarket;

public class Order {
    private String order_id;
    private String p_image_url, p_name;
    private int p_id;
    private int p_quantity, p_price;
    private String order_date, status;
    private String address;

    public Order(String order_id, String p_image_url, int p_id, String p_name, int p_quantity, int p_price, String order_date, String status, String address){
        this.order_id =order_id;
        this.p_image_url = p_image_url;
        this.p_id = p_id;
        this.p_name = p_name;
        this.p_quantity = p_quantity;
        this.p_price = p_price;
        this.order_date = order_date;
        this.status = status;
        this.address =address;
    }
    public String getOrder_id() { return this.order_id; }

    public String getP_image_url() { return this.p_image_url; }

    public String getP_id() { return Integer.toString(this.p_id); }

    public String getP_name() { return this.p_name; }

    public String getP_quantity() { return Integer.toString(this.p_quantity); }

    public String getP_price() { return Integer.toString(this.p_price); }

    public String getOrder_date() { return this.order_date; }

    public String getStatus() { return status; }

    public String getAddress() {
        return address;
    }
}
