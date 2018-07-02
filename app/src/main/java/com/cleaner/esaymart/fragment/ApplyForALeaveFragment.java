package com.cleaner.esaymart.fragment;

import android.app.DatePickerDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cleaner.esaymart.R;
import com.cleaner.esaymart.activity_cleaningBoy.Register_Cleaning_BoyActivity;
import com.cleaner.esaymart.customtoast.CustomToast;
import com.cleaner.esaymart.utils.MyProgressDialog;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.cleaner.esaymart.activity_cleaningBoy.Home_CleaningActivity.s_emp_id;
import static com.cleaner.esaymart.utils.utils.isNetworkAvailable;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ApplyForALeaveFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ApplyForALeaveFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ApplyForALeaveFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private EditText service_Date_start, service_Date_end;
    private int mYear, mMonth, mDay, mHour, mMinute;
    String key_date;
    private Button signUpBtn;
    private EditText message;
    private String s_message;
    private String to = "";
    private String from = "";
    private View view;
    private TextView leave_status;
    private FrameLayout apply_layout;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private String leaveApi = "http://esaymart.com/admin_dryclean/users/leave.php";

    public ApplyForALeaveFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ApplyForALeaveFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ApplyForALeaveFragment newInstance(String param1, String param2) {
        ApplyForALeaveFragment fragment = new ApplyForALeaveFragment();
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
        view = inflater.inflate(R.layout.fragment_apply_for_aleave, container, false);

        getIds(view);
        service_Date_start.setOnClickListener(this);
        service_Date_end.setOnClickListener(this);
        signUpBtn.setOnClickListener(this);

        return view;
    }

    private void getIds(View view) {
        service_Date_start = (EditText) view.findViewById(R.id.service_Date_start);
        service_Date_end = (EditText) view.findViewById(R.id.service_Date_end);
        signUpBtn = (Button) view.findViewById(R.id.signUpBtn);
        message = (EditText) view.findViewById(R.id.message);
        leave_status = (TextView) view.findViewById(R.id.leave_status);
        apply_layout = (FrameLayout) view.findViewById(R.id.apply_layout);

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

        if (view == service_Date_start) {
            findDate();
        }
        if (view == service_Date_end) {
            findDate1();
        }
        if (view == signUpBtn) {
            ApiCall();
        }
    }

    private void ApiCall() {
        boolean isExecuteNext = true;
        s_message = message.getText().toString();
        if (s_message.isEmpty()) {
            Log.d("name", s_message + "");
            message.setError(getResources().getString(R.string.name_error));
            isExecuteNext = false;
        }
        if (to.isEmpty()) {
            Log.d("name", to + "");
            service_Date_start.setError(getResources().getString(R.string.name_error));
            isExecuteNext = false;
        }
        if (from.isEmpty()) {
            Log.d("name", from + "");
            service_Date_end.setError(getResources().getString(R.string.name_error));
            isExecuteNext = false;
        }
        if (!isExecuteNext) {
            //registerLayout.startAnimation(shakeAnimation);
            // scrollView.startAnimation(shakeAnimation);
            new CustomToast().Show_Toast(getContext(), view,
                    "All fields are required.");
            return;
        } else {
            MyProgressDialog.showPDialog(getContext());
            Log.d("abcd->>>>>>", s_message + to + from);
            leaveApiCall();
        }


    }

    private void findDate1() {
        // Get Current Date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        service_Date_end.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        to = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 10000);

        long timestamp = Long.parseLong(String.valueOf(System.currentTimeMillis()));
        Log.d("date", System.currentTimeMillis() + getDate(timestamp) + "");
        datePickerDialog.show();
    }

    private void findDate() {
        // Get Current Date
        final Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, -1);
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        service_Date_start.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        from = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        long timestamp = Long.parseLong(String.valueOf(System.currentTimeMillis()));
        Log.d("date", System.currentTimeMillis() + getDate(timestamp) + "");
        datePickerDialog.show();
    }

    private void leaveApiCall() {
        Boolean result = isNetworkAvailable(getActivity());
        if (result) {
            MyProgressDialog.showPDialog(getContext());
            RequestQueue queue = Volley.newRequestQueue(getContext());
            String response = null;
            final String finalResponse = response;

            StringRequest postRequest = new StringRequest(Request.Method.POST, leaveApi,
                    new com.android.volley.Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("amit", response);
                            try {
                                Object json = new JSONTokener(response).nextValue();
                                JSONObject jsonObject = (JSONObject) json;
                                if (jsonObject.getString("status").equals("1") || jsonObject.getString("status").equals("STATUS_SUCCESS")) {

                                    leave_status.setVisibility(View.VISIBLE);
                                    Toast.makeText(getContext(), jsonObject.getString("data"), Toast.LENGTH_LONG).show();
                                    MyProgressDialog.hidePDialog();

                                } else {
                                    Toast.makeText(getContext(), jsonObject.getString("data"), Toast.LENGTH_LONG).show();
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
                    params.put("super_id", 1 + "");
                    params.put("message", s_message);
                    params.put("fromm", from);
                    params.put("too", to);
                    return params;
                }
            };
            postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            queue.add(postRequest);
        } else {
            Snackbar.make(apply_layout, "Data Not Found", Snackbar.LENGTH_LONG).show();

        }
    }

    private String getDate(long timeStamp) {

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
            Date netDate = (new Date(timeStamp));
            return sdf.format(netDate);
        } catch (Exception ex) {
            return "xx";
        }
    }

    public void submit_for_leave(View view) {

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
