package com.ahmad.helpmeapp.HelperSection;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.ahmad.helpmeapp.adapter.RecycleRequestsAdapter;
import com.ahmad.helpmeapp.customData.CustomAskerData;
import com.ahmad.helpmeapp.databinding.ActivityMainMenuHelperBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class RequestList {
    ActivityMainMenuHelperBinding mainMenuHelperBinding;
    Activity activity;
    DatabaseReference helperReference;
    ArrayList <CustomAskerData>askerData=new ArrayList<>();
    ArrayList <String>idUser=new ArrayList<>();
    FragmentManager fragmentManager;

    public RequestList(ActivityMainMenuHelperBinding mainMenuHelperBinding, Activity activity, FragmentManager fragmentManager) {
        this.mainMenuHelperBinding = mainMenuHelperBinding;
        this.activity = activity;
        this.fragmentManager = fragmentManager;
    }

    public void RecycleViewInitialize(){
        FirebaseAuth auth=FirebaseAuth.getInstance();
String Helperid=auth.getUid();
helperReference= FirebaseDatabase.getInstance().getReference("helperList").child(Helperid);
    RecycleRequestsAdapter adapter=new RecycleRequestsAdapter(activity,askerData,idUser,mainMenuHelperBinding, fragmentManager);
    mainMenuHelperBinding.requestList.setAdapter(adapter);
    mainMenuHelperBinding.requestList.setLayoutManager(new LinearLayoutManager(activity,LinearLayoutManager.VERTICAL,false));
       helperReference.addChildEventListener(new ChildEventListener() {
           @Override
           public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
               CustomAskerData Asker=snapshot.getValue(CustomAskerData.class);
               idUser.add(snapshot.getKey());
               askerData.add(Asker);
             adapter.notifyItemInserted(askerData.size() - 1);

           }

           @Override
           public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

           }

           @Override
           public void onChildRemoved(@NonNull DataSnapshot snapshot) {
               String removedKey = snapshot.getKey();
               int indexToRemove = idUser.indexOf(removedKey);
               if (indexToRemove != -1) {
                   idUser.remove(indexToRemove);
                   askerData.remove(indexToRemove);
                   adapter.notifyItemRemoved(indexToRemove);
           }}

           @Override
           public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {

           }
       });
    }
}
