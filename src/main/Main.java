import CSV.CSVManager;
import MySQL.MySQLManager;

public class Main {

    public static void main (String[] args) {
        CSVManager csvManager = CSVManager.getInstance();
        MySQLManager mySQLManager = MySQLManager.getInstance();
    }
}