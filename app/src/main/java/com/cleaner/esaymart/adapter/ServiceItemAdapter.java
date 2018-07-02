package com.cleaner.esaymart.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cleaner.esaymart.R;
import com.cleaner.esaymart.model.ServiceItem;

import java.util.List;

/**
 * Created by user on 12/29/2017.
 */

public class ServiceItemAdapter extends RecyclerView.Adapter<ServiceItemAdapter.MyViewHolder> {

    private Context mContext;
    private List<ServiceItem> serviceItemList;

    //declare interface
    private ServiceItemAdapter.Product_OnItemClicked onClick;

    //make interface like this
    public interface Product_OnItemClicked {
        void Product_onItemClick(int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView product_name, product_discription, product_price;
        private ImageView product_image;
        private CardView cardview;

        public MyViewHolder(View view) {
            super(view);
            product_discription = (TextView) view.findViewById(R.id.product_discription);
            product_price = (TextView) view.findViewById(R.id.product_price);
            product_name = (TextView) view.findViewById(R.id.product_name);
            product_image = (ImageView) view.findViewById(R.id.product_image);
            cardview = (CardView) view.findViewById(R.id.cardview);
        }
    }


    public ServiceItemAdapter(Context mContext, List<ServiceItem> serviceItemList) {
        this.mContext = mContext;
        this.serviceItemList = serviceItemList;
    }

    @Override
    public ServiceItemAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cart_view_service_item, parent, false);

        return new ServiceItemAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ServiceItemAdapter.MyViewHolder holder, final int position) {

        final ServiceItem serviceItem = serviceItemList.get(position);
        holder.product_discription.setText(serviceItem.getProduct_discription());
        holder.product_name.setText(serviceItem.getProduct_name());
        holder.product_price.setText("Rs. " + serviceItem.getProduct_price() + "");
        Glide.with(mContext).load(serviceItem.getProduct_image()).into(holder.product_image);
        Log.d("Image", serviceItem.getProduct_image());

        holder.cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick.Product_onItemClick(position);
            }
        });
    }

    public void setOnClick(ServiceItemAdapter.Product_OnItemClicked onClick) {
        this.onClick = onClick;
    }

    @Override
    public int getItemCount() {
        return serviceItemList.size();
    }

}

