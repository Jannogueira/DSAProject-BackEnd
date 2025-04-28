package edu.upc.dsa.models;

public class ShopItem {
    String name;
    String description;
    String url_icon;

    public ShopItem(String name, String description, String url_icon) {
        this.name = name;
        this.description = description;
        this.url_icon = url_icon;
    }
    public ShopItem(){}


    public void setName(String name) {this.name = name;}
    public String getName() {return name;}
    public void setDescription(String description) {this.description = description;}
    public String getDescription() {return description;}
    public void setUrl_icon(String url_icon) {this.url_icon = url_icon;}
    public String getUrl_icon() {return url_icon;}
}
