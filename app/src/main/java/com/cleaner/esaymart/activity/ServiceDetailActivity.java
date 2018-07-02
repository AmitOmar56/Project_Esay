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
import com.cleaner.esaymart.utils.MyProgressDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import static com.cleaner.esaymart.utils.utils.isNetworkAvailable;

public class ServiceDetailActivity extends AppCompatActivity {


    private ImageView service_detail_image;
    private TextView textView_s_name, textView_s_price, textView_service_disc, textView_service_include;

    private String service_name;
    private int service_price;
    private String service_disc;
    private String service_time;
    private String service_include;
    private String service_image;
    private int key_id;
    private int service_cat_id;
    private LinearLayout ser_layout;
    private String imagePath = "http://littlejoy.co.in/esaymart/public_html/admin_dryclean/api/";
    private String url = "http://littlejoy.co.in/esaymart/public_html/admin_dryclean/users/service.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getId();
        getdatafromIntent();
        signupRequest();

    }

    private void getdatafromIntent() {
        if (getIntent() != null) {
            key_id = Integer.parseInt(getIntent().getStringExtra("id"));
            Toast.makeText(this, key_id + "", Toast.LENGTH_LONG).show();
        }
    }

    private void getId() {
        service_detail_image = (ImageView) findViewById(R.id.service_detail_image);
        textView_s_name = (TextView) findViewById(R.id.textView_s_name);
        textView_s_price = (TextView) findViewById(R.id.textView_s_price);
        textView_service_disc = (TextView) findViewById(R.id.textView_service_disc);
        textView_service_include = (TextView) findViewById(R.id.textView_service_include);
        ser_layout = (LinearLayout) findViewById(R.id.ser_layout);
    }

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
                                    service_time = profile.getString("time");
                                    service_include = profile.getString("ser_include");
                                    service_image = profile.getString("image");
                                    service_image = imagePath + service_image;
                                    service_cat_id = Integer.parseInt(profile.getString("service_id"));

                                    if (key_id == service_cat_id) {
                                        textView_s_name.setText(service_name);
                                        textView_s_price.setText("Rs. " + service_price);
                                        textView_service_disc.setText(service_disc);
                                        textView_service_include.setText(service_include);
                                        Glide.with(ServiceDetailActivity.this).load(service_image).into(service_detail_image);
                                    }
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
                            Toast.makeText(ServiceDetailActivity.this, "Network Error", Toast.LENGTH_LONG).show();

                            MyProgressDialog.hidePDialog();
                        }
                    }
            );
            postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            queue.add(postRequest);
        } else {
            Snackbar.make(ser_layout, "Network Not Found", Snackbar.LENGTH_LONG).show();

        }
    }

    public void BookNow(View view) {
        Intent intent = new Intent(this, ServiceLocationActivity.class);
        intent.putExtra("id", key_id + "");
        startActivity(intent);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }
}
