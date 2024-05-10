package com.example.szallas;

public class User {
    private String nev;
    private String email;
    private String phone_num;
    private String lakcim;

    public User(){}

    public User(String nev, String email, String phone_num, String lakcim) {
        this.nev = nev;
        this.email = email;
        this.phone_num = phone_num;
        this.lakcim = lakcim;
    }

    public String getNev() {
        return nev;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone_num() {
        return phone_num;
    }

    public String getLakcim() {
        return lakcim;
    }

    public void setNev(String nev) {
        this.nev = nev;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone_num(String phone_num) {
        this.phone_num = phone_num;
    }

    public void setLakcim(String lakcim) {
        this.lakcim = lakcim;
    }
}
