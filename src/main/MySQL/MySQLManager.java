package MySQL;

import java.sql.*;

public class MySQLManager {

    private static MySQLManager mySQLManager;

    private MySQLManager() {
        try{
            String username = "sa";
            String password = "F1shg0ld";
            Class.forName("com.mysql.jdbc.Driver");
            Connection con= DriverManager.getConnection(
                    "jdbc:mysql://localhost:8080/sys?useSSL=false",username,password);
            String path = "C:/Users/Ben/IdeaProjects/My_Mobile_Workers_Technical_Test/src/resources/jobdata.csv";
            String table = "jobs3";
            String loadQuery = "LOAD DATA LOCAL INFILE '" + path + "' INTO TABLE " + table + " FIELDS TERMINATED BY ','"
                        + " LINES TERMINATED BY '\n' IGNORE 1 LINES (Company, DC, DeliveryDate, VehicleReg, DropNo, DropReference, Timeslot, " +
                        "CustomersName, AddressLine1, AddressLine2, AddressLine3, AddressLine4, AddressLine5, Postcode, " +
                        "DeliveryInstructions, DropType, StockCode, Qty, StorePhoneNo, CustomerPhone1, CustomerPhone2, " +
                        "CustomerPhone3, StoreCustomerDrop, GoodsDescription, RouteNumber, RouteDesc, TakePhoto, TomTom, " +
                        "RequiresAssembly, ConcordeRef, Barcode) ";
            Statement stmt = con.createStatement();
            stmt.execute(loadQuery);
            con.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static MySQLManager getInstance() {
        if(mySQLManager == null) {
            mySQLManager = new MySQLManager();
        }
        return mySQLManager;
    }

}
