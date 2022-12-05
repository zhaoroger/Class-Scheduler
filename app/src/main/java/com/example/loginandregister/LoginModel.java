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

    public static void loginUser(String name) {
        RealtimeDatabase.loginStudent(name, new LoginCallback() {
            @Override
            public void onCallback(boolean loggedIn) {
                // if loggedIn is true, do whatever task you need
                // e.g. start the student activity
            }
        });
        RealtimeDatabase.loginAdmin(name, new LoginCallback() {
            @Override
            public void onCallback(boolean loggedIn) {
                // if loggedIn is true, do whatever task you need
                // e.g. start the admin activity
            }
        });
    }

}
