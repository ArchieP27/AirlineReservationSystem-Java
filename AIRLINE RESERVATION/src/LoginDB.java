import java.sql.*;
import java.text.SimpleDateFormat;

public class LoginDB {
    private static final String url = "jdbc:mysql://localhost:3306/airline";
    private static final String username = "root";
    private static final String password = "12345678";
    static String query;
    static Connection connection;
    static Statement statement;
    static PreparedStatement preparedStatement;
    static ResultSet resultSet;
    static int affectedRows;
    static SimpleDateFormat originalFormat = new SimpleDateFormat("dd-MMM-yyyy");

    private LoginDB() throws RuntimeException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(url, username, password);
            statement = connection.createStatement();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static ResultSet viewMessages(String user){
        new LoginDB();
        query = "SELECT * FROM message WHERE username = '"+user+"';";
        try {
            resultSet = statement.executeQuery(query);
            return resultSet;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void changeFlight(String fno, String updatedFno) throws SQLException {
        new LoginDB();
        query = String.format("UPDATE Passenger SET Flight_number = '%s' WHERE Flight_Number = '%s';",updatedFno,fno);
        statement.executeUpdate(query);
        updateMessage(fno,updatedFno);
    }

    private static void updateMessage(String fno, String updatedFno) throws SQLException {
        new LoginDB();
        String message = "Flight Number updated from " + fno + " to : " + updatedFno;
        ResultSet localResultSet = null;

        try {
            localResultSet = statement.executeQuery("SELECT * FROM PASSENGER WHERE Flight_Number = '" + updatedFno + "';");

            while (localResultSet.next()) {
                String insertQuery = String.format("INSERT INTO MESSAGE(Username, Flight_number, Passenger_ID, Description) VALUES ('%s','%s','%s','%s')",
                        localResultSet.getString(1), localResultSet.getString(5), localResultSet.getString(3), message);
                statement.executeUpdate(insertQuery);
            }
        } finally {
            if (localResultSet != null) {
                try {
                    localResultSet.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }


    public static void updateFlightInfo(String departureTime, String arrivalTime, String flightDate, String price, String fno) throws SQLException {
        new LoginDB();
        query = String.format("UPDATE FLIGHT_INFO SET Departure_time = '%s', Arrival_time = '%s' , Flight_date = '%s', price = %d WHERE Flight_Number = '%s' ;",departureTime,arrivalTime,flightDate,Integer.parseInt(price),fno);
        statement.executeUpdate(query);
    }

    public static ResultSet getFlightDetails(String fno){
        query = "SELECT * FROM FLIGHT_INFO WHERE Flight_number = '"+fno+"';";
        try {
            resultSet = statement.executeQuery(query);
            return resultSet;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static ResultSet getFlightsId(){
        new LoginDB();
        query = "SELECT Flight_Number FROM FLIGHT_INFO;";
        try {
            resultSet = statement.executeQuery(query);
            return resultSet;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void changeUserPassword(String user, String password) throws SQLException {
        new LoginDB();
        query = String.format("UPDATE USER SET Password = '%s' WHERE Username = '%s';",password,user);
        statement.executeUpdate(query);
    }


    public static void updateFlight(String field, String value, String passenger_ID){
        new LoginDB();
        if(field.equalsIgnoreCase("passenger name"))
            field = "Passenger_Name";
        try{
        if (field.equalsIgnoreCase("age")) {
            query = "UPDATE PASSENGER SET " + field + " = ? WHERE Passenger_ID = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, Integer.parseInt(value));
            preparedStatement.setString(2, passenger_ID);
            preparedStatement.executeUpdate();
        } else {
            query = "UPDATE PASSENGER SET " + field + " = ? WHERE Passenger_ID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, value);
            preparedStatement.setString(2, passenger_ID);
            preparedStatement.executeUpdate();
        }
    } catch (SQLException e) {
        throw new RuntimeException(e);
    }
    }

    public static void alterRequest(int requestID,String status, String remark){
        new LoginDB();
        query = "UPDATE REQUESTS SET Request_Status = '"+status+"' , " +
                "Admin_remarks = '"+remark+"' WHERE Request_ID = "+requestID;
        try {
            statement.executeUpdate(query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void cancelFlight(String pID){
        new LoginDB();
        query = "DELETE FROM Passenger WHERE Passenger_ID = '"+pID+"'";
        try {
            statement.executeUpdate(query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static ResultSet getRequests(){
        new LoginDB();
        query = "SELECT * FROM REQUESTS";
        try {
            resultSet = statement.executeQuery(query);
            return resultSet;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public static ResultSet getRequests(String user){
        new LoginDB();
        query = String.format("SELECT * FROM REQUESTS NATURAL JOIN PASSENGER WHERE Username = '%s'",user);
        try {
            resultSet = statement.executeQuery(query);
            return resultSet;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getPassword(String user){
        new LoginDB();
        query = "SELECT Password FROM USER WHERE Username = '"+user+"';";
        try {
            resultSet = statement.executeQuery(query);
            if(resultSet.next())
                return resultSet.getString(1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return "";
    }

    public static boolean checkUser(String user) {
        new LoginDB();
        query = String.format("SELECT Username FROM USER WHERE Username = '%s'", user);
        try {
            resultSet = statement.executeQuery(query);
            return resultSet.next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean checkAdmin(String user) {
        new LoginDB();
        query = String.format("SELECT Username FROM ADMIN WHERE Username = '%s'", user);
        try {
            resultSet = statement.executeQuery(query);
            return resultSet.next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public static boolean addAdmin(String user, String password) {
        new LoginDB();
        if (!checkAdmin(user)) {
            query = String.format("INSERT INTO ADMIN VALUES ( '%s','%s')", user, password);
            try {
                affectedRows = statement.executeUpdate(query);
                return affectedRows != 0;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            return false;
        }
    }

    public static boolean checkPass(String user, String pass) {
        new LoginDB();
        query = String.format("SELECT Username FROM USER WHERE username = '%s' AND Password = '%s'", user, pass);
        try {
            resultSet = statement.executeQuery(query);
            return resultSet.next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean addUser(String user, String password) {
        new LoginDB();
        if (!checkUser(user)) {
            query = String.format("INSERT INTO USER VALUES ( '%s','%s')", user, password);
            try {
                affectedRows = statement.executeUpdate(query);
                if (affectedRows != 0) {
                    return true;
                } else
                    return false;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            return false;
        }
    }

    public static boolean checkFlight(String arrivalCode, String departureCode, String date) {
        new LoginDB();
        try {
            java.util.Date formattedDate = originalFormat.parse(date);
            java.sql.Date sqlDate = new java.sql.Date(formattedDate.getTime());
            query = String.format("SELECT * FROM FLIGHT_INFO WHERE Departure_airport_code = '%s' AND Arrival_airport_code = '%s' AND Flight_date = '%s'", departureCode, arrivalCode, sqlDate);
            resultSet = statement.executeQuery(query);
            return resultSet.next();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void insertFlight(String departureCode, String arrivalCode, String date, String departureTime, String arrivalTime, int price) {
        new LoginDB();
        int number = 0;
        try {
            query = "SELECT MAX(flight_number) as maxFlightNumber FROM Flight_info";
            resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                String lastFlightNumber = resultSet.getString("maxFlightNumber");
                number = Integer.parseInt(lastFlightNumber.substring(2));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        String Flight_number = "AI" + (number + 1);
        try {
            java.util.Date formattedDate = originalFormat.parse(date);
            java.sql.Date sqlDate = new java.sql.Date(formattedDate.getTime());

            query = String.format("INSERT INTO Flight_info VALUES ('%s','%s','%s','%s','%s','%s',%d)",
                    Flight_number, departureCode, arrivalCode, sqlDate, departureTime, arrivalTime, price);

            statement.executeUpdate(query);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static ResultSet getFlights(String departureCode, String arrivalCode, String date) {
        new LoginDB();
        try {
            java.util.Date formattedDate = originalFormat.parse(date);
            java.sql.Date sqlDate = new java.sql.Date(formattedDate.getTime());
            query = String.format("SELECT * FROM FLIGHT_INFO WHERE Departure_airport_code = '%s' AND Arrival_airport_code = '%s' AND Flight_date = '%s'", departureCode, arrivalCode, sqlDate);
            resultSet = statement.executeQuery(query);
            return resultSet;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static ResultSet getFlights(String FlightNumber) {
        new LoginDB();
        try {
            query = String.format("""
                    SELECT\s
                        f.*,
                        a_dep.AirportName AS DepartureAirportName,
                        a_arr.AirportName AS ArrivalAirportName
                    FROM\s
                        flight_info f
                    JOIN\s
                        airport a_dep ON f.Departure_airport_code = a_dep.AirportCode
                    JOIN\s
                        airport a_arr ON f.Arrival_airport_code = a_arr.AirportCode
                    WHERE Flight_number = '%s'""", FlightNumber);
            resultSet = statement.executeQuery(query);
            return resultSet;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static ResultSet displayAllFLight(){
        new LoginDB();
        query = """
                SELECT\s
                    FLIGHT_INFO.*,
                    DATE_FORMAT(Flight_date,'%e-%b-%Y') AS FormattedDate,
                    COUNT(PASSENGER.Flight_number) AS Bookings,
                    CASE
                        WHEN COUNT(PASSENGER.Flight_number) > 0\s
                        THEN (
                            SELECT COALESCE(SUM(Price), 0)
                            FROM FLIGHT_INFO FI
                            LEFT JOIN PASSENGER P ON FI.Flight_Number = P.Flight_number
                            WHERE FI.Flight_Number = FLIGHT_INFO.Flight_Number
                            GROUP BY FI.Flight_Number
                        )
                        ELSE 0
                    END AS Total_sales
                FROM FLIGHT_INFO
                LEFT JOIN PASSENGER ON FLIGHT_INFO.Flight_Number = PASSENGER.Flight_number
                GROUP BY FLIGHT_INFO.Flight_Number;
                """;
        try {
            resultSet = statement.executeQuery(query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return resultSet;
    }

    public static String getCustomerID() {
        new LoginDB();
        query = "SELECT MAX(Passenger_ID) AS MAXID FROM Passenger";
        try {
            resultSet = statement.executeQuery(query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            if (resultSet.next())
                return resultSet.getString("MAXID");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return "";
    }

    public static void addCustomer(String user, String passengerName, String pid, String cno, String fno, String email, String seatNumber, int age, String g) {
        new LoginDB();
        query = String.format("INSERT INTO Passenger VALUES ('%s','%s','%s','%s','%s','%s','%s',%d,'%s')", user, passengerName, pid, cno, fno, email, seatNumber, age, g);
        try {
            statement.executeUpdate(query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static ResultSet getReservedSeats(String Flight_Number) {
        new LoginDB();
        query = String.format("SELECT Seat_Number FROM Passenger WHERE Flight_Number = '%s'", Flight_Number);
        try {
            resultSet = statement.executeQuery(query);
            return resultSet;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static ResultSet searchFlights(String user){
        new LoginDB();
        query = "SELECT *, DATE_FORMAT(Flight_date,'%e-%b-%Y') AS FormattedDate\n" +
                "FROM PASSENGER\n" +
                "NATURAL JOIN FLIGHT_INFO\n" +
                "JOIN AIRPORT AS Arrival_Airport ON FLIGHT_INFO.Arrival_Airport_code = Arrival_Airport.AirportCode\n" +
                "JOIN AIRPORT AS Departure_Airport ON FLIGHT_INFO.Departure_Airport_code = Departure_Airport.AirportCode\n"+
                "WHERE Username = '"+user+"'" +
                "ORDER BY Flight_date";
        try {
            resultSet = statement.executeQuery(query);
            return resultSet;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static ResultSet getPID(String FlightNumber, String username) {
        new LoginDB();
        query = "SELECT Passenger_ID FROM PASSENGER " +
                "WHERE Flight_Number = '" + FlightNumber + "' AND " +
                "Username = '" + username + "';";
        try{
        resultSet = statement.executeQuery(query);
        return resultSet;
        }catch (SQLException se){
            throw new RuntimeException(se);
        }
    }

    public static void createRequest(String fno, String pid, String field, String value, String reqType) throws SQLException {
        new LoginDB();
        if(field.equalsIgnoreCase("age")) {
            query = String.format("INSERT INTO REQUESTS(Flight_Number,Passenger_ID,FieldModified,RequestType,updatedValue) VALUES('%s','%s','%s','%s',%d);", fno, pid, field, reqType, Integer.parseInt(value));
        }else{
            query = String.format("INSERT INTO REQUESTS(Flight_Number,Passenger_ID,FieldModified,RequestType,updatedValue) VALUES('%s','%s','%s','%s','%s');", fno, pid, field, reqType, value);
        }
        statement.executeUpdate(query);
    }

    public static void main(String[] args) {
        new LoginDB();
//        loginDB.insertFlight("MAA","DEL","12-Jan-2023","12:50","2:50",5600);

        getFlights("AI123");

    }
}