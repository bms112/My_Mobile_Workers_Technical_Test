package MySQL;

import java.io.File;
import java.io.FileWriter;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MySQLManager {

    private static MySQLManager mySQLManager;
    private static Logger logger = Logger.getLogger("ImportLog");

    private MySQLManager() {

    }

    //Singleton class getInstance method ensures only one instance of this class is ever initialised and used within the
    //program
    public static MySQLManager getInstance() {
        if(mySQLManager == null) {
            mySQLManager = new MySQLManager();
        }
        return mySQLManager;
    }

    //This public method is used by other classes to import the CSV File to the specified database
    //It calls the other private methods within this class to accomplish each step
    public boolean importCSVFileToDatabase(String pathToCSV, String portNo, String database, String table,
                                              String username, String password, String[] headers, boolean isJobData) {
        logger.info("Import Process Began");
        //First create the database if it doesn't already exist
        boolean result = createJobDatabase(portNo, database, username, password);
        if(!result) {
            //If an exception occurs return
            return false;
        }
        //Next create the table within the database if it doesn't already exist
        result = createJobTable(portNo, database, table, username, password, headers, isJobData);
        //Finally if no exceptions have occurred attempt to import the CSV File into the table within the database
        return result && importCSVFileToKnownDatabase(pathToCSV, portNo, database, table, username, password, headers);
    }

    //This method creates the Database if it does not exist
    private boolean createJobDatabase(String portNo, String database, String username, String password) {
        String url = "jdbc:mysql://localhost:" + portNo + "/?useSSL=false";
        String sql = "CREATE DATABASE IF NOT EXISTS " + database;

        return executeQuery(sql, url, username, password);
    }

    //This method creates the table within the Database if it does not exist
    private boolean createJobTable(String portNo, String database, String table, String username, String password,
                                   String[] headers, boolean isJobData) {
        String url = "jdbc:mysql://localhost:" + portNo + "/" + database + "?useSSL=false";
        String sql = "CREATE TABLE IF NOT EXISTS `" + table + "` (\n" +
                "  `PK` int(11) NOT NULL AUTO_INCREMENT,\n";
        //If the file to import is the CSV file for the given task, use the more accurately typed SQL
        if(isJobData) {
            sql +=  "  `Company` varchar(255) DEFAULT NULL,\n" +
                    "  `DC` varchar(255) DEFAULT NULL,\n" +
                    "  `DeliveryDate` DATE DEFAULT NULL,\n" +
                    "  `VehicleReg` varchar(255) DEFAULT NULL,\n" +
                    "  `DropNo` int(11) DEFAULT NULL,\n" +
                    "  `DropReference` varchar(255) NOT NULL UNIQUE,\n" +
                    "  `Timeslot` varchar(255) DEFAULT NULL,\n" +
                    "  `CustomersName` varchar(255) DEFAULT NULL,\n" +
                    "  `AddressLine1` varchar(255) DEFAULT NULL,\n" +
                    "  `AddressLine2` varchar(255) DEFAULT NULL,\n" +
                    "  `AddressLine3` varchar(255) DEFAULT NULL,\n" +
                    "  `AddressLine4` varchar(255) DEFAULT NULL,\n" +
                    "  `AddressLine5` varchar(255) DEFAULT NULL,\n" +
                    "  `Postcode` varchar(255) DEFAULT NULL,\n" +
                    "  `DeliveryInstructions` varchar(255) DEFAULT NULL,\n" +
                    "  `DropType` varchar(255) DEFAULT NULL,\n" +
                    "  `StockCode` varchar(255) DEFAULT NULL,\n" +
                    "  `Qty` int(11) DEFAULT NULL,\n" +
                    "  `StorePhoneNo` varchar(255) DEFAULT NULL,\n" +
                    "  `CustomerPhone1` varchar(255) DEFAULT NULL,\n" +
                    "  `CustomerPhone2` varchar(255) DEFAULT NULL,\n" +
                    "  `CustomerPhone3` varchar(255) DEFAULT NULL,\n" +
                    "  `StoreCustomerDrop` varchar(255) DEFAULT NULL,\n" +
                    "  `GoodsDescription` varchar(255) DEFAULT NULL,\n" +
                    "  `RouteNumber` varchar(255) DEFAULT NULL,\n" +
                    "  `RouteDesc` varchar(255) DEFAULT NULL,\n" +
                    "  `TakePhoto` varchar(1) DEFAULT NULL,\n" +
                    "  `TomTom` varchar(1) DEFAULT NULL,\n" +
                    "  `RequiresAssembly` varchar(1) DEFAULT NULL,\n" +
                    "  `ConcordeRef` varchar(255) DEFAULT NULL,\n" +
                    "  `Barcode` varchar(255) DEFAULT NULL,\n";
        } else {
            //Otherwise use the headers to generate the columns of the database table
            for(String header : headers) {
                sql = sql.concat("   `" + header + "` varchar(255) DEFAULT NULL,\n");
            }
        }
        sql += "   PRIMARY KEY(PK)) ENGINE=InnoDB DEFAULT CHARSET=utf8;";

        return executeQuery(sql, url, username, password);
    }

    //This method executes the sql query it is given to the url specified
    private boolean executeQuery(String sql, String url, String username, String password) {
        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.execute();
            conn.close();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Exception was thrown whilst executing a query", e);
            return false;
        }
        return true;
    }

    //This method imports the data into the MySQL Database table using a LOAD DATA query
    private boolean importCSVFileToKnownDatabase(String pathToCSV, String portNo, String database, String table,
                                                String username, String password, String[] headers) {
        String url ="jdbc:mysql://localhost:" + portNo + "/" + database + "?useSSL=false";
        //Creates the LOAD query using the CSV File and Database table
        String loadQuery = "LOAD DATA LOCAL INFILE '" + pathToCSV + "' " +
                    "INTO TABLE " + table + " " +
                    "FIELDS TERMINATED BY ',' " +
                    "LINES TERMINATED BY '\n' " +
                    "IGNORE 1 LINES " +
                    "(";
        //Use the headers within the CSV file to retrieve the column names to add to the LOAD query
        for(int i = 0; i < headers.length - 1; i++) {
            loadQuery = loadQuery.concat(headers[i] + ", ");
        }
        loadQuery += headers[headers.length - 1] + ") ";
        //Finally execute the query
        return executeQuery(loadQuery, url, username, password);
    }

    //This public method is used by other classes to export the specified Database Table to a new CSV File
    public boolean exportTableToCSVFile(String filename, String portNo, String database, String table,
                                           String username, String password) {
        logger.info("Export Process Began");

        String url ="jdbc:mysql://localhost:" + portNo + "/" + database + "?useSSL=false";
        String sql = "SELECT * FROM " + table;
        //Create the new CSV file
        new File(filename);

        //As I need the ResultSet and the ResultSetMetaData from the result of the sql query, I cannot use the private
        //method as the database connections needs to remain open for the duration
        try (Connection conn = DriverManager.getConnection(url, username, password);
        PreparedStatement stmt = conn.prepareStatement(sql)){

            //Execute the query and store the results
            ResultSet rs = stmt.executeQuery();
            FileWriter fileWriter = new FileWriter(filename);
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnNo = rsmd.getColumnCount();

            //To gain the Column Names of the database table, use the ResultSetMetaData and set the Header
            //of the new CSV file
            for(int i = 2; i < columnNo; i++) {
                fileWriter.append(rsmd.getColumnName(i));
                fileWriter.append(',');
            }
            fileWriter.append(rsmd.getColumnName(columnNo));
            fileWriter.append('\n');

            //While there is still another row, add the data within it to the new CSV file
            while (rs.next()) {
                for(int i = 2; i < columnNo; i++) {
                    fileWriter.append(rs.getString(i));
                    fileWriter.append(',');
                }
                fileWriter.append(rs.getString(columnNo));
                fileWriter.append('\n');
            }

            //Close the fileWriter and the database connection
            fileWriter.flush();
            fileWriter.close();
            conn.close();

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Exception was thrown whilst exporting a table", e);
            return false;
        }
        return true;
    }

}
