package com.example.jua_kaligo;

public class ModelShop {
    private String uid,email,name,shopName,Phone,deliveryFee,country,city,state,address,latitude,longitude,timestamp,accountType,online,shopOpen,profileImage;

    public ModelShop() {
    }
    public ModelShop(String uid , String email , String name , String shopName , String Phone , String deliveryFee , String country , String city , String state , String address , String latitude , String longitude , String timestamp , String accountType , String online , String shopOpen , String profileImage) {
        this.uid = uid;
//        this.email = email;
//        this.name = name;
        this.shopName = shopName;
        this.Phone = Phone;
        this.deliveryFee = deliveryFee;
        this.country = country;
        this.city = city;
        this.state = state;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.timestamp = timestamp;
        this.accountType = accountType;
        this.online = online;
        this.shopOpen = shopOpen;
        this.profileImage = profileImage;
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

    public void setDeliveryFee(String deliveryFee) {
        this.deliveryFee = deliveryFee;
    }

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

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

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
