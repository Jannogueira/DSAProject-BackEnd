package edu.upc.dsa.util;

import edu.upc.dsa.util.ObjectHelper;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class QueryHelper {

    public static String createQueryINSERT(Object entity) {
        StringBuffer sb = new StringBuffer("INSERT INTO ");
        sb.append(entity.getClass().getSimpleName()).append(" ");
        sb.append("(");

        String[] fields = ObjectHelper.getFields(entity);
        boolean first = true;

        for (String field : fields) {
            if (!first) sb.append(", ");
            sb.append(field);
            first = false;
        }

        sb.append(") VALUES (");

        first = true;

        for (String field : fields) {
            if (!first) sb.append(", ");
            sb.append("?");
            first = false;
        }

        sb.append(")");


        System.out.println("Generated Query: " + sb.toString());

        return sb.toString();
    }

    public static String createQuerySELECT(Object entity) {
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT * FROM ").append(entity.getClass().getSimpleName());
        sb.append(" WHERE ID = ?");

        return sb.toString();
    }

    public static String createSelectAll(Class theClass) {
        return "SELECT * FROM " + theClass.getSimpleName();
    }

    public static String createSelectFindAll(Class theClass, HashMap<String, String> params) {

        Set<Map.Entry<String, String>> set = params.entrySet();

        StringBuffer sb = new StringBuffer("SELECT * FROM "+theClass.getSimpleName()+" WHERE 1=1");
        for (String key: params.keySet()) {
            sb.append(" AND "+key+"=?");
        }


        return sb.toString();
    }

    public static String createQueryUPDATE(Object entity) {
        StringBuilder sb = new StringBuilder("UPDATE ");
        sb.append(entity.getClass().getSimpleName());
        sb.append(" SET ");

        String[] fields = ObjectHelper.getFields(entity);
        boolean first = true;

        for (String field : fields) {
            if (!field.equalsIgnoreCase("id")) {
                if (!first) sb.append(", ");
                sb.append(field).append(" = ?");
                first = false;
            }
        }

        sb.append(" WHERE id = ?");

        System.out.println("Generated UPDATE Query: " + sb);
        return sb.toString();
    }
    public static String createQueryUpdateCompositeKey(Object entity, String[] keyFields) {
        StringBuilder sb = new StringBuilder("UPDATE ");
        sb.append(entity.getClass().getSimpleName());
        sb.append(" SET ");
        String[] fields = ObjectHelper.getFields(entity);
        boolean first = true;
        for (String field : fields) {
            boolean isKey = false;
            for (String key : keyFields) {
                if (key.equalsIgnoreCase(field)) {
                    isKey = true;
                    break;
                }
            }
            if (!isKey) {
                if (!first) sb.append(", ");
                sb.append(field).append(" = ?");
                first = false;
            }
        }
        sb.append(" WHERE ");
        first = true;
        for (String key : keyFields) {
            if (!first) sb.append(" AND ");
            sb.append(key).append(" = ?");
            first = false;
        }
        System.out.println("Generated Composite Key UPDATE Query: " + sb.toString());
        return sb.toString();
    }

    public static String createQueryDELETE(Object entity) {
        return "DELETE FROM " + entity.getClass().getSimpleName() + " WHERE id = ?";
    }

    public static String createQuerySELECTbyField(Class theClass, String field) {
        return "SELECT * FROM " + theClass.getSimpleName() + " WHERE " + field + " = ?";
    }
}