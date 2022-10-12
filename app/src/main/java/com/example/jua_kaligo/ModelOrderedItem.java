package com.example.jua_kaligo;

public class ModelOrderedItem {

    private String pId, name, cost, price, quantity;

    public ModelOrderedItem() {
    }

    public ModelOrderedItem(String pId, String name, String cost, String price, String quantity) {
        this.pId = pId;
        this.name = name;
        this.cost = cost;
        this.price = price;
        this.quantity = quantity;
    }

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}
