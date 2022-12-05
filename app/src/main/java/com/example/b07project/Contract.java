package com.example.loginandregister;

public interface Contract {

    public interface Presenter {
        public void Authenticate();
    }

    public interface View {
        public String getUsername();
        public String getPassword();
        public void sendToStudentAcct();
        public void sendToAdminAcct();
        public void displayMessage(String message);
    }
}
