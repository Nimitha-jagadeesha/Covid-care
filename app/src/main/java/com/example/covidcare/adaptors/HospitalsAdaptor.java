package com.example.covidcare.adaptors;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.covidcare.R;
import com.example.covidcare.dataexpert.HospitalExpert;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HospitalsAdaptor extends RecyclerView.Adapter<HospitalsAdaptor.ViewHolder> {

    private Context context;

    public HospitalsAdaptor(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public HospitalsAdaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View cv= (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.hospital_detail_view,parent,false);
        return new HospitalsAdaptor.ViewHolder(cv);
    }

    @Override
    public void onBindViewHolder(@NonNull HospitalsAdaptor.ViewHolder holder, int position) {
        View view=holder.view;
        TextView textView=view.findViewById(R.id.hospitalName);
        textView.setText("Hospital Name: "+HospitalExpert.getHospitalName(position));
        TextView textViewNumberOfBeds=view.findViewById(R.id.numberOfBeds);
        textViewNumberOfBeds.setText("Number Of Beds :"+HospitalExpert.getNumberOfBedsAvailable(position));
    }

    @Override
    public int getItemCount() {
        return HospitalExpert.getHospitalsCount();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        private View view;
        ViewHolder(View view)
        {
            super(view);
            this.view=view;
        }
    }
}
