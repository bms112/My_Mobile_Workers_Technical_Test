package CSV;

public class CSVManager {

    private static CSVManager csvManager;

    private CSVManager() {

    }

    public static CSVManager getInstance() {
        if(csvManager == null) {
            csvManager = new CSVManager();
        }
        return csvManager;
    }
}
