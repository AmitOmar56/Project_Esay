package com.cleaner.esaymart.activity;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
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
import com.cleaner.esaymart.adapter.ServiceItemAdapter;
import com.cleaner.esaymart.model.ServiceItem;
import com.cleaner.esaymart.utils.MyProgressDialog;
import com.cleaner.esaymart.utils.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.List;

import static com.cleaner.esaymart.utils.utils.dpToPx;
import static com.cleaner.esaymart.utils.utils.isNetworkAvailable;

public class ProductItemActivity extends AppCompatActivity implements ServiceItemAdapter.Product_OnItemClicked {

    private RecyclerView recyclerView;
    private ServiceItemAdapter adapter = null;
    private List<ServiceItem> serviceItemList;
    private String product_name;
    private int product_price;
    private String product_disc;
    private String product_image;
    private int product_id;
    private String cat_id;
    private LinearLayout p_item_layout;
    private String imagePath = "http://littlejoy.co.in/esaymart/public_html/admin_dryclean/api/";
    private String url = "http://littlejoy.co.in/esaymart/public_html/admin_dryclean/users/product.php";
    private ServiceItem serviceItem;
    private TextView product_noData;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_item);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getDataFromIntent();
        p_item_layout = (LinearLayout) findViewById(R.id.p_item_layout);
        product_noData = (TextView) findViewById(R.id.product_noData);
        /*****************(Start) code For Card View*****************/

        recyclerView = (RecyclerView) findViewById(R.id.productItem_recyclerView);
        serviceItemList = new ArrayList<>();
        adapter = new ServiceItemAdapter(this, serviceItemList);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new utils.GridSpacingItemDecoration(2, dpToPx(this, 10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        adapter.setOnClick(this);
        signupRequest();
        /*****************(End) code For Card View Vertical*****************/

    }

    private void getDataFromIntent() {
        if (getIntent() != null) {
            id = getIntent().getStringExtra("id");
        }
    }

    /**
     * Adding few albums for testing
     */
    @Override
    public void Product_onItemClick(int position) {
        ServiceItem serviceItem = serviceItemList.get(position);
        Toast.makeText(this, "clicked", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, ProductDetailActivity.class);
        intent.putExtra("img", serviceItem.getProduct_image());
        intent.putExtra("name", serviceItem.getProduct_name());
        intent.putExtra("price", serviceItem.getProduct_price() + "");
        intent.putExtra("disc", serviceItem.getProduct_discription());
        intent.putExtra("id", serviceItem.getProduct_id() + "");
        startActivity(intent);
    }

    /**
     * Adding few albums for testing
     */

    private void signupRequest() {
        Boolean result = isNetworkAvailable(this);
        if (result) {
            MyProgressDialog.showPDialog(this);

            RequestQueue queue = Volley.newRequestQueue(this);
            String response = null;
            final String finalResponse = response;

            StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                    new com.android.volley.Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("amit", response);
                            try {
                                Object json = new JSONTokener(response).nextValue();
                                JSONObject jsonObject = (JSONObject) json;
                                if (jsonObject.getString("status").equals("1") || jsonObject.getString("status").equals("STATUS_SUCCESS")) {

                                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                                    for (int i = 0; i < jsonArray.length(); i++) {

                                        JSONObject profile = jsonArray.getJSONObject(i);
                                        product_name = profile.getString("pr_name");
                                        product_price = profile.getInt("pr_price");
                                        product_disc = profile.getString("pr_desc");
                                        product_image = profile.getString("image");
                                        product_image = imagePath + product_image;
                                        product_id = Integer.parseInt(profile.getString("product_id"));
                                        cat_id = profile.getString("cat_id");
                                        if ((id.equals(cat_id))) {
                                            serviceItem = new ServiceItem(product_disc, product_name, product_id, product_image, product_price);
                                            serviceItemList.add(serviceItem);
                                        } else {
                                            MyProgressDialog.hidePDialog();
                                            product_noData.setVisibility(View.VISIBLE);
                                        }
                                        adapter.notifyDataSetChanged();
                                    }
                                    MyProgressDialog.hidePDialog();
                                } else {
                                    MyProgressDialog.hidePDialog();
                                    product_noData.setVisibility(View.VISIBLE);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new com.android.volley.Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(ProductItemActivity.this, "Network Error", Toast.LENGTH_LONG).show();

                            MyProgressDialog.hidePDialog();
                        }
                    }
            );
            postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            queue.add(postRequest);
        } else {
            Snackbar.make(p_item_layout, "Network Not Found", Snackbar.LENGTH_LONG).show();

        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }

}
