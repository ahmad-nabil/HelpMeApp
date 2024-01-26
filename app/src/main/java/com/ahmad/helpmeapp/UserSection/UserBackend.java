package com.ahmad.helpmeapp.UserSection;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.ahmad.helpmeapp.customData.CustomAskerData;
import com.ahmad.helpmeapp.customData.CustomLocationHelper;
import com.ahmad.helpmeapp.customData.CustomUserData;
import com.ahmad.helpmeapp.customData.CustomOrdersData;
import com.ahmad.helpmeapp.databinding.DialogeRateBinding;
import com.ahmad.helpmeapp.databinding.DialogeinfohelperBinding;
import com.ahmad.helpmeapp.gecodingAdapter.gecoding;
import com.ahmad.helpmeapp.gecodingAdapter.gecodingListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mapbox.maps.MapView;

public class UserBackend {
    Dialog DialogCustom;
    Activity activity;
    MapView mapView;
    CustomLocationHelper DataHelper;
    int DistanceInMeter;
    String RegionUser;
    FirebaseAuth auth;
    FirebaseDatabase realtimeDb;
    DatabaseReference Reference;
    CustomAskerData askerData;
    double longitude;
    double latitude;

    public UserBackend(Dialog DialogCustom, Activity activity, MapView mapView, CustomLocationHelper dataHelper, int distanceInMeter, String regionUser, double longitude, double latitude) {
        this.DialogCustom = DialogCustom;
        this.activity = activity;
        this.mapView = mapView;
        DataHelper = dataHelper;
        DistanceInMeter = distanceInMeter;
        RegionUser = regionUser;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public void checkAddress() {
        auth = FirebaseAuth.getInstance();
        realtimeDb = FirebaseDatabase.getInstance();
        String longitude = DataHelper.getLongitude().toString();
        String latitude = DataHelper.getLatitude().toString();
        gecoding.getGeocoding(activity, latitude, longitude, new gecodingListener() {
            @Override
            public void FullAddressName(String FullAddress) {

            }

            @Override
            public void cityName(String cityName) {
                if (cityName.equals(RegionUser)) {
                    AddToDb(DataHelper, DistanceInMeter);
                } else {
                    Toast.makeText(activity, "sorry we don't find any one in your city we will send you to nearest one in our database", Toast.LENGTH_SHORT).show();
                    AddToDb(DataHelper, DistanceInMeter);
                    Toast.makeText(activity, cityName, Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void pointofinterest(String Poi) {

            }

            @Override
            public void Error(String errorMessage) {

            }
        });
    }

    //show Data Helper
    private void showData(CustomLocationHelper dataHelper) {
        DialogCustom.dismiss(); //we dismissed dialog previously
        DialogeinfohelperBinding dialogeinfohelperBinding = DialogeinfohelperBinding.inflate(activity.getLayoutInflater());
        DialogCustom.setContentView(dialogeinfohelperBinding.getRoot());
        String typeExp = dataHelper.getTypeExp();
        String levelExp = dataHelper.getLevelExp();
        String name = dataHelper.getName();
        String num_tel = dataHelper.getPhone();
        dialogeinfohelperBinding.Helperid.setText(name);
        dialogeinfohelperBinding.hisExperience.setText(typeExp);
        dialogeinfohelperBinding.levelexp.setRating(Float.parseFloat(levelExp));
       //check if stay any dialog in activity
        if (!((Activity) activity).isFinishing()) {
            DialogCustom.show();
        }
        dialogeinfohelperBinding.callbtn.setOnClickListener(v -> {
            Intent callHelper = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", num_tel, null));
            activity.startActivity(callHelper);
        });
        dialogeinfohelperBinding.cancelbtn.setOnClickListener(v -> {
            DialogCustom.dismiss();
            removeData(dataHelper);
        });


        dialogeinfohelperBinding.tickimg.setOnClickListener(v -> {
            showRate(dataHelper);
        });


    }

    //get data user and send it to realtime database
    private void AddToDb(CustomLocationHelper dataHelper, int distance) {
        FirebaseFirestore Db;
        //get data user from Firestore
        Db = FirebaseFirestore.getInstance();
        Db.collection("users").document(auth.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                CustomUserData UserData = new CustomUserData();
                UserData = documentSnapshot.toObject(CustomUserData.class);
                String name = UserData.getName();
                String typeCar = UserData.getTypeCar();
                String colorCar = UserData.getColorCar();

                //get id helper
                String id = dataHelper.getId();
                //put them in custom opject
                askerData = new CustomAskerData(name, String.valueOf(distance), typeCar, colorCar, longitude, latitude);
                //store data user in helper list with uId (id helper)
                Reference = FirebaseDatabase.getInstance().getReference("helperList").child(id).child(auth.getUid());
                Reference.setValue(askerData);
                approved(auth.getUid(), dataHelper);
            }
        });

    }
    //remove request user and update it to realtime database in acc helper

    public void removeData(CustomLocationHelper dataHelper) {

        String id = dataHelper.getId();
        //remove data user in helper list with uId (id helper)
        Reference = FirebaseDatabase.getInstance().getReference("helperList").child(id).child(auth.getUid());
        Reference.removeValue();
        DatabaseReference ReferenceOperation = FirebaseDatabase.getInstance().getReference("approveOperation");
        ReferenceOperation.child(auth.getUid()).setValue("dismissed");
        DialogCustom.dismiss();


    }


    public void approved(String IdUser, CustomLocationHelper dataHelper) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("approveOperation");
        databaseReference.child(IdUser).setValue("binding");
        databaseReference.child(IdUser).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String value = snapshot.getValue(String.class);
                if (value.equals("true")) {
                    showData(dataHelper);
                } else if (value.equals("false")) {
                    Toast.makeText(activity, "sorry  helper we found him was dismiss request it retry to search nearest on to you after moments", Toast.LENGTH_SHORT).show();
                    DialogCustom.dismiss();
                } else if (value.equals("completed")) {
                    showRate(dataHelper);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
//show dialog rate
    private void showRate(CustomLocationHelper dataHelper) {
        DialogCustom.dismiss();
        DialogeRateBinding binding = DialogeRateBinding.inflate(activity.getLayoutInflater());
        DialogCustom.setContentView(binding.getRoot());
        binding.Experience.setText(dataHelper.getTypeExp());
        binding.HelperName.setText(dataHelper.getName());
        if (!((Activity) activity).isFinishing()) {
            DialogCustom.show();
        }
        String name = dataHelper.getName();
        String experience = dataHelper.getTypeExp();
        DatabaseReference rateuser = FirebaseDatabase.getInstance().getReference("Ratehelpers").child(auth.getUid());
        binding.donebtn.setOnClickListener(v -> {
            String rate = String.valueOf(binding.rate.getRating());
            CustomOrdersData data = new CustomOrdersData(name, rate, experience);
            rateuser.push().setValue(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(activity, "thanks for rate our helper", Toast.LENGTH_SHORT).show();
                    DialogCustom.dismiss();
                }
            });

        });
    }


}
