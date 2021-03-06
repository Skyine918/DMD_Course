import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;


public class Main {

    private static void createNewDatabase(String fileName) {

        String url = "jdbc:sqlite:./" + fileName;

        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("The driver name is " + meta.getDriverName());
                System.out.println("A new database has been created.");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    public static void main(String[] args) {
        DBFiller filler = new DBFiller();
        DBSelectTest s  = new DBSelectTest();

        filler.cleanDB();

        filler.createTables();
        filler.fillTables();

        s.select7();
    }

}