package com.example.b07project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegistrationActivity extends AppCompatActivity implements Contract.View{

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
        loginTransition = (TextView) findViewById(R.id.loginTransition);
        Username = (EditText) findViewById(R.id.Username);
        Password = (EditText) findViewById(R.id.Password);
        name = (EditText) findViewById(R.id.name);
        ConfirmPassword = (EditText) findViewById(R.id.ConfirmPassword);
        registerButton = (Button) findViewById(R.id.registerButton);
        isAdmin = (Switch) findViewById(R.id.userTypeSwitch);
        progress = new ProgressDialog(this);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        presenter = new RegisterPresenter(this);

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

    @Override
    public String getUsername(){
        EditText Username = findViewById(R.id.Username);
        return Username.getText().toString();
    }

    @Override
    public String getPassword(){
        EditText Password = findViewById(R.id.Password);
        return Password.getText().toString();
    }

    @Override
    public void sendToStudentAcct(){
        // replace HomeActivity with corresponding activity
        Intent intent = new Intent(RegistrationActivity.this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void sendToAdminAcct(){
        // replace HomeActivity with corresponding activity
        Intent intent = new Intent(RegistrationActivity.this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void displayMessage(String message){
        Toast.makeText(RegistrationActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    public void userExists() {
        Toast.makeText(RegistrationActivity.this, "User with this email already exist.", Toast.LENGTH_SHORT).show();
    }


}
