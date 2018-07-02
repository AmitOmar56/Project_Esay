package com.cleaner.esaymart.activity_cleaningBoy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cleaner.esaymart.R;
import com.cleaner.esaymart.customtoast.CustomToast;
import com.cleaner.esaymart.model.Service;
import com.cleaner.esaymart.utils.MyProgressDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.cleaner.esaymart.utils.utils.isValidEmail;

public class Register_Cleaning_BoyActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Spinner spinner;
    private String item;
    private String supervisiorApi = "http://esaymart.com/admin_dryclean/users/superv_name.php";
    private List<String> supervisor_name = new ArrayList<String>();
    private List<String> supervisor_id = new ArrayList<String>();
    private int supervisior_id;
    private String register_url = "http://esaymart.com/admin_dryclean/users/emp_register.php";
    private EditText fullName, userEmailId, mobileNumber, user_password;
    private String name, email, phone, password;
    private static Animation shakeAnimation;
    private ScrollView scrollView;
    private String id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register__cleaning__boy);
        getId();
        spinner.setPrompt("supervisor");
        supervisiorApiCall();
        setListeners();
        Log.d("item", item + "");
    }

    private void getId() {
        spinner = (Spinner) findViewById(R.id.spinner);
        fullName = (EditText) findViewById(R.id.fullName);
        userEmailId = (EditText) findViewById(R.id.userEmailId);
        mobileNumber = (EditText) findViewById(R.id.mobileNumber);
        user_password = (EditText) findViewById(R.id.password);
        scrollView = (ScrollView) findViewById(R.id.scrollView_cleaning);
        shakeAnimation = AnimationUtils.loadAnimation(this, R.anim.shake);
    }

    private void setListeners() {
        spinner.setOnItemSelectedListener(this);
    }

    public void goTologinPage(View view) {
        startActivity(new Intent(this, Login_CleaningBoyActivity.class));
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        item = adapterView.getItemAtPosition(i).toString();
        supervisior_id = Integer.parseInt(supervisor_id.get(i));
        Toast.makeText(this, supervisior_id + "" + supervisor_name, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void registerApiCall() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String response = null;
        final String finalResponse = response;

        StringRequest postRequest = new StringRequest(Request.Method.POST, register_url,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("amit", response);
                        try {
                            Object json = new JSONTokener(response).nextValue();
                            JSONObject jsonObject = (JSONObject) json;
                            if (jsonObject.getString("status").equals("1")) {

                                Toast.makeText(Register_Cleaning_BoyActivity.this, jsonObject.getString("data"), Toast.LENGTH_LONG).show();
                                MyProgressDialog.hidePDialog();

                            } else {
                                Toast.makeText(Register_Cleaning_BoyActivity.this, jsonObject.getString("data"), Toast.LENGTH_LONG).show();
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
                        Toast.makeText(Register_Cleaning_BoyActivity.this, "Network Error", Toast.LENGTH_LONG).show();
                        MyProgressDialog.hidePDialog();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("emp_name", name);
                params.put("emp_email", email);
                params.put("emp_phone", phone);
                params.put("emp_pass", password);
                params.put("super_id", supervisior_id + "");
                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(postRequest);

    }

    private void supervisiorApiCall() {
        MyProgressDialog.showPDialog(this);

        RequestQueue queue = Volley.newRequestQueue(this);
        String response = null;
        final String finalResponse = response;

        StringRequest postRequest = new StringRequest(Request.Method.GET, supervisiorApi,
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
                                supervisor_id.add(profile.getString("super_id"));
                                supervisor_name.add(profile.getString("su_name"));

                            }
                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(Register_Cleaning_BoyActivity.this, android.R.layout.simple_spinner_item, supervisor_name);

                            // Drop down layout style - list view with radio button
                            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                            // attaching data adapter to spinner
                            spinner.setAdapter(dataAdapter);
                            MyProgressDialog.hidePDialog();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Register_Cleaning_BoyActivity.this, "Network Error", Toast.LENGTH_LONG).show();

                        MyProgressDialog.hidePDialog();
                    }
                }
        );
        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(postRequest);

    }

    public void signUpCleaning(View view) {

        boolean isExecuteNext = true;
        name = fullName.getText().toString();
        email = userEmailId.getText().toString();
        phone = mobileNumber.getText().toString();
        password = user_password.getText().toString();

        if (name.isEmpty()) {
            Log.d("name", name + "");
            fullName.setError(getResources().getString(R.string.name_error));
            isExecuteNext = false;
        }

        if (email.isEmpty()) {
            userEmailId.setError(getResources().getString(R.string.enter_email));
            isExecuteNext = false;
        } else if (!isValidEmail(email)) {
            userEmailId.setError(getResources().getString(R.string.enter_valid_email));
            isExecuteNext = false;
        }
        if (phone.isEmpty()) {
            mobileNumber.setError(getResources().getString(R.string.Phone_error));
            isExecuteNext = false;
        } else if (phone.length() < 10) {
            mobileNumber.setError(getResources().getString(R.string.Phone_length_validation));
            isExecuteNext = false;
        }
        if (password.isEmpty()) {
            Log.d("password", password + "");
            user_password.setError(getResources().getString(R.string.password_error));
            isExecuteNext = false;
        }
        if (!isExecuteNext) {
            //registerLayout.startAnimation(shakeAnimation);
            scrollView.startAnimation(shakeAnimation);
            new CustomToast().Show_Toast(this, view,
                    "All fields are required.");
            return;
        } else {
            MyProgressDialog.showPDialog(this);

            Log.d("data->>>>>>>>>>>>>>", name + email + phone + password+supervisior_id);
            registerApiCall();
        }


    }
}
