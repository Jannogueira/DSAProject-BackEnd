package edu.upc.dsa.util;

import org.apache.log4j.Logger;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ObjectHelper {
    private static final Logger logger = Logger.getLogger(ObjectHelper.class);

    public static String[] getFields(Object entity) {
        Class<?> theClass = entity.getClass();
        Field[] fields = theClass.getDeclaredFields();
        List<String> nonEmptyFields = new ArrayList<>();

        for (Field f : fields) {
            f.setAccessible(true);
            try {
                Object value = f.get(entity);
                if (value != null &&
                        !(value instanceof String && ((String) value).isEmpty()) &&
                        !(value instanceof Number && ((Number) value).intValue() == 0)) {
                    nonEmptyFields.add(f.getName());
                }
            } catch (IllegalAccessException e) {
                logger.error("Error accediendo al campo " + f.getName() + ": " + e.getMessage(), e);
            }
        }

        return nonEmptyFields.toArray(new String[0]);
    }

    public static void setter(Object object, String property, Object value) {
        try {
            Field field = object.getClass().getDeclaredField(property);
            field.setAccessible(true);
            field.set(object, value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            logger.error("Error en setter para propiedad '" + property + "': " + e.getMessage(), e);
        }
    }

    public static Object getter(Object object, String property) {
        try {
            Field field = object.getClass().getDeclaredField(property);
            field.setAccessible(true);
            return field.get(object);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            logger.error("Error en getter para propiedad '" + property + "': " + e.getMessage(), e);
            return null;
        }
    }
}
