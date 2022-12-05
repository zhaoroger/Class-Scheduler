package com.example.b07project;

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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity implements Contract.View{

    private LoginPresenter presenter;
    TextView registerTransition;
    Button loginButton;
    EditText Username, Password;
    ProgressDialog progress;
    Switch isAdmin;
    FirebaseAuth auth;
    FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        registerTransition = findViewById(R.id.registerTransition);
        loginButton = findViewById(R.id.loginButton);
        Username = findViewById(R.id.Username);
        Password = findViewById(R.id.Password);
        isAdmin = findViewById(R.id.userTypeSwitch);
        progress = new ProgressDialog(this);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        presenter = new LoginPresenter(this);

        registerTransition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegistrationActivity.class));
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
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
        Intent intent = new Intent(LoginActivity.this, StudentActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void sendToAdminAcct(){
        // replace HomeActivity with corresponding activity
        Intent intent = new Intent(LoginActivity.this, AdminMainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void displayMessage(String message){
        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
    }

}
