package com.example.loginandregister;

import java.util.List;

public class LoginModel {

    public boolean isStudent(String name){
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
