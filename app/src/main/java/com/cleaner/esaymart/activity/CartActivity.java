package com.cleaner.esaymart.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cleaner.esaymart.R;
import com.cleaner.esaymart.adapter.CartAdapter;
import com.cleaner.esaymart.model.Cart;
import com.cleaner.esaymart.utils.MyProgressDialog;
import com.cleaner.esaymart.utils.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.cleaner.esaymart.activity.HomeActivity.cartList;
import static com.cleaner.esaymart.activity.HomeActivity.itemofcart;
import static com.cleaner.esaymart.activity_cleaningBoy.Home_CleaningActivity.s_user_id;
import static com.cleaner.esaymart.adapter.CartAdapter.item;
import static com.cleaner.esaymart.utils.utils.dpToPx;
import static com.cleaner.esaymart.utils.utils.isNetworkAvailable;

public class CartActivity extends AppCompatActivity implements CartAdapter.Product_OnItemClicked, CartAdapter.Product_OnClicked, CartAdapter.Product_On_spin_Clicked {

    private RecyclerView recyclerView;
    private CartAdapter adapter = null;
    int price = 0;
    private TextView totalcartPrice;
    private int product_spin_id;
    private Cart cart;
    private String product_name;
    private String product_disc;
    private String product_price;
    private String product_cut_price;
    private String product_image;
    private int product_id;
    private String product_cat_name;
    private int product_qty;
    private String url = "http://littlejoy.co.in/esaymart/public_html/admin_dryclean/users/cart_get.php";
    private String imageUrl = "http://littlejoy.co.in/esaymart/public_html/admin_dryclean/api/";
    private String cart_delete_url = "http://littlejoy.co.in/esaymart/public_html/admin_dryclean/users/cart_del.php";
    private int key_id;
    private String cartQtyapi = "http://littlejoy.co.in/esaymart/public_html/admin_dryclean/users/cart_qty.php";
    private LinearLayout cart_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initId();
        signupRequest();
//        for (int i = 0; i < cartList.size(); i++) {
//            Cart cart = cartList.get(i);
//            price = price + cart.getProduct_price() * cart.getProduct_quantity();
//        }
//        totalcartPrice.setText("Rs." + price + "");
//        setCartLayout();

        /*****************(Start) code For Card View*****************/

