package com.example.studentmodule;

final class AdminAccount extends Account implements Admin {
    public AdminAccount(String username, String password, String name) {
        super(username, password, name);
    }
}
