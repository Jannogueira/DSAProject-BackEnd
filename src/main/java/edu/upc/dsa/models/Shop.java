package edu.upc.dsa.models;

import java.util.List;


public class Shop {

    List<Items> items;

    public Shop(List<Items> Items) {
        items = Items;
    }

    public void addShopItem(Items item){
        items.add(item);
    }

    public List<Items> getAllShopItems(){
        return items;
    }

    public void setItems(List<Items> items) {
        this.items = items;
    }

}
