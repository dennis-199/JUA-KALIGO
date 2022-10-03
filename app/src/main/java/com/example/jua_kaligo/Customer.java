package com.example.jua_kaligo;

public class Customer {
    public String fullNames, phoneNum, idNum, Location,country,state,city,address, accountType, online,Phone,uid;
    public Customer(){

    }
    public Customer(String fullNames, String phoneNum, String idNum, String Location,String country,
                    String state,String city,String address, String accountType, String online,String Phone, String uid){
        this.fullNames = fullNames;
        this.phoneNum = phoneNum;
        this.idNum = idNum;
        this.Location = Location;
        this.country = country;
        this.state = state;
        this.city = city;
        this.address = address;
        this.accountType = accountType;
        this.online = online;
        this.Phone = Phone;
        this.uid = uid;

    }
}
