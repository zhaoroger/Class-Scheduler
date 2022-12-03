package com.example.loginandregister;

import java.util.List;

public class RegisterModel {

    public boolean isRegistered(String name){
        final boolean[] registered = new boolean[2];
        RealtimeDatabase.getAdminAccount(name, new GetAdminAccountCallback() {
            @Override
            public void onCallback(AdminAccount adminAccount) {
                RealtimeDatabase.loginAdmin(adminAccount, new LoginCallback() {
                    @Override
                    public void onCallback(boolean loggedIn) {
                        registered[0] = loggedIn;
                    }
                });
            }
        });
        RealtimeDatabase.getStudentAccount(name, new GetStudentAccountCallback() {
            @Override
            public void onCallback(StudentAccount studentAccount) {
                RealtimeDatabase.loginStudent(studentAccount, new LoginCallback() {
                    @Override
                    public void onCallback(boolean loggedIn) {
                        registered[1] = loggedIn;
                    }
                });
            }
        });
        return (registered[0] || registered[1]);
    }
}
