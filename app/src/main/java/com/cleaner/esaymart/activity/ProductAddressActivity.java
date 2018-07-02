package com.cleaner.esaymart.activity;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
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
import com.cleaner.esaymart.model.Cart;
import com.cleaner.esaymart.utils.MyProgressDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.HashMap;
import java.util.Map;

import static com.cleaner.esaymart.activity.FirstActivity.s_Address;
import static com.cleaner.esaymart.activity.HomeActivity.cartList;
import static com.cleaner.esaymart.activity.HomeActivity.itemofcart;
import static com.cleaner.esaymart.activity_cleaningBoy.Home_CleaningActivity.s_user_id;
import static com.cleaner.esaymart.activity_cleaningBoy.Home_CleaningActivity.s_user_name;
import static com.cleaner.esaymart.utils.utils.isNetworkAvailable;

public class ProductAddressActivity extends AppCompatActivity {

    private EditText user_address, user_name;
    private String key_id, key_data, key_type;
    private int cart_price;
    private int cart_qty;
    private String url = "http://littlejoy.co.in/esaymart/public_html/admin_dryclean/users/cart_get.php";
    private int totalPrice = 0;
    private TextView text_totalPrice;
    private LinearLayout add_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_address);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getId();
        getDatafromIntent();
        signupRequest();
        user_address.setText(s_Address);
        user_name.setText(s_user_name);
    }

    private void getId() {
        user_address = (EditText) findViewById(R.id.user_address);
        user_name = (EditText) findViewById(R.id.user_name);
        add_layout = (LinearLayout) findViewById(R.id.add_layout);
        text_totalPrice = (TextView) findViewById(R.id.text_totalPrice);
    }

    private void getDatafromIntent() {
        if (getIntent() != null) {
            key_id = getIntent().getStringExtra("id");
            key_data = getIntent().getStringExtra("key");
            key_type = getIntent().getStringExtra("type");
        }
    }

//    public void changeAddress(View view) {
//        startActivity(new Intent(this, ProductBookingDetailActivity.class));
//    }

    public void paymentPage(View view) {
        Intent intent = new Intent(this, PaymentGatewayActivity.class);
        intent.putExtra("id", key_id + "");
        intent.putExtra("key", key_data);
        intent.putExtra("type", key_type);
        startActivity(intent);
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

                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject profile = jsonArray.getJSONObject(i);
                                        cart_price = profile.getInt("pr_price");
                                        cart_qty = profile.getInt("cart_qty");
                                        totalPrice = totalPrice + cart_price * cart_qty;
                                    }
                                    text_totalPrice.setText("Rs. " + totalPrice + "");
                                    MyProgressDialog.hidePDialog();
                                } else {
                                    MyProgressDialog.hidePDialog();
                                    Toast.makeText(ProductAddressActivity.this, "No data Found", Toast.LENGTH_LONG).show();

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new com.android.volley.Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(ProductAddressActivity.this, "Network Error", Toast.LENGTH_LONG).show();
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
            Snackbar.make(add_layout, "Network Not Found", Snackbar.LENGTH_LONG).show();

        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }
}
