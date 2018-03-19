package m;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class MysqlHelper {
    private static final String dbDriver = "com.mysql.jdbc.Driver";
    private static final String dbUrl = "jdbc:mysql://127.0.0.1:3307/";
    private static final String dbDatabase = "koni";
    private static final String dbUser = "root";
    private static final String dbPassword = "";
    private static final String characterEncoding = "utf-8";
    private Connection connection = null;

    public MysqlHelper() {
        try {
            Class.forName(dbDriver);
            connection = DriverManager.getConnection(dbUrl + dbDatabase + "?characterEncoding=utf-8&useUnicode=true", dbUser, dbPassword);

            String sqlCharset = "SET NAMES utf8 COLLATE utf8_general_ci";
            Statement st = connection.createStatement();
            st.executeQuery(sqlCharset);
            st.executeQuery("SET CHARACTER SET utf8");

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    public Connection getConnection() {
        return connection;
    }
}