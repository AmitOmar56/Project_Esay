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
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cleaner.esaymart.R;
import com.cleaner.esaymart.customtoast.CustomToast;
import com.cleaner.esaymart.utils.MyProgressDialog;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.HashMap;
import java.util.Map;

import static com.cleaner.esaymart.activity_cleaningBoy.Home_CleaningActivity.s_user_id;
import static com.cleaner.esaymart.utils.utils.isNetworkAvailable;
import static com.cleaner.esaymart.utils.utils.isValidEmail;

public class ReferAndEarn extends AppCompatActivity {

    private EditText personFullName, personMobileNumber, personAadharNumber;
    private String personName, personMobile, personNumber;
    private String referAperson_url = "http://littlejoy.co.in/esaymart/public_html/admin_dryclean/users/referp.php";
    private LinearLayout person_refer_layout, referPersonlayout2, referPersonlayout1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refer_and_earn2);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getId();
    }

    private void getId() {
        personFullName = (EditText) findViewById(R.id.personName);
        personMobileNumber = (EditText) findViewById(R.id.personMobileNumber);
        personAadharNumber = (EditText) findViewById(R.id.personAadharNumber);
        person_refer_layout = (LinearLayout) findViewById(R.id.person_refer_layout);
        referPersonlayout2 = (LinearLayout) findViewById(R.id.referPersonlayout2);
        referPersonlayout1 = (LinearLayout) findViewById(R.id.referPersonlayout1);


    }

    public void referApersonButton(View view) {

        boolean isExecuteNext = true;
        personName = personFullName.getText().toString();
        personMobile = personMobileNumber.getText().toString();
        personNumber = personAadharNumber.getText().toString();

        if (personName.isEmpty()) {
            personFullName.setError(getResources().getString(R.string.name_error));
            isExecuteNext = false;
        }
        if (personMobile.isEmpty()) {
            personMobileNumber.setError(getResources().getString(R.string.Phone_error));
            isExecuteNext = false;
        } else if (personMobile.length() < 10) {
            personMobileNumber.setError(getResources().getString(R.string.Phone_length_validation));
            isExecuteNext = false;
        }
//        if (carNumber.isEmpty()) {
//            userCarcarNumber.setError(getResources().getString(R.string.password_error));
//            isExecuteNext = false;
//        }

        if (!isExecuteNext) {
            //registerLayout.startAnimation(shakeAnimation);
            // referCarLayout.startAnimation(shakeAnimation);
            new CustomToast().Show_Toast(this, view,
                    "All fields are required.");
            return;
        } else {

            Log.d("data->>>>>>>>>>>>>>", personName + personMobile + personNumber);
            referApersonApiCall();
        }

    }

    private void referApersonApiCall() {
        Boolean result = isNetworkAvailable(this);
        if (result) {
            MyProgressDialog.showPDialog(this);
            RequestQueue queue = Volley.newRequestQueue(this);
            String response = null;
            final String finalResponse = response;

            StringRequest postRequest = new StringRequest(Request.Method.POST, referAperson_url,
                    new com.android.volley.Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("amit", response);
                            try {
                                Object json = new JSONTokener(response).nextValue();
                                JSONObject jsonObject = (JSONObject) json;
                                if (jsonObject.getString("status").equals("1") || jsonObject.getString("status").equals("STATUS_SUCCESS")) {
                                    Toast.makeText(ReferAndEarn.this, jsonObject.getString("data"), Toast.LENGTH_LONG).show();
                                    Snackbar.make(person_refer_layout, jsonObject.getString("data"), Snackbar.LENGTH_LONG).show();
                                    referPersonlayout1.setVisibility(View.GONE);
                                    referPersonlayout2.setVisibility(View.VISIBLE);
                                    MyProgressDialog.hidePDialog();

                                } else {
                                    Snackbar.make(person_refer_layout, jsonObject.getString("data"), Snackbar.LENGTH_LONG).show();
                                    MyProgressDialog.hidePDialog();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new com.android.volley.Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(ReferAndEarn.this, "Network Error", Toast.LENGTH_LONG).show();
                            MyProgressDialog.hidePDialog();
                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();

                    params.put("referp_name", personName);
                    params.put("referp_phone", personMobile);
                    params.put("referp_adh", personNumber);
                    params.put("user_id", s_user_id);
                    return params;
                }
            };
            postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            queue.add(postRequest);
        } else {
            Snackbar.make(person_refer_layout, "Network Not Found", Snackbar.LENGTH_LONG).show();

        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }

    public void refer_personHome(View view) {
        startActivity(new Intent(this, HomeActivity.class));
    }
}
