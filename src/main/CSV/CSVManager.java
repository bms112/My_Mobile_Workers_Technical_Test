package CSV;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

    public List<String> readCSVFile(String path) {
        String line;
        List<String> lines = new ArrayList<>();
        try(BufferedReader br = new BufferedReader(new FileReader(path))) {
            br.readLine();
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return lines;
    }
}
