package MySQL;

import org.junit.Test;

import static org.junit.Assert.*;

public class MySQLManagerTest {
    @Test
    public void getInstance() throws Exception {
        MySQLManager.getInstance();
    }

    @Test
    public void importCSVFileToDatabase() throws Exception {
        String pathCSV = "";
        String portNo = "";
        String database = "";
        String table = "";
        String username = "";
        String password = "";
        String[] headers = new String[0];
        MySQLManager mySQLManager = MySQLManager.getInstance();
        boolean result = mySQLManager.importCSVFileToDatabase(pathCSV, portNo, database, table, username, password, headers, true);
        if(result) {
            throw new Exception();
        }
    }

}