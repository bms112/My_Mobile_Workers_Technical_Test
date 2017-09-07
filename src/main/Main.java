import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Main {

    public static void main (String[] args) {
        Logger logger = Logger.getLogger("ImportLog");
        FileHandler fh;
        try {
            File logFile = new File("log.txt");
            fh = new FileHandler("log.txt");
            logger.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);

        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        logger.info("PROGRAM STARTED");
        //Start the GUI
        new GUIManager();
    }
}