import java.sql.*;

public class DBSelectTest {

    String path = ".";
    String dbname = "test.db";

    private Connection connect() {
        // SQLite connection string
        String url = "jdbc:sqlite:" + path + "/" + dbname;
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public String getstats(String date, String from, String to) {

        String sql = "SELECT COUNT(*)" +
                " FROM Sockets " +
                " INNER JOIN Charges C on Sockets.SID = C.SID" +
                " WHERE " +
                " C.TIME BETWEEN date('" + from + "') AND date('" + to + "')" +
                " AND " +
                " C.date = date('" + date + "')" +
                " ORDER BY COUNT(*) DESC " +
                ";";

        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // loop through the result set
            while (rs.next()) {
                return "Sockets " + rs.getString("COUNT(*)");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
//    01:00, 02:00
    public void select2(String date, String from, String to) {

        String sql = "SELECT COUNT(*)" +
                " FROM Sockets " +
                " INNER JOIN Charges C on Sockets.SID = C.SID" +
                " WHERE " +
                " C.TIME BETWEEN date('"+from+"') AND date('"+to+"')" +
                " AND " +
                " C.date = date('"+date+"')" +
                " ORDER BY COUNT(*) DESC " +
                ";";

        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // loop through the result set
            while (rs.next()) {
                System.out.println(rs.getString("OID") + ": | Paid " + rs.getString("COUNT(*)") + " times");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void select4(){

        String sql = "SELECT Orders.OID, COUNT(*)" +
                "FROM Orders " +
                "INNER JOIN Payments ON Payments.OID = Orders.OID " +
                "WHERE " +
                "username = 'user2'" +
                " AND " +
                "IsPaid = 1" +
                " AND " +

                "Payments.date BETWEEN date('now', 'start of month') AND date('now', 'localtime')"  +
                "GROUP BY Orders.OID, amount " +
                "HAVING COUNT(*) > 1 " +
                ";";

        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){

            // loop through the result set
            while (rs.next()) {
                System.out.println(rs.getString("OID") + ": | Paid " + rs.getString("COUNT(*)") + " times");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    public void select5(String inputdate){

        String sql = "SELECT AVG(distance_to_init_address)" +
                "FROM Trips " +
                "WHERE " +
                "starttime BETWEEN datetime('"+ inputdate + "', 'start of day') AND datetime('"+ inputdate + "', '+1 day', 'start of day')"  +
                ";";

        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){

            // loop through the result set
            while (rs.next()) {
                System.out.println(rs.getString("AVG(distance_to_init_address)"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        sql = "SELECT strftime('%H:%M',CAST (AVG(julianday(finishtime) - julianday(starttime)) AS REAL),'12:00')" +
                "FROM Trips " +
                "WHERE " +
                "starttime BETWEEN datetime('"+ inputdate + "', 'start of day') AND datetime('"+ inputdate + "', '+1 day', 'start of day')"  +
                ";";

        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){

            // loop through the result set
            while (rs.next()) {
                System.out.println(rs.getString("strftime('%H:%M',CAST (AVG(julianday(finishtime) - julianday(starttime)) AS REAL),'12:00')"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void select6(){

        System.out.println("TOP 3 Most used initial addresses [07:00 - 10:00]");
        String sql = "SELECT COUNT(*), init_address FROM Orders " +
                    "WHERE time BETWEEN time('07:00') AND time('10:00')"  +
                    "GROUP BY init_address " +
                    "ORDER BY COUNT(*) DESC " +
                    "LIMIT 3" +
                    ";";


        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){

            // loop through the result set
            while (rs.next()) {
                System.out.println(rs.getString("init_address") + ": " + rs.getString("COUNT(*)") + " Times");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        System.out.println("TOP 3 Most used arriving addresses  [07:00 - 10:00]");
        sql = "SELECT COUNT(*), arriving_address FROM Orders " +
                "WHERE time BETWEEN time('07:00') AND time('10:00')"  +
                "GROUP BY arriving_address " +
                "ORDER BY COUNT(*) DESC " +
                "LIMIT 3" +
                ";";


        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){

            // loop through the result set
            while (rs.next()) {
                System.out.println(rs.getString("arriving_address") + ": " + rs.getString("COUNT(*)") + " Times");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }


        System.out.println("TOP 3 Most used initial addresses [12:00 - 14:00]");
        sql = "SELECT COUNT(*), init_address FROM Orders " +
                "WHERE time BETWEEN time('12:00') AND time('14:00')"  +
                "GROUP BY init_address " +
                "ORDER BY COUNT(*) DESC " +
                "LIMIT 3" +
                ";";


        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){

            // loop through the result set
            while (rs.next()) {
                System.out.println(rs.getString("init_address") + ": " + rs.getString("COUNT(*)") + " Times");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        System.out.println("TOP 3 Most used arriving addresses  [12:00 - 14:00]");
        sql = "SELECT COUNT(*), arriving_address FROM Orders " +
                "WHERE time BETWEEN time('12:00') AND time('14:00')"  +
                "GROUP BY arriving_address " +
                "ORDER BY COUNT(*) DESC " +
                "LIMIT 3" +
                ";";


        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){

            // loop through the result set
            while (rs.next()) {
                System.out.println(rs.getString("arriving_address") + ": " + rs.getString("COUNT(*)") + " Times");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        System.out.println("TOP 3 Most used initial addresses [17:00 - 19:00]");
        sql = "SELECT COUNT(*), init_address FROM Orders " +
                "WHERE time BETWEEN time('17:00') AND time('19:00')"  +
                "GROUP BY init_address " +
                "ORDER BY COUNT(*) DESC " +
                "LIMIT 3" +
                ";";


        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){

            // loop through the result set
            while (rs.next()) {
                System.out.println(rs.getString("init_address") + ": " + rs.getString("COUNT(*)") + " Times");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        System.out.println("TOP 3 Most used arriving addresses  [17:00 - 19:00]");
        sql = "SELECT COUNT(*), arriving_address FROM Orders " +
                "WHERE time BETWEEN time('17:00') AND time('19:00')"  +
                "GROUP BY arriving_address " +
                "ORDER BY COUNT(*) DESC " +
                "LIMIT 3" +
                ";";


        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){

            // loop through the result set
            while (rs.next()) {
                System.out.println(rs.getString("arriving_address") + ": " + rs.getString("COUNT(*)") + " Times");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    public void select7(){
        int limit = 0;
        if (getCarCount() != null) limit = (int) Math.ceil(Integer.parseInt(getCarCount()) * 0.1);
        System.out.println(getCarCount());

        String sql = "SELECT COUNT(*), plate FROM Orders " +
                "WHERE date BETWEEN date('now', '-3 months') AND date('now')"  +
                "GROUP BY plate " +
                "ORDER BY COUNT(*) ASC " +
                "LIMIT "+limit+"" +
                ";";


        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){

            // loop through the result set
            while (rs.next()) {
                System.out.println(rs.getString("plate") + ": [" + rs.getString("COUNT(*)") + " times]");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private String getCarCount() {
        String sql = "SELECT COUNT(*) FROM Cars;";

        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){

            // loop through the result set
            String result = null;
            while (rs.next()) {
                 result = rs.getString("COUNT(*)");
            }
            return result;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
}
