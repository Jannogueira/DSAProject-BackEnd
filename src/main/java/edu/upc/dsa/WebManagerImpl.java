package edu.upc.dsa;

import edu.upc.dsa.models.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WebManagerImpl implements WebManager {

    private static WebManagerImpl instance;
    private List<Users> users;
    private Shop shop;
    private WebManagerImpl() {
        users = new ArrayList<>();
        List<Items> items = new ArrayList<>();
        items.add(new Items(1,"bomba", "Objeto para explotar cualquier bola a tu alrededor!", "./imagenes/bomba.jpg", 300));
        items.add(new Items(2,"delete", "Elimina una simple bola o incluso una bola grande!", "./imagenes/delete.jpg", 500));
        items.add(new Items(3, "oro", "Multiplica x2 tu oro obtenida durante 30 minutos!", "./imagenes/oro.jpg", 1000));
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
        session.close();
        return 1;
    }

    @Override
    public Boolean existeUser(String user) {
        Session session = GameSession.openSession();
        Users u = session.getByField(Users.class, "usuario", user);
        session.close();
        return u != null;
    }

    @Override
    public Boolean existeEmail(String email) {
        Session session = GameSession.openSession();
        Users u = session.getByField(Users.class, "correo", email);
        session.close();
        return u != null;
    }

    @Override
    public Boolean loginUser(String correo, String password) {
        Session session = GameSession.openSession();
        Users user = session.getByField(Users.class, "correo", correo);
        session.close();
        return user != null && user.getPassword().equals(password);
    }

    @Override
    public List<Items> getAllShopItems() {
        return this.shop.getAllShopItems();
    }

    @Override
    public void addShopItem(Items item) {
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
        session.close();
        return usersList;
    }

    @Override
    public Users getUser(String username) {
        Session session = GameSession.openSession();
        Users user = session.getByField(Users.class, "usuario", username);
        session.close();
        return user;
    }

    @Override
    public boolean eliminarUsuario(String usuario) {
        Session session = GameSession.openSession();
        Users user = session.getByField(Users.class, "usuario", usuario);
        if (user == null){
            session.close();
            return false;
        }
        session.delete(user);
        session.close();
        return true;
    }

    @Override
    public boolean actualizarUsuario(String usuario, String nuevoUsuario) {
        Session session = GameSession.openSession();
        Users user = session.getByField(Users.class, "usuario", usuario);
        if (user == null) {
            session.close();
            return false;
        }
        user.setUsuario(nuevoUsuario);
        session.update(user);
        session.close();
        return true;
    }

    @Override
    public boolean actualizarCorreo(String usuario, String nuevoCorreo) {
        Session session = GameSession.openSession();
        Users user = session.getByField(Users.class, "usuario", usuario);
        if (user == null){
            session.close();
            return false;
        }
        user.setCorreo(nuevoCorreo);
        session.update(user);
        session.close();
        return true;
    }

    @Override
    public boolean actualizarContrasena(String usuario, String nuevaContrasena) {
        Session session = GameSession.openSession();
        Users user = session.getByField(Users.class, "usuario", usuario);
        if (user == null){
            session.close();
            return false;
        }
        user.setPassword(nuevaContrasena);
        session.update(user);
        session.close();
        return true;
    }

    @Override
    public String getCorreo(String usuario) {
        Session session = GameSession.openSession();
        Users user = session.getByField(Users.class, "usuario", usuario);
        session.close();
        return (user != null) ? user.getCorreo() : null;
    }

    @Override
    public String getUsername(String correo) {
        Session session = GameSession.openSession();
        Users user = session.getByField(Users.class, "correo", correo);
        session.close();
        return (user != null) ? user.getUsuario() : null;
    }

    @Override
    public int comprarItems(String usuario, Map<Integer, Integer> itemsACobrar) {
        Session session = GameSession.openSession();
        try {
            // Paso 1: Obtener el usuario por su nombre
            Users user = session.getByField(Users.class, "usuario", usuario);
            if (user == null) {
                session.close();
                return -1; // Usuario no encontrado
            }
            int dineroUsuario = user.getMoney();
            int costeTotal = 0;

            // Paso 2: Calcular el coste total
            for (Map.Entry<Integer, Integer> entry : itemsACobrar.entrySet()) {
                int itemId = entry.getKey();
                int cantidad = entry.getValue();

                Items item = session.getByField(Items.class, "id", itemId);
                if (item == null) {
                    session.close();
                    return -2; // Algún item no encontrado
                }
                costeTotal += item.getPrecio() * cantidad;
            }

            // Paso 3: Verificar si el usuario tiene suficiente dinero
            if (dineroUsuario < costeTotal) {
                session.close();
                return 0; // Dinero insuficiente
            }

            // Paso 4: Restar dinero y actualizar usuario
            user.setMoney(dineroUsuario - costeTotal);
            session.update(user);

            // Paso 5: Procesar inventario
            for (Map.Entry<Integer, Integer> entry : itemsACobrar.entrySet()) {
                int itemId = entry.getKey();
                int cantidad = entry.getValue();

                java.util.HashMap<String, Object> condiciones = new java.util.HashMap<>();
                condiciones.put("ID_user", user.getId());
                condiciones.put("ID_item", itemId);
                List<Inventario> resultado = (List<Inventario>) (List<?>) session.findAll(Inventario.class, condiciones);

                if (resultado.isEmpty()) {
                    // No existe en inventario: insertamos uno nuevo
                    Inventario nuevo = new Inventario(user.getId(), itemId, cantidad);
                    session.save(nuevo);
                } else {
                    // Ya existe: actualizamos cantidad usando updateWithCompositeKey
                    Inventario existente = resultado.get(0);
                    existente.setCantidad(existente.getCantidad() + cantidad);

                    String[] keys = {"ID_user", "ID_item"};
                    ((SessionImpl)session).updateWithCompositeKey(existente, keys);
                }
            }

            session.close();
            return 1; // Compra exitosa
        } catch (Exception e) {
            e.printStackTrace();
            session.close();
            return -99; // Error inesperado
        }
    }

    @Override
    public List<Items> getAllItems() {
        Session session = GameSession.openSession();
        List<Object> itemsBBDD = session.findAll(Items.class);
        session.close();
        List<Items> items = new ArrayList<>();
        for (Object o : itemsBBDD) {
            items.add((Items) o);
        }
        return items;
    }
    @Override
    public Integer getScore(String username) {
        Session session = GameSession.openSession();
        Users user = session.getByField(Users.class, "usuario", username);
        session.close();
        return user != null ? user.getScore() : null;
    }
    @Override
    public Integer getMoney(String username) {
        Session session = GameSession.openSession();
        Users user = session.getByField(Users.class, "usuario", username);
        session.close();
        return user != null ? user.getMoney() : null;
    }


    @Override
    public String getInventarioPorUsuario(String username) {
        Session session = GameSession.openSession();
        StringBuilder json = new StringBuilder("[");
        boolean firstItem = true;

        try {
            Users user = this.getUser(username);
            if (user == null) return "[]";

            HashMap<String, Object> params = new HashMap<>();
            params.put("ID_user", user.getId());

            List<Object> resultados = session.findAll(Inventario.class, params);
            for (Object obj : resultados) {
                Inventario inv = (Inventario) obj;
                Items item = session.getByField(Items.class, "id", inv.getID_item());

                if (item != null) {
                    if (!firstItem) {
                        json.append(",");
                    }
                    json.append(String.format(
                            "{\"id\":%d, \"nombre\":\"%s\", \"descripcion\":\"%s\", \"url_icon\":\"%s\", \"cantidad\":%d}",
                            item.getId(),
                            item.getNombre().replace("\"", "\\\""),
                            item.getDescripcion().replace("\"", "\\\""),
                            item.getUrl_icon().replace("\"", "\\\""),
                            inv.getCantidad()
                    ));
                    firstItem = false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "[]";
        } finally {
            session.close();
        }

        json.append("]");
        return json.toString();
    }

    @Override
    public List<UsersScoreDTO> getAllUsersScoresDTO() {
        Session session = GameSession.openSession();
        List<Object> objs = session.findAll(Users.class);
        List<UsersScoreDTO> result = new ArrayList<>();

        for (Object o : objs) {
            Users user = (Users) o;

            UsersScoreDTO dto = new UsersScoreDTO(user.getUsuario(), user.getScore());
            result.add(dto);
        }

        session.close();
        return result;
    }

}