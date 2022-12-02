package com.example.loginandregister;

import java.util.List;

public class LoginModel {
    List<String> studentNames;
    List<String> adminNames;

    public LoginModel(){
        List<StudentAccount> students = RealtimeDatabase.getAllStudents();
        List<AdminAccount> admins = RealtimeDatabase.getAllAdmins();
        for(StudentAccount student : students) {
            studentNames.add(student.username);
        }
        for(AdminAccount admin : admins) {
            adminNames.add(admin.username);
        }
    }

    public boolean isStudent(String name){
        if(studentNames.contains(name)) {
            return true;
        }
        return false;
    }

    public boolean isAdmin(String name){
        if(adminNames.contains(name)) {
            return true;
        }
        return false;
    }
}
