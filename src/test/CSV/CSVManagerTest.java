package CSV;

import org.junit.Test;

import static org.junit.Assert.*;

public class CSVManagerTest {
    @Test
    public void getInstance() throws Exception {
        CSVManager.getInstance();
    }

    @Test
    public void validateCSVFile() throws Exception {
        CSVManager csvManager = CSVManager.getInstance();
        csvManager.validateCSVFile("");
    }

}