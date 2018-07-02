package com.cleaner.esaymart.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import com.cleaner.esaymart.customtoast.CustomToast;
import com.cleaner.esaymart.utils.MyProgressDialog;
import com.cleaner.esaymart.utils.utils;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText emailId;
    private String getEmailId;
    private String forgotApi = "http://littlejoy.co.in/esaymart/public_html/admin_dryclean/users/forgot_password_user.php";
    private LinearLayout u_layout, user_forgotlayout;
    private TextView success_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        getId();
    }

    public void cleaning_boy_forgot(View view) {
        getEmailId = emailId.getText().toString();

        // Pattern for email id validation
        Pattern p = Pattern.compile(utils.regEx);

        // Match the pattern
        Matcher m = p.matcher(getEmailId);

        // First check if email id is not null else show error toast
        if (getEmailId.equals("") || getEmailId.length() == 0)

            new CustomToast().Show_Toast(this, view,
                    "Please enter your Email Id.");

            // Check if email id is valid or not
        else if (!m.find())
            new CustomToast().Show_Toast(this, view,
                    "Your Email Id is Invalid.");

            // Else submit email id and fetch passwod or do your stuff
        else {
            logInRequest();
            Toast.makeText(this, "Get Forgot Password.",
                    Toast.LENGTH_SHORT).show();
        }

    }

    private void logInRequest() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String response = null;
        final String finalResponse = response;

        StringRequest postRequest = new StringRequest(Request.Method.POST, forgotApi,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        pd.hide();
                        //Response
//                        showSnackbar(response);
                        Log.d("amit", response);
                        Object json = null;
                        try {
                            json = new JSONTokener(response).nextValue();
                            JSONObject jsonObject = (JSONObject) json;

                            Log.d("json", json + "");
                            Log.d("jsonObject", jsonObject.getString("status") + "");

                            if (jsonObject.getString("status").equals("STATUS_SUCCESS")) {
                                u_layout.setVisibility(View.GONE);
                                user_forgotlayout.setVisibility(View.VISIBLE);
                                success_email.setText(getEmailId);
                                MyProgressDialog.hidePDialog();

                            } else {
                                MyProgressDialog.hidePDialog();
                                user_forgotlayout.setVisibility(View.GONE);
                                emailId.setError("incorrect email");
                                Toast.makeText(ForgotPasswordActivity.this, "Incorrect email", Toast.LENGTH_LONG).show();

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("email", getEmailId);


                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(postRequest);

    }

    private void getId() {
        emailId = (EditText) findViewById(R.id.cleaning_emailid);
        u_layout = (LinearLayout) findViewById(R.id.user_layout);
        user_forgotlayout = (LinearLayout) findViewById(R.id.user_forgotlayout);
        success_email = (TextView) findViewById(R.id.user_success_email);
    }

    public void ok(View view) {
        finish();
    }
}
