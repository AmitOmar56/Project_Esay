package com.cleaner.esaymart.activity;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
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

public class ReferACar extends AppCompatActivity {

    private EditText userCarName, userCarMobileNumber, userCarcarNumber;
    private String carName, carMobile, carNumber;
    private LinearLayout referCarLayout, refercarlayout1, refercarlayout2;
    private String referAcar_url = "http://littlejoy.co.in/esaymart/public_html/admin_dryclean/users/referc.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refer_acar);
        getId();
    }

    private void getId() {
        userCarName = (EditText) findViewById(R.id.userCarName);
        userCarMobileNumber = (EditText) findViewById(R.id.userCarMobileNumber);
        userCarcarNumber = (EditText) findViewById(R.id.userCarcarNumber);
        referCarLayout = (LinearLayout) findViewById(R.id.referCarLayout);
        refercarlayout1 = (LinearLayout) findViewById(R.id.refercarlayout1);
        refercarlayout2 = (LinearLayout) findViewById(R.id.refercarlayout2);

    }

    public void referAcarButton(View view) {

        boolean isExecuteNext = true;
        carName = userCarName.getText().toString();
        carMobile = userCarMobileNumber.getText().toString();
        carNumber = userCarcarNumber.getText().toString();

        if (carName.isEmpty()) {
            userCarName.setError(getResources().getString(R.string.name_error));
            isExecuteNext = false;
        }
        if (carMobile.isEmpty()) {
            userCarMobileNumber.setError(getResources().getString(R.string.Phone_error));
            isExecuteNext = false;
        } else if (carMobile.length() < 10) {
            userCarMobileNumber.setError(getResources().getString(R.string.Phone_length_validation));
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

            Log.d("data->>>>>>>>>>>>>>", carName + carMobile + carNumber);
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

            StringRequest postRequest = new StringRequest(Request.Method.POST, referAcar_url,
                    new com.android.volley.Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("amit", response);
                            try {
                                Object json = new JSONTokener(response).nextValue();
                                JSONObject jsonObject = (JSONObject) json;
                                if (jsonObject.getString("status").equals("1") || jsonObject.getString("status").equals("STATUS_SUCCESS")) {
                                    Toast.makeText(ReferACar.this, jsonObject.getString("data"), Toast.LENGTH_LONG).show();
                                    Snackbar.make(referCarLayout, jsonObject.getString("data"), Snackbar.LENGTH_LONG).show();
                                    refercarlayout1.setVisibility(View.GONE);
                                    refercarlayout2.setVisibility(View.VISIBLE);
                                    MyProgressDialog.hidePDialog();

                                } else {
                                    Snackbar.make(referCarLayout, jsonObject.getString("data"), Snackbar.LENGTH_LONG).show();
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
                            Toast.makeText(ReferACar.this, "Network Error", Toast.LENGTH_LONG).show();
                            MyProgressDialog.hidePDialog();
                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();

                    params.put("referc_name", carName);
                    params.put("referc_phone", carMobile);
                    params.put("referc_vno", carNumber);
                    params.put("user_id", s_user_id);
                    return params;
                }
            };
            postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            queue.add(postRequest);
        } else {
            Snackbar.make(referCarLayout, "Network Not Found", Snackbar.LENGTH_LONG).show();

        }
    }

    public void refer_carHome(View view) {
        startActivity(new Intent(this, HomeActivity.class));
    }
}
