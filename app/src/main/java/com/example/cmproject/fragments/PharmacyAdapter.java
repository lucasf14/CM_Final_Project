package com.example.cmproject.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cmproject.R;
import com.example.cmproject.objects.Pharmacy;

import java.util.ArrayList;


public class PharmacyAdapter extends RecyclerView.Adapter<PharmacyAdapter.ViewHolder> {

    private ArrayList<Pharmacy> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Context ctx;

    PharmacyAdapter(Context context, ArrayList<Pharmacy> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.ctx = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.pharmacy_view, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Pharmacy pharmacyList = mData.get(position);
        holder.pharmacyView.setText(pharmacyList.getName());
        holder.stateView.setText(pharmacyList.getState());
        if(pharmacyList.getState().equals("ABERTO"))
            holder.stateView.setTextColor(ContextCompat.getColor(ctx, R.color.green));
        else
            holder.stateView.setTextColor(ContextCompat.getColor(ctx, R.color.red));
        holder.addressView.setText(pharmacyList.getAddress());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView pharmacyView, stateView, addressView;

        ViewHolder(View itemView) {
            super(itemView);
            pharmacyView = itemView.findViewById(R.id.pharmacyView);
            stateView = itemView.findViewById(R.id.stateView);
            addressView = itemView.findViewById(R.id.addressView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }


    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}