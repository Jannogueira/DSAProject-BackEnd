package edu.upc.dsa;

import edu.upc.dsa.models.*;

import java.util.ArrayList;
import java.util.List;


public class WebManagerImpl implements WebManager {

    private static WebManagerImpl instance;
    List<User> users;
    Shop shop;

    private WebManagerImpl() {
        users = new ArrayList<User>();
        List<ShopItem> items = new ArrayList<>();

        shop = new Shop(items);

    }

    public static WebManagerImpl getInstance() {
        if(instance == null){
            instance = new WebManagerImpl();
        }
        return instance;
    }

    @Override
    public Boolean RegisterUser(String username, String correo, String password){
        // Verifica si el usuario ya existe por correo
        for (User u : users) {
            if (u.getCorreo().equals(correo)){
                System.out.println("EXISTE EL CORREO: " + correo);
                return false;
                // Ya registrado
            }

        }

        // Si no existe, lo añade
        User newUser = new User(username, correo, password);
        users.add(newUser);
        System.out.println(" REGISTER Correo: " + correo + " | Password: " + password + " | Usuario: " + username);
        return true;
    }
    @Override
    public Boolean LoginUser(String correo, String password) {
        for (User u : users) {
            if (u.getCorreo().equals(correo) && u.getPassword().equals(password)) {
                System.out.println("Correo: " + correo + " | Password: " + password);
                return true; // Coincide login
            }
        }
        return false; // No encontrado o contraseña incorrecta
    }

    @Override
    public List<ShopItem> getAllShopItems() {
        return this.shop.getAllShopItems();
    }

    @Override
    public void addShopItem(ShopItem item) {
        this.shop.addShopItem(item);
    }

    @Override
    public List<User> getAllUsers() {
        return this.users;
    }
}