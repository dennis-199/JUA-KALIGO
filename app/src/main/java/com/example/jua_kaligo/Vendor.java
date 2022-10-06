package com.example.jua_kaligo;

public class Vendor {
    public String shopName, KRApin, IDNumber,Gender,country,state,city,address,accountType,online,Phone,uid,profileImage,shopOpen;
    public Vendor(){

    }
    public Vendor(String shopName, String KRApin, String idnumber,  String Gender,String country,
                  String state,String city,String address,String accountType,String online,String Phone, String uid, String profileImage,
                  String shopOpen){
        this.shopName = shopName;
        this.KRApin = KRApin;
        this.IDNumber = idnumber;
        this.Gender = Gender;
        this.country = country;
        this.state = state;
        this.city = city;
        this.address = address;
        this.accountType = accountType;
        this.online = online;
        this.Phone = Phone;
        this.uid = uid;
        this.profileImage = profileImage;
        this.shopOpen = shopOpen;
    }
}
