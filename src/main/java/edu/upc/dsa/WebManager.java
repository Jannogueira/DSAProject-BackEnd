package edu.upc.dsa;

import edu.upc.dsa.models.Users;
import edu.upc.dsa.models.Items;

import java.util.List;

public interface WebManager {

    public int registerUser(String username, String correo, String password);

    public Boolean existeUser(String user);
    public Boolean existeEmail(String email);

    public Boolean loginUser(String correo, String password);

    public List<Items> getAllShopItems();

    public void addShopItem(Items item);

    public List<Users> getAllUsers();
    public Users getUser(String usuario);
    public boolean eliminarUsuario(String usuario);
    public boolean actualizarUsuario(String usuario, String nuevoUsuario);
    public boolean actualizarCorreo(String usuario, String nuevoCorreo);
    public boolean actualizarContrasena(String usuario, String nuevaContrasena);
    public String getCorreo(String usuario);
    public String getUsername(String correo);
}
