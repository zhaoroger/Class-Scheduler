package com.example.b07project;

import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class RegisterPresenter implements Contract.Presenter {
    RegistrationActivity view;

    public RegisterPresenter(RegistrationActivity view) {
        this.view = view;
    }

    @Override
    public void Authenticate(){
        final String username[] = new String[2];
        username[0] = view.getUsername();
        username[1] = username[0].substring(0, username[0].indexOf("."));
        String password = view.getPassword();
        String cPassword = view.ConfirmPassword.getText().toString();
        String Name = view.name.getText().toString();
        String validEmail = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        List<String> courses = new ArrayList<String>();

        if (!username[0].matches(validEmail)){
            view.Username.setError("Please enter a valid e-mail address");
        } else if (password.isEmpty() || username[0].isEmpty() || cPassword.isEmpty() || Name.isEmpty()){
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

            view.auth.createUserWithEmailAndPassword(username[0], password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        view.progress.dismiss();
                        view.auth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
                            @Override
                            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                                if (view.isAdmin.isChecked()) {
                                    RealtimeDatabase.addAdmin(new AdminAccount(username[1], password, Name));
                                    view.sendToAdminAcct();
                                } else {
                                    RealtimeDatabase.addStudent(new StudentAccount(username[1], password, Name, courses));
                                    System.out.println("Started to switch to the student activity");
                                    // ~~~~~~~~~~~~ Switching to StudentActivity ~~~~~~~~~~~~~~~~
                                    // ~~~~~~~~~~~~~~ begin ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                                    RealtimeDatabase.getAllCourses(new AllCoursesCallback() {
                                        @Override
                                        public void onCallback(List<Course> courseList) {
                                            System.out.println("initializing the student all courses list");
                                            if (!StudentActivity.isInitiated) {
                                                StudentModuleCommunicator.getInstance().setSortedAllCoursesArray(new ArrayList<>(courseList));
                                                if (StudentModuleCommunicator.isStudentModuleCommunicatorReady) {
                                                    view.sendToStudentAcct();
                                                    view.displayMessage("Student login successful");
                                                }
                                            }
                                        }
                                    });
                                    // username of the authenticated student should be passed to
                                    // this call: here, I assumed uid[0] contains the username
                                    RealtimeDatabase.getStudentAccount(username[1], new GetStudentAccountCallback() {
                                        @Override
                                        public void onCallback(StudentAccount studentAccount) {
                                            System.out.println("initializing the student profile courses");
                                            if (!StudentActivity.isInitiated) {
                                                StudentModuleCommunicator.getInstance().setStudentAccount(studentAccount);
                                                if (StudentModuleCommunicator.isStudentModuleCommunicatorReady) {
                                                    view.sendToStudentAcct();
                                                    view.displayMessage("Student login successful");
                                                }
                                            }
                                        }
                                    });
                                    System.out.println("Done with to the student activity");
                                    //~~~~~~~~~~~~ Switching to StudentActivity ~~~~~~~~~~~~~~~~~
                                    // ~~~~~~~~~~~~~~ ends ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
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
