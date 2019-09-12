package com.example.projectfuelfree.model;

public class Users {
    private String username;
    private String email;
    private String password;
    private String penghasilan;
    private String saldo;

    public Users(){

    }

    public Users(String username, String email, String saldo) {
        this.username = username;
        this.email = email;
        this.saldo = saldo;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getPenghasilan() {
        return penghasilan;
    }

    public void setPenghasilan(String penghasilan) {
        this.penghasilan = penghasilan;
    }

    public String getSaldo() {
        return saldo;
    }

    public void setSaldo(String saldo) {
        this.saldo = saldo;
    }
}
