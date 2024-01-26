package com.ahmad.helpmeapp.register;

import static com.ahmad.helpmeapp.register.signup.typeAcc;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.fragment.app.FragmentManager;

import com.ahmad.helpmeapp.HelperSection.MainMenuHelper;
import com.ahmad.helpmeapp.customData.CustomHelperData;
import com.ahmad.helpmeapp.databinding.CustomSnackbarBinding;
import com.ahmad.helpmeapp.databinding.FragmentMaphelperBinding;
import com.ahmad.helpmeapp.databinding.SignupHelperBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;


public class SignupHelper implements PassLocationData {
    SignupHelperBinding signupHelperBinding;
    Activity signup;
    FirebaseAuth auth;
    FirebaseFirestore Db;
    double latitude = 0.0, longitude = 0.0;
    FragmentManager fragmentManager;
    FragmentMaphelperBinding maphelperBinding;
    MapHelper mapHelper;

    public SignupHelper(SignupHelperBinding signupHelperBinding, Activity activity, FragmentManager fragmentManager) {
        this.signupHelperBinding = signupHelperBinding;
        this.signup = activity;
        this.fragmentManager = fragmentManager;
        maphelperBinding = FragmentMaphelperBinding.inflate(activity.getLayoutInflater());
        mapHelper = new MapHelper();
        auth = FirebaseAuth.getInstance();
        Db = FirebaseFirestore.getInstance();
    }

    //initialize UI components
    public void initializeUi() {

        signupHelperBinding.typeAccTitle.setText(typeAcc);
        signupHelperBinding.getLocation.setOnClickListener(this::getLocation);
        signupHelperBinding.registerBtn.setOnClickListener(this::register);


    }

    //show dialog ask user if he want pick location manual or auto
    public void getLocation(View locationBtn) {
        Dialog dialog = new Dialog(signup);
        CustomSnackbarBinding snackbarBinding = CustomSnackbarBinding.inflate(signup.getLayoutInflater());
        dialog.setContentView(snackbarBinding.getRoot());

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        snackbarBinding.autoAction.setOnClickListener(v -> {
            AutoDetectLocation autoDetectLocation = new AutoDetectLocation(signup, this);
            autoDetectLocation.startLocationUpdates();

            dialog.dismiss();
        });
        snackbarBinding.manulAction.setOnClickListener(v -> {


            mapHelper.setDataPass(this);
            mapHelper.show(fragmentManager, "map helper");

            dialog.dismiss();
        });
    }


    //check fields if everything Ok  will register else  will check all fields
    public void register(View RegisterBtn) {
        if (!TextUtils.isEmpty(signupHelperBinding.FullNameETSignup.getText())
                && !TextUtils.isEmpty(signupHelperBinding.EmailETSignup.getText())
                && !TextUtils.isEmpty(signupHelperBinding.PasswordETSignup.getText())
                && !TextUtils.isEmpty(signupHelperBinding.phoneEtSignup.getText())
                && !TextUtils.isEmpty(signupHelperBinding.LevelExperience.getText())
                && !TextUtils.isEmpty(signupHelperBinding.getLocation.getText())
                && !TextUtils.isEmpty(signupHelperBinding.TypeExperience.getText())) {
            RegisterUser();
        } else {
            CheckFields();
        }

    }

    private void RegisterUser() {
        String fullname = signupHelperBinding.FullNameETSignup.getText().toString().trim();
        String email = signupHelperBinding.EmailETSignup.getText().toString().trim();
        String password = signupHelperBinding.PasswordETSignup.getText().toString().trim();
        String phone = signupHelperBinding.phoneEtSignup.getText().toString().trim();
        String levelExp = signupHelperBinding.LevelExperience.getText().toString().trim();
        String typeExp = signupHelperBinding.TypeExperience.getText().toString().trim();
        String location = signupHelperBinding.getLocation.getText().toString().trim();

        auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                String helperid = auth.getUid();
                CustomHelperData customHelperData = new CustomHelperData(fullname, email, password, phone, levelExp, typeExp, location, longitude, latitude, helperid);
                Db.collection("helpers").document(helperid).set(customHelperData)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                signup.startActivity(new Intent(signup, MainMenuHelper.class));
                            }
                        });
            }
        });

    }


    private void CheckFields() {
        if (TextUtils.isEmpty(signupHelperBinding.FullNameETSignup.getText())) {
            signupHelperBinding.FullNameETSignup.setError("name required");
        }
        if (TextUtils.isEmpty(signupHelperBinding.EmailETSignup.getText())) {
            signupHelperBinding.EmailETSignup.setError("Email required");

        }
        if (TextUtils.isEmpty(signupHelperBinding.PasswordETSignup.getText())) {
            signupHelperBinding.PasswordETSignup.setError("password required");

        }
        if (TextUtils.isEmpty(signupHelperBinding.phoneEtSignup.getText())) {
            signupHelperBinding.phoneEtSignup.setError("phone required");

        }
        if (TextUtils.isEmpty(signupHelperBinding.LevelExperience.getText())) {
            signupHelperBinding.LevelExperience.setError("this field required");

        }
        if (TextUtils.isEmpty(signupHelperBinding.TypeExperience.getText())) {
            signupHelperBinding.TypeExperience.setError("this field required");

        }
        if (TextUtils.isEmpty(signupHelperBinding.getLocation.getText())) {
            signupHelperBinding.getLocation.setError("this field required");
        }
    }


    @Override
    public void onDataPass(double longitude, double latitude, String data) {
        this.latitude = latitude;
        this.longitude = longitude;
        signupHelperBinding.getLocation.setText(data);
    }
}




