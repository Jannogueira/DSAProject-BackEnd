package edu.upc.dsa.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ObjectHelper {
    public static String[] getFields(Object entity) {
        Class<?> theClass = entity.getClass();

        Field[] fields = theClass.getDeclaredFields();

        // Lista para almacenar los nombres de los campos con valores no nulos o no vacíos
        List<String> nonEmptyFields = new ArrayList<>();

        // Iterar sobre los campos para verificar los valores
        for (Field f : fields) {
            f.setAccessible(true);  // Asegúrate de que el campo sea accesible

            try {
                // Obtener el valor del campo
                Object value = f.get(entity);

                // Si el valor no es nulo, vacío o 0, agregar el campo a la lista
                if (value != null &&
                        !(value instanceof String && ((String) value).isEmpty()) &&
                        !(value instanceof Number && ((Number) value).intValue() == 0)) {
                    nonEmptyFields.add(f.getName());
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        // Convertir la lista a un array y devolverlo
        return nonEmptyFields.toArray(new String[0]);
    }



    public static void setter(Object object, String property, Object value) {
        try {
            Field field = object.getClass().getDeclaredField(property);
            field.setAccessible(true);
            field.set(object, value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static Object getter(Object object, String property) {
        try {
            Field field = object.getClass().getDeclaredField(property);
            field.setAccessible(true);
            return field.get(object);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }
}
