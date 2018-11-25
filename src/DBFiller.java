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
                + "	fullname VARCHAR(50),\n"
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
                + " total_amount INTEGER,\n"
                + " available_amount INTEGER,\n"
                + " socket VARCHAR(100),\n"
                + " FOREIGN KEY (CSID) REFERENCES charging_station(CSID)\n"
                + ");");

        sqls.add("CREATE TABLE IF NOT EXISTS car (\n"
                + " PLATE VARCHAR(50) PRIMARY KEY,\n"
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
                + " PLATE VARCHAR(50),\n"
                + "	datetime smalldatetime,\n"
                + " color VARCHAR(15),\n"
                + " model VARCHAR(50),\n"
                + " capacity INTEGER,\n"
                + " init_address VARCHAR(50),\n"
                + " arriving_address VARCHAR(50),\n"
                + " IsPaid BIT,\n"
                + " CardData VARCHAR(50),\n"
                + " FOREIGN KEY (username) REFERENCES customer(username),\n"
                + " FOREIGN KEY (PLATE) REFERENCES car(PLATE)\n"
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
                + "	OID INTEGER,\n"
                + "	starttime smalldatetime NOT NULL,\n"
                + "	finishtime smalldatetime NOT NULL,\n"
                + "	distance_to_init_address INTEGER NOT NULL,\n"
                + "	total_distance INTEGER NOT NULL,\n"
                + " FOREIGN KEY (OID) REFERENCES orders(OID)\n"
                + ");");

        sqls.add("CREATE TABLE IF NOT EXISTS repair (\n"
                + "	RID INTEGER PRIMARY KEY,\n"
                + "	ProviderID INTEGER,\n"
                + "	PLATE VARCHAR(50),\n"
                + "	WorkshopID INTEGER,\n"
                + "	parts VARCHAR(100),\n"
                + "	price INTEGER,\n"
                + "	datetime smalldatetime NOT NULL,\n"
                + " FOREIGN KEY (ProviderID) REFERENCES provider(PID),\n"
                + " FOREIGN KEY (PLATE) REFERENCES car(PLATE),\n"
                + " FOREIGN KEY (WorkshopID) REFERENCES workshop(WID)\n"
                + ");");

        sqls.add("CREATE TABLE IF NOT EXISTS charge (\n"
                + " PLATE VARCHAR(50),\n"
                + " CSID INTEGER,\n"
                + "	time smalldatetime,\n"
                + " Price INTEGER,\n"
                + " FOREIGN KEY (PLATE) REFERENCES car(PLATE),\n"
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

        sqls.add("CREATE TABLE IF NOT EXISTS payments (\n"
                + "	PID INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                + " OID INTEGER,\n"
                + " amount INTEGER,\n"
                + " carddata VARCHAR(100),\n"
                + " payment_time smalldatetime,\n"
                + " FOREIGN KEY (OID) REFERENCES orders(OID)\n"
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
        sqls.add("DROP TABLE IF EXISTS payments;");
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
        sqls.add("INSERT INTO car (PLATE, model, color, location) VALUES" +
                " ('AA100A', 'Tesla Xyesla', 'Black', 'Street 1'),\n" +
                " ('AA100B', 'Lada Kalina', 'Black', 'Street 1'),\n" +
                " ('AA100C', 'Toyota KEK', 'White', 'Street 2'),\n" +
                " ('AA100D', 'Toyota KEK', 'White', 'Street 2'),\n" +
                " ('AA100E', 'Toyota KEK', 'White', 'Street 2'),\n" +
                " ('AA100F', 'Toyota KEK', 'White', 'Street 3'),\n" +
                " ('AA100G', 'Toyota KEK', 'White', 'Street 3'),\n" +
                " ('AA100H', 'Toyota KEK', 'White', 'Street 3'),\n" +
                " ('AA100I', 'Toyota KEK', 'White', 'Street 3')\n" +

                ";");

        sqls.add("INSERT INTO orders (username, PLATE, datetime) VALUES" +
                " ('user1', 'AA100A', '25/11/2017 12:30'),\n" +
                " ('user1', 'AA100A', '25/11/2017 12:30'),\n" +
                " ('user1', 'AA100A', '25/11/2017 12:30'),\n" +
                " ('user1', 'AA100A', '25/11/2017 12:30'),\n" +
                " ('user2', 'AA100A', '25/11/2016 12:30'),\n" +
                " ('user2', 'AA100A', '25/11/2015 12:30'),\n" +
                " ('user2', 'AA100A', '25/11/2018 12:30'),\n" +
                " ('user3', 'AA100A', '25/11/2018 12:30'),\n" +
                " ('user5', 'AA100A', '25/11/2018 12:30')\n" +

                ";");

        sqls.add("INSERT INTO payments (OID, amount, payment_time) VALUES" +
                " ('1', '222', '25/11/2017 12:30'),\n" +
                " ('2', '22', '25/11/2017 12:30'),\n" +
                " ('3', '2', '25/11/2017 12:30'),\n" +
                " ('4', '222', '25/11/2017 12:30'),\n" +
                " ('5', '222', '25/11/2016 12:30'),\n" +
                " ('5', '222', '25/11/2015 12:30'),\n" +
                " ('5', '222', '25/11/2018 12:30'),\n" +
                " ('6', '222', '25/11/2018 12:30'),\n" +
                " ('3', '222', '25/11/2018 12:30')\n" +

                ";");

        sqls.add("INSERT INTO customer (username) VALUES" +
                " ('user1'),\n" +
                " ('user2'),\n" +
                " ('user3'),\n" +
                " ('user4'),\n" +
                " ('user5')\n" +
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

    public String select1(String username) {
        String url = "jdbc:sqlite:" + path + "/" + dbname;

        LinkedList<String> sqls = new LinkedList();

        // SQL statement for creating a new table
        sqls.add("SELECT PLATE FROM car" +
                " JOIN trip." +
                " ON trip.OID = orders.OID\n" +
                " JOIN order ON order.OID = trip.OID\n");


        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {

            for (String sql: sqls) {
                stmt.execute(sql);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}
