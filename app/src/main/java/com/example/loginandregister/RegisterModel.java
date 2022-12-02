package com.example.loginandregister;

import java.util.List;

public class RegisterModel {
    List<String> studentNames;
    List<String> adminNames;

    public RegisterModel(){
        List<StudentAccount> students = RealtimeDatabase.getAllStudents();
        List<AdminAccount> admins = RealtimeDatabase.getAllAdmins();
        for(StudentAccount student : students) {
            studentNames.add(student.username);
        }
        for(AdminAccount admin : admins) {
            adminNames.add(admin.username);
        }
    }

    public boolean isRegistered(String name){
        if(adminNames.contains(name) || studentNames.contains(name)) {
            return true;
        }
        return false;
    }
}
