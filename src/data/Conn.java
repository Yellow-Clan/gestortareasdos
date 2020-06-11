package data;

/**
 *
 * @author Asullom
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
import org.sqlite.SQLiteConfig;
//import java.sql.ResultSet;
//import java.sql.Statement;

public class Conn {

  
        
     public static final String DEFAULT_DATE_STRING_FORMAT_PE = "dd/MM/yyyy";
     public static final String DEFAULT_DATE_STRING_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static Connection connectSQLite() {

        Connection conn = null;
        try {
            Class.forName("org.sqlite.JDBC");
            String dbURL = "jdbc:sqlite:db2.db?foreign_keys=on;";
            
            //SQLiteConfig config = new SQLiteConfig();  
            //config.enforceForeignKeys(true);
            //connection = DriverManager.getConnection(DB_URL,config.toProperties());
        
            conn = DriverManager.getConnection(dbURL);
            //SQLiteDatabase db
            //db.execSQL("PRAGMA foreign_keys=ON;");
            //Statement statement = conn.createStatement();
            //ResultSet rs = statement.executeQuery("PRAGMA foreign_keys=ON;");
            //rs.next();
            /*
            String name = "";
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery("select * from Person");
            while (rs.next()) {

                name = name + ", " + rs.getString("name");
            }
            JOptionPane.showMessageDialog(null, "Connect to " + name);*/

        } catch (ClassNotFoundException | SQLException e) {
            JOptionPane.showMessageDialog(null, "Error en la conexión" + e);
        }
        return conn;
    }

    public static void closeSQLite(Connection conn) {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

}
