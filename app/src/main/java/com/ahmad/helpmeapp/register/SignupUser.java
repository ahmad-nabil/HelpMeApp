package com.ahmad.helpmeapp.register;

import static com.ahmad.helpmeapp.register.signup.typeAcc;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.ahmad.helpmeapp.UserSection.MainMenuUser;
import com.ahmad.helpmeapp.customData.CustomUserData;
import com.ahmad.helpmeapp.databinding.SignupUserBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;


public class SignupUser {
    SignupUserBinding signupUserBinding;
    Activity Signup;
    FirebaseAuth auth;
    FirebaseFirestore Db;

    public SignupUser(SignupUserBinding signupUserBinding, Activity signup) {
        this.signupUserBinding = signupUserBinding;
        this.Signup = signup;
    }

    public void initializeUi() {
        auth = FirebaseAuth.getInstance();
        Db = FirebaseFirestore.getInstance();
        signupUserBinding.typeAccTitle.setText(typeAcc);
        signupUserBinding.registerBtn.setOnClickListener(this::register);
    }


    //check fields if everything Ok  will register else  will check all fields
    public void register(View RegisterBtn) {
        if (!TextUtils.isEmpty(signupUserBinding.FullNameETSignup.getText())
                && !TextUtils.isEmpty(signupUserBinding.EmailETSignup.getText())
                && !TextUtils.isEmpty(signupUserBinding.PasswordETSignup.getText())
                && !TextUtils.isEmpty(signupUserBinding.phoneEtSignup.getText())
                && !TextUtils.isEmpty(signupUserBinding.ColorCar.getText())
                && !TextUtils.isEmpty(signupUserBinding.TypeCar.getText())
                && !TextUtils.isEmpty(signupUserBinding.carmodel.getText())) {
            RegisterUser();
        } else {
            CheckFields();
        }

    }

    private void RegisterUser() {
        String name = signupUserBinding.FullNameETSignup.getText().toString().trim();
        String email = signupUserBinding.EmailETSignup.getText().toString().trim();
        String password = signupUserBinding.PasswordETSignup.getText().toString().trim();
        String phone = signupUserBinding.phoneEtSignup.getText().toString().trim();
        String colorCar = signupUserBinding.ColorCar.getText().toString().trim();
        String typeCar = signupUserBinding.TypeCar.getText().toString().trim();
        String carModel = signupUserBinding.carmodel.getText().toString().trim();
        auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                String userid = auth.getUid();
                CustomUserData userData = new CustomUserData(name, email, password, phone, typeCar, colorCar, carModel, userid);
                Db.collection("users").document(userid).set(userData)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Signup.startActivity(new Intent(Signup, MainMenuUser.class));
                            }
                        });

            }
        });

    }


    private void CheckFields() {
        if (TextUtils.isEmpty(signupUserBinding.FullNameETSignup.getText())) {
            signupUserBinding.FullNameETSignup.setError("name required");
        }
        if (TextUtils.isEmpty(signupUserBinding.EmailETSignup.getText())) {
            signupUserBinding.EmailETSignup.setError("Email required");

        }
        if (TextUtils.isEmpty(signupUserBinding.PasswordETSignup.getText())) {
            signupUserBinding.PasswordETSignup.setError("password required");

        }
        if (TextUtils.isEmpty(signupUserBinding.phoneEtSignup.getText())) {
            signupUserBinding.phoneEtSignup.setError("phone required");

        }
        if (TextUtils.isEmpty(signupUserBinding.carmodel.getText())) {
            signupUserBinding.carmodel.setError("this field required");

        }
        if (TextUtils.isEmpty(signupUserBinding.TypeCar.getText())) {
            signupUserBinding.TypeCar.setError("this field required");

        }
        if (TextUtils.isEmpty(signupUserBinding.ColorCar.getText())) {
            signupUserBinding.ColorCar.setError("this field required");

        }

    }
}
