package com.cleaner.esaymart.model;

/**
 * Created by user on 12/22/2017.
 */

public class Cart {
    private String product_name;
    private int product_id;
    private String product_image;
    private int product_price;
    private int product_quantity;

    public Cart( String product_name, int product_id, String product_image, int product_price, int product_quantity) {
        this.product_name = product_name;
        this.product_id = product_id;
        this.product_image = product_image;
        this.product_price = product_price;
        this.product_quantity = product_quantity;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public String getProduct_image() {
        return product_image;
    }

    public void setProduct_image(String product_image) {
        this.product_image = product_image;
    }

    public int getProduct_price() {
        return product_price;
    }

    public void setProduct_price(int product_price) {
        this.product_price = product_price;
    }

    public int getProduct_quantity() {
        return product_quantity;
    }

    public void setProduct_quantity(int product_quantity) {
        this.product_quantity = product_quantity;
    }
}
