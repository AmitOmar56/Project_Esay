package com.cleaner.esaymart.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cleaner.esaymart.R;
import com.cleaner.esaymart.model.Service;

import java.util.List;

/**
 * Created by user on 12/29/2017.
 */

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.MyViewHolder> {
    private Context context;
    private List<Service> serviceList;

    //declare interface
    private ServiceAdapter.News_OnItemClicked onClick;

    //make interface like this
    public interface News_OnItemClicked {
        void news_onItemClick(int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView service_name;
        public ImageView service_image;

        public MyViewHolder(View view) {
            super(view);
            service_name = (TextView) view.findViewById(R.id.service_name);
            service_image = (ImageView) view.findViewById(R.id.service_image);
        }
    }

    public ServiceAdapter(Context context, List<Service> serviceList) {
        this.context = context;
        this.serviceList = serviceList;
    }

    @Override
    public ServiceAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_service_screen, parent, false);

        return new ServiceAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ServiceAdapter.MyViewHolder holder, final int position) {
        Service service = serviceList.get(position);
        holder.service_name.setText(service.getService_name());
        // loading album cover using Glide library
        Glide.with(context).load(service.getService_image()).into(holder.service_image);

        holder.service_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick.news_onItemClick(position);
            }
        });
    }
    public void setOnClick(ServiceAdapter.News_OnItemClicked onClick) {
        this.onClick = onClick;
    }

    @Override
    public int getItemCount() {
        return serviceList.size();
    }
}
