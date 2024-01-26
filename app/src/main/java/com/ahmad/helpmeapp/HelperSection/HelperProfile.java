package com.ahmad.helpmeapp.HelperSection;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ahmad.helpmeapp.R;
import com.ahmad.helpmeapp.customData.CustomHelperData;
import com.ahmad.helpmeapp.databinding.FragmentHelperProfileBinding;
import com.ahmad.helpmeapp.register.AutoDetectLocation;
import com.ahmad.helpmeapp.register.MapHelper;
import com.ahmad.helpmeapp.register.PassLocationData;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class HelperProfile extends Fragment implements PassLocationData {
FragmentHelperProfileBinding helperProfileBinding;
CustomHelperData HelperData;
FirebaseAuth firebaseAuth;
FirebaseFirestore Db;
    String Address;
double lng,lat;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Db=FirebaseFirestore.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();
        helperProfileBinding=FragmentHelperProfileBinding.inflate(getLayoutInflater());
        GetValue();
        helperProfileBinding.Saveinfo.setOnClickListener(this::saveValues);
        helperProfileBinding.LocationHelper.setOnClickListener(this::getlocation);
        return helperProfileBinding.getRoot();
    }

    private void getlocation(View view) {
        MapHelper mapHelper=new MapHelper();
        mapHelper.setDataPass(this);
        mapHelper.show(requireActivity().getSupportFragmentManager(),"map");
    }



    private void GetValue() {
        String Helperid=firebaseAuth.getUid();
        Db.collection("helpers").document(Helperid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
              HelperData=new CustomHelperData();
              HelperData=documentSnapshot.toObject(CustomHelperData.class);
                String Name=HelperData.getFullName();
                String Email=HelperData.getEmail();
                String Password=HelperData.getPassword();
                String LocationHelper=HelperData.getLocationHelper();
                String TypeExp=HelperData.getTypeExp();
                String LevelExp=HelperData.getLevelExp();
                String PhoneNumber=HelperData.getPhone();
               lat=HelperData.getLatitude();
               lng=HelperData.getLongitude();
                helperProfileBinding.EmailHelper.setText(Email);
                helperProfileBinding.NameHelper.setText(Name);
                helperProfileBinding.LocationHelper.setText(LocationHelper);
                helperProfileBinding.PasswordHelper.setText(Password);
                helperProfileBinding.TypeExp.setText(TypeExp);
                helperProfileBinding.LevelExP.setText(LevelExp);
                helperProfileBinding.PhoneHelper.setText(PhoneNumber);
            }
        });


    }
    private void saveValues(View view) {
        if (!TextUtils.isEmpty(helperProfileBinding.NameHelper.getText())
                &&!TextUtils.isEmpty(helperProfileBinding.EmailHelper.getText())
                &&!TextUtils.isEmpty(helperProfileBinding.PasswordHelper.getText())
                &&!TextUtils.isEmpty(helperProfileBinding.TypeExp.getText())
                &&!TextUtils.isEmpty( helperProfileBinding.LevelExP.getText())
                &&!TextUtils.isEmpty(helperProfileBinding.PhoneHelper.getText())
                &&!TextUtils.isEmpty(helperProfileBinding.LocationHelper.getText())){
            UpdateHelperData();
        }else {
            CheckFields();
        }
    }

    private void UpdateHelperData() {
String fullName=helperProfileBinding.NameHelper.getText().toString().trim();
String email=helperProfileBinding.EmailHelper.getText().toString().trim();
String password=helperProfileBinding.PasswordHelper.getText().toString().trim();
String typeExp=helperProfileBinding.TypeExp.getText().toString().trim();
String location=helperProfileBinding.LocationHelper.getText().toString().trim();
String levelexp=helperProfileBinding.LevelExP.getText().toString().trim();
String phone=helperProfileBinding.PhoneHelper.getText().toString().trim();
String id=firebaseAuth.getUid();

       Map <String,Object>update=new HashMap<>();
        update.put("fullName", fullName);
        update.put("email", email);
        update.put("password", password);
        update.put("phone", phone);
        update.put("typeExp", typeExp);
        update.put("levelExp", levelexp);
        update.put("locationHelper", location);
        update.put("id", id);
        update.put("longitude",lng);
        update.put("latitude",lat);
firebaseAuth.getCurrentUser().updateEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
    @Override
    public void onSuccess(Void unused) {
  firebaseAuth.getCurrentUser().updatePassword(password).addOnSuccessListener(new OnSuccessListener<Void>() {
      @Override
      public void onSuccess(Void unused) {
          Db.collection("helpers").document(id).update(update).addOnSuccessListener(new OnSuccessListener<Void>() {
              @Override
              public void onSuccess(Void unused) {
                  Toast.makeText(requireContext()  , "updated data ", Toast.LENGTH_SHORT).show();

              }
          });
      }
  })      ;
    }
});
    }

    private void CheckFields() {
        if (TextUtils.isEmpty(helperProfileBinding.NameHelper.getText())){
            helperProfileBinding.NameHelper.setError("name required");
        }
        if (TextUtils.isEmpty(helperProfileBinding.EmailHelper.getText())){
            helperProfileBinding.EmailHelper.setError("Email required");

        }
        if (TextUtils.isEmpty(helperProfileBinding.PasswordHelper.getText())){
            helperProfileBinding.PasswordHelper.setError("password required");

        }
        if (TextUtils.isEmpty(helperProfileBinding.PhoneHelper.getText())){
            helperProfileBinding.PhoneHelper.setError("phone required");

        }
        if (TextUtils.isEmpty(helperProfileBinding.TypeExp.getText())){
            helperProfileBinding.TypeExp.setError("this field required");

        }
        if (TextUtils.isEmpty(helperProfileBinding.LevelExP.getText())){
            helperProfileBinding.LevelExP.setError("this field required");

        }
        if (TextUtils.isEmpty(helperProfileBinding.LocationHelper.getText())){
            helperProfileBinding.LocationHelper.setError("this field required");

        }}

    @Override
    public void onDataPass(double longitude, double latitude, String data) {
        helperProfileBinding.LocationHelper.setText(data);
        this.lat=latitude;
        this.lng=longitude;
    }

}