        recyclerView = (RecyclerView) findViewById(R.id.cart_recyclerView);
//        cartList = new ArrayList<>();
        adapter = new CartAdapter(this, cartList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new utils.GridSpacingItemDecoration(2, dpToPx(this, 10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        adapter.setOnClick(this);
        adapter.setClick(this);
        adapter.setOn_Spin_Click(this);

        /*****************(End) code For Card View Vertical*****************/
    }

    private void initId() {
        totalcartPrice = (TextView) findViewById(R.id.totalcartPrice);
        cart_layout = (LinearLayout) findViewById(R.id.cart_layout);
    }

    @Override
    public void Product_onItemClick(int position) {
        Cart cart = cartList.get(position);
        Toast.makeText(this, cart.getProduct_price() + "", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, ProductDetailActivity.class);
        intent.putExtra("img", cart.getProduct_image());
        intent.putExtra("name", cart.getProduct_name());
        intent.putExtra("price", cart.getProduct_price() + "");
        intent.putExtra("id", cart.getProduct_id() + "");
        startActivity(intent);
        Toast.makeText(this, cart.getProduct_id() + "getProduct_id", Toast.LENGTH_LONG).show();
    }

    protected void setCartLayout() {
        LinearLayout layoutCartItems = (LinearLayout) findViewById(R.id.layout_items);
        LinearLayout layoutCartPayments = (LinearLayout) findViewById(R.id.layout_payment);
        LinearLayout layoutCartNoItems = (LinearLayout) findViewById(R.id.layout_cart_empty);

        if (itemofcart > 0) {
            layoutCartNoItems.setVisibility(View.GONE);
            layoutCartItems.setVisibility(View.VISIBLE);
            layoutCartPayments.setVisibility(View.VISIBLE);
        } else {
            layoutCartNoItems.setVisibility(View.VISIBLE);
            layoutCartItems.setVisibility(View.GONE);
            layoutCartPayments.setVisibility(View.GONE);

            Button bStartShopping = (Button) findViewById(R.id.bAddNew);
            bStartShopping.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(CartActivity.this, HomeActivity.class));
                }
            });
        }
    }

    protected void setCart() {

        LinearLayout layoutCartItems = (LinearLayout) findViewById(R.id.layout_items);
        LinearLayout layoutCartPayments = (LinearLayout) findViewById(R.id.layout_payment);
        LinearLayout layoutCartNoItems = (LinearLayout) findViewById(R.id.layout_cart_empty);
        layoutCartNoItems.setVisibility(View.VISIBLE);
        layoutCartItems.setVisibility(View.GONE);
        layoutCartPayments.setVisibility(View.GONE);

        Button bStartShopping = (Button) findViewById(R.id.bAddNew);
        bStartShopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CartActivity.this, HomeActivity.class));
            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }

    @Override
    public void Product_onClick(int position) {
        Log.d("cartList", cartList + "" + cartList.size());
        Log.d("cartList", cartList.get(position) + "" + position);

        Cart cart = cartList.get(position);
        key_id = cart.getProduct_id();
        cartDelete();
//        Toast.makeText(this, "Enter", Toast.LENGTH_LONG).show();
//        price = 0;
//        for (int i = 0; i < cartList.size(); i++) {
////            if (cartList.get(position) == cartList.get(i)) {
////                cart.setProduct_quantity(cart.getProduct_quantity() + item);
////                Toast.makeText(this, "asd" + cart.getProduct_quantity(), Toast.LENGTH_LONG).show();
////            }
////            Toast.makeText(this, "asd" + cart.getProduct_quantity(), Toast.LENGTH_LONG).show();
//            if (position != i) {
//                cart = cartList.get(i);
//                price = price + cart.getProduct_price() * cart.getProduct_quantity();
//            }
//        }
//        totalcartPrice.setText("Rs." + price + "");
    }

    @Override
    public void Product_on_spin_Clicked(int position) {
        Cart cart = cartList.get(position);
        product_spin_id = cart.getProduct_id();
        cartqtyApiCall();
        price = 0;
        Toast.makeText(CartActivity.this, cart.getProduct_id() + cart.getProduct_name() + "clicked_on" + item + "", Toast.LENGTH_LONG).show();

//        for (int i = 0; i < cartList.size(); i++) {
//            if (position == i) {
//                cart = cartList.get(i);
//                price = price + cart.getProduct_price() * item;
//            } else {
//                cart = cartList.get(i);
//                price = price + cart.getProduct_price() * cart.getProduct_quantity();
//            }
////            if (cartList.get(position) == cartList.get(i)) {
////                cart.setProduct_quantity(cart.getProduct_quantity() + item);
////            }
//            totalcartPrice.setText("Rs." + price + "");
//        }
    }

    private void signupRequest() {
        Boolean result = isNetworkAvailable(this);
        if (result) {
            MyProgressDialog.showPDialog(this);
            MyProgressDialog.showPDialog(this);
            cartList.clear();
            RequestQueue queue = Volley.newRequestQueue(this);
            String response = null;
            final String finalResponse = response;

            StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                    new com.android.volley.Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("amit_get", response);
                            try {
                                Object json = new JSONTokener(response).nextValue();
                                JSONObject jsonObject = (JSONObject) json;
                                if (jsonObject.getString("status").equals("STATUS_SUCCESS")) {
                                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                                    Log.d("jsonObject", jsonObject + "");
                                    Log.d("jsonArray", jsonArray + "");
                                    itemofcart = jsonArray.length();

                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject profile = jsonArray.getJSONObject(i);
                                        product_name = profile.getString("pr_name");
                                        product_price = profile.getString("pr_price");
                                        product_image = profile.getString("image");
                                        product_image = imageUrl + product_image;
                                        product_id = Integer.parseInt(profile.getString("product_id"));
                                        product_qty = profile.getInt("cart_qty");

                                        cart = new Cart(product_name, product_id, product_image, Integer.parseInt(product_price), product_qty);
                                        cartList.add(cart);
                                        adapter.notifyDataSetChanged();
                                    }
                                    setCartLayout();

                                    MyProgressDialog.hidePDialog();
                                } else {
                                    MyProgressDialog.hidePDialog();
                                    Toast.makeText(CartActivity.this, "No data Found", Toast.LENGTH_LONG).show();

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new com.android.volley.Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(CartActivity.this, "Network Error", Toast.LENGTH_LONG).show();
                            MyProgressDialog.hidePDialog();
                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();

                    params.put("user_id", s_user_id);

                    return params;
                }
            };
            postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            queue.add(postRequest);
        } else {
            Snackbar.make(cart_layout, "Network Not Found", Snackbar.LENGTH_LONG).show();

        }
    }

    private void cartDelete() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String response = null;
        final String finalResponse = response;

        StringRequest postRequest = new StringRequest(Request.Method.POST, cart_delete_url,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("amit_delete", response);
                        try {
                            Object json = new JSONTokener(response).nextValue();
                            JSONObject jsonObject = (JSONObject) json;
                            if (jsonObject.getString("status").equals("STATUS_SUCCESS")) {
                                setCartLayout();
                            } else {
                                Toast.makeText(CartActivity.this, "No data Found", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(CartActivity.this, "Network Error", Toast.LENGTH_LONG).show();
                        MyProgressDialog.hidePDialog();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("user_id", s_user_id);
                params.put("product_id", key_id + "");

                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(postRequest);

    }

    private void cartqtyApiCall() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String response = null;
        final String finalResponse = response;

        StringRequest postRequest = new StringRequest(Request.Method.POST, cartQtyapi,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("amit_qty", response);
                        try {
                            Object json = new JSONTokener(response).nextValue();
                            JSONObject jsonObject = (JSONObject) json;

                            Toast.makeText(CartActivity.this, jsonObject.getString("data"), Toast.LENGTH_LONG).show();
                            MyProgressDialog.hidePDialog();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new com.android.volley.Response.ErrorListener()

                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(CartActivity.this, "Network Error", Toast.LENGTH_LONG).show();
                        MyProgressDialog.hidePDialog();
                    }
                }
        )

        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("user_id", s_user_id);
                params.put("product_id", product_spin_id + "");
                params.put("cart_qty", item + "");

                return params;
            }
        };
        postRequest.setRetryPolicy(new

                DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(postRequest);

    }

    public void cartAddressDetail(View view) {
        Intent intent = new Intent(this, ProductAddressActivity.class);
        intent.putExtra("id", s_user_id + "");
        intent.putExtra("key", "1000");
        intent.putExtra("type", "pro");
        startActivity(intent);
    }

    public void countinueShooping(View view) {
        startActivity(new Intent(this, HomeActivity.class));
    }
}
