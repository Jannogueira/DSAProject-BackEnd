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
    private List<User> users;
    private Shop shop;
    private WebManagerImpl() {
        users = new ArrayList<>();
        List<ShopItem> items = new ArrayList<>();
        items.add(new ShopItem("bomba", "Objeto para explotar cualquier bola a tu alrededor!", "https://img.freepik.com/vector-premium/balas-canon-bomba-negra-dibujos-animados-lindo_634248-10.jpg"));
        items.add(new ShopItem("delete", "Elimina una simple bola o incluso una bola grande!", "https://media.istockphoto.com/id/1282050925/es/vector/icono-de-destino-sobre-fondo-transparente.jpg?s=612x612&w=0&k=20&c=OLYEqH0fltogk4XcSOqB__Q9_YYM6kDOY1bQP0P2qI4="));
        items.add(new ShopItem("oro", "Multiplica x2 tu oro obtenida durante 30 minutos!", "https://img.freepik.com/vector-premium/bolsa-monedas-oro-moneda-oro-saco-antiguo-antiguo-ahorro-monedero-riqueza-oro-3d-realista_102902-1094.jpg"));
        shop = new Shop(items);
    }

    public static WebManagerImpl getInstance() {
        if (instance == null) {
            instance = new WebManagerImpl();
        }
        return instance;
    }

    @Override
    public int RegisterUser(String username, String correo, String password) {
        if (existeEmail(correo)) return 3;
        if (existeUser(username)) return 2;

        try (Connection conn = DBUtils.getConnection()) {
            String sql = "INSERT INTO users (usuario, correo, password) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, correo);
            stmt.setString(3, password);
            stmt.executeUpdate();
            return 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public Boolean existeUser(String user) {
        try (Connection conn = DBUtils.getConnection()) {
            String sql = "SELECT COUNT(*) FROM users WHERE usuario = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, user);
            ResultSet rs = stmt.executeQuery();
            rs.next();
            return rs.getInt(1) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Boolean existeEmail(String email) {
        try (Connection conn = DBUtils.getConnection()) {
            String sql = "SELECT COUNT(*) FROM users WHERE correo = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            rs.next();
            return rs.getInt(1) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Boolean LoginUser(String correo, String password) {
        try (Connection conn = DBUtils.getConnection()) {
            String sql = "SELECT * FROM users WHERE correo = ? AND password = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, correo);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
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
        List<User> userList = new ArrayList<>();
        try (Connection conn = DBUtils.getConnection()) {
            String sql = "SELECT * FROM users";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                userList.add(new User(
                        rs.getInt("id"),
                        rs.getString("usuario"),
                        rs.getString("correo"),
                        rs.getString("password"),
                        rs.getInt("score"),
                        rs.getInt("money")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userList;
    }

    @Override
    public User getUser(String usuario) {
        try (Connection conn = DBUtils.getConnection()) {
            String sql = "SELECT * FROM users WHERE usuario = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, usuario);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new User(
                        rs.getInt("id"),
                        rs.getString("usuario"),
                        rs.getString("correo"),
                        rs.getString("password"),
                        rs.getInt("score"),
                        rs.getInt("money")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean eliminarUsuario(String usuario) {
        try (Connection conn = DBUtils.getConnection()) {
            String sql = "DELETE FROM users WHERE usuario = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, usuario);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean actualizarUsuario(String usuario, String nuevoUsuario) {
        try (Connection conn = DBUtils.getConnection()) {
            String sql = "UPDATE users SET usuario = ? WHERE usuario = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, nuevoUsuario);
            stmt.setString(2, usuario);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean actualizarCorreo(String usuario, String nuevoCorreo) {
        try (Connection conn = DBUtils.getConnection()) {
            String sql = "UPDATE users SET correo = ? WHERE usuario = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, nuevoCorreo);
            stmt.setString(2, usuario);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean actualizarContrasena(String usuario, String nuevaContrasena) {
        try (Connection conn = DBUtils.getConnection()) {
            String sql = "UPDATE users SET password = ? WHERE usuario = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, nuevaContrasena);
            stmt.setString(2, usuario);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public String getCorreo(String usuario) {
        try (Connection conn = DBUtils.getConnection()) {
            String sql = "SELECT correo FROM users WHERE usuario = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, usuario);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("correo");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getUsername(String correo) {
        try (Connection conn = DBUtils.getConnection()) {
            String sql = "SELECT usuario FROM users WHERE correo = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, correo);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("usuario");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
