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
    public String[] validateCSVFile(String path) {
        //This simple validation checks that the desired file is a CSV file
        if(!path.endsWith(".csv")) {
            return null;
        }
        String[] headers = readCSVFileHeader(path);
        int columnNo = headers.length;
        //This for loop reads through each data line of the CSV file (it ignores the header) to determine whether the
        //number of columns it has match the number of columns required for the jobdata database table
        int count = 2;
        for(String line : readCSVFileBody(path)) {
            if(line.split(CSV_SPLIT_CHAR).length != columnNo) {
                logger.log(Level.SEVERE, "Validation of CSV File failed at line " + count + "due to invalid " +
                        "number of columns", line);
                return null;
            }
            count++;
        }
        return headers;
    }

    //This method simply reads the CSV File line by line and returns a List of the Lines while excluding the header
    private String[] readCSVFileHeader(String path) {
        String[] headers = new String[0];
        try(BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line = br.readLine();
            headers = line.split(CSV_SPLIT_CHAR);
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "Exception was thrown whilst reading the CSV File", ex);
        }
        return headers;
    }

    //This method simply reads the CSV File line by line and returns a List of the Lines while excluding the header
    private List<String> readCSVFileBody(String path) {
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
