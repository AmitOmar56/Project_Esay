package com.cleaner.esaymart.fragment;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cleaner.esaymart.R;
import com.cleaner.esaymart.model.Attendence;

import java.util.List;

/**
 * Created by LifePlayTrip on 2/20/2018.
 */

public class AttendenceAdapter extends RecyclerView.Adapter<AttendenceAdapter.MyViewHolder> {
    private Context context;
    private List<Attendence> attendenceList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView attendence_date;
        public ImageView attendence_present, attendence_absent;

        public MyViewHolder(View view) {
            super(view);
            attendence_date = (TextView) view.findViewById(R.id.attendence_date);
            attendence_present = (ImageView) view.findViewById(R.id.attendence_present);
            attendence_absent = (ImageView) view.findViewById(R.id.attendence_absent);

        }
    }

    public AttendenceAdapter(Context context, List<Attendence> attendenceList) {
        this.context = context;
        this.attendenceList = attendenceList;
    }

    @Override
    public AttendenceAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.attendence_card_view, parent, false);

        return new AttendenceAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final AttendenceAdapter.MyViewHolder holder, final int position) {
        Attendence attendence = attendenceList.get(position);
        holder.attendence_date.setText(attendence.getDate());

        // loading album cover using Glide library
        Glide.with(context).load(attendence.getPresent()).into(holder.attendence_present);
        Glide.with(context).load(attendence.getAbsent()).into(holder.attendence_absent);

    }

    @Override
    public int getItemCount() {
        return attendenceList.size();
    }
}
