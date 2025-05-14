package edu.upc.dsa;
import edu.upc.dsa.models.Users;

import java.util.HashMap;
import java.util.List;

public interface Session {
    void save(Object entity);                                           // Crud
    void close();

    Object get(Class theClass, Object ID);                                 // cRud
    void update(Object object);                                         // crUd
    void delete(Object object);                                         // cruD
    List<Object> findAll(Class theClass);                               // cR
    List<Object> findAll(Class theClass, HashMap params);
    List<Object> query(String query, Class theClass, HashMap params);

    <T> T getByField(Class<T> theClass, String fieldName, Object value);
}