package edu.upc.dsa.models;

public class ShopItem {
    String name;
    String description;

    public ShopItem(String name, String description) {
        this.name = name;
        this.description = description;
    }
    public ShopItem(){}


    public void setName(String name) {this.name = name;}
    public String getName() {return name;}
    public void setDescription(String description) {this.description = description;}
    public String getDescription() {return description;}
}
