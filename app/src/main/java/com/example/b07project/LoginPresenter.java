package com.example.b07project;

import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class LoginPresenter implements Contract.Presenter {
    private LoginActivity view;
    private com.example.b07project.LoginModel model;

    public LoginPresenter(com.example.b07project.LoginModel model, LoginActivity view){
        this.model = model;
        this.view = view;
    }

    @Override
    public void Authenticate() {
        String validEmail = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        final String username[] = new String[2];
        username[0] = view.getUsername();
        username[1] = username[0].substring(0, username[0].indexOf("."));
        String password = view.getPassword();

        if(!username[0].matches(validEmail)){
            view.Username.setError("Please enter a valid e-mail address");
        } else if (password.isEmpty()) {
            view.Password.setError("Please enter a valid password");
        } else {
            view.progress.setMessage("Please wait...");
            view.progress.setTitle("Login");
            view.progress.setCanceledOnTouchOutside(false);
            view.progress.show();

            view.auth.signInWithEmailAndPassword(username[0], password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    view.auth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {

                        @Override
                        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                            System.out.println("UID: "+ username[1]);
                            System.out.println("username : "+ username[0]);
                            model.loginStudent(username[1], new LoginCallback() {
                                @Override
                                public void onCallback(boolean loggedIn) {
                                    if (task.isSuccessful() && loggedIn && !view.isAdmin.isChecked()) {
                                        view.progress.dismiss();
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
                            model.loginAdmin(username[1], new LoginCallback() {
                                @Override
                                public void onCallback(boolean loggedIn) {
                                    view.progress.dismiss();
                                    if (task.isSuccessful() && loggedIn && view.isAdmin.isChecked()) {
                                        view.sendToAdminAcct();
                                        view.displayMessage("Admin login successful");
                                    }
                                }
                            });
                        }
                    });

                }
            });
        }
    }

}
