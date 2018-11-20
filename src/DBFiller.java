import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;

public class DBFiller {

    String path = ".";
    String dbname = "test.db";


    public void createTables() {
        // SQLite connection string
        String url = "jdbc:sqlite:" + path + "/" + dbname;

        LinkedList<String> sqls = new LinkedList();

        // SQL statement for creating a new table
        sqls.add("CREATE TABLE IF NOT EXISTS customer (\n"
                + "	username VARCHAR(30) PRIMARY KEY,\n"
                + "	fullname VARCHAR(50) NOT NULL,\n"
                + "	email VARCHAR(320),\n"
                + " phone VARCHAR(15),\n"
                + " country VARCHAR(15),\n"
                + " city VARCHAR(15),\n"
                + " zipcode VARCHAR(15)\n"
                + ");");

        sqls.add("CREATE TABLE IF NOT EXISTS car_order (\n"
                + "	OID int PRIMARY KEY,\n"
                + "	timestamp TIMESTAMP NOT NULL,\n"
                + " model VARCHAR(50),\n"
                + " color VARCHAR(15),\n"
                + " capacity VARCHAR(15),\n"
                + " init_address VARCHAR(50),\n"
                + " arriving_address VARCHAR(50)\n"
                + ");");

        sqls.add("CREATE TABLE IF NOT EXISTS car (\n"
                + " CID INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                + "	model VARCHAR(50) NOT NULL,\n"
                + "	color VARCHAR(30),\n"
                + " location VARCHAR(15)\n"
                + ");");

        sqls.add("CREATE TABLE IF NOT EXISTS payment (\n"
                + "	ID INT PRIMARY KEY,\n"
                + "	timestamp TIMESTAMP NOT NULL,\n"
                + "	carddata VARCHAR(50) NOT NULL,\n"
                + "	amount INT,\n"
                + " CID INT,\n"
                + " FOREIGN KEY (CID) REFERENCES customer(username) on delete cascade\n"
                + ");");

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {

            for (String sql: sqls) {
                stmt.execute(sql);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void cleanDB() {

        String url = "jdbc:sqlite:" + path + "/" + dbname;

        LinkedList<String> sqls = new LinkedList();

        // SQL statement for creating a new table
        sqls.add("DROP TABLE IF EXISTS customer;");
        sqls.add("DROP TABLE IF EXISTS car_order;");
        sqls.add("DROP TABLE IF EXISTS payment;");
        sqls.add("DROP TABLE IF EXISTS car;");


        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {

            for (String sql: sqls) {
                stmt.execute(sql);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void fillTables() {

        String url = "jdbc:sqlite:" + path + "/" + dbname;

        LinkedList<String> sqls = new LinkedList();

        // SQL statement for creating a new table
        sqls.add("INSERT INTO car (model, color, location) VALUES" +
                " ('Tesla Xyesla', 'Black', 'Street 1'),\n" +
                " ('Lada Kalina', 'Black', 'Street 2'),\n" +
                " ('Toyota KEK', 'White', 'Street 3'),\n" +
                " ('Toyota KEK', 'White', 'Street 3'),\n" +
                " ('Toyota KEK', 'White', 'Street 3'),\n" +
                " ('Toyota KEK', 'White', 'Street 3'),\n" +
                " ('Toyota KEK', 'White', 'Street 3'),\n" +
                " ('Toyota KEK', 'White', 'Street 3'),\n" +
                " ('Toyota KEK', 'White', 'Street 3')\n" +

                ";");




        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {

            for (String sql: sqls) {
                stmt.execute(sql);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
