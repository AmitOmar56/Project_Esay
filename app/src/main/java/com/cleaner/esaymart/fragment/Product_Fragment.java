package com.cleaner.esaymart.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cleaner.esaymart.R;
import com.cleaner.esaymart.activity.ProductItemActivity;
import com.cleaner.esaymart.activity.ServiceItemActivity;
import com.cleaner.esaymart.adapter.ServiceAdapter;
import com.cleaner.esaymart.model.Service;
import com.cleaner.esaymart.utils.MyProgressDialog;
import com.cleaner.esaymart.utils.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.List;

import static com.cleaner.esaymart.utils.utils.dpToPx;
import static com.cleaner.esaymart.utils.utils.isNetworkAvailable;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Product_Fragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Product_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Product_Fragment extends Fragment implements ServiceAdapter.News_OnItemClicked {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private RecyclerView recyclerView;
    private ServiceAdapter adapter = null;
    private List<Service> serviceList;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private String product_name;
    private String product_image;
    private int product_id;
    private String imagePath = "http://littlejoy.co.in/esaymart/public_html/admin_dryclean/api/";
    private String url = "http://littlejoy.co.in/esaymart/public_html/admin_dryclean/users/category.php";
    private Service service;
    private RelativeLayout prod_layout;

    public Product_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Product_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Product_Fragment newInstance(String param1, String param2) {
        Product_Fragment fragment = new Product_Fragment();
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
        View view = inflater.inflate(R.layout.fragment_product_, container, false);

        /*****************(Start) code For Card View*****************/
        prod_layout = (RelativeLayout) view.findViewById(R.id.prod_layout);
        recyclerView = (RecyclerView) view.findViewById(R.id.product_recyclerView);
        serviceList = new ArrayList<>();
        adapter = new ServiceAdapter(getContext(), serviceList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new utils.GridSpacingItemDecoration(2, dpToPx(getActivity(), 10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        adapter.setOnClick(this);
        signupRequest();
        /*****************(End) code For Card View Vertical*****************/
        return view;
    }

    @Override
    public void news_onItemClick(int position) {
        service = serviceList.get(position);
        Toast.makeText(getContext(), "clicked", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(getContext(), ProductItemActivity.class);
        intent.putExtra("id", service.getService_id() + "");
        startActivity(intent);
    }

    /**
     * Adding few albums for testing
     */

    private void signupRequest() {
        Boolean result = isNetworkAvailable(getActivity());
        if (result) {
            MyProgressDialog.showPDialog(getContext());
            RequestQueue queue = Volley.newRequestQueue(getContext());
            String response = null;
            final String finalResponse = response;

            StringRequest postRequest = new StringRequest(Request.Method.GET, url,
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

                                        JSONObject profile = jsonArray.getJSONObject(i);
                                        product_name = profile.getString("cat_name");
                                        product_image = profile.getString("image");
                                        product_image = imagePath + product_image;
                                        product_id = Integer.parseInt(profile.getString("cat_id"));

                                        service = new Service(product_name, product_id, product_image);
                                        serviceList.add(service);
                                        adapter.notifyDataSetChanged();

                                    }
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
                            Toast.makeText(getContext(), "Network Error", Toast.LENGTH_LONG).show();

                            MyProgressDialog.hidePDialog();
                        }
                    }
            );
            postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            queue.add(postRequest);
        } else {
            Snackbar.make(prod_layout, "Data Not Found", Snackbar.LENGTH_LONG).show();

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
