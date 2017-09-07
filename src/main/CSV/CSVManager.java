package CSV;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

    public List<String> readCSVFile() {
        String line;
        String splitChar = ",";
        List<String> lines = new ArrayList<>();
        try(BufferedReader br = new BufferedReader(new FileReader(path))) {
            line = br.readLine();
            if(checkColumnNames(line)) {
                while ((line = br.readLine()) != null) {
                    lines.add(line);
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return lines;
    }

    private boolean checkColumnNames(String line) {
        return true;
    }
}
