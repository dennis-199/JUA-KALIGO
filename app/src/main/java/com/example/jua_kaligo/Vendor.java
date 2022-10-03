package com.example.jua_kaligo;

public class Vendor {
    public String fullN, phoneN, IDNumber,Location,country,state,city,address,accountType,online,Phone,uid,profileImage;
    public Vendor(){

    }
    public Vendor(String fullN, String phoneno, String idnumber,  String Location,String country,
                  String state,String city,String address,String accountType,String online,String Phone, String uid, String profileImage){
        this.fullN = fullN;
        this.phoneN = phoneno;
        this.IDNumber = idnumber;
        this.Location = Location;
        this.country = country;
        this.state = state;
        this.city = city;
        this.address = address;
        this.accountType = accountType;
        this.online = online;
        this.Phone = Phone;
        this.uid = uid;
        this.profileImage = profileImage;
    }
}
