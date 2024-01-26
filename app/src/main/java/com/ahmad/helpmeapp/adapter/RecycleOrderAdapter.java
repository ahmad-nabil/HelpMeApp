package com.ahmad.helpmeapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ahmad.helpmeapp.customData.CustomOrdersData;
import com.ahmad.helpmeapp.databinding.FragmentOrdersBinding;
import com.ahmad.helpmeapp.databinding.RateOrdersRvBinding;

import java.util.ArrayList;
import java.util.List;

public class RecycleOrderAdapter extends RecyclerView.Adapter<RecycleOrderAdapter.OrderHolder> {
    ArrayList <CustomOrdersData>List;
    Context context;

    public RecycleOrderAdapter(ArrayList<CustomOrdersData> list, Context context) {
        List = list;
        this.context = context;
    }

    @NonNull
    @Override
    public OrderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RateOrdersRvBinding binding=RateOrdersRvBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new OrderHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderHolder holder, int position) {
holder.rateOrdersRvBinding.nameRatte.setText(List.get(position).getName());
holder.rateOrdersRvBinding.typExp.setText(List.get(position).getExperience());
holder.rateOrdersRvBinding.rate.setRating(Float.parseFloat(List.get(position).getRate()));
    }

    @Override
    public int getItemCount() {
        return List.size();
    }

    public class OrderHolder extends RecyclerView.ViewHolder{
        RateOrdersRvBinding rateOrdersRvBinding;
        public OrderHolder( RateOrdersRvBinding binding){
            super(binding.getRoot());
            this.rateOrdersRvBinding=binding;

        }
    }
}
