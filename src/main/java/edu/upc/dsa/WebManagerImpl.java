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
        items.add(new ShopItem("bomba", "Objeto para explotar cualquier bola a tu alrededor!", "https://drive.google.com/file/d/1E1K5wNHPy4_AUfZXNLffNEqD95LOcLbE/view?usp=drive_link"));
        items.add(new ShopItem("delete", "Elimina una simple bola o incluso una bola grande!", "https://drive.google.com/file/d/1nXQB1rSdX9NW7bXdYJ3dkqZJvrpSrDpo/view?usp=drive_link"));
        items.add(new ShopItem("oro", "Multiplica x2 tu oro obtenida durante 30 minutos!", "https://drive.google.com/file/d/1wtb2F-WLKAJ8Io0-ge5dDrJlYfXLyS9_/view?usp=drive_link"));
        shop = new Shop(items);

    }

    public static WebManagerImpl getInstance() {
        if(instance == null){
            instance = new WebManagerImpl();
        }
        return instance;
    }

    @Override
    public int RegisterUser(String username, String correo, String password){
        if(existeEmail(correo)) //verificamos si existe ya el correo
            return 3;
        if(existeUser(username)) //verificamos si existe ya el usuario
            return 2;

        User newUser = new User(username, correo, password);
        users.add(newUser);
        System.out.println(" REGISTER Correo: " + correo + " | Password: " + password + " | Usuario: " + username);
        return 1;
    }


    @Override
    public Boolean existeUser(String user){
        for (User u : users) {
            if (u.getUsario().equals(user))
                return true;
        }
        return false;
    }
    @Override
    public Boolean existeEmail(String email){
        for (User u : users) {
            if(u.getCorreo().equals(email))
                return true;
        }
        return false;
    }
    @Override
    public String usuarioPorCorreo(String email){
        for (User u : users) {
            if(u.getCorreo().equals(email))
                return u.getUsario();
        }
        return null;
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

    @Override
    public User getUser(String usuario) {
        if(existeUser(usuario)){
            for (User u : users) {
                if (u.getUsario().equals(usuario))
                    return u;
            }
        }
        return null;
    }

    @Override
    public boolean eliminarUsuario(String usuario){
        if(existeUser(usuario)){
            users.remove(getUser(usuario));
            return true;
        }
        return false;
    }
    @Override
    public boolean actualizarUsuario(String usuario, String nuevoUsuario){
        if(existeUser(usuario)){
            getUser(usuario).setUsuario(nuevoUsuario);
            return true;
        }
        return false;
    }
    @Override
    public boolean actualizarCorreo(String usuario, String nuevoCorreo){
        if(existeUser(usuario)){
            getUser(usuario).setUsuario(nuevoCorreo);
            return true;
        }
        return false;
    }
    @Override
    public boolean actualizarContrasena(String usuario, String nuevaContrasena){
        if(existeUser(usuario)){
            getUser(usuario).setPassword(nuevaContrasena);
            return true;
        }
        return false;
    }
    @Override
    public String getCorreo(String usuario){
        if(existeUser(usuario)){

            return getUser(usuario).getCorreo();
        }
        return null;
    }


}