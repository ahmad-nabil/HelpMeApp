package com.ahmad.helpmeapp.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ahmad.helpmeapp.HelperSection.LocationAskerFragment;
import com.ahmad.helpmeapp.customData.CustomAskerData;
import com.ahmad.helpmeapp.databinding.ActivityMainMenuHelperBinding;
import com.ahmad.helpmeapp.databinding.CardDetailsAskerBinding;
import com.ahmad.helpmeapp.databinding.RecycleListRequestsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RecycleRequestsAdapter extends RecyclerView.Adapter<RecycleRequestsAdapter.HolderList> {
    Activity activity;
    ArrayList<CustomAskerData> askerData;
    ArrayList<String> idUser;

    ActivityMainMenuHelperBinding mainMenuHelperBinding;
    double longitude, latitude;
    FragmentManager fragmentManager;
    FirebaseAuth auth;

    public RecycleRequestsAdapter(Activity activity, ArrayList<CustomAskerData> askerData, ArrayList<String> idUser, ActivityMainMenuHelperBinding mainMenuHelperBinding, FragmentManager fragmentManager) {
        this.activity = activity;
        this.askerData = askerData;
        this.idUser = idUser;
        this.mainMenuHelperBinding = mainMenuHelperBinding;
        this.fragmentManager = fragmentManager;
        auth = FirebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public RecycleRequestsAdapter.HolderList onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecycleListRequestsBinding recycleListRequestsBinding = RecycleListRequestsBinding
                .inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new HolderList(recycleListRequestsBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderList holder, int position) {
        holder.recycleListRequestsBinding.nameAsker.setText(askerData.get(position).getName() + "\t Need Help");
        holder.recycleListRequestsBinding.distance.setText(askerData.get(position).getDistance() + "\tM");
        holder.recycleListRequestsBinding.Accept.setOnClickListener(v -> {
            //sho data asker
            showCardAsker(position);
            //send data to real time data base to approved operation and show Card info Helper in user Account
            EnableCard(position);
        });
        holder.recycleListRequestsBinding.dismiss.setOnClickListener(v -> {
            //send data to real time data base to dismiss operation ands notify user in this and dismiss card info Helper in user Account
            disableCard(position);
        });
    }

    //Enable Card info Helper in user Account
    private void EnableCard(int position) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("approveOperation");
        databaseReference.child(idUser.get(position)).setValue("true");
    }

    //dismiss Card info Helper in user Account
    private void disableCard(int position) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("approveOperation");
        databaseReference.child(idUser.get(position)).setValue("false");
        DatabaseReference deleteValue = FirebaseDatabase.getInstance().getReference("helperList").child(auth.getUid()).child(idUser.get(position));
        deleteValue.removeValue();

    }

    private void showCardAsker(int position) {
        CardDetailsAskerBinding CardBinding = CardDetailsAskerBinding.inflate(LayoutInflater.from(activity));
        Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(CardBinding.getRoot());
        CardBinding.name.setText(askerData.get(position).getName());
        CardBinding.car.setText(askerData.get(position).getCar());
        CardBinding.colorcar.setText(askerData.get(position).getColorCar());
        latitude = askerData.get(position).getLatitude();
        longitude = askerData.get(position).getLongitude();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        CardBinding.completeBtn.setOnClickListener(v -> {
            CompletedOperation(position, dialog);

        });
        CardBinding.locationAskerBtn.setOnClickListener(v ->
                showMap(dialog));

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("approveOperation").child(idUser.get(position));

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String value = snapshot.getValue(String.class);
                //if user dismiss operation this will shopw to helper and dismiss operation for helper
                if (value.equals("dismissed")) {
                    Toast.makeText(activity.getApplicationContext(), "User dismissed request", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    //send data to real time database to notify  operation completed
    private void CompletedOperation(int position, Dialog dialog) {
        Toast.makeText(activity.getApplicationContext(), "Thanks for your effort", Toast.LENGTH_SHORT).show();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("approveOperation");
        databaseReference.child(idUser.get(position)).setValue("completed");
        DatabaseReference deleteValue = FirebaseDatabase.getInstance().getReference("helperList").child(auth.getUid()).child(idUser.get(position));
        deleteValue.removeValue();
        dialog.dismiss();
    }


    private void showMap(Dialog dialog) {
        dialog.dismiss();
        LocationAskerFragment askerFragment = LocationAskerFragment.newInstance(longitude, latitude);
        fragmentManager.beginTransaction().add(mainMenuHelperBinding.ViewsHelper.getId(), askerFragment).addToBackStack(null).commit();
    }

    @Override
    public int getItemCount() {
        return askerData.size();
    }

    public class HolderList extends RecyclerView.ViewHolder {
        RecycleListRequestsBinding recycleListRequestsBinding;

        public HolderList(RecycleListRequestsBinding recycleListRequestsBinding) {
            super(recycleListRequestsBinding.getRoot());
            this.recycleListRequestsBinding = recycleListRequestsBinding;
        }
    }
}
