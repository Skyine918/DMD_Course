import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;

public class DBFiller {

    String path = ".";
    String dbname = "test.db";


    void createTables() {
        // SQLite connection string
        String url = "jdbc:sqlite:" + path + "/" + dbname;

        LinkedList<String> sqls = new LinkedList();

        // SQL statement for creating a new table
        sqls.add("CREATE TABLE IF NOT EXISTS customer (\n"
                + "	username VARCHAR(30) PRIMARY KEY,\n"
                + "	fullname VARCHAR(50) NOT NULL,\n"
                + "	email VARCHAR(320),\n"
                + " phone VARCHAR(15),\n"
                + " country VARCHAR(30),\n"
                + " city VARCHAR(30),\n"
                + " zipcode VARCHAR(30)\n"
                + ");");

        sqls.add("CREATE TABLE IF NOT EXISTS charging_station (\n"
                + " CSID INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                + " sockets_count INTEGER,\n"
                + " price_for_charge_unit INTEGER,\n"
                + " location VARCHAR(100)\n"
                + ");");

        sqls.add("CREATE TABLE IF NOT EXISTS sockets (\n"
                + " SID INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                + " CSID INTEGER,\n"
                + " socket VARCHAR(100),\n"
                + " FOREIGN KEY (CSID) REFERENCES charging_station(CSID)\n"
                + ");");

        sqls.add("CREATE TABLE IF NOT EXISTS car (\n"
                + " CID INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                + " location VARCHAR(100),\n"
                + "	model VARCHAR(50) NOT NULL,\n"
                + "	color VARCHAR(30),\n"
                + " charge INTEGER,\n"
                + " plug VARCHAR(50),\n"
                + " time_of_charging INTEGER,\n"
                + " km_per_percent INTEGER,\n"
                + " IsCharging BIT,\n"
                + " IsBroken BIT,\n"
                + " InUse BIT,\n"
                + " TotalDistance INTEGER\n"
                + ");");

        sqls.add("CREATE TABLE IF NOT EXISTS orders (\n"
                + " OID INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                + " username VARCHAR(30),\n"
                + " CID INTEGER,\n"
                + "	datetime smalldatetime,\n"
                + " color VARCHAR(15),\n"
                + " model VARCHAR(50),\n"
                + " capacity INTEGER,\n"
                + " init_address VARCHAR(50),\n"
                + " arriving_address VARCHAR(50),\n"
                + " IsPaid BIT,\n"
                + " CardData VARCHAR(50),\n"
                + " FOREIGN KEY (username) REFERENCES customer(username),\n"
                + " FOREIGN KEY (CID) REFERENCES car(CID)\n"
                + ");");

        sqls.add("CREATE TABLE IF NOT EXISTS provider (\n"
                + " PID INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                + " address VARCHAR(50),\n"
                + " phone VARCHAR(15)\n"
                + ");");

        sqls.add("CREATE TABLE IF NOT EXISTS workshop (\n"
                + " WID INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                + " location VARCHAR(50),\n"
                + " availability BIT\n"
                + ");");

        sqls.add("CREATE TABLE IF NOT EXISTS trip (\n"
                + "	TID INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                + "	starttime smalldatetime NOT NULL,\n"
                + "	finishtime smalldatetime NOT NULL,\n"
                + "	distance_to_init_address INTEGER NOT NULL,\n"
                + "	total_distance INTEGER NOT NULL\n"
                + ");");

        sqls.add("CREATE TABLE IF NOT EXISTS repair (\n"
                + "	RID INTEGER PRIMARY KEY,\n"
                + "	ProviderID INTEGER,\n"
                + "	CarID INTEGER,\n"
                + "	WorkshopID INTEGER,\n"
                + "	parts VARCHAR(100),\n"
                + "	price INTEGER,\n"
                + "	datetime smalldatetime NOT NULL,\n"
                + " FOREIGN KEY (ProviderID) REFERENCES provider(PID),\n"
                + " FOREIGN KEY (CarID) REFERENCES car(CID),\n"
                + " FOREIGN KEY (WorkshopID) REFERENCES workshop(WID)\n"
                + ");");

        sqls.add("CREATE TABLE IF NOT EXISTS charge (\n"
                + " CarID INTEGER,\n"
                + " CSID INTEGER,\n"
                + " Price INTEGER,\n"
                + " FOREIGN KEY (CarID) REFERENCES car(CID),\n"
                + " FOREIGN KEY (CSID) REFERENCES charging_station(CSID)\n"
                + ");");

        sqls.add("CREATE TABLE IF NOT EXISTS provider_parts (\n"
                + "	PPID INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                + " PID INTEGER,\n"
                + " model VARCHAR(100),\n"
                + " part VARCHAR(100),\n"
                + " amount INTEGER,\n"
                + " FOREIGN KEY (PID) REFERENCES provider(RID)\n"
                + ");");

        sqls.add("CREATE TABLE IF NOT EXISTS workshop_parts (\n"
                + "	WPID INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                + " WID INTEGER,\n"
                + " model VARCHAR(100),\n"
                + " part VARCHAR(100),\n"
                + " amount INTEGER,\n"
                + " FOREIGN KEY (WID) REFERENCES workshop(WID)\n"
                + ");");

        sqls.add("CREATE TABLE IF NOT EXISTS error (\n"
                + "	ID INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                + " ExternalID INTEGER,\n"
                + " TableName INTEGER,\n"
                + " ErrorMessage VARCHAR(100)\n"
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

    void cleanDB() {

        String url = "jdbc:sqlite:" + path + "/" + dbname;

        LinkedList<String> sqls = new LinkedList();

        // SQL statement for creating a new table
        sqls.add("DROP TABLE IF EXISTS customer;");
        sqls.add("DROP TABLE IF EXISTS sockets;");
        sqls.add("DROP TABLE IF EXISTS car;");
        sqls.add("DROP TABLE IF EXISTS orders;");
        sqls.add("DROP TABLE IF EXISTS provider;");
        sqls.add("DROP TABLE IF EXISTS workshop;");
        sqls.add("DROP TABLE IF EXISTS charging_station;");
        sqls.add("DROP TABLE IF EXISTS trip;");
        sqls.add("DROP TABLE IF EXISTS repair;");
        sqls.add("DROP TABLE IF EXISTS charge;");
        sqls.add("DROP TABLE IF EXISTS workshop_parts;");
        sqls.add("DROP TABLE IF EXISTS provider_parts;");
        sqls.add("DROP TABLE IF EXISTS error;");


        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {

            for (String sql: sqls) {
                stmt.execute(sql);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    void fillTables() {

        String url = "jdbc:sqlite:" + path + "/" + dbname;

        LinkedList<String> sqls = new LinkedList();

        // SQL statement for creating a new table
        sqls.add("INSERT INTO car (model, color, location) VALUES" +
                " ('Tesla Xyesla', 'Black', 'Street 1'),\n" +
                " ('Lada Kalina', 'Black', 'Street 1'),\n" +
                " ('Toyota KEK', 'White', 'Street 2'),\n" +
                " ('Toyota KEK', 'White', 'Street 2'),\n" +
                " ('Toyota KEK', 'White', 'Street 2'),\n" +
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
