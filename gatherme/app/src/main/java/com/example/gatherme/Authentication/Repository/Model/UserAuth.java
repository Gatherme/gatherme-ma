package com.example.gatherme.Authentication.Repository.Model;

public class UserAuth {
    private  String email;
    private  String password;
    public UserAuth(String email,String password){
        this.email = email;
        this.password = password;
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
