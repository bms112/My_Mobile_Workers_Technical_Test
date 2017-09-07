import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Main {

    public static void main (String[] args) {
        //This code is to initialise the Logger that will be used mainly to record all exceptions that occur.
        Logger logger = Logger.getLogger("ImportLog");
        FileHandler fh;
        try {
            //This ensures that the log file is cleared from the last run and assigns the file to the logger via a
            //FileHandler
            new File("log.txt");
            fh = new FileHandler("log.txt");
            logger.addHandler(fh);
            fh.setFormatter(new SimpleFormatter());
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