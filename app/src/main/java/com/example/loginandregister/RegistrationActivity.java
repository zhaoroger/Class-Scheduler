package com.example.loginandregister;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
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

    private RegisterPresenter presenter;
    TextView loginTransition;
    EditText Username, Password, ConfirmPassword, name;
    Button registerButton;
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
        name = findViewById(R.id.name);
        ConfirmPassword = findViewById(R.id.ConfirmPassword);
        registerButton = findViewById(R.id.registerButton);
        isAdmin = findViewById(R.id.userTypeSwitch);
        progress = new ProgressDialog(this);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        presenter = new RegisterPresenter(new RegisterModel(), this);

        loginTransition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.Authenticate();
            }
        });
    }

    public String getUsername(){
        EditText Username = findViewById(R.id.Username);
        return Username.getText().toString();
    }

    public String getPassword(){
        EditText Password = findViewById(R.id.Password);
        return Password.getText().toString();
    }

    public void sendToStudentAcct(){
        // replace HomeActivity with corresponding activity
        Intent intent = new Intent(RegistrationActivity.this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void sendToAdminAcct(){
        // replace HomeActivity with corresponding activity
        Intent intent = new Intent(RegistrationActivity.this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void displayMessage(String message){
        Toast.makeText(RegistrationActivity.this, message, Toast.LENGTH_SHORT).show();
    }

}