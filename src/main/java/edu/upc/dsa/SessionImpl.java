package edu.upc.dsa;

import edu.upc.dsa.Session;
import edu.upc.dsa.util.ObjectHelper;
import edu.upc.dsa.util.QueryHelper;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class SessionImpl implements Session {
    private final Connection conn;

    public SessionImpl(Connection conn) {
        this.conn = conn;
    }

    public void save(Object entity) {
        String insertQuery = QueryHelper.createQueryINSERT(entity);
        PreparedStatement pstm = null;

        try {
            pstm = conn.prepareStatement(insertQuery);
            int i = 1;  // Empezamos en el índice 1 para el primer valor del PreparedStatement.

            for (String field : ObjectHelper.getFields(entity)) {
                // Obtenemos el valor del campo usando el getter
                Object value = ObjectHelper.getter(entity, field);

                // Si el valor es null, le asignamos un valor por defecto
                if (value == null) {
                    // Por ejemplo, asignamos un valor por defecto según el tipo de campo
                    if (field.equalsIgnoreCase("score") || field.equalsIgnoreCase("money")) {
                        value = 0;  // Para campos numéricos
                    } else {
                        value = "";  // Para campos de texto
                    }
                }

                // Verificamos el valor antes de asignarlo al PreparedStatement
                System.out.println("Inserting value for field: " + field + " = " + value);

                // Establecemos el valor en el PreparedStatement
                pstm.setObject(i++, value);
            }

            // Ejecutar la consulta de inserción
            pstm.executeUpdate();
            System.out.println("Insertion successful.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    public void close() {

    }

    @Override
    public Object get(Class theClass, Object ID) {
        return null;
    }

    public Object get(Class theClass, int ID) {
/*
        String sql = QueryHelper.createQuerySELECT(theClass);

        Object o = theClass.newInstance();


        ResultSet res = null;

        ResultSetMetaData rsmd = res.getMetaData();

        int numColumns = rsmd.getColumnCount();
        int i=0;

        while (i<numColumns) {
            String key = rsmd.getColumnName(i);
            String value = res.getObject(i);

            ObjectHelper.setter(o, key, value);

        }

*/
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

            // Añadir ID como último parámetro del WHERE
            Object id = ObjectHelper.getter(object, "id");
            pstm.setObject(i, id);

            pstm.executeUpdate();
            System.out.println("Update successful.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(Object object) {
        try {
            Class<?> theClass = object.getClass();
            Object idValue = ObjectHelper.getter(object, "id");
            if (idValue == null) {
                System.err.println("Delete failed: id is null");
                return;
            }

            String sql = "DELETE FROM " + theClass.getSimpleName() + " WHERE id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setObject(1, idValue);
                int affected = stmt.executeUpdate();
                if (affected > 0) {
                    System.out.println("Eliminado satisfactoriamente.");
                } else {
                    System.out.println("Delete: ninguna fila afectada.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Object> findAll(Class theClass) {
        List<Object> result = new ArrayList<>();
        String sql = "SELECT * FROM " + theClass.getSimpleName();

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
            e.printStackTrace();
        }

        return result;
    }

    public List<Object> findAll(Class theClass, HashMap params) {
     /*   String theQuery = QueryHelper.createSelectFindAll(theClass, params);
        PreparedStatement pstm = null;
        pstm = conn.prepareStatement(theQuery);

        int i=1;
        for (Object value : params.values()) {
            pstm.setObject(i++, value );
        }
        //ResultSet rs = pstm.executeQuery();




        return result;
*/
        return null;
    }

    public List<Object> query(String query, Class theClass, HashMap params) {
        return null;
    }
    @Override
    public <T> T getByField(Class<T> theClass, String fieldName, Object value) {
        String sql = "SELECT * FROM " + theClass.getSimpleName() + " WHERE " + fieldName + " = ?";
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
            e.printStackTrace();
        }
        return null;
    }
}