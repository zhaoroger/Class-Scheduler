package com.example.loginandregister;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegistrationActivity extends AppCompatActivity {

    TextView loginTransition;
    EditText Username, Password, ConfirmPassword;
    Button registerButton;
    String validEmail = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    ProgressDialog progress;
    Switch isAdmin;


    FirebaseAuth auth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        loginTransition = findViewById(R.id.loginTransition);
        Username = findViewById(R.id.Username);
        Password = findViewById(R.id.Password);
        ConfirmPassword = findViewById(R.id.ConfirmPassword);
        registerButton = findViewById(R.id.registerButton);
        isAdmin = findViewById(R.id.userTypeSwitch);
        progress = new ProgressDialog(this);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        loginTransition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Authenticate();
            }
        });
    }

    private void Authenticate(){
        String username = Username.getText().toString();
        String password = Password.getText().toString();
        String cPassword = ConfirmPassword.getText().toString();

        if(!username.matches(validEmail)){
            Username.setError("Please enter a valid e-mail address");
        } else if(password.isEmpty()){
            Password.setError("Please enter valid password.");
        } else if(!password.equals(cPassword)) {
            ConfirmPassword.setError("Passwords do not match.");
        } else {
            progress.setMessage("Please wait...");
            progress.setTitle("Registration");
            progress.setCanceledOnTouchOutside(false);
            progress.show();

            auth.createUserWithEmailAndPassword(username, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        progress.dismiss();
                        Toast.makeText(RegistrationActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                        if (isAdmin.isChecked()) {
                            sendToAdminAcct();
                        } else {
                            sendToStudentAcct();
                        }
                    } else {
                        progress.dismiss();
                        Toast.makeText(RegistrationActivity.this, ""+task.getException(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void sendToStudentAcct(){
        // replace HomeActivity with corresponding activity
        Intent intent = new Intent(RegistrationActivity.this, StudentAccount.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void sendToAdminAcct(){
        // replace HomeActivity with corresponding activity
        Intent intent = new Intent(RegistrationActivity.this, AdminAccount.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}