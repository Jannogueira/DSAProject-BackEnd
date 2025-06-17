package edu.upc.dsa;

import edu.upc.dsa.models.*;
import edu.upc.dsa.util.*;
import org.apache.log4j.Logger;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WebManagerImpl implements WebManager {

    private static WebManagerImpl instance;
    private List<Users> users;
    private Shop shop;
    final static Logger logger = Logger.getLogger(WebManagerImpl.class);

    private WebManagerImpl() {}

    public static WebManagerImpl getInstance() {
        if (instance == null) {
            instance = new WebManagerImpl();
        }
        return instance;
    }

    @Override
    public int registerUser(String username, String correo, String password) {
        logger.info("Nuevo registro de usuario: " + username);
        if (existeEmail(correo)) return 3;
        if (existeUser(username)) return 2;
        String hashedPassword = PasswordUtil.hashPassword(password);
        Session session = GameSession.openSession();
        Users users = new Users(username, correo, hashedPassword);
        session.save(users);
        logger.info("Registro completado para usuario: " + username);
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
        logger.info("Intento de login para correo: " + correo);
        Session session = GameSession.openSession();
        Users user = session.getByField(Users.class, "correo", correo);
        session.close();
        return user != null && PasswordUtil.verifyPassword(password, user.getPassword());
    }

    @Override
    public List<Items> getAllShopItems() {
        logger.debug("Recuperando todos los items de la tienda");
        return this.shop.getAllShopItems();
    }

    @Override
    public void addShopItem(Items item) {
        logger.debug("Añadiendo item a la tienda: " + item.getNombre());
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
        logger.debug("Usuarios recuperados: " + usersList.size());
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
        logger.info("Eliminando usuario: " + usuario);
        Session session = GameSession.openSession();
        Users user = session.getByField(Users.class, "usuario", usuario);
        if (user == null){
            logger.error("Intento de eliminar usuario inexistente: " + usuario);
            session.close();
            return false;
        }
        session.delete(user);
        session.close();
        logger.info("Usuario eliminado: " + usuario);
        return true;
    }

    @Override
    public boolean actualizarUsuario(String usuario, String nuevoUsuario) {
        logger.info("Actualizando usuario: " + usuario + " -> " + nuevoUsuario);
        Session session = GameSession.openSession();
        Users user = session.getByField(Users.class, "usuario", usuario);
        if (user == null) {
            logger.error("Intento de actualizar usuario inexistente: " + usuario);
            session.close();
            return false;
        }
        user.setUsuario(nuevoUsuario);
        session.update(user);
        session.close();
        logger.info("Usuario actualizado: " + usuario + " -> " + nuevoUsuario);
        return true;
    }

    @Override
    public boolean actualizarCorreo(String usuario, String nuevoCorreo) {
        logger.info("Actualizando correo de usuario: " + usuario);
        Session session = GameSession.openSession();
        Users user = session.getByField(Users.class, "usuario", usuario);
        if (user == null){
            logger.error("Intento de actualizar correo para usuario inexistente: " + usuario);
            session.close();
            return false;
        }
        user.setCorreo(nuevoCorreo);
        session.update(user);
        session.close();
        logger.info("Correo actualizado para usuario: " + usuario);
        return true;
    }

    @Override
    public boolean actualizarContrasena(String usuario, String nuevaContrasena) {
        logger.info("Actualizando contraseña de usuario: " + usuario);
        Session session = GameSession.openSession();
        Users user = session.getByField(Users.class, "usuario", usuario);
        if (user == null){
            logger.error("Intento de actualizar contraseña de usuario inexistente: " + usuario);
            session.close();
            return false;
        }
        user.setPassword(PasswordUtil.hashPassword(nuevaContrasena));
        session.update(user);
        session.close();
        logger.info("Contraseña actualizada para usuario: " + usuario);
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
        logger.info("Compra de items para usuario: " + usuario);
        Session session = GameSession.openSession();
        try {
            Users user = session.getByField(Users.class, "usuario", usuario);
            if (user == null) {
                logger.error("Usuario no encontrado: " + usuario);
                session.close();
                return -1;
            }
            int dineroUsuario = user.getMoney();
            int costeTotal = 0;

            for (Map.Entry<Integer, Integer> entry : itemsACobrar.entrySet()) {
                int itemId = entry.getKey();
                int cantidad = entry.getValue();

                Items item = session.getByField(Items.class, "id", itemId);
                if (item == null) {
                    logger.error("Item no encontrado: " + itemId);
                    session.close();
                    return -2;
                }
                costeTotal += item.getPrecio() * cantidad;
            }

            if (dineroUsuario < costeTotal) {
                logger.error("Dinero insuficiente para usuario: " + usuario);
                session.close();
                return 0;
            }

            int dineroFinal = dineroUsuario - costeTotal;
            user.setMoney(dineroFinal);
            session.update(user);

            for (Map.Entry<Integer, Integer> entry : itemsACobrar.entrySet()) {
                int itemId = entry.getKey();
                int cantidad = entry.getValue();

                HashMap<String, Object> condiciones = new HashMap<>();
                condiciones.put("ID_user", user.getId());
                condiciones.put("ID_item", itemId);
                List<Inventario> resultado = (List<Inventario>) (List<?>) session.findAll(Inventario.class, condiciones);

                if (resultado.isEmpty()) {
                    Inventario nuevo = new Inventario(user.getId(), itemId, cantidad);
                    logger.debug("Nuevo item en inventario: " + itemId + " cantidad: " + cantidad);
                    session.save(nuevo);
                } else {
                    Inventario existente = resultado.get(0);
                    existente.setCantidad(existente.getCantidad() + cantidad);
                    logger.debug("Actualizando item existente: " + itemId + " cantidad añadida: " + cantidad);
                    String[] keys = {"ID_user", "ID_item"};
                    ((SessionImpl)session).updateWithCompositeKey(existente, keys);
                }
            }

            session.close();
            logger.info("Compra exitosa para usuario: " + usuario + ". Dinero restante: " + dineroFinal);
            return dineroFinal;
        } catch (Exception e) {
            logger.error("Error inesperado en comprarItems para usuario: " + usuario, e);
            session.close();
            return -99;
        }
    }

    @Override
    public List<Items> getAllItems() {
        logger.debug("Recuperando todos los items");
        Session session = GameSession.openSession();
        List<Object> itemsBBDD = session.findAll(Items.class);
        session.close();
        List<Items> items = new ArrayList<>();
        for (Object o : itemsBBDD) {
            items.add((Items) o);
        }
        logger.debug("Items recuperados: " + items.size());
        return items;
    }

    @Override
    public Integer getScore(String username) {
        logger.debug("Buscando puntuación de " + username);
        Session session = GameSession.openSession();
        Users user = session.getByField(Users.class, "usuario", username);
        session.close();
        logger.debug("Puntuacion de " + username + " es: " + (user != null ? user.getScore() : null));
        return user != null ? user.getScore() : null;
    }

    @Override
    public Integer getMoney(String username) {
        logger.debug("Buscando dinero de " + username);
        Session session = GameSession.openSession();
        Users user = session.getByField(Users.class, "usuario", username);
        session.close();
        logger.debug("Dinero de " + username + " es: " + (user != null ? user.getMoney() : null));
        return user != null ? user.getMoney() : null;
    }

    @Override
    public String getInventarioPorUsuario(String username) {
        logger.debug("Buscando inventario de " + username);
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
            logger.error("Error obteniendo inventario de " + username, e);
            return "[]";
        } finally {
            session.close();
        }

        json.append("]");
        logger.debug("Inventario de " + username + " es: " + json.toString());
        return json.toString();
    }

    @Override
    public List<UsersScoreDTO> getAllUsersScoresDTO() {
        logger.debug("Buscando todas las puntuaciones");
        Session session = GameSession.openSession();
        List<Object> objs = session.findAll(Users.class);
        List<UsersScoreDTO> result = new ArrayList<>();

        for (Object o : objs) {
            Users user = (Users) o;
            UsersScoreDTO dto = new UsersScoreDTO(user.getUsuario(), user.getScore());
            result.add(dto);
        }

        session.close();
        logger.debug("Scores recuperadas: " + result.size());
        return result;
    }

    @Override
    public List<InsigniaDTO> getUserInsignia(String username) {
        logger.debug("Buscando insignias de " + username);
        Session session = GameSession.openSession();
        List<InsigniaDTO> result = new ArrayList<>();
        List<Insignias> insignias = session.getListByField(Insignias.class, "User", username);
        for (Insignias ins : insignias) {
            Insignia insignia = session.getByField(Insignia.class, "id", ins.getId_Insignia());
            result.add(new InsigniaDTO(insignia.getName(), insignia.getAvatar()));
        }
        session.close();
        logger.debug("Insignias de " + username + ": " + result.size());
        return result;
    }

    @Override
    public int anadirInsignia(String username, int id) {
        logger.info("Añadiendo insignia " + id + " al usuario: " + username);
        Session session = GameSession.openSession();
        List<Insignias> insignias = session.getListByField(Insignias.class, "User", username);
        for (Insignias ins : insignias) {
            if(ins.getId_Insignia() == id){
                logger.error("El usuario " + username + " ya tiene la insignia " + id);
                session.close();
                return 0;
            }
        }
        Insignias insignia = new Insignias(username, id);
        session.save(insignia);
        session.close();
        logger.info("Insignia añadida al usuario: " + username);
        return 1;
    }

    @Override
    public void crearPregunta(String username, String titulo, String mensaje) {
        logger.info("Creando pregunta de " + username + " | Título: " + titulo);
        Session session = GameSession.openSession();
        LocalDate ahora = LocalDate.now();
        java.sql.Date fecha = java.sql.Date.valueOf(ahora);
        Question pregunta = new Question(fecha, titulo, mensaje, username);
        session.save(pregunta);
        session.close();
        logger.debug("Pregunta creada con éxito: " + pregunta);
    }

    @Override
    public List<FAQ> getAllFAQs() {
        logger.debug("Buscando todas las FAQs");
        Session session = GameSession.openSession();
        List<Object> itemsBBDD = session.findAll(FAQ.class);
        session.close();
        List<FAQ> faqList = new ArrayList<>();
        for (Object faqs : itemsBBDD) {
            faqList.add((FAQ) faqs);
        }
        logger.debug("FAQs recuperadas: " + faqList.size());
        return faqList;
    }

    @Override
    public List<Media> getAllMedia() {
        logger.debug("Buscando todos los medios");
        Session session = GameSession.openSession();
        List<Object> mediaBBDD = session.findAll(Media.class);
        session.close();
        List<Media> videos = new ArrayList<>();
        for (Object urls : mediaBBDD) {
            videos.add((Media) urls);
        }
        logger.debug("Media recuperada: " + videos.size());
        return videos;
    }

    @Override
    public int consumirObjeto(String username, int idObjeto) {
        logger.info("Consumiendo objeto " + idObjeto + " para usuario: " + username);
        Session session = GameSession.openSession();
        HashMap<String, Object> condiciones = new HashMap<>();
        Users user = session.getByField(Users.class, "usuario", username);
        if (user == null) {
            logger.error("El usuario " + username + " no existe");
            session.close();
            return -1;
        }
        condiciones.put("ID_user", user.getId());
        condiciones.put("ID_item", idObjeto);

        List<Inventario> resultado = (List<Inventario>) (List<?>) session.findAll(Inventario.class, condiciones);

        Inventario existente = resultado.get(0);
        if(existente.getCantidad() <= 0){
            logger.error("El usuario " + username + " no tiene ese objeto");
            session.close();
            return -1;
        }
        existente.setCantidad(existente.getCantidad() - 1);
        String[] keys = {"ID_user", "ID_item"};
        ((SessionImpl)session).updateWithCompositeKey(existente, keys);
        session.close();
        logger.info("Objeto consumido: " + idObjeto + " para usuario: " + username);
        return 1;
    }

    @Override
    public void nuevaPuntuacion(String user, int puntuacion){
        logger.info("Nueva puntuación recibida: " + puntuacion + " para usuario: " + user);
        Session session = GameSession.openSession();
        Users usuario = session.getByField(Users.class, "usuario", user);
        if (usuario == null) {
            logger.error("El usuario " + user + " no existe");
        }
        else if(usuario.getScore() < puntuacion){
            usuario.setScore(puntuacion);
            session.update(usuario);
            logger.info("Puntuación mejorada y actualizada para usuario: " + user);
        }
        else
            logger.debug("Puntuación no superada, no se actualiza para usuario: " + user);
        session.close();
    }

    @Override
    public int anadirDinero(String user, int cantidad){
        logger.info("Añadiendo dinero " + cantidad + " al usuario: " + user);
        Session session = GameSession.openSession();
        Users usuario = session.getByField(Users.class, "usuario", user);
        if (usuario == null) {
            logger.error("El usuario " + user + " no existe");
            session.close();
            return -1;
        }
        int Dinero = usuario.getMoney() + cantidad;
        usuario.setMoney(Dinero);
        session.update(usuario);
        session.close();
        logger.info("Dinero añadido al usuario: " + user + " - Cantidad actualizada: " + Dinero);
        return Dinero;
    }
}