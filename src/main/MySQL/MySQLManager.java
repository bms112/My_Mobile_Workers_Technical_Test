package MySQL;

public class MySQLManager {

    private static MySQLManager mySQLManager;

    private MySQLManager() {

    }

    public static MySQLManager getInstance() {
        if(mySQLManager == null) {
            mySQLManager = new MySQLManager();
        }
        return mySQLManager;
    }

}
