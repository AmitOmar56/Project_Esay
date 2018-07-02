package com.cleaner.esaymart.fragment;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
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

import static com.cleaner.esaymart.utils.utils.isNetworkAvailable;

public class ForgotPassword_Fragment extends Fragment implements
        OnClickListener {
    private static View view;

    private static EditText emailId;
    private static TextView submit, back;
    private LinearLayout f_layout, forgotlayout, for_layout;
    private TextView success_email;
    private String getEmailId;
    private String forgotApi = "http://esaymart.com/admin_dryclean/users/emp_forgot.php";
    private Button forgot_ok;

    public ForgotPassword_Fragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.forgotpassword_layout, container,
                false);
        initViews();
        setListeners();
        return view;
    }

    // Initialize the views
    private void initViews() {
        emailId = (EditText) view.findViewById(R.id.registered_emailid);
        submit = (TextView) view.findViewById(R.id.forgot_button);
        back = (TextView) view.findViewById(R.id.backToLoginBtn);
        f_layout = (LinearLayout) view.findViewById(R.id.f_layout);
        forgotlayout = (LinearLayout) view.findViewById(R.id.forgotlayout);
        success_email = (TextView) view.findViewById(R.id.success_email);
        forgot_ok = (Button) view.findViewById(R.id.forgot_ok);
        for_layout = (LinearLayout) view.findViewById(R.id.for_layout);
        // Setting text selector over textviews
        @SuppressLint("ResourceType") XmlResourceParser xrp = getResources().getXml(R.drawable.text_selector);
        try {
            ColorStateList csl = ColorStateList.createFromXml(getResources(),
                    xrp);

            back.setTextColor(csl);
            submit.setTextColor(csl);

        } catch (Exception e) {
        }

    }

    // Set Listeners over buttons
    private void setListeners() {
        back.setOnClickListener(this);
        submit.setOnClickListener(this);
        forgot_ok.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backToLoginBtn:

                // Replace Login Fragment on Back Presses
                new MainActivity().replaceLoginFragment();
                break;

            case R.id.forgot_button:

                // Call Submit button task
                submitButtonTask();
                break;
            case R.id.forgot_ok:

                // Call Submit button task
                //  submitButtonTask();
                break;

        }

    }

    private void submitButtonTask() {
        getEmailId = emailId.getText().toString();

        // Pattern for email id validation
        Pattern p = Pattern.compile(utils.regEx);

        // Match the pattern
        Matcher m = p.matcher(getEmailId);

        // First check if email id is not null else show error toast
        if (getEmailId.equals("") || getEmailId.length() == 0)

            new CustomToast().Show_Toast(getActivity(), view,
                    "Please enter your Email Id.");

            // Check if email id is valid or not
        else if (!m.find())
            new CustomToast().Show_Toast(getActivity(), view,
                    "Your Email Id is Invalid.");

            // Else submit email id and fetch passwod or do your stuff
        else {
            logInRequest();
            Toast.makeText(getActivity(), "Get Forgot Password.",
                    Toast.LENGTH_SHORT).show();
        }

    }

    private void logInRequest() {
        Boolean result = isNetworkAvailable(getActivity());
        if (result) {
            MyProgressDialog.showPDialog(getContext());
            RequestQueue queue = Volley.newRequestQueue(getContext());
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
                                    f_layout.setVisibility(View.GONE);
                                    forgotlayout.setVisibility(View.VISIBLE);
                                    success_email.setText(getEmailId);
                                    MyProgressDialog.hidePDialog();

                                } else {
                                    MyProgressDialog.hidePDialog();
                                    forgotlayout.setVisibility(View.GONE);
                                    emailId.setError("incorrect email");
                                    Toast.makeText(getContext(), "Incorrect email", Toast.LENGTH_LONG).show();

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
        } else {
            Snackbar.make(for_layout, "Data Not Found", Snackbar.LENGTH_LONG).show();

        }
    }

}