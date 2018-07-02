package com.cleaner.esaymart.activity;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;


import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.cleaner.esaymart.R;
import com.cleaner.esaymart.model.Cart;
import com.cleaner.esaymart.utils.MyProgressDialog;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.HashMap;
import java.util.Map;

import static com.cleaner.esaymart.activity.HomeActivity.cartList;
import static com.cleaner.esaymart.activity_cleaningBoy.Home_CleaningActivity.s_user_id;
import static com.cleaner.esaymart.utils.utils.isNetworkAvailable;

public class ProductDetailActivity extends AppCompatActivity {

    private ImageView product_detail_image;
    private TextView p_name, p_price, p_disc;
    private String product_name;
    private String product_price;
    private String product_disc;
    private String product_image;
    private int product_id;
    private int key = 0;
    private Cart cart;
    private LinearLayout p_leniar;
    private String cartUrl = "http://littlejoy.co.in/esaymart/public_html/admin_dryclean/users/cart.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getId();
        getDataByIntent();

        p_name.setText(product_name);
        p_price.setText("Rs. " + product_price);
        p_disc.setText(product_disc);
        Glide.with(this).load(product_image).into(product_detail_image);
    }

    private void getDataByIntent() {
        if (getIntent() != null) {
            product_id = Integer.parseInt(getIntent().getStringExtra("id"));
            product_name = getIntent().getStringExtra("name");
            product_price = getIntent().getStringExtra("price");
            product_image = getIntent().getStringExtra("img");
            product_disc = getIntent().getStringExtra("disc");
        }
    }

    private void getId() {
        product_detail_image = (ImageView) findViewById(R.id.product_detail_image);
        p_name = (TextView) findViewById(R.id.p_name);
        p_price = (TextView) findViewById(R.id.p_price);
        p_disc = (TextView) findViewById(R.id.p_disc);
        p_leniar = (LinearLayout) findViewById(R.id.p_leniar);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }

    public void product_book_now(View view) {
//        for (Cart cart : cartList) {
//            if (cart.getProduct_id() == product_id) {
//                key = 1;
//                startActivity(new Intent(this, CartActivity.class));
//                break;
//            }
//        }
//
//        if (key == 0) {
//            cart = new Cart(product_name, product_id, product_image, Integer.parseInt(product_price), 1);
//            cartList.add(cart);
//            itemofcart++;
//            startActivity(new Intent(this, CartActivity.class));
//        }
//        key = 0;
        addtocartApiCall();
        Log.d("result->>>>>>", s_user_id + product_id);

    }

    private void addtocartApiCall() {
        Boolean result = isNetworkAvailable(this);
        if (result) {
            RequestQueue queue = Volley.newRequestQueue(this);
            String response = null;
            final String finalResponse = response;

            StringRequest postRequest = new StringRequest(Request.Method.POST, cartUrl,
                    new com.android.volley.Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("amit_cart", response);
                            try {
                                Object json = new JSONTokener(response).nextValue();
                                JSONObject jsonObject = (JSONObject) json;
                                startActivity(new Intent(ProductDetailActivity.this, CartActivity.class));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new com.android.volley.Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(ProductDetailActivity.this, "Network Error", Toast.LENGTH_LONG).show();
                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();

                    params.put("user_id", s_user_id);
                    params.put("product_id", product_id + "");

                    return params;
                }
            };
            postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            queue.add(postRequest);
        } else {
            Snackbar.make(p_leniar, "Network Not Found", Snackbar.LENGTH_LONG).show();
        }
    }

}
