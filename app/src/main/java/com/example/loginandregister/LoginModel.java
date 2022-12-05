package com.example.loginandregister;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class LoginModel {

    public LoginModel() {
    }

    public static boolean isStudent(String name) {
        final boolean[] isStudent = new boolean[1];
        RealtimeDatabase.getStudentAccount(name, new GetStudentAccountCallback() {
            @Override
            public void onCallback(StudentAccount studentAccount) {
                RealtimeDatabase.loginStudent(studentAccount, new LoginCallback() {
                    @Override
                    public void onCallback(boolean loggedIn) {
                        isStudent[0] = loggedIn;
                    }
                });
            }
        });
        return isStudent[0];
    }
}
