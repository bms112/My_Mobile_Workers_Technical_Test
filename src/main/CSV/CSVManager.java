package CSV;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class CSVManager {

    private static CSVManager csvManager;
    private String path;

    private CSVManager() {
        path = "C:\\Users\\Ben\\IdeaProjects\\My_Mobile_Workers_Technical_Test\\src\\resources\\jobdata.csv";
    }

    public static CSVManager getInstance() {
        if(csvManager == null) {
            csvManager = new CSVManager();
        }
        return csvManager;
    }

    public void readCSVFile() {
        String line;
        String splitChar = ",";
        try(BufferedReader br = new BufferedReader(new FileReader(path))) {
            line = br.readLine();
            if(checkColumnNames(line)) {
                while ((line = br.readLine()) != null) {
                    System.out.println(line);
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private boolean checkColumnNames(String line) {
        return true;
    }
}
