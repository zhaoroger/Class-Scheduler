package com.example.b07project;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.b07project.LoginCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class LoginModel {
    private final static DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://cscb07-project-851d6-default-rtdb.firebaseio.com/").getReference();
    private static final String ADMINS = "admins";
    private static final String STUDENTS = "students";

    public LoginModel() {
    }

    public void loginStudent(String username, LoginCallback loginCallback) {
        databaseReference.child(STUDENTS).child(username).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e(null, "Error getting student data", task.getException());
                } else {
                    loginCallback.onCallback(task.getResult().exists());
                }
            }
        });
    }

    public void loginAdmin(String username, LoginCallback loginCallback) {
        databaseReference.child(ADMINS).child(username).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e(null, "Error getting admin data", task.getException());
                } else {
                    loginCallback.onCallback(task.getResult().exists());
                }
            }
        });
    }
}
