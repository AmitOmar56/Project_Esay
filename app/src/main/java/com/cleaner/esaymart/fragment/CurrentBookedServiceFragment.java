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
import com.cleaner.esaymart.activity_cleaningBoy.Home_CleaningActivity;
import com.cleaner.esaymart.activity_cleaningBoy.MainActivity;
import com.cleaner.esaymart.utils.MyProgressDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.HashMap;
import java.util.Map;

import static com.cleaner.esaymart.activity_cleaningBoy.Home_CleaningActivity.s_emp_id;
import static com.cleaner.esaymart.activity_cleaningBoy.Home_CleaningActivity.s_user_id;
import static com.cleaner.esaymart.utils.utils.isNetworkAvailable;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CurrentBookedServiceFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CurrentBookedServiceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CurrentBookedServiceFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private View view;
    private TextView decline, accept;
    private LinearLayout booking_layout_2, booking_layout_1, curr_linear_layout;
    private TextView sevice_payment, merchant_location, service_name;
    private String s_name, s_location, s_payment, service_id;
    private int int_s_payment, s_id;
    private String status;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private String current_booking_url = "http://192.168.1.11/admin_dryclean/users/approva.php";
    private String accept_url = "http://192.168.1.11/admin_dryclean/users/booked.php";

    public CurrentBookedServiceFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CurrentBookedServiceFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CurrentBookedServiceFragment newInstance(String param1, String param2) {
        CurrentBookedServiceFragment fragment = new CurrentBookedServiceFragment();
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
        view = inflater.inflate(R.layout.fragment_current_booked, container, false);
        getIds();
        setListners();
        current_bookingCall();
        return view;
    }

    private void setListners() {
        accept.setOnClickListener(this);
        decline.setOnClickListener(this);
    }

    private void getIds() {
        accept = (TextView) view.findViewById(R.id.accept);
        decline = (TextView) view.findViewById(R.id.decline);
        booking_layout_2 = (LinearLayout) view.findViewById(R.id.booking_layout_2);
        booking_layout_1 = (LinearLayout) view.findViewById(R.id.booking_layout_1);
        sevice_payment = (TextView) view.findViewById(R.id.sevice_payment);
        merchant_location = (TextView) view.findViewById(R.id.merchant_location);
        service_name = (TextView) view.findViewById(R.id.service_name);
        curr_linear_layout = (LinearLayout) view.findViewById(R.id.curr_linear_layout);
    }

    private void current_bookingCall() {
        Boolean result = isNetworkAvailable(getActivity());
        if (result) {
            MyProgressDialog.showPDialog(getContext());
            RequestQueue queue = Volley.newRequestQueue(getContext());
            String response = null;
            final String finalResponse = response;

            StringRequest postRequest = new StringRequest(Request.Method.GET, current_booking_url,
                    new com.android.volley.Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("amit", response);
                            try {
                                Object json = new JSONTokener(response).nextValue();
                                JSONObject jsonObject = (JSONObject) json;
                                if (jsonObject.getString("status").equals("1") || jsonObject.getString("status").equals("STATUS_SUCCESS")) {
                                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                                    Log.d("jsonObject", jsonObject + "");
                                    Log.d("jsonArray", jsonArray + "");

                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        Toast.makeText(getActivity(), "Enter", Toast.LENGTH_LONG).show();

                                        JSONObject profile = jsonArray.getJSONObject(i);
                                        s_name = profile.getString("ser_name");
                                        s_location = profile.getString("user_addr");
                                        int_s_payment = profile.getInt("payment");
                                        service_id = profile.getString("service_id");
                                        status = profile.getString("status");
//                                    accept.setText("Enter");
                                        if (int_s_payment == 0) {
                                            s_payment = "Cash On Delivery";
                                        } else {
                                            s_payment = "Paid";
                                        }
                                        if (status.equals("0")) {
                                            accept.setText("Accept");
                                        } else {
                                            accept.setText("Enter");
                                        }
                                        s_id = profile.getInt("employee_id");
                                        if (s_id == Integer.parseInt("9")) {
                                            Toast.makeText(getActivity(), s_emp_id, Toast.LENGTH_LONG).show();
                                            sevice_payment.setText(s_payment);
                                            merchant_location.setText(s_location);
                                            service_name.setText(s_name);
                                            booking_layout_2.setVisibility(View.GONE);
                                            booking_layout_1.setVisibility(View.VISIBLE);
                                            MyProgressDialog.hidePDialog();
                                            Toast.makeText(getActivity(), "Enter", Toast.LENGTH_LONG).show();
                                            break;
                                        } else {

                                            booking_layout_2.setVisibility(View.VISIBLE);
                                            booking_layout_1.setVisibility(View.GONE);
                                            MyProgressDialog.hidePDialog();

                                        }
//                                    startActivity(new Intent(getContext(), Home_CleaningActivity.class));
                                    }
                                } else {
//                                booking_layout_2.setVisibility(View.VISIBLE);
//                                booking_layout_1.setVisibility(View.GONE);
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
            );
            postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            queue.add(postRequest);
        } else {
            Snackbar.make(curr_linear_layout, "Data Not Found", Snackbar.LENGTH_LONG).show();

        }
    }

    private void acceptApiCall() {
        Boolean result = isNetworkAvailable(getActivity());
        if (result) {
            MyProgressDialog.showPDialog(getContext());
            RequestQueue queue = Volley.newRequestQueue(getContext());
            String response = null;
            final String finalResponse = response;

            StringRequest postRequest = new StringRequest(Request.Method.POST, accept_url,
                    new com.android.volley.Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("amit", response);
                            try {
                                Object json = new JSONTokener(response).nextValue();
                                JSONObject jsonObject = (JSONObject) json;
                                if (jsonObject.getString("status").equals("STATUS_SUCCESS") || jsonObject.getString("status").equals("1")) {

                                    new Home_CleaningActivity().acceptServiceFragment();
                                    MyProgressDialog.hidePDialog();

                                } else {
                                    Toast.makeText(getContext(), jsonObject.getString("data"), Toast.LENGTH_LONG).show();
                                    Snackbar.make(curr_linear_layout, jsonObject.getString("data"), Snackbar.LENGTH_LONG).show();
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
                    params.put("service_id", service_id);
                    params.put("status", "1");

                    return params;
                }
            };
            postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            queue.add(postRequest);

        } else {
            Snackbar.make(curr_linear_layout, "Data Not Found", Snackbar.LENGTH_LONG).show();

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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.accept:
                if (accept.getText().equals("Accept")) {
                    acceptApiCall();
                } else {
                    new Home_CleaningActivity().acceptServiceFragment();

                }

                // Call checkValidation method
                //checkValidation();
                break;

            case R.id.decline:

                // Replace login fragment

                break;
        }
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
