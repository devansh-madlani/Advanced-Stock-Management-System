import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {
    public static Connection getConnection() throws Exception {
        
        Class.forName("org.sqlite.JDBC"); 

        String url = "jdbc:sqlite:D:/College/OOPM/IA/Stock Management System/stock.db";
        System.out.println("DB Path: " + url);
        return DriverManager.getConnection(url);
    }
}