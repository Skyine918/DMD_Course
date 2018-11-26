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
        sqls.add("CREATE TABLE IF NOT EXISTS Customers (\n"
                + "	username VARCHAR(30) PRIMARY KEY,\n"
                + "	fullname VARCHAR(50),\n"
                + "	email VARCHAR(320),\n"
                + " phone VARCHAR(15),\n"
                + " country VARCHAR(30),\n"
                + " city VARCHAR(30),\n"
                + " zipcode VARCHAR(30)\n"
                + ");");

        sqls.add("CREATE TABLE IF NOT EXISTS Charging_stations (\n"
                + " CSID INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                + " sockets_count INTEGER,\n"
                + " price_for_minute INTEGER,\n"
                + " location VARCHAR(100)\n"
                + ");");

        sqls.add("CREATE TABLE IF NOT EXISTS Sockets (\n"
                + " SID INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                + " CSID INTEGER,\n"
                + " socket VARCHAR(100),\n"
                + " total_amount INTEGER,\n"
                + " available_amount INTEGER,\n"
                + " FOREIGN KEY (CSID) REFERENCES Charging_stations(CSID)\n"
                + ");");

        sqls.add("CREATE TABLE IF NOT EXISTS Cars (\n"
                + " plate VARCHAR(50) PRIMARY KEY,\n"
                + " location VARCHAR(100),\n"
                + "	model VARCHAR(50) NOT NULL,\n"
                + "	color VARCHAR(30),\n"
                + " charge_level INTEGER,\n"
                + " plug VARCHAR(50),\n"
                + " time_of_charging INTEGER,\n"
                + " km_per_percent INTEGER,\n"
                + " IsCharging BIT,\n"
                + " IsBroken BIT,\n"
                + " InUse BIT,\n"
                + " TotalDistance INTEGER\n"
                + ");");

        sqls.add("CREATE TABLE IF NOT EXISTS Orders (\n"
                + " OID INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                + " username VARCHAR(30),\n"
                + " plate VARCHAR(50),\n"
                + "	date date,\n"
                + " time time,\n"
                + " color VARCHAR(15),\n"
                + " model VARCHAR(50),\n"
                + " init_address VARCHAR(50),\n"
                + " arriving_address VARCHAR(50),\n"
                + " FOREIGN KEY (username) REFERENCES Customers(username),\n"
                + " FOREIGN KEY (plate) REFERENCES Cars(plate)\n"
                + ");");

        sqls.add("CREATE TABLE IF NOT EXISTS Providers (\n"
                + " PID INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                + " model VARCHAR(100),\n"
                + " address VARCHAR(50),\n"
                + " phone VARCHAR(15)\n"
                + ");");

        sqls.add("CREATE TABLE IF NOT EXISTS Workshops (\n"
                + " WID INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                + " location VARCHAR(50),\n"
                + " total_amount INTEGER,\n"
                + " available_amount INTEGER,\n"
                + " availability BIT\n"
                + ");");

        sqls.add("CREATE TABLE IF NOT EXISTS Trips (\n"
                + "	TID INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                + "	OID INTEGER,\n"
                + "	starttime smalldatetime NOT NULL,\n"
                + "	finishtime smalldatetime NOT NULL,\n"
                + "	distance_to_init_address INTEGER NOT NULL,\n"
                + "	total_distance INTEGER NOT NULL,\n"
                + " FOREIGN KEY (OID) REFERENCES Orders(OID)\n"
                + ");");

        sqls.add("CREATE TABLE IF NOT EXISTS Repairs (\n"
                + "	RID INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                + "	ProviderID INTEGER,\n"
                + "	plate VARCHAR(50),\n"
                + "	WorkshopID INTEGER,\n"
                + "	parts VARCHAR(100),\n"
                + "	price INTEGER,\n"
                + "	date date,\n"
                + " time time,\n"
                + " FOREIGN KEY (ProviderID) REFERENCES Providers(PID),\n"
                + " FOREIGN KEY (plate) REFERENCES Cars(plate),\n"
                + " FOREIGN KEY (WorkshopID) REFERENCES Workshops(WID)\n"
                + ");");

        sqls.add("CREATE TABLE IF NOT EXISTS Charges (\n"
                + "	CID INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                + " plate VARCHAR(50),\n"
                + " CSID INTEGER,\n"
                + "	time smalldatetime,\n"
                + " Price INTEGER,\n"
                + " FOREIGN KEY (plate) REFERENCES Cars(plate),\n"
                + " FOREIGN KEY (CSID) REFERENCES Charging_stations(CSID)\n"
                + ");");

        sqls.add("CREATE TABLE IF NOT EXISTS Provider_parts (\n"
                + "	PPID INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                + " PID INTEGER,\n"
                + " part VARCHAR(100),\n"
                + " amount INTEGER,\n"
                + " FOREIGN KEY (PID) REFERENCES Providers(RID)\n"
                + ");");

        sqls.add("CREATE TABLE IF NOT EXISTS Workshop_parts (\n"
                + "	WPID INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                + " WID INTEGER,\n"
                + " model VARCHAR(100),\n"
                + " part VARCHAR(100),\n"
                + " amount INTEGER,\n"
                + " FOREIGN KEY (WID) REFERENCES Workshops(WID)\n"
                + ");");

        sqls.add("CREATE TABLE IF NOT EXISTS Logs (\n"
                + "	ID INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                + " ExternalID INTEGER,\n"
                + " TableName INTEGER,\n"
                + " ErrorMessage VARCHAR(100)\n"
                + ");");

        sqls.add("CREATE TABLE IF NOT EXISTS Payments (\n"
                + "	PID INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                + " OID INTEGER,\n"
                + " amount INTEGER,\n"
                + " carddata VARCHAR(100),\n"
                + " payment_time date,\n"
                + " IsPaid BIT,\n"
                + " FOREIGN KEY (OID) REFERENCES Orders(OID)\n"
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
        sqls.add("DROP TABLE IF EXISTS Customers;");
        sqls.add("DROP TABLE IF EXISTS Payments;");
        sqls.add("DROP TABLE IF EXISTS Sockets;");
        sqls.add("DROP TABLE IF EXISTS Cars;");
        sqls.add("DROP TABLE IF EXISTS Orders;");
        sqls.add("DROP TABLE IF EXISTS Providers;");
        sqls.add("DROP TABLE IF EXISTS Workshops;");
        sqls.add("DROP TABLE IF EXISTS Charging_stations;");
        sqls.add("DROP TABLE IF EXISTS Trips;");
        sqls.add("DROP TABLE IF EXISTS Repairs;");
        sqls.add("DROP TABLE IF EXISTS Charges;");
        sqls.add("DROP TABLE IF EXISTS Workshop_parts;");
        sqls.add("DROP TABLE IF EXISTS Provider_parts;");
        sqls.add("DROP TABLE IF EXISTS Logs;");


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
        sqls.add("INSERT INTO Cars (plate, model, color, location) VALUES" +
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

        sqls.add("INSERT INTO Orders (username, plate, date, time) VALUES" +
                " ('user1', 'AA100A', '25/11/2017', '12:30'),\n" +
                " ('user1', 'AA100A', '25/11/2017', '12:30'),\n" +
                " ('user1', 'AA100A', '25/11/2017', '12:30'),\n" +
                " ('user1', 'AA100A', '25/11/2017', '12:30'),\n" +
                " ('user2', 'AA100A', '25/11/2016', '12:30'),\n" +
                " ('user2', 'AA100A', '25/11/2015', '12:30'),\n" +
                " ('user2', 'AA100A', '25/11/2018', '12:30'),\n" +
                " ('user3', 'AA100A', '25/11/2018', '12:30'),\n" +
                " ('user5', 'AA100A', '25/11/2018', '12:30')\n" +

                ";");

        sqls.add("INSERT INTO Payments (OID, amount, payment_time) VALUES" +
                " ('1', '222', date('now', '-5 days')),\n" +
                " ('2', '22', date('now', '-5 days')),\n" +
                " ('3', '2', date('now', '-5 days')),\n" +
                " ('7', '222', date('now', '-5 days')),\n" +
                " ('5', '222', date('now', '-5 days')),\n" +
                " ('5', '222', date('now', '-5 days')),\n" +
                " ('5', '222', date('now', '-5 days')),\n" +
                " ('6', '222', date('now', '-5 days')),\n" +
                " ('3', '222', date('now', '-5 days'))\n" +

                ";");

        sqls.add("INSERT INTO Customers (username) VALUES" +
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
        sqls.add("SELECT plate FROM Cars" +
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
