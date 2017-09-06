import CSV.CSVManager;
import MySQL.MySQLManager;

import java.util.List;

public class Main {

    public static void main (String[] args) {
        CSVManager csvManager = CSVManager.getInstance();
        MySQLManager mySQLManager = MySQLManager.getInstance();
        List<String> lines = csvManager.readCSVFile();

    }
}