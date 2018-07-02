package com.cleaner.esaymart.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cleaner.esaymart.R;
import com.cleaner.esaymart.activity.ServiceItemActivity;
import com.cleaner.esaymart.adapter.ServiceAdapter;
import com.cleaner.esaymart.model.Service;
import com.cleaner.esaymart.utils.utils;

import java.util.ArrayList;
import java.util.List;

import static com.cleaner.esaymart.utils.utils.dpToPx;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Service_Fragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Service_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Service_Fragment extends Fragment implements ServiceAdapter.News_OnItemClicked {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private RecyclerView recyclerView;
    private ServiceAdapter adapter = null;
    private List<Service> serviceList;
    private String path = "http://esaymart.com/admin_dryclean/uploads/";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Service_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Service_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Service_Fragment newInstance(String param1, String param2) {
        Service_Fragment fragment = new Service_Fragment();
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
        View view = inflater.inflate(R.layout.fragment_service, container, false);

        /*****************(Start) code For Card View*****************/

        recyclerView = (RecyclerView) view.findViewById(R.id.service_recyclerView);
        serviceList = new ArrayList<>();
        adapter = new ServiceAdapter(getContext(), serviceList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new utils.GridSpacingItemDecoration(2, dpToPx(getActivity(), 10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        adapter.setOnClick(this);
        prepareAlbums();
        /*****************(End) code For Card View Vertical*****************/

        return view;
    }

    @Override
    public void news_onItemClick(int position) {
        Service service = serviceList.get(position);

        Toast.makeText(getContext(), "clicked", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(getContext(), ServiceItemActivity.class);
        intent.putExtra("id", service.getService_id() + "");
        startActivity(intent);
    }

    /**
     * Adding few albums for testing
     */

    private void prepareAlbums() {

        Service service = new Service("HATCHBACK", 0, path + "small_car.jpg");
        serviceList.add(service);

        service = new Service("SEDAN", 1, path + "large_car.jpg");
        serviceList.add(service);

        service = new Service("PREMIUMS", 2, path + "small_suv.jpg");
        serviceList.add(service);

        service = new Service("LUXURY", 3, path + "large_SUV.jpg");
        serviceList.add(service);

        adapter.notifyDataSetChanged();
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
