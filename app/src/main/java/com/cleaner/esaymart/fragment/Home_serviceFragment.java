package com.cleaner.esaymart.fragment;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cleaner.esaymart.R;
import com.cleaner.esaymart.service.MyService;
import com.cleaner.esaymart.utils.MyProgressDialog;
import com.skyfishjy.library.RippleBackground;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.HashMap;
import java.util.Map;

import static com.cleaner.esaymart.activity_cleaningBoy.Home_CleaningActivity.s_emp_id;
import static com.cleaner.esaymart.utils.utils.isNetworkAvailable;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Home_serviceFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Home_serviceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Home_serviceFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private Button start_serviceButton;
    private RippleBackground rippleBackground;
    private View view;
    private Long timestamp;
    private String start_time, end_time;
    private LinearLayout service_linear;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private String service_start_url = "http://esaymart.com/admin_dryclean/users/serv_start.php";
    private String service_stop_url = "http://esaymart.com/admin_dryclean/users/serv_stop.php";

    public Home_serviceFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Home_serviceFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Home_serviceFragment newInstance(String param1, String param2) {
        Home_serviceFragment fragment = new Home_serviceFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestPermissionsIfNotGranted();

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home_service, container, false);
        initId();
        setListeners();
        timestamp = System.currentTimeMillis() / 1000;
        isMyServiceRunning(MyService.class);
        return view;
    }

    private void initId() {
        start_serviceButton = (Button) view.findViewById(R.id.start_serviceButton);
        rippleBackground = (RippleBackground) view.findViewById(R.id.content);
        service_linear = (LinearLayout) view.findViewById(R.id.service_linear);

    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                rippleBackground.startRippleAnimation();
                start_serviceButton.setText("stop");

                return true;
            }
        }
        return false;
    }

    private void requestPermissionsIfNotGranted() {

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }
    }

    public void start_service() {
        Boolean result = isNetworkAvailable(getActivity());
        if (result) {
            if (start_serviceButton.getText().equals("start")) {
                rippleBackground.startRippleAnimation();
                getActivity().startService(new Intent(getActivity(), MyService.class));
                start_serviceButton.setText("stop");
                start_time = timestamp.toString();
                Log.d("start_time", start_time + "");
                serviceStartApiCall();
            } else if (start_serviceButton.getText().equals("stop")) {
                rippleBackground.stopRippleAnimation();
                getActivity().stopService(new Intent(getActivity(), MyService.class));
                start_serviceButton.setText("start");
                end_time = timestamp.toString();
                Log.d("end_time", end_time + "");
                serviceStopApiCall();
            }
        } else {
            Snackbar.make(service_linear, "Data Not Found", Snackbar.LENGTH_LONG).show();

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

    // Set Listeners
    private void setListeners() {
        start_serviceButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.start_serviceButton:
                start_service();
                break;
        }
    }

    private void serviceStartApiCall() {
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String response = null;
        final String finalResponse = response;

        StringRequest postRequest = new StringRequest(Request.Method.POST, service_start_url,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("amit", response);
                        try {
                            Object json = new JSONTokener(response).nextValue();
                            JSONObject jsonObject = (JSONObject) json;
                            if (jsonObject.getString("status").equals("STATUS_SUCCESS") || jsonObject.getString("status").equals("1")) {
                                //   Snackbar.make(service_linear, "Service is start", Snackbar.LENGTH_LONG).show();
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
                params.put("fromm", start_time + "");
                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(postRequest);

    }

    private void serviceStopApiCall() {
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String response = null;
        final String finalResponse = response;

        StringRequest postRequest = new StringRequest(Request.Method.POST, service_stop_url,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("amit", response);
                        try {
                            Object json = new JSONTokener(response).nextValue();
                            JSONObject jsonObject = (JSONObject) json;
                            if (jsonObject.getString("status").equals("STATUS_SUCCESS") || jsonObject.getString("status").equals("1")) {
                                Snackbar.make(service_linear, "Service is stop", Snackbar.LENGTH_LONG).show();
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
                params.put("too", end_time + "");
                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(postRequest);

    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

//    public boolean isNetworkAvailable() {
//        ConnectivityManager connectivityManager
//                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
//        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
//    }
}
