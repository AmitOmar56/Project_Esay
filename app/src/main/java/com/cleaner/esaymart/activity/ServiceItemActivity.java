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
import android.widget.LinearLayout;
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

public class ServiceItemActivity extends AppCompatActivity implements ServiceItemAdapter.Product_OnItemClicked {
    private RecyclerView recyclerView;
    private ServiceItemAdapter adapter = null;
    private List<ServiceItem> serviceItemList;
    private String service_name;
    private int service_price;
    private String service_disc;
    private String service_image;
    private int service_id;
    private int service_cat_id;
    private int key_id;
    private String imagePath = "http://littlejoy.co.in/esaymart/public_html/admin_dryclean/api/";
    private String url = "http://littlejoy.co.in/esaymart/public_html/admin_dryclean/users/service.php";
    private ServiceItem serviceItem;
    private LinearLayout servic_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_item);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        servic_layout=(LinearLayout)findViewById(R.id.servic_layout);
        getdatafromIntent();
        /*****************(Start) code For Card View*****************/

        recyclerView = (RecyclerView) findViewById(R.id.serviceItem_recyclerView);
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

    private void getdatafromIntent() {
        if (getIntent() != null) {
            key_id = Integer.parseInt(getIntent().getStringExtra("id"));
        }
    }

    /**
     * Adding few albums for testing
     */
    @Override
    public void Product_onItemClick(int position) {
        ServiceItem serviceItem = serviceItemList.get(position);

        Toast.makeText(this, "clicked", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, ServiceDetailActivity.class);
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
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            Log.d("jsonObject", jsonObject + "");
                            Log.d("jsonArray", jsonArray + "");

                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject profile = jsonArray.getJSONObject(i);
                                service_name = profile.getString("ser_name");
                                service_price = profile.getInt("ser_price");
                                service_disc = profile.getString("ser_desc");
                                service_image = profile.getString("image");
                                service_image = imagePath + service_image;
                                service_id = Integer.parseInt(profile.getString("service_id"));
                                service_cat_id = Integer.parseInt(profile.getString("car_id"));

                                if (key_id == service_cat_id) {
                                    serviceItem = new ServiceItem(service_disc, service_name, service_id, service_image, service_price);
                                    serviceItemList.add(serviceItem);
                                }
                                adapter.notifyDataSetChanged();

                            }
                            MyProgressDialog.hidePDialog();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ServiceItemActivity.this, "Network Error", Toast.LENGTH_LONG).show();

                        MyProgressDialog.hidePDialog();
                    }
                }
        );
        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(postRequest);
        } else {
            Snackbar.make(servic_layout, "Network Not Found", Snackbar.LENGTH_LONG).show();

        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }

}
