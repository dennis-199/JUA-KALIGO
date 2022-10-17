package com.example.jua_kaligo;

public class ModelPromotion {
    String id, timestamp, description, promoCode, promoPrice, minimumOrderPrice, expireDate;

    public ModelPromotion() {
    }

    public ModelPromotion(String id, String timestamp, String description, String promoCode, String promoPrice, String minimumOrderPrice, String expireDate) {
        this.id = id;
        this.timestamp = timestamp;
        this.description = description;
        this.promoCode = promoCode;
        this.promoPrice = promoPrice;
        this.minimumOrderPrice = minimumOrderPrice;
        this.expireDate = expireDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPromoCode() {
        return promoCode;
    }

    public void setPromoCode(String promoCode) {
        this.promoCode = promoCode;
    }

    public String getPromoPrice() {
        return promoPrice;
    }

    public void setPromoPrice(String promoPrice) {
        this.promoPrice = promoPrice;
    }

    public String getMinimumOrderPrice() {
        return minimumOrderPrice;
    }

    public void setMinimumOrderPrice(String minimumOrderPrice) {
        this.minimumOrderPrice = minimumOrderPrice;
    }

    public String getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(String expireDate) {
        this.expireDate = expireDate;
    }
}
