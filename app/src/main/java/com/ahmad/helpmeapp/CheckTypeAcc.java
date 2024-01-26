package com.ahmad.helpmeapp;


import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class CheckTypeAcc {
    String typeAcc;
    FirebaseFirestore Db;

    public CheckTypeAcc(String typeAcc, FirebaseFirestore db) {
        this.typeAcc = typeAcc;
        Db = db;
    }

    public void checklogin(String email, OnLoginCheckListener loginCheckListener) {
        Db.collection(typeAcc).whereEqualTo("email", email).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        loginCheckListener.onLoginCheck(true);
                    }
                } else {
                    loginCheckListener.onLoginCheck(false);
                }
            }
        });

    }

    public interface OnLoginCheckListener {
        void onLoginCheck(boolean accountFound);
    }

}

