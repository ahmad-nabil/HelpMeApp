package com.ahmad.helpmeapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.ahmad.helpmeapp.HelperSection.MainMenuHelper;
import com.ahmad.helpmeapp.UserSection.MainMenuUser;
import com.ahmad.helpmeapp.databinding.ActivityLoginBinding;
import com.ahmad.helpmeapp.databinding.ActivityTypeAccBinding;
import com.ahmad.helpmeapp.register.signup;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class login extends AppCompatActivity implements View.OnClickListener {
    private String typeAcc = null;
    ActivityTypeAccBinding typeAccBinding;
    ActivityLoginBinding loginBinding;
    SharedPreferences TypeAccValue;
    FirebaseAuth auth;
    FirebaseFirestore Db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //retrieve type Acc to determine any screen we will go
        TypeAccValue = getSharedPreferences("typeAcc", MODE_PRIVATE);
        typeAcc = TypeAccValue.getString("typeAcc", null);
        typeAccBinding = ActivityTypeAccBinding.inflate(getLayoutInflater());
        loginBinding = ActivityLoginBinding.inflate(getLayoutInflater());
        //check type acc to determine what layout show
        if (typeAcc != null) {
            setContentView(loginBinding.getRoot());
            loginBinding.signupBtn.setOnClickListener(this);
            loginBinding.loginBtn.setOnClickListener(this);
            loginBinding.ForgetpasssBtn.setOnClickListener(this);
            auth = FirebaseAuth.getInstance();
            Db = FirebaseFirestore.getInstance();
        } else {
            setContentView(typeAccBinding.getRoot());
            String text = "<font color=#8A0404>Your </font> <font color=#000000>car </font><font color=#8A0404>in the right  </font> <font color=#000000>hands </font>";
            typeAccBinding.textView3.setText(Html.fromHtml(text));
            typeAccBinding.userBtn.setOnClickListener(this);
            typeAccBinding.helperBtn.setOnClickListener(this);

        }


    }

    @Override
    public void onClick(View v) {

        SharedPreferences.Editor editor = TypeAccValue.edit();
        //store value type Acc in shared preference to be permanent

        if (v.getId() == typeAccBinding.helperBtn.getId()) {
            typeAcc = "helpers";
            editor.putString("typeAcc", typeAcc).commit();
            recreate();
        } else if (v.getId() == typeAccBinding.userBtn.getId()) {
            typeAcc = "users";
            editor.putString("typeAcc", typeAcc).commit();
            recreate();

        } else if (v.getId() == loginBinding.loginBtn.getId()) {
            String Email = loginBinding.EmailLogin.getText().toString();
            String pass = loginBinding.PassLogin.getText().toString();
            //check if email user in database this type of user
            CheckTypeAcc checkTypeAcc = new CheckTypeAcc(typeAcc, Db);
            auth.signInWithEmailAndPassword(Email, pass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    checkTypeAcc.checklogin(Email, new CheckTypeAcc.OnLoginCheckListener() {
                        @Override
                        public void onLoginCheck(boolean accountFound) {
                            //if account found will join to type Acc
                            if (accountFound) {

                                if (typeAcc.equals("helpers")) {
                                    startActivity(new Intent(getApplicationContext(), MainMenuHelper.class));
                                } else if ("users".equals(typeAcc)) {
                                    startActivity(new Intent(login.this, MainMenuUser.class));
                                }
                            }
                        }
                    });
                }

            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), "your password or email invalid", Toast.LENGTH_SHORT).show();
                }
            });


        }
        else if (v.getId() == loginBinding.signupBtn.getId()) {
            startActivity(new Intent(this, signup.class));
        }
        else if (v.getId() == loginBinding.ForgetpasssBtn.getId()) {
            //AShow dialog to  enter Email  and if found in this type Acc we will send link till user rest password
            EditText editText = new EditText(this);
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(login.this);
            alertDialog.setMessage("Enter your email");
            alertDialog.setTitle("rest password");
            alertDialog.setView(editText);
            alertDialog.setPositiveButton("rest", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String email = editText.getText().toString();
                    CheckTypeAcc checkTypeAcc = new CheckTypeAcc(typeAcc, Db);
                    checkTypeAcc.checklogin(email, accountFound -> {
                        if (accountFound) {

                            Toast.makeText(getApplicationContext(), "link sent to your email please check your email", Toast.LENGTH_SHORT).show();
                        }
                    });

                }

            });

            alertDialog.show();
        }

    }


}