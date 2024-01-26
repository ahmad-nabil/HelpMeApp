package com.ahmad.helpmeapp.UserSection;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ahmad.helpmeapp.adapter.RecycleOrderAdapter;
import com.ahmad.helpmeapp.customData.CustomOrdersData;
import com.ahmad.helpmeapp.databinding.FragmentOrdersBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class Orders extends Fragment {
    ArrayList<CustomOrdersData> list = new ArrayList<>();
    RecycleOrderAdapter recycleOrderAdapter;
    FragmentOrdersBinding ordersBinding;
    FirebaseAuth auth;
    DatabaseReference reference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //inflate
        ordersBinding = FragmentOrdersBinding.inflate(getLayoutInflater());
        auth = FirebaseAuth.getInstance();
        //initialize recycle view
        recycleOrderAdapter = new RecycleOrderAdapter(list, requireContext());
        ordersBinding.RvOrders.setAdapter(recycleOrderAdapter);
        ordersBinding.RvOrders.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
        //get data from real time database
        reference = FirebaseDatabase.getInstance().getReference("Ratehelpers").child(auth.getUid());
        getdata(reference);

        return ordersBinding.getRoot();
    }

    private void getdata(DatabaseReference reference) {
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                CustomOrdersData ordersData = snapshot.getValue(CustomOrdersData.class);
                list.add(ordersData);
                recycleOrderAdapter.notifyItemInserted(list.size() - 1);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}