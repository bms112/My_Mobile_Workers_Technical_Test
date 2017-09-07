package CSV;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CSVManager {

    private static CSVManager csvManager;
    private static Logger logger = Logger.getLogger("ImportLog");
    private final static int CSV_COLUMN_NO = 31;
    private final static String CSV_SPLIT_CHAR = ",";

    private CSVManager() {

    }

    //Singleton class getInstance method ensures only one instance of this class is ever initialised and used within the
    //program
    public static CSVManager getInstance() {
        if(csvManager == null) {
            csvManager = new CSVManager();
        }
        return csvManager;
    }

    //This method validates the CSV File to mitigate the potential errors that could happen whilst importing the File
    //to the database
    public boolean validateCSVFile(String path) {
        if(!path.endsWith(".csv")) {
            return false;
        }
        int count = 2;
        for(String line : readCSVFile(path)) {
            if(line.split(CSV_SPLIT_CHAR).length != CSV_COLUMN_NO) {
                logger.log(Level.SEVERE, "Validation of CSV File failed at line " + count + "due to invalid " +
                        "number of columns", line);
                return false;
            }
            count++;
        }
        return true;
    }

    //This method simply reads the CSV File line by line and returns a List of the Lines
    private List<String> readCSVFile(String path) {
        String line;
        List<String> lines = new ArrayList<>();
        try(BufferedReader br = new BufferedReader(new FileReader(path))) {
            br.readLine();
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "Exception was thrown whilst reading the CSV File", ex);
        }
        return lines;
    }
}
