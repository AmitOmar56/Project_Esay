package com.cleaner.esaymart.fragment;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
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
import com.cleaner.esaymart.activity_cleaningBoy.MainActivity;
import com.cleaner.esaymart.activity_cleaningBoy.Register_Cleaning_BoyActivity;
import com.cleaner.esaymart.customtoast.CustomToast;
import com.cleaner.esaymart.utils.MyProgressDialog;
import com.cleaner.esaymart.utils.utils;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.cleaner.esaymart.utils.utils.isNetworkAvailable;
import static com.cleaner.esaymart.utils.utils.isValidEmail;

public class SignUp_Fragment extends Fragment implements OnClickListener {
    private static View view;
    private static EditText fullName, emailId, mobileNumber,
            password, confirmPassword;
    private static TextView login, termsText;
    private static Button signUpButton;
    private static CheckBox terms_conditions;
    private static FragmentManager fragmentManager;
    private String getFullName;
    private String getEmailId;
    private String getMobileNumber;
    private String getPassword;
    private String getConfirmPassword;
    private LinearLayout linear;
    private String register_url = "http://esaymart.com/admin_dryclean/users/emp_register.php";
    private static Animation shakeAnimation;

    public SignUp_Fragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.signup_layout, container, false);
        initViews();
        setListeners();
        return view;
    }

    // Initialize all views
    private void initViews() {
        fragmentManager = getActivity().getSupportFragmentManager();
        fullName = (EditText) view.findViewById(R.id.fullName);
        emailId = (EditText) view.findViewById(R.id.userEmailId);
        mobileNumber = (EditText) view.findViewById(R.id.mobileNumber);
        password = (EditText) view.findViewById(R.id.password);
        confirmPassword = (EditText) view.findViewById(R.id.confirmPassword);
        signUpButton = (Button) view.findViewById(R.id.signUpBtn);
        login = (TextView) view.findViewById(R.id.already_user);
        terms_conditions = (CheckBox) view.findViewById(R.id.terms_conditions);
        termsText = (TextView) view.findViewById(R.id.termsText);
        linear = (LinearLayout) view.findViewById(R.id.linear);
        shakeAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.shake);

        // Setting text selector over textviews
        @SuppressLint("ResourceType") XmlResourceParser xrp = getResources().getXml(R.drawable.text_selector);
        try {
            ColorStateList csl = ColorStateList.createFromXml(getResources(),
                    xrp);

            login.setTextColor(csl);
            terms_conditions.setTextColor(csl);
        } catch (Exception e) {
        }
    }

    // Set Listeners
    private void setListeners() {
        signUpButton.setOnClickListener(this);
        login.setOnClickListener(this);
        termsText.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signUpBtn:

                // Call checkValidation method
                checkValidation();
                break;

            case R.id.already_user:

                // Replace login fragment
                new MainActivity().replaceLoginFragment();
                break;
            case R.id.termsText:
                gotoTermsPage();
                break;
        }

    }

    // Check Validation Method
    private void checkValidation() {
        boolean isExecuteNext = true;

        // Get all edittext texts
        getFullName = fullName.getText().toString();
        getEmailId = emailId.getText().toString();
        getMobileNumber = mobileNumber.getText().toString();
        getPassword = password.getText().toString();
        getConfirmPassword = confirmPassword.getText().toString();


        if (getFullName.isEmpty()) {
            Log.d("name", getFullName + "");
            fullName.setError(getResources().getString(R.string.name_error));
            isExecuteNext = false;
        }

        if (getEmailId.isEmpty()) {
            emailId.setError(getResources().getString(R.string.enter_email));
            isExecuteNext = false;
        } else if (!isValidEmail(getEmailId)) {
            emailId.setError(getResources().getString(R.string.enter_valid_email));
            isExecuteNext = false;
        }
        if (getMobileNumber.isEmpty()) {
            mobileNumber.setError(getResources().getString(R.string.Phone_error));
            isExecuteNext = false;
        } else if (getMobileNumber.length() < 10) {
            mobileNumber.setError(getResources().getString(R.string.Phone_length_validation));
            isExecuteNext = false;
        }
        if (getPassword.isEmpty()) {
            Log.d("password", getPassword + "");
            password.setError(getResources().getString(R.string.password_error));
            isExecuteNext = false;
        }
        if (!getConfirmPassword.equals(getPassword)) {
            new CustomToast().Show_Toast(getActivity(), view,
                    "Both password doesn't match.");
            isExecuteNext = false;

        }

        // Make sure user should check Terms and Conditions checkbox
        if (!terms_conditions.isChecked()) {

            new CustomToast().Show_Toast(getActivity(), view,
                    "Please select Terms and Conditions.");
            isExecuteNext = false;
        }

        if (!isExecuteNext) {
            //registerLayout.startAnimation(shakeAnimation);
            linear.startAnimation(shakeAnimation);
            new CustomToast().Show_Toast(getContext(), view,
                    "All fields are required.");
            return;
        } else {
            registerApiCall();
        }


    }

    private void registerApiCall() {
        Boolean result = isNetworkAvailable(getActivity());
        if (result) {
            MyProgressDialog.showPDialog(getContext());
            RequestQueue queue = Volley.newRequestQueue(getContext());
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
                                if (jsonObject.getString("status").equals("STATUS_SUCCESS") || jsonObject.getString("status").equals("1")) {

                                    Toast.makeText(getContext(), jsonObject.getString("data"), Toast.LENGTH_LONG).show();
                                    Snackbar.make(linear, "Registration is successfull", Snackbar.LENGTH_LONG).show();
                                    new MainActivity().replaceLoginFragment();
                                    MyProgressDialog.hidePDialog();

                                } else {
                                    Toast.makeText(getContext(), jsonObject.getString("data"), Toast.LENGTH_LONG).show();
                                    Snackbar.make(linear, jsonObject.getString("data"), Snackbar.LENGTH_LONG).show();
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
                            Toast.makeText(getContext(), "Network Error", Toast.LENGTH_LONG).show();
                            MyProgressDialog.hidePDialog();
                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();

                    params.put("emp_name", getFullName);
                    params.put("emp_email", getEmailId);
                    params.put("emp_phone", getMobileNumber);
                    params.put("emp_pass", getPassword);

                    return params;
                }
            };
            postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            queue.add(postRequest);

        } else {
            Snackbar.make(linear, "Data Not Found", Snackbar.LENGTH_LONG).show();

        }
    }

    private void gotoTermsPage() {
//        fragmentManager
//                .beginTransaction()
//                .setCustomAnimations(R.anim.left_enter, R.anim.right_out)
//                .replace(R.id.frameContainer, new RuleandRegulationsFragment(),
//                        utils.RuleandRegulationFragment).commit();
    }
}
