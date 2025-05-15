package edu.upc.dsa;

import edu.upc.dsa.models.*;
import edu.upc.dsa.util.DBUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class WebManagerImpl implements WebManager {

    private static WebManagerImpl instance;
    private List<Users> users;
    private Shop shop;
    private WebManagerImpl() {
        users = new ArrayList<>();
        List<ShopItem> items = new ArrayList<>();
        items.add(new ShopItem("bomba", "Objeto para explotar cualquier bola a tu alrededor!", "http://dsa2.upc.edu/imagenes/bomba.jpg", 300));
        items.add(new ShopItem("delete", "Elimina una simple bola o incluso una bola grande!", "http://dsa2.upc.edu/imagenes/delete.jpg", 500));
        items.add(new ShopItem("oro", "Multiplica x2 tu oro obtenida durante 30 minutos!", "http://dsa2.upc.edu/imagenes/oro.jpg", 1000));

        shop = new Shop(items);
    }

    public static WebManagerImpl getInstance() {
        if (instance == null) {
            instance = new WebManagerImpl();
        }
        return instance;
    }

    @Override
    public int registerUser(String username, String correo, String password) {
        if (existeEmail(correo)) return 3;
        if (existeUser(username)) return 2;
        System.out.println(username + " " + correo + " " + password);
        Session session = GameSession.openSession();
        Users users = new Users(username, correo, password);
        session.save(users);
        return 1;
    }

    @Override
    public Boolean existeUser(String user) {
        Session session = GameSession.openSession();
        Users u = session.getByField(Users.class, "usuario", user);
        return u != null;
    }

    @Override
    public Boolean existeEmail(String email) {
        Session session = GameSession.openSession();
        Users u = session.getByField(Users.class, "correo", email);
        return u != null;
    }

    @Override
    public Boolean loginUser(String correo, String password) {
        Session session = GameSession.openSession();
        Users user = session.getByField(Users.class, "correo", correo);
        return user != null && user.getPassword().equals(password);
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
    public List<Users> getAllUsers() {
        Session session = GameSession.openSession();
        List<Object> objs = session.findAll(Users.class);
        List<Users> usersList = new ArrayList<>();
        for (Object o : objs) {
            usersList.add((Users) o);
        }
        return usersList;
    }

    @Override
    public Users getUser(String username) {
        Session session = GameSession.openSession();
        return session.getByField(Users.class, "usuario", username);
    }

    @Override
    public boolean eliminarUsuario(String usuario) {
        Session session = GameSession.openSession();
        Users user = session.getByField(Users.class, "usuario", usuario);
        if (user == null) return false;
        session.delete(user);
        return true;
    }

    @Override
    public boolean actualizarUsuario(String usuario, String nuevoUsuario) {
        Session session = GameSession.openSession();
        Users user = getUser(usuario);
        if (user == null) {
            return false;
        }
        user.setUsuario(nuevoUsuario);
        session.update(user);
        return true;
    }

    @Override
    public boolean actualizarCorreo(String usuario, String nuevoCorreo) {
        Session session = GameSession.openSession();
        Users user = session.getByField(Users.class, "usuario", usuario);
        if (user == null) return false;
        user.setCorreo(nuevoCorreo);
        session.update(user);
        return true;
    }

    @Override
    public boolean actualizarContrasena(String usuario, String nuevaContrasena) {
        Session session = GameSession.openSession();
        Users user = session.getByField(Users.class, "usuario", usuario);
        if (user == null) return false;
        user.setPassword(nuevaContrasena);
        session.update(user);
        return true;
    }

    @Override
    public String getCorreo(String usuario) {
        Session session = GameSession.openSession();
        Users user = session.getByField(Users.class, "usuario", usuario);
        return (user != null) ? user.getCorreo() : null;
    }

    @Override
    public String getUsername(String correo) {
        Session session = GameSession.openSession();
        Users user = session.getByField(Users.class, "correo", correo);
        return (user != null) ? user.getUsuario() : null;
    }
}
