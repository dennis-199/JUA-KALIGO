package com.example.jua_kaligo;

public class ModelShop {
    private String shopName, KRApin, IDNumber,Gender,country,state,city,address,accountType,online,Phone,uid,profileImage,shopOpen;

    public ModelShop() {
    }
    public ModelShop(String shopName, String KRApin, String idnumber,  String Gender,String country,
                     String state,String city,String address,String accountType,String online,String Phone, String uid, String profileImage,
                     String shopOpen) {
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
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

//    public String getEmail() {
//        return email;
//    }

//    public void setEmail(String email) {
//        this.email = email;
//    }

//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        this.Phone = phone;
    }

//    public String getDeliveryFee() {
//        return deliveryFee;
//    }

//    public void setDeliveryFee(String deliveryFee) {
//        this.deliveryFee = deliveryFee;
//    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

//    public String getLatitude() {
//        return latitude;
//    }
//
//    public void setLatitude(String latitude) {
//        this.latitude = latitude;
//    }
//
//    public String getLongitude() {
//        return longitude;
//    }
//
//    public void setLongitude(String longitude) {
//        this.longitude = longitude;
//    }
//
//    public String getTimestamp() {
//        return timestamp;
//    }
//
//    public void setTimestamp(String timestamp) {
//        this.timestamp = timestamp;
//    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getOnline() {
        return online;
    }

    public void setOnline(String online) {
        this.online = online;
    }

    public String getShopOpen() {
        return shopOpen;
    }

    public void setShopOpen(String shopOpen) {
        this.shopOpen = shopOpen;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }
}
