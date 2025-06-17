package edu.upc.dsa;

import com.sun.jna.platform.win32.Netapi32Util;
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

    private WebManagerImpl() {
    }

    public static WebManagerImpl getInstance() {
        if (instance == null) {
            instance = new WebManagerImpl();
        }
        return instance;
    }

    @Override
    public int registerUser(String username, String correo, String password) {
        logger.info("New register -username: " + username + " -correo: " + correo);
        if (existeEmail(correo)) return 3;
        if (existeUser(username)) return 2;
        String hashedPassword = PasswordUtil.hashPassword(password);
        Session session = GameSession.openSession();
        Users users = new Users(username, correo, hashedPassword);
        session.save(users);
        logger.info("New register completed!");
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
        logger.info("New login -correo: " + correo);
        Session session = GameSession.openSession();
        Users user = session.getByField(Users.class, "correo", correo);
        session.close();
        return user != null && PasswordUtil.verifyPassword(password, user.getPassword());
    }

    @Override
    public List<Items> getAllShopItems() {
        logger.info("GetAllShopItems");
        return this.shop.getAllShopItems();
    }

    @Override
    public void addShopItem(Items item) {
        logger.info("addShopItem");
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
        logger.info("getAllUsers");
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
        logger.info("Usuario eliminado!");
        return true;
    }

    @Override
    public boolean actualizarUsuario(String usuario, String nuevoUsuario) {
        logger.info("Actualizando usuario: " + usuario + " -a nuevoUsuario: " + nuevoUsuario);
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
        logger.info("Usuario actualizado!");
        return true;
    }

    @Override
    public boolean actualizarCorreo(String usuario, String nuevoCorreo) {
        logger.info("Actualizando correo de: " + usuario + " -a nuevoCorreo: " + nuevoCorreo);
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
        logger.info("Correo actualizado!");
        return true;
    }

    @Override
    public boolean actualizarContrasena(String usuario, String nuevaContrasena) {
        logger.info("Actualizando contraseña de " + usuario);
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
        logger.info("Contraseña actualizada!");
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
        logger.info("Comprando items de " + usuario + " - " + itemsACobrar);
        Session session = GameSession.openSession();
        try {
            // Paso 1: Obtener el usuario por su nombre
            Users user = session.getByField(Users.class, "usuario", usuario);
            if (user == null) {
                logger.error("Usuario no encontrado: " + usuario);
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
                    logger.error("Item no encontrado: " + itemId);
                    session.close();
                    return -2; // Algún item no encontrado
                }
                costeTotal += item.getPrecio() * cantidad;
            }

            // Paso 3: Verificar si el usuario tiene suficiente dinero
            if (dineroUsuario < costeTotal) {
                session.close();
                logger.error("Dinero insuficiente para usuario: " + usuario);
                return 0; // Dinero insuficiente
            }

            // Paso 4: Restar dinero y actualizar usuario
            int dineroFinal = dineroUsuario - costeTotal;
            user.setMoney(dineroFinal);
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
                    Inventario nuevo = new Inventario(user.getId(), itemId, cantidad);
                    logger.info("Nuevo item: " + itemId + " - " + cantidad);
                    session.save(nuevo);
                } else {
                    Inventario existente = resultado.get(0);
                    existente.setCantidad(existente.getCantidad() + cantidad);
                    logger.info("Existente item: " + itemId + " - " + cantidad);
                    String[] keys = {"ID_user", "ID_item"};
                    ((SessionImpl)session).updateWithCompositeKey(existente, keys);
                }
            }

            session.close();
            logger.info("Compra Exitosa!");
            return dineroFinal; // Compra exitosa
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Error inesperado en comprarItems para usuario: " + usuario, e);
            session.close();
            return -99; // Error inesperado
        }
    }

    @Override
    public List<Items> getAllItems() {
        logger.info("getAllItems");
        Session session = GameSession.openSession();
        List<Object> itemsBBDD = session.findAll(Items.class);
        session.close();
        List<Items> items = new ArrayList<>();
        for (Object o : itemsBBDD) {
            items.add((Items) o);
        }
        logger.info("getAllItems returned: " + items);
        return items;
    }
    @Override
    public Integer getScore(String username) {
        logger.info("Buscando puntuación de " + username);
        Session session = GameSession.openSession();
        Users user = session.getByField(Users.class, "usuario", username);
        session.close();
        logger.info("Puntuacion de " + username + " es: " + user.getScore());
        return user != null ? user.getScore() : null;
    }
    @Override
    public Integer getMoney(String username) {
        logger.info("Buscando dinero de " + username);
        Session session = GameSession.openSession();
        Users user = session.getByField(Users.class, "usuario", username);
        session.close();
        logger.info("Dinero de " + username + " es: " + user.getMoney());
        return user != null ? user.getMoney() : null;
    }


    @Override
    public String getInventarioPorUsuario(String username) {
        logger.info("Buscando inventario de " + username);
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
        logger.info("Inventario de " + username + " es: " + json.toString());
        return json.toString();
    }

    @Override
    public List<UsersScoreDTO> getAllUsersScoresDTO() {
        logger.info("Buscando todas las scores");
        Session session = GameSession.openSession();
        List<Object> objs = session.findAll(Users.class);
        List<UsersScoreDTO> result = new ArrayList<>();

        for (Object o : objs) {
            Users user = (Users) o;

            UsersScoreDTO dto = new UsersScoreDTO(user.getUsuario(), user.getScore());
            result.add(dto);
        }

        session.close();
        logger.info("Scores: " + result);
        return result;
    }
    @Override
    public List<InsigniaDTO> getUserInsignia(String username) {
        logger.info("Buscando insignia de " + username);
        Session session = GameSession.openSession();
        List<InsigniaDTO> result = new ArrayList<>();
        List<Insignias> insignias = session.getListByField(Insignias.class, "User", username);
        for (Insignias ins : insignias) {
            Insignia insignia = session.getByField(Insignia.class, "id", ins.getId_Insignia());
            result.add(new InsigniaDTO(insignia.getName(), insignia.getAvatar()));
        }
        session.close();
        logger.info("Las insignias de: " +username+ " -son:" + result);
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
        logger.info("Creando Pregunta de " + username + " Titulo: " + titulo + " Mensaje: " + mensaje);
        Session session = GameSession.openSession();
        LocalDate ahora = LocalDate.now();
        java.sql.Date fecha = java.sql.Date.valueOf(ahora);
        Question pregunta = new Question(fecha, titulo, mensaje, username);
        session.save(pregunta);
        session.close();
        logger.info("Pregunta creada con exito: " + pregunta);
    }

    @Override
    public List<FAQ> getAllFAQs() {
        logger.info("Buscando todas las FAQs");
        Session session = GameSession.openSession();
        List<Object> itemsBBDD = session.findAll(FAQ.class);
        session.close();
        List<FAQ> faqList = new ArrayList<>();
        for (Object faqs : itemsBBDD) {
            faqList.add((FAQ) faqs);
        }
        logger.info("FAQs: " + faqList);
        return faqList;
    }

    @Override
    public List<Media> getAllMedia() {
        logger.info("Buscando todas las Media");
        Session session = GameSession.openSession();
        List<Object> mediaBBDD = session.findAll(Media.class);
        session.close();
        List<Media> videos = new ArrayList<>();
        for (Object urls : mediaBBDD) {
            videos.add((Media) urls);
        }
        logger.info("Media: " + videos);
        return videos;
    }

    @Override
    public int consumirObjeto(String username, int idObjeto) {
        logger.info("Consumiendo objeto " + idObjeto + " al usuario: " + username);
        Session session = GameSession.openSession();
        java.util.HashMap<String, Object> condiciones = new java.util.HashMap<>();
        Users user = session.getByField(Users.class, "usuario", username);
        if (user == null) {
            logger.error("El usuario " + user + " no tiene existe");
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
        logger.info("Item: " + idObjeto + " Consumido al usuario: " + username);
        return 1;
    }

    @Override
    public void nuevaPuntuacion(String user, int puntuacion){
        logger.info("Nueva puntuación recibida " + puntuacion + " al usuario: " + user);
        Session session = GameSession.openSession();
        Users usuario = session.getByField(Users.class, "usuario", user);
        if (usuario == null) {
            logger.error("El usuario " + user + " no tiene existe");
        }
        else if(usuario.getScore() < puntuacion){
            usuario.setScore(puntuacion);
            session.update(usuario);
            logger.info("Puntuación mejor, actualizada con éxito");
        }
        else
            logger.info("Puntuación peor, no actualizada");
        session.close();
    }

    @Override
    public int anadirDinero(String user, int cantidad){
        logger.info("Añadiendo dinero " + cantidad + " al usuario: " + user);
        Session session = GameSession.openSession();
        Users usuario = session.getByField(Users.class, "usuario", user);
        if (usuario == null) {
            logger.error("El usuario " + user + " no tiene existe");
            return -1;
        }
        int Dinero = usuario.getMoney() + cantidad;
        usuario.setMoney(Dinero);
        session.update(usuario);
        session.close();
        logger.info("Dinero añadido al usuario: " + user + "- Cantidad actualizada: " + Dinero);
        return Dinero;
    }



}