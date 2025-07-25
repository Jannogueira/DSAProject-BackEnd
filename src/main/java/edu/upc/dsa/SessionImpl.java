package edu.upc.dsa;

import edu.upc.dsa.Session;
import edu.upc.dsa.util.ObjectHelper;
import edu.upc.dsa.util.QueryHelper;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SessionImpl implements Session {
    private static final Logger logger = Logger.getLogger(SessionImpl.class);
    private final Connection conn;

    public SessionImpl(Connection conn) {
        this.conn = conn;
    }

    public void save(Object entity) {
        String insertQuery = QueryHelper.createQueryINSERT(entity);
        PreparedStatement pstm = null;

        try {
            pstm = conn.prepareStatement(insertQuery);
            int i = 1;

            for (String field : ObjectHelper.getFields(entity)) {
                Object value = ObjectHelper.getter(entity, field);

                if (value == null) {
                    if (field.equalsIgnoreCase("score") || field.equalsIgnoreCase("money")) {
                        value = 0;
                    } else {
                        value = "";
                    }
                }

                logger.info("Inserting value for field: " + field + " = " + value);
                pstm.setObject(i++, value);
            }

            pstm.executeUpdate();
            logger.info("Insertion successful.");

        } catch (SQLException e) {
            logger.error("Error during insertion", e);
        }
    }

    public void close() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
                logger.debug("Conexión cerrada correctamente.");
            }
        } catch (SQLException e) {
            logger.error("Error closing connection", e);
        }
    }

    @Override
    public Object get(Class theClass, Object ID) {
        return null;
    }

    public Object get(Class theClass, int ID) {
        // Método no implementado
        return null;
    }

    public void update(Object object) {
        String query = QueryHelper.createQueryUPDATE(object);
        try (PreparedStatement pstm = conn.prepareStatement(query)) {
            String[] fields = ObjectHelper.getFields(object);
            int i = 1;

            for (String field : fields) {
                if (!field.equalsIgnoreCase("id")) {
                    Object value = ObjectHelper.getter(object, field);
                    if (value == null) {
                        if (field.equalsIgnoreCase("score") || field.equalsIgnoreCase("money")) {
                            value = 0;
                        } else {
                            value = "";
                        }
                    }
                    pstm.setObject(i++, value);
                }
            }

            Object id = ObjectHelper.getter(object, "id");
            pstm.setObject(i, id);

            pstm.executeUpdate();
            logger.info("Update successful.");
        } catch (SQLException e) {
            logger.error("Error during update", e);
        }
    }

    public void delete(Object object) {
        String sql = QueryHelper.createQueryDELETE(object);
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            Object id = ObjectHelper.getter(object, "id");
            stmt.setObject(1, id);
            int rowsAffected = stmt.executeUpdate();
            logger.info("Delete completado, fila afectada: " + rowsAffected);
        } catch (SQLException e) {
            logger.error("Error during delete", e);
        }
    }

    public List<Object> findAll(Class theClass) {
        List<Object> result = new ArrayList<>();
        String sql = QueryHelper.createSelectAll(theClass);
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            ResultSetMetaData meta = rs.getMetaData();
            int columnCount = meta.getColumnCount();
            while (rs.next()) {
                Object entity = theClass.getDeclaredConstructor().newInstance();
                for (int i = 1; i <= columnCount; i++) {
                    String column = meta.getColumnName(i);
                    Object value = rs.getObject(i);
                    ObjectHelper.setter(entity, column, value);
                }
                result.add(entity);
            }
        } catch (Exception e) {
            logger.error("Error during findAll", e);
        }
        return result;
    }

    @Override
    public List<Object> findAll(Class theClass, HashMap params) {
        List<Object> result = new ArrayList<>();
        HashMap<String, String> stringParams = new HashMap<>();

        logger.debug("==> findAll DEBUG - Clase objetivo: " + theClass.getSimpleName());

        for (Object key : params.keySet()) {
            stringParams.put(key.toString(), params.get(key).toString());
            logger.debug("==> Param: " + key + " = " + params.get(key));
        }

        String sql = QueryHelper.createSelectFindAll(theClass, stringParams);
        logger.debug("==> SQL generado: " + sql);

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            int index = 1;
            for (Object value : params.values()) {
                stmt.setObject(index, value);
                logger.debug("==> Set param[" + index + "]: " + value);
                index++;
            }

            try (ResultSet rs = stmt.executeQuery()) {
                ResultSetMetaData meta = rs.getMetaData();
                int columnCount = meta.getColumnCount();

                while (rs.next()) {
                    Object entity = theClass.getDeclaredConstructor().newInstance();
                    logger.debug("==> Instanciado objeto de tipo: " + entity.getClass().getSimpleName());

                    for (int i = 1; i <= columnCount; i++) {
                        String column = meta.getColumnName(i);
                        Object columnValue = rs.getObject(i);

                        logger.debug("    -> columna: " + column + ", valor: " + columnValue);

                        try {
                            ObjectHelper.setter(entity, column, columnValue);
                        } catch (Exception e) {
                            logger.error("    [ERROR] Al hacer setter para " + column + ": " + e.getMessage(), e);
                        }
                    }

                    result.add(entity);
                    logger.debug("==> Objeto añadido a la lista.");
                }

            } catch (Exception e) {
                logger.error("==> [ERROR] durante la ejecución del ResultSet: " + e.getMessage(), e);
            }

        } catch (Exception e) {
            logger.error("==> [ERROR] preparando el statement o ejecutando SQL: " + e.getMessage(), e);
        }

        logger.info("==> Total resultados encontrados: " + result.size());
        return result;
    }

    public List<Object> query(String query, Class theClass, HashMap params) {
        return null;
    }

    @Override
    public <T> T getByField(Class<T> theClass, String fieldName, Object value) {
        String sql = QueryHelper.createQuerySELECTbyField(theClass, fieldName);

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setObject(1, value);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    T entity = theClass.getDeclaredConstructor().newInstance();
                    ResultSetMetaData meta = rs.getMetaData();
                    int columnCount = meta.getColumnCount();
                    for (int i = 1; i <= columnCount; i++) {
                        String column = meta.getColumnName(i);
                        Object columnValue = rs.getObject(i);
                        ObjectHelper.setter(entity, column, columnValue);
                    }
                    return entity;
                }
            }
        } catch (Exception e) {
            logger.error("Error during getByField", e);
        }
        return null;
    }

    public void updateWithCompositeKey(Object entity, String[] keyFields) {
        String query = QueryHelper.createQueryUpdateCompositeKey(entity, keyFields);
        try (PreparedStatement pstm = conn.prepareStatement(query)) {
            String[] fields = ObjectHelper.getFields(entity);
            int i = 1;
            for (String field : fields) {
                boolean isKey = false;
                for (String key : keyFields) {
                    if (key.equalsIgnoreCase(field)) {
                        isKey = true;
                        break;
                    }
                }
                if (!isKey) {
                    Object value = ObjectHelper.getter(entity, field);
                    if (value == null) {
                        value = "";
                    }
                    pstm.setObject(i++, value);
                }
            }
            for (String key : keyFields) {
                Object keyValue = ObjectHelper.getter(entity, key);
                pstm.setObject(i++, keyValue);
            }
            pstm.executeUpdate();
            logger.info("Composite key update successful.");
        } catch (SQLException e) {
            logger.error("Error during composite key update", e);
        }
    }

    @Override
    public <T> List<T> getListByField(Class<T> theClass, String fieldName, Object value) {
        HashMap<String, Object> params = new HashMap<>();
        params.put(fieldName, value);

        List<Object> results = findAll(theClass, params);
        List<T> typedResults = new ArrayList<>();

        for (Object obj : results) {
            if (theClass.isInstance(obj)) {
                typedResults.add(theClass.cast(obj));
            }
        }
        return typedResults;
    }
}
