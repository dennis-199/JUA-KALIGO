package com.example.jua_kaligo;

public class Customer {
    public String uid, idnumberuser,name,phone,country,city,state,address,genderuser,latitude,longitude,timestamp,accountyType,online,profileImage;
    public Customer(){

    }

    public Customer(String uid, String idnumberuser, String name, String phone, String country, String city, String state, String address, String genderuser, String latitude, String longitude, String timestamp, String accountyType, String online, String profileImage) {
        this.uid = uid;
        this.idnumberuser = idnumberuser;
        this.name = name;
        this.phone = phone;
        this.country = country;
        this.city = city;
        this.state = state;
        this.address = address;
        this.genderuser = genderuser;
        this.latitude = latitude;
        this.longitude = longitude;
        this.timestamp = timestamp;
        this.accountyType = accountyType;
        this.online = online;
        this.profileImage = profileImage;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getIdnumberuser() {
        return idnumberuser;
    }

    public void setIdnumberuser(String idnumberuser) {
        this.idnumberuser = idnumberuser;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public String getGenderuser() {
        return genderuser;
    }

    public void setGenderuser(String genderuser) {
        this.genderuser = genderuser;
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

    public String getAccountyType() {
        return accountyType;
    }

    public void setAccountyType(String accountyType) {
        this.accountyType = accountyType;
    }

    public String getOnline() {
        return online;
    }

    public void setOnline(String online) {
        this.online = online;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }
}
