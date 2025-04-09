package edu.upc.dsa;

import edu.upc.dsa.models.Shop;
import edu.upc.dsa.models.ShopItem;

import java.util.List;

public interface WebManager {

    public Boolean RegisterUser(String username, String correo, String password);

    public Boolean LoginUser(String correo, String password);

    public List<ShopItem> getAllShopItems();

    public void addShopItem(ShopItem item);
}
