package com.example.studentmodule;

abstract class Account {
    String username;
    String password;
    String name;

    public Account(String username, String password, String name) {
        this.username = username;
        this.password = password;
        this.name = name;
    }

    protected String getUsername() {
        return username;
    }

    protected void setUsername(String username) {
        this.username = username;
    }

    protected String getPassword() {
        return password;
    }

    protected void setPassword(String password) {
        this.password = password;
    }

    protected String getName() {
        return name;
    }

    protected void setName(String name) {
        this.name = name;
    }
}
