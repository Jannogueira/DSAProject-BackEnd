package edu.upc.dsa;

import edu.upc.dsa.util.DBUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class GameSession {

    public static Session openSession() {
        Connection conn = getConnection();
        return new SessionImpl(conn);
    }

    private static Connection getConnection() {
        String db = DBUtils.getDb();
        String host = DBUtils.getDbHost();
        String port = DBUtils.getDbPort();
        String user = DBUtils.getDbUser();
        String pass = DBUtils.getDbPasswd();

        try {
            return DriverManager.getConnection("jdbc:mariadb://" + host + ":" + port + "/" + db + "?user=" + user + "&password=" + pass);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}