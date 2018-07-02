package com.cleaner.esaymart.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
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
import com.cleaner.esaymart.activity_cleaningBoy.Home_CleaningActivity;
import com.cleaner.esaymart.activity_cleaningBoy.MainActivity;
import com.cleaner.esaymart.customtoast.CustomToast;
import com.cleaner.esaymart.utils.MyProgressDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.HashMap;
import java.util.Map;

import static com.cleaner.esaymart.activity_cleaningBoy.Home_CleaningActivity.s_emp_id;
import static com.cleaner.esaymart.utils.utils.isNetworkAvailable;
import static com.cleaner.esaymart.utils.utils.isValidEmail;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private View view;
    private TextView emp_profile_name, emp_profile_email;
    private EditText edit_emp_profile_name, edit_emp_profile_email, edit_emp_profile_phone;
    private Button emp_update_Btn;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private String profile_url = "http://esaymart.com/admin_dryclean/users/emp_get.php";
    private String profile_edit_url = "http://esaymart.com/admin_dryclean/users/mer_update.php";
    private String getemail;
    private String getname;
    private ScrollView emp_scroll;
    private static Animation shakeAnimation;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        getIds();
        profileApiCall();
        setListners();
        return view;
    }

    private void setListners() {
        emp_update_Btn.setOnClickListener(this);
    }

    private void getIds() {
        emp_profile_name = (TextView) view.findViewById(R.id.emp_profile_name);
        emp_profile_email = (TextView) view.findViewById(R.id.emp_profile_email);
        edit_emp_profile_name = (EditText) view.findViewById(R.id.edit_emp_profile_name);
        edit_emp_profile_email = (EditText) view.findViewById(R.id.edit_emp_profile_email);
        edit_emp_profile_phone = (EditText) view.findViewById(R.id.edit_emp_profile_phone);
        emp_update_Btn = (Button) view.findViewById(R.id.emp_update_Btn);
        emp_scroll = (ScrollView) view.findViewById(R.id.emp_scroll);
        shakeAnimation = AnimationUtils.loadAnimation(getActivity(),
                R.anim.shake);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.emp_update_Btn:

                // Call checkValidation method
                checkValidation();
                break;

        }
    }

    private void checkValidation() {
        boolean isExecuteNext = true;

        // Get email id and password
        getname = edit_emp_profile_name.getText().toString();
        getemail = edit_emp_profile_email.getText().toString();

        // Check patter for email id
//        Pattern p = Pattern.compile(utils.regEx);
//
//        Matcher m = p.matcher(getEmailId);

        // Check for both field is empty or not
        if (getname.equals("")
                || getemail.equals("")) {
            emp_scroll.startAnimation(shakeAnimation);
            new CustomToast().Show_Toast(getActivity(), view,
                    "Enter both credentials.");

        }

        if (getname.isEmpty()) {
            edit_emp_profile_name.setError("Enter Name");
            isExecuteNext = false;
        }
        if (getemail.isEmpty()) {
            edit_emp_profile_email.setError(getResources().getString(R.string.enter_email));
            isExecuteNext = false;
        } else if (!isValidEmail(getemail)) {
            edit_emp_profile_email.setError(getResources().getString(R.string.enter_valid_email));
            isExecuteNext = false;
        }
        if (!isExecuteNext) {
            //registerLayout.startAnimation(shakeAnimation);
            emp_scroll.startAnimation(shakeAnimation);
            new CustomToast().Show_Toast(getActivity(), view,
                    "All fields are required.");
        } else {
            Toast.makeText(getActivity(), "Do Login.", Toast.LENGTH_SHORT)
                    .show();
            MyProgressDialog.showPDialog(getContext());
            profileEditApiCall();
        }

    }

    private void profileApiCall() {
        Boolean result = isNetworkAvailable(getActivity());
        if (result) {
            MyProgressDialog.showPDialog(getContext());
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String response = null;
        final String finalResponse = response;

        StringRequest postRequest = new StringRequest(Request.Method.POST, profile_url,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("amit", response);
                        try {
                            Object json = new JSONTokener(response).nextValue();
                            JSONObject jsonObject = (JSONObject) json;
                            if (jsonObject.getString("status").equals("STATUS_SUCCESS") || jsonObject.getString("status").equals("1")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                Log.d("jsonObject", jsonObject + "");
                                Log.d("jsonArray", jsonArray + "");

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject profile = jsonArray.getJSONObject(i);
                                    emp_profile_name.setText(profile.getString("emp_name"));
                                    emp_profile_email.setText(profile.getString("emp_email"));
                                    edit_emp_profile_name.setText(profile.getString("emp_name"));
                                    edit_emp_profile_phone.setText(profile.getString("emp_phone"));
                                    edit_emp_profile_email.setText(profile.getString("emp_email"));
                                    //  Toast.makeText(LoginActivity.this, jsonObject.getString("data"), Toast.LENGTH_LONG).show();
                                    MyProgressDialog.hidePDialog();
                                }
                            } else {
                                Toast.makeText(getContext(), "No data Found", Toast.LENGTH_LONG).show();
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

                params.put("employee_id", s_emp_id);
                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(postRequest);
    } else {
        Snackbar.make(emp_scroll, "Data Not Found", Snackbar.LENGTH_LONG).show();

    }
    }

    private void profileEditApiCall() {
        Boolean result = isNetworkAvailable(getActivity());
        if (result) {
            MyProgressDialog.showPDialog(getContext());
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String response = null;
        final String finalResponse = response;

        StringRequest postRequest = new StringRequest(Request.Method.POST, profile_edit_url,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("amit", response);
                        try {
                            Object json = new JSONTokener(response).nextValue();
                            JSONObject jsonObject = (JSONObject) json;
                            if (jsonObject.getString("status").equals("STATUS_SUCCESS") || jsonObject.getString("status").equals("1")) {

                                emp_profile_name.setText(getname);
                                emp_profile_email.setText(getemail);
                                edit_emp_profile_name.setText(getname);
                                edit_emp_profile_email.setText(getemail);
                                Snackbar.make(emp_scroll, "profile is Update", Snackbar.LENGTH_LONG).show();
                                MyProgressDialog.hidePDialog();

                            } else {
                                Toast.makeText(getContext(), "No data Found", Toast.LENGTH_LONG).show();
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

                params.put("employee_id", s_emp_id);
                params.put("emp_name", getname);
                params.put("emp_email", getemail);
                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(postRequest);
        } else {
            Snackbar.make(emp_scroll, "Data Not Found", Snackbar.LENGTH_LONG).show();

        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
