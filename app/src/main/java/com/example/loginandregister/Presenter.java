package com.example.loginandregister;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

public class Presenter {
    private Model model;
    private LoginActivity view;

    public Presenter(Model model, LoginActivity view){
        this.model = model;
        this.view = view;
    }

    public void Login() {
        String validEmail = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        String username = view.getUsername();
        String password = view.getPassword();

        if(!username.matches(validEmail)){
            view.Username.setError("Please enter a valid e-mail address");
        } else if (password.isEmpty()) {
            view.Password.setError("Please enter valid password.");
        } else if (view.isAdmin.isChecked() && model.isAdmin(username) == false) {
            view.Username.setError("User not registered as admin");
        } else if (view.isAdmin.isChecked() == false && model.isStudent(username) == false) {
            view.Username.setError("User not registered as student");
        } else {
            view.progress.setMessage("Please wait...");
            view.progress.setTitle("Login");
            view.progress.setCanceledOnTouchOutside(false);
            view.progress.show();

            view.auth.signInWithEmailAndPassword(username, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        view.progress.dismiss();
                        if (view.isAdmin.isChecked()) {
                            view.sendToAdminAcct();
                        } else {
                            view.sendToStudentAcct();
                        }
                        view.displayMessage("Login successful");
                    } else {
                        view.progress.dismiss();
                        view.displayMessage("Login unsuccessful");
                    }
                }
            });
        }
    }

}
