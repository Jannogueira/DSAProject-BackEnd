package edu.upc.dsa.models;

public class Items {
    String name;
    String description;
    String url_icon;
    int price;

    public Items(String name, String description, String url_icon, int price) {
        this.name = name;
        this.description = description;
        this.url_icon = url_icon;
        this.price = price;
    }
    public Items(){}


    public void setName(String name) {this.name = name;}
    public String getName() {return name;}
    public void setDescription(String description) {this.description = description;}
    public String getDescription() {return description;}
    public void setUrl_icon(String url_icon) {this.url_icon = url_icon;}
    public String getUrl_icon() {return url_icon;}
    public void setPrice(int price) {this.price = price;}
    public int getPrice() {return price;}
}
