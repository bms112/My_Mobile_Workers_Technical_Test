package MySQL;

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
                                              String username, String password) {
        logger.info("Import Process Began");
        //First create the database if it doesn't already exist
        boolean result = createJobDatabase(portNo, database, username, password);
        if(!result) {
            //If an exception occurs return
            return false;
        }
        //Next create the table within the database if it doesn't already exist
        result = createJobTable(portNo, database, table, username, password);
        //Finally if no exceptions have occurred attempt to import the CSV File into the table within the database
        return result && importCSVFileToKnownDatabase(pathToCSV, portNo, database, table, username, password);
    }

    private boolean createJobDatabase(String portNo, String database, String username, String password) {
        String url = "jdbc:mysql://localhost:" + portNo + "/?useSSL=false";
        String sql = "CREATE DATABASE IF NOT EXISTS " + database;

        return executeQuery(sql, url, username, password);
    }

    private boolean createJobTable(String portNo, String database, String table, String username, String password) {
        String url = "jdbc:mysql://localhost:" + portNo + "/" + database + "?useSSL=false";
        String sql = "CREATE TABLE IF NOT EXISTS `" + table + "` (\n" +
                "  `PK` int NOT NULL AUTO_INCREMENT,\n" +
                "  `Company` varchar(255) DEFAULT NULL,\n" +
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
                "  `Barcode` varchar(255) DEFAULT NULL,\n" +
                "   PRIMARY KEY(PK)) ENGINE=InnoDB DEFAULT CHARSET=utf8;";

        return executeQuery(sql, url, username, password);
    }

    private boolean executeQuery(String sql, String url, String username, String password) {
        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.execute();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Exception was thrown whilst executing a query", e);
            return false;
        }
        return true;
    }

    private boolean importCSVFileToKnownDatabase(String pathToCSV, String portNo, String database, String table,
                                                String username, String password) {
        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection con= DriverManager.getConnection(
                    "jdbc:mysql://localhost:" + portNo + "/" + database + "?useSSL=false",username,password);
            String loadQuery = "LOAD DATA LOCAL INFILE '" + pathToCSV + "' INTO TABLE " + table + " FIELDS TERMINATED BY ','"
                    + " LINES TERMINATED BY '\n' IGNORE 1 LINES (Company, DC, DeliveryDate, VehicleReg, DropNo, DropReference, Timeslot, " +
                    "CustomersName, AddressLine1, AddressLine2, AddressLine3, AddressLine4, AddressLine5, Postcode, " +
                    "DeliveryInstructions, DropType, StockCode, Qty, StorePhoneNo, CustomerPhone1, CustomerPhone2, " +
                    "CustomerPhone3, StoreCustomerDrop, GoodsDescription, RouteNumber, RouteDesc, TakePhoto, TomTom, " +
                    "RequiresAssembly, ConcordeRef, Barcode) ";
            Statement stmt = con.createStatement();
            stmt.execute(loadQuery);
            con.close();
        } catch(Exception e) {
            logger.log(Level.SEVERE, "Exception was thrown whilst importing the CSV into the database", e);
            return false;
        }
        return true;
    }

}
