package com.example.szallas;

public class Foglalas {
    private String id;
    private String foglaloEmail;
    private String szallasName;
    private String szallasHely;
    private String szallasInfo;
    private String szallasPrice;
    private float szallasRating;
    private int imageRes;
    private String kezdoDatum;
    private String vegDatum;

    public Foglalas(){}

    public Foglalas(String foglaloEmail, String szallasName, String szallasHely, String szallasInfo, String szallasPrice, float szallasRating, int imageRes, String kezdoDatum, String vegDatum) {
        this.foglaloEmail = foglaloEmail;
        this.szallasName = szallasName;
        this.szallasHely = szallasHely;
        this.szallasInfo = szallasInfo;
        this.szallasPrice = szallasPrice;
        this.szallasRating = szallasRating;
        this.imageRes = imageRes;
        this.kezdoDatum = kezdoDatum;
        this.vegDatum = vegDatum;
    }

    public String getFoglaloEmail() {
        return foglaloEmail;
    }

    public String getSzallasName() {
        return szallasName;
    }

    public String getSzallasHely() {
        return szallasHely;
    }

    public String getKezdoDatum() {
        return kezdoDatum;
    }

    public String getVegDatum() {
        return vegDatum;
    }

    public String getSzallasInfo() {
        return szallasInfo;
    }

    public String getSzallasPrice() {
        return szallasPrice;
    }

    public float getSzallasRating() {
        return szallasRating;
    }

    public int getImageRes() {
        return imageRes;
    }

    public void setFoglaloEmail(String foglaloEmail) {
        this.foglaloEmail = foglaloEmail;
    }

    public void setSzallasName(String szallasName) {
        this.szallasName = szallasName;
    }

    public void setSzallasHely(String szallasHely) {
        this.szallasHely = szallasHely;
    }

    public void setKezdoDatum(String kezdoDatum) {
        this.kezdoDatum = kezdoDatum;
    }

    public void setVegDatum(String vegDatum) {
        this.vegDatum = vegDatum;
    }

    public void setSzallasInfo(String szallasInfo) {
        this.szallasInfo = szallasInfo;
    }

    public void setSzallasPrice(String szallasPrice) {
        this.szallasPrice = szallasPrice;
    }

    public void setSzallasRating(float szallasRating) {
        this.szallasRating = szallasRating;
    }

    public void setImageRes(int imageRes) {
        this.imageRes = imageRes;
    }
    public String _getId(){
        return id;
    }

    public void setId(String id){
        this.id=id;
    }
}
