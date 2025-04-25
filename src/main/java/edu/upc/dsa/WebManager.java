package edu.upc.dsa;

import edu.upc.dsa.models.User;
import edu.upc.dsa.models.ShopItem;

import java.util.List;

public interface WebManager {

    public int RegisterUser(String username, String correo, String password);

    public Boolean existeUser(String user);
    public Boolean existeEmail(String email);
    public String usuarioPorCorreo(String email);

    public Boolean LoginUser(String correo, String password);

    public List<ShopItem> getAllShopItems();

    public void addShopItem(ShopItem item);

    public List<User> getAllUsers();
    public User getUser(String usuario);
    public boolean eliminarUsuario(String usuario);
    public boolean actualizarUsuario(String usuario, String nuevoUsuario);
    public boolean actualizarCorreo(String usuario, String nuevoCorreo);
    public boolean actualizarContrasena(String usuario, String nuevaContrasena);
    public String getCorreo(String usuario);
}
