package com.example.szallas;

public class SzallasItem {
    private String id;
    private String name;
    private String place;
    private String info;
    private String price;
    private float rating;
    private int imageRes;
    private int foglalasDb;

    public SzallasItem(){}

    public SzallasItem(String name, String place, String info, String price, float rating, int imageRes, int foglalasDb) {
        this.name = name;
        this.place = place;
        this.info = info;
        this.price = price;
        this.rating = rating;
        this.imageRes = imageRes;
        this.foglalasDb = foglalasDb;
    }

    public String getName() {
        return name;
    }

    public String getPlace() {
        return place;
    }

    public String getInfo() {
        return info;
    }

    public String getPrice() {
        return price;
    }

    public float getRating() {
        return rating;
    }

    public int getImageRes() {
        return imageRes;
    }

    public int getFoglalasDb() {
        return foglalasDb;
    }

    public void setFoglalasDb(int foglalasDb) {
        this.foglalasDb = foglalasDb;
    }

    public String _getId(){
        return id;
    }

    public void setId(String id){
        this.id=id;
    }



}
