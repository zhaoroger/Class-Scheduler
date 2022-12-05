package com.example.loginandregister;

import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginPresenter implements Contract.Presenter {
    private LoginModel model;
    private LoginActivity view;

    public LoginPresenter(LoginModel model, LoginActivity view){
        this.model = model;
        this.view = view;
    }

    @Override
    public void Authenticate() {
        String validEmail = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        final String[] uid = new String[1];
        String username = view.getUsername();
        String password = view.getPassword();

        if(!username.matches(validEmail)){
            view.Username.setError("Please enter a valid e-mail address");
        } else if (password.isEmpty()) {
            view.Password.setError("Please enter a valid password");
        } else {
            view.progress.setMessage("Please wait...");
            view.progress.setTitle("Login");
            view.progress.setCanceledOnTouchOutside(false);
            view.progress.show();

            view.auth.signInWithEmailAndPassword(username, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    view.auth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
                        @Override
                        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                            uid[0] = view.auth.getCurrentUser().getUid();
                            if (task.isSuccessful()) {
                                view.progress.dismiss();
                                if (view.isAdmin.isChecked()) {
                                    view.sendToAdminAcct();
                                    view.displayMessage("Admin login successful");
                                } else {
                                    RealtimeDatabase.getStudentAccount(uid[0], new GetStudentAccountCallback() {
                                        @Override
                                        public void onCallback(StudentAccount studentAccount) {
                                            //StudentModuleCommunicator.setStudentAccount(studentAccount);
                                            view.sendToStudentAcct();
                                            view.displayMessage("Student login successful");
                                        }
                                    });
                                }
                            } else {
                                view.progress.dismiss();
                                view.displayMessage("Login unsuccessful");
                            }
                        }
                    });

                }
            });
        }
    }

}
