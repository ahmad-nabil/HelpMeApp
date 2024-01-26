package com.ahmad.helpmeapp.UserSection;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.ahmad.helpmeapp.customData.CustomUserData;
import com.ahmad.helpmeapp.databinding.FragmentUserProfileBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class UserProfile extends Fragment {
    FragmentUserProfileBinding UserProfileBinding;
    FirebaseFirestore Db;
    FirebaseAuth auth;
    CustomUserData UserData = new CustomUserData();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        UserProfileBinding = FragmentUserProfileBinding.inflate(getLayoutInflater());
       //initialize database
        auth = FirebaseAuth.getInstance();
        Db = FirebaseFirestore.getInstance();

        GetValue();
        UserProfileBinding.saveDataUser.setOnClickListener(this::saveValues);
        return UserProfileBinding.getRoot();
    }


    private void GetValue() {
        String Id = auth.getUid();
        Db.collection("users").document(Id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                UserData = documentSnapshot.toObject(CustomUserData.class);
                String Name = UserData.getName();
                String Email = UserData.getEmail();
                String Password = UserData.getPassword();
                String TypeCar = UserData.getTypeCar();
                String ModelCar = UserData.getCarModel();
                String ColorCar = UserData.getColorCar();
                String PhoneNumber = UserData.getPhone();
                UserProfileBinding.NameUser.setText(Name);
                UserProfileBinding.EmailUser.setText(Email);
                UserProfileBinding.PasswordUser.setText(Password);
                UserProfileBinding.typeCarUser.setText(TypeCar);
                UserProfileBinding.modelCarUser.setText(ModelCar);
                UserProfileBinding.ColorCarUser.setText(ColorCar);
                UserProfileBinding.PhoneUser.setText(PhoneNumber);
            }
        });


    }


    private void saveValues(View view) {
        if (!TextUtils.isEmpty(UserProfileBinding.NameUser.getText())
                && !TextUtils.isEmpty(UserProfileBinding.EmailUser.getText())
                && !TextUtils.isEmpty(UserProfileBinding.PasswordUser.getText())
                && !TextUtils.isEmpty(UserProfileBinding.PhoneUser.getText())
                && !TextUtils.isEmpty(UserProfileBinding.ColorCarUser.getText())
                && !TextUtils.isEmpty(UserProfileBinding.typeCarUser.getText())
                && !TextUtils.isEmpty(UserProfileBinding.modelCarUser.getText())) {
            UpdateUserData();
        } else {
            CheckFields();
        }
    }

    private void UpdateUserData() {
        String password = UserProfileBinding.PasswordUser.getText().toString().trim();
        String email = UserProfileBinding.EmailUser.getText().toString().trim();
        String name = UserProfileBinding.NameUser.getText().toString().trim();
        String typeCar = UserProfileBinding.typeCarUser.getText().toString().trim();
        String modelCar = UserProfileBinding.modelCarUser.getText().toString().trim();
        String colorCar = UserProfileBinding.ColorCarUser.getText().toString().trim();
        String phoneNumber = UserProfileBinding.PhoneUser.getText().toString().trim();
        auth.getCurrentUser().updateEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                auth.getCurrentUser().updatePassword(password).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        String userID = auth.getUid();
                        Map<String, Object> UpdateData = new HashMap<>();
                        UpdateData.put("name", name);
                        UpdateData.put("email", email);
                        UpdateData.put("password", password);
                        UpdateData.put("phone", phoneNumber);
                        UpdateData.put("typeCar", typeCar);
                        UpdateData.put("colorCar", colorCar);
                        UpdateData.put("carModel", modelCar);
                        UpdateData.put("userId", userID);

                        Db.collection("users").document(userID).update(UpdateData).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(getContext(), "updated data", Toast.LENGTH_SHORT).show();

                            }
                        });

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("email ", e.toString());
                        Toast.makeText(getContext(), "failed not updated", Toast.LENGTH_SHORT).show();

                    }
                });

            }
        });
    }

    private void CheckFields() {
        if (TextUtils.isEmpty(UserProfileBinding.NameUser.getText())) {
            UserProfileBinding.NameUser.setError("name required");
        }
        if (TextUtils.isEmpty(UserProfileBinding.EmailUser.getText())) {
            UserProfileBinding.EmailUser.setError("Email required");

        }
        if (TextUtils.isEmpty(UserProfileBinding.PasswordUser.getText())) {
            UserProfileBinding.PasswordUser.setError("password required");

        }
        if (TextUtils.isEmpty(UserProfileBinding.PhoneUser.getText())) {
            UserProfileBinding.PhoneUser.setError("phone required");

        }
        if (TextUtils.isEmpty(UserProfileBinding.modelCarUser.getText())) {
            UserProfileBinding.modelCarUser.setError("this field required");

        }
        if (TextUtils.isEmpty(UserProfileBinding.typeCarUser.getText())) {
            UserProfileBinding.typeCarUser.setError("this field required");

        }
        if (TextUtils.isEmpty(UserProfileBinding.ColorCarUser.getText())) {
            UserProfileBinding.ColorCarUser.setError("this field required");

        }
    }
}



