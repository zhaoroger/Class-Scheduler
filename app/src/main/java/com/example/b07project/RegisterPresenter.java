package com.example.b07project;

import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterPresenter implements Contract.Presenter {
    RegistrationActivity view;

    public RegisterPresenter(RegistrationActivity view) {
        this.view = view;
    }

    @Override
    public void Authenticate(){
        String username = view.getUsername();
        final String[] uid = new String[1];
        String password = view.getPassword();
        String cPassword = view.ConfirmPassword.getText().toString();
        String Name = view.name.getText().toString();
        String validEmail = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (!username.matches(validEmail)){
            view.Username.setError("Please enter a valid e-mail address");
        } else if (password.isEmpty() || username.isEmpty() || cPassword.isEmpty() || Name.isEmpty()){
            view.Password.setError("Please fill out all fields");
        } else if (!password.equals(cPassword)) {
            view.ConfirmPassword.setError("Passwords do not match.");
        } else if (password.length() < 6) {
            view.Password.setError("Password must be at least 6 characters long");
        } else {
            view.progress.setMessage("Please wait...");
            view.progress.setTitle("Registration");
            view.progress.setCanceledOnTouchOutside(false);
            view.progress.show();

            view.auth.createUserWithEmailAndPassword(username, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        view.progress.dismiss();
                        view.auth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
                            @Override
                            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                                uid[0] = view.user.getUid();
                                if (view.isAdmin.isChecked()) {
                                    RealtimeDatabase.addAdmin(new AdminAccount(uid[0], password, Name));
                                    view.sendToAdminAcct();
                                } else {
                                    RealtimeDatabase.addStudent(new StudentAccount(uid[0], password, Name));
                                    view.sendToStudentAcct();
                                }
                            }
                        });
                        view.displayMessage("Registration successful");
                    } else {
                        view.progress.dismiss();
                        view.displayMessage("Registration unsuccessful");
                    }
                }
            });
        }
    }
}
