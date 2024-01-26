package com.ahmad.helpmeapp.register;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.ahmad.helpmeapp.Splash;
import com.ahmad.helpmeapp.databinding.SignupHelperBinding;
import com.ahmad.helpmeapp.databinding.SignupUserBinding;


public class signup extends AppCompatActivity {
    SharedPreferences gettypeAcc;
    SignupHelperBinding signupHelperBinding;
    SignupUserBinding signupUserBinding;
    public static String typeAcc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //retrieve type acc from shared preference
        gettypeAcc = getSharedPreferences("typeAcc", MODE_PRIVATE);
        typeAcc = gettypeAcc.getString("typeAcc", null);
        assert typeAcc != null;//to assurance typeAcc not null
        if (typeAcc.equals("users")){
            signupUserBinding=SignupUserBinding.inflate(getLayoutInflater());
            setContentView(signupUserBinding.getRoot());
SignupUser user=new SignupUser(signupUserBinding,this);
user.initializeUi();
        }
        else  if (typeAcc.equals("helpers")){
            signupHelperBinding = SignupHelperBinding.inflate(getLayoutInflater());
            setContentView(signupHelperBinding.getRoot());
            SignupHelper helper=new SignupHelper(signupHelperBinding,this,getSupportFragmentManager());
            helper.initializeUi();
        }
        else {
            startActivity(new Intent(this, Splash.class));
        }


    }



}