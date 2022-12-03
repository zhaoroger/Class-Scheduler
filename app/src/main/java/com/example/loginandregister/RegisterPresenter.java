package com.example.loginandregister;

import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

public class RegisterPresenter implements Contract.Presenter {
    RegisterModel model;
    RegistrationActivity view;

    public RegisterPresenter(RegisterModel model, RegistrationActivity view) {
        this.model = model;
        this.view = view;
    }

    @Override
    public void Authenticate(){
        String username = view.getUsername();
        String password = view.getPassword();
        String cPassword = view.ConfirmPassword.getText().toString();
        String Name = view.name.getText().toString();
        String validEmail = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (!username.matches(validEmail)){
            view.Username.setError("Please enter a valid e-mail address");
        } else if (password.isEmpty()){
            view.Password.setError("Please enter valid password.");
        } else if (!password.equals(cPassword)) {
            view.ConfirmPassword.setError("Passwords do not match.");
        } else if (model.isRegistered(username)) {
            view.Username.setError("This e-mail is already registered under another account");
        } else {
            view.progress.setMessage("Please wait...");
            view.progress.setTitle("Registration");
            view.progress.setCanceledOnTouchOutside(false);
            view.progress.show();

            view.auth.createUserWithEmailAndPassword(username, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        view.progress.dismiss();
                        view.displayMessage("Registration successful");
                        if (view.isAdmin.isChecked()) {
                            RealtimeDatabase.addAdmin(new AdminAccount(username, password, Name));
                            view.sendToAdminAcct();
                        } else {
                            RealtimeDatabase.addStudent(new StudentAccount(username, password, Name));
                            view.sendToStudentAcct();
                        }
                    } else {
                        view.progress.dismiss();
                        view.displayMessage("Registration unsuccessful");
                    }
                }
            });
        }
    }
}
