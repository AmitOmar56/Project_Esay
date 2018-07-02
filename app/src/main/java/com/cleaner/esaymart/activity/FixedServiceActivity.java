package com.cleaner.esaymart.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TimePicker;
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

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static com.cleaner.esaymart.activity.FirstActivity.s_Address;
import static com.cleaner.esaymart.activity_cleaningBoy.Home_CleaningActivity.s_user_id;
import static com.cleaner.esaymart.utils.utils.isNetworkAvailable;
import static com.cleaner.esaymart.utils.utils.isValidEmail;

public class FixedServiceActivity extends AppCompatActivity implements View.OnClickListener {

    private int mYear, mMonth, mDay, mHour, mMinute;
    private EditText service_Date, service_Time, service_location;
    private Button service_location_clear;
    private EditText service_fullName, service_mobileNumber, service_vehical_name, service_vehical_number;
    private String name, mobile, location, v_name, v_number;
    private String date = "";
    private String time = "";
    private String appointmentApi = "http://littlejoy.co.in/esaymart/public_html/admin_dryclean/users/appoint.php";
    private String key_id;
    private ScrollView fixed_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fixed_service);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getId();
        getdatafromIntent();
        service_location.setText(s_Address);
        service_Date.setOnClickListener(this);
        service_Time.setOnClickListener(this);
        service_location_clear.setOnClickListener(this);

    }

    private void getId() {
        service_Date = (EditText) findViewById(R.id.service_Date);
        service_Time = (EditText) findViewById(R.id.service_Time);
        service_location = (EditText) findViewById(R.id.service_location);
        service_location_clear = (Button) findViewById(R.id.service_location_clear);
        service_fullName = (EditText) findViewById(R.id.service_fullName);
        service_mobileNumber = (EditText) findViewById(R.id.service_mobileNumber);
        service_vehical_name = (EditText) findViewById(R.id.service_vehical_name);
        service_vehical_number = (EditText) findViewById(R.id.service_vehical_number);
        fixed_layout = (ScrollView) findViewById(R.id.fixed_layout);
    }

    private void getdatafromIntent() {
        if (getIntent() != null) {
            key_id = getIntent().getStringExtra("id");
        }
    }

    @Override
    public void onClick(View v) {

        if (v == service_Date) {
            findDate();
        }
        if (v == service_Time) {
            findTime();
        }
        if (v == service_location_clear) {
            service_location.setText("");
        }
    }

    private void findTime() {
        // Get Current Time
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(FixedServiceActivity.this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {

                        service_Time.setText(hourOfDay + ":" + minute);
                        time = hourOfDay + ":" + minute;
                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }

    private void findDate() {
        // Get Current Date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(FixedServiceActivity.this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        service_Date.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                        date = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }

    public void bookAppointment(View view) {
        boolean isExecuteNext = true;
        name = service_fullName.getText().toString();
        mobile = service_mobileNumber.getText().toString();
        v_number = service_vehical_number.getText().toString();
        v_name = service_vehical_name.getText().toString();
        location = service_location.getText().toString();


        if (name.isEmpty()) {
            Log.d("name", name + "");
            service_fullName.setError(getResources().getString(R.string.name_error));
            isExecuteNext = false;
        }

        if (mobile.isEmpty()) {
            service_mobileNumber.setError(getResources().getString(R.string.Phone_error));
            isExecuteNext = false;
        } else if (mobile.length() < 10) {
            service_mobileNumber.setError(getResources().getString(R.string.Phone_length_validation));
            isExecuteNext = false;
        }
        if (location.isEmpty()) {
            Log.d("password", v_name + "");
            service_location.setError(getResources().getString(R.string.password_error));
            isExecuteNext = false;
        }
        if (v_name.isEmpty()) {
            Log.d("password", v_name + "");
            service_vehical_name.setError(getResources().getString(R.string.password_error));
            isExecuteNext = false;
        }
        if (v_number.isEmpty()) {
            Log.d("password", v_number + "");
            service_vehical_number.setError(getResources().getString(R.string.password_error));
            isExecuteNext = false;
        }
        if (date.isEmpty()) {
            Log.d("password", v_number + "");
            service_Date.setError("Enter date");
            isExecuteNext = false;
        }

        if (time.isEmpty()) {
            Log.d("password", v_number + "");
            service_Time.setError("Enter Time");
            isExecuteNext = false;
        }

        if (!isExecuteNext) {
            //registerLayout.startAnimation(shakeAnimation);
            // scrollView.startAnimation(shakeAnimation);
            new CustomToast().Show_Toast(this, view,
                    "All fields are required.");
            return;
        } else {
//            Log.d("data->>>>>>>>>>", key_id + name + mobile + location + v_name + v_number + date + time);
            appointmentApiCall();
        }

    }

    private void appointmentApiCall() {
        Boolean result = isNetworkAvailable(this);
        if (result) {
            MyProgressDialog.showPDialog(this);
            RequestQueue queue = Volley.newRequestQueue(this);
            String response = null;
            final String finalResponse = response;

            StringRequest postRequest = new StringRequest(Request.Method.POST, appointmentApi,
                    new com.android.volley.Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("amit", response);
                            try {
                                Object json = new JSONTokener(response).nextValue();
                                JSONObject jsonObject = (JSONObject) json;
                                if (jsonObject.getString("status").equals("STATUS_SUCCESS")) {

                                    Toast.makeText(FixedServiceActivity.this, jsonObject.getString("data"), Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(FixedServiceActivity.this, PaymentGatewayActivity.class);
                                    intent.putExtra("id", key_id + "");
                                    intent.putExtra("key", mobile);
                                    intent.putExtra("type", "ser");
                                    startActivity(intent);
                                    MyProgressDialog.hidePDialog();

                                } else {
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
                            Toast.makeText(FixedServiceActivity.this, "Network Error", Toast.LENGTH_LONG).show();
                            MyProgressDialog.hidePDialog();
                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();

                    params.put("user_id", s_user_id);
                    params.put("service_id", key_id);
                    params.put("user_name", name);
                    params.put("user_phone", mobile);
                    params.put("user_addr", location);
                    params.put("v_name", v_name);
                    params.put("v_no", v_number);
                    params.put("app_date", date);
                    params.put("app_time", time);
                    return params;
                }
            };
            postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            queue.add(postRequest);

        } else {
            Snackbar.make(fixed_layout, "Network Not Found", Snackbar.LENGTH_LONG).show();

        }
    }

}
