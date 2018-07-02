package com.cleaner.esaymart.activity;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
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

public class ChangePasswordActivity extends AppCompatActivity {

    private TextView retype_Pass, new_Pass, old_Pass;
    private String u_pass, u_new_pass, u_old_pass;
    private LinearLayout linear_layout;
    private static Animation shakeAnimation;
    private String change_pass_url = "http://littlejoy.co.in/esaymart/public_html/admin_dryclean/users/password.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getId();
    }

    private void getId() {
        retype_Pass = (TextView) findViewById(R.id.retype_Pass);
        new_Pass = (TextView) findViewById(R.id.new_Pass);
        old_Pass = (TextView) findViewById(R.id.old_Pass);
        linear_layout = (LinearLayout) findViewById(R.id.linear_layout);
        shakeAnimation = AnimationUtils.loadAnimation(this, R.anim.shake);

    }

    public void update_pass(View view) {
        boolean isExecuteNext = true;
        u_old_pass = old_Pass.getText().toString();
        u_pass = retype_Pass.getText().toString();
        u_new_pass = new_Pass.getText().toString();

        if (u_old_pass.isEmpty()) {
            Log.d("name", u_pass + "");
            old_Pass.setError(getResources().getString(R.string.pass_error));
            isExecuteNext = false;
        }
        if (u_pass.isEmpty()) {
            Log.d("name", u_pass + "");
            retype_Pass.setError(getResources().getString(R.string.pass_new_error));
            isExecuteNext = false;
        }
        if (u_new_pass.isEmpty()) {
            Log.d("password", u_new_pass + "");
            new_Pass.setError(getResources().getString(R.string.pass_re_error));
            isExecuteNext = false;
        } else if (!u_new_pass.equals(u_pass)) {
            linear_layout.startAnimation(shakeAnimation);
            new CustomToast().Show_Toast(this, view,
                    "The new and retype password do not match");
            return;
        }
        if (!isExecuteNext) {
            linear_layout.startAnimation(shakeAnimation);
            new CustomToast().Show_Toast(this, view,
                    "All fields are required.");
            return;
        } else {
            MyProgressDialog.showPDialog(this);

            // Log.d("data->>>>>>>>>>>>>>", name + email + phone + password + referal);
            changepassApiCall();
        }
    }

    private void changepassApiCall() {
        Boolean result = isNetworkAvailable(this);
        if (result) {
            MyProgressDialog.showPDialog(this);
            RequestQueue queue = Volley.newRequestQueue(this);
            String response = null;
            final String finalResponse = response;

            StringRequest postRequest = new StringRequest(Request.Method.POST, change_pass_url,
                    new com.android.volley.Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("amit", response);
                            try {
                                Object json = new JSONTokener(response).nextValue();
                                JSONObject jsonObject = (JSONObject) json;
                                if (jsonObject.getString("status").equals("1") || jsonObject.getString("status").equals("STATUS_SUCCESS")) {

                                    Toast.makeText(ChangePasswordActivity.this, jsonObject.getString("data"), Toast.LENGTH_LONG).show();
                                    Snackbar.make(linear_layout, jsonObject.getString("data"), Snackbar.LENGTH_LONG).show();
                                    MyProgressDialog.hidePDialog();

                                } else {
                                    Snackbar.make(linear_layout, jsonObject.getString("data"), Snackbar.LENGTH_LONG).show();

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new com.android.volley.Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(ChangePasswordActivity.this, "Network Error", Toast.LENGTH_LONG).show();
                            MyProgressDialog.hidePDialog();
                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("user_id", s_user_id);
                    params.put("old", u_old_pass);
                    params.put("pass", u_new_pass);
                    params.put("co_pass", u_pass);

                    return params;
                }
            };
            postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            queue.add(postRequest);
        } else {
            Snackbar.make(linear_layout, "Network Not Found", Snackbar.LENGTH_LONG).show();

        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }
}
