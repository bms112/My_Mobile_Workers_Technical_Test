import CSV.CSVManager;
import MySQL.MySQLManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Logger;

public class GUIManager extends JFrame implements ActionListener {
    private static Logger logger = Logger.getLogger("ImportLog");

    private JLabel mySqlLabel = new JLabel("MySQL Parameters");
    private JLabel mySqlPortLabel = new JLabel("MySQL Port Number");
    private JTextField mySqlPortField = new JTextField();

    private JLabel mySqlDBLabel = new JLabel("MySQL Database Name");
    private JTextField mySqlDBField = new JTextField();
    private JLabel mySqlTableLabel = new JLabel("MySQL Table Name");
    private JTextField mySqlTableField = new JTextField();
    private JLabel mySqlUserLabel = new JLabel("MySQL Username");
    private JTextField mySqlUserField = new JTextField();
    private JLabel mySqlPassLabel = new JLabel("MySQL Password");
    private JPasswordField mySqlPassField = new JPasswordField();

    private JLabel importLabel = new JLabel("Import Function");

    private JLabel csvPathLabel = new JLabel("Path to CSV File");
    private JTextField csvPathField = new JTextField();
    private JButton csvPathButton = new JButton("Browse...");

    private JLabel csvIsJobDataLabel = new JLabel("Is Selected CSV File jobdata.csv?");
    private JCheckBox csvIsJobData = new JCheckBox();
    private JButton importButton = new JButton("Import");

    private JLabel exportLabel = new JLabel("Export Function");

    private JLabel exportFileNameLabel = new JLabel("Export File Name (.csv)");
    private JTextField exportFileNameField = new JTextField();
    private JButton exportButton = new JButton("Export");

    GUIManager() {
        init();
    }

    private void init() {
        setDefaultLookAndFeelDecorated(true);
        setTitle("MySQL CSV Import/Export Java Utility");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);

        JPanel panel = new JPanel();
        add(panel);
        setContentPane(panel);
        setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        setHeading(mySqlLabel);
        panel.add(mySqlLabel);
        panel.add(mySqlPortLabel);
        panel.add(mySqlPortField);
        panel.add(mySqlDBLabel);
        panel.add(mySqlDBField);
        panel.add(mySqlTableLabel);
        panel.add(mySqlTableField);
        panel.add(mySqlUserLabel);
        panel.add(mySqlUserField);
        panel.add(mySqlPassLabel);
        panel.add(mySqlPassField);
        panel.add(new JSeparator(JSeparator.HORIZONTAL));
        setHeading(importLabel);
        panel.add(importLabel);
        panel.add(csvPathLabel);
        panel.add(csvPathField);
        panel.add(csvPathButton);
        panel.add(csvIsJobDataLabel);
        panel.add(csvIsJobData);
        panel.add(importButton);
        panel.add(new JSeparator(JSeparator.HORIZONTAL));
        setHeading(exportLabel);
        panel.add(exportLabel);
        panel.add(exportFileNameLabel);
        panel.add(exportFileNameField);
        panel.add(exportButton);

        setFieldSize(mySqlDBField);
        setFieldSize(mySqlPortField);
        setFieldSize(mySqlTableField);
        setFieldSize(mySqlUserField);
        setFieldSize(mySqlPassField);
        setFieldSize(csvPathLabel);
        setFieldSize(csvPathField);
        setFieldSize(exportFileNameField);

        csvIsJobData.setSelected(true);
        csvPathButton.addActionListener(this);
        importButton.addActionListener(this);
        exportButton.addActionListener(this);
        pack();
    }

    private void setHeading(JLabel labelField) {
        labelField.setFont(new Font("Tahoma", Font.BOLD, 20));
        labelField.setForeground(Color.BLUE);
    }

    //To reduce duplicate code in init() the setFieldSize method has been Overloaded to deal with the sizes of most of
    //the Components within the GUI
    private void setFieldSize(JLabel labelField) {
        labelField.setAlignmentX(Component.LEFT_ALIGNMENT);
        labelField.setSize(500, 25);
        labelField.setPreferredSize(new Dimension(500, 25));
        labelField.setMinimumSize(labelField.getPreferredSize());
        labelField.setMaximumSize(labelField.getPreferredSize());
    }

    private void setFieldSize(JTextField textField) {
        textField.setAlignmentX(Component.LEFT_ALIGNMENT);
        textField.setSize(500, 25);
        textField.setPreferredSize(new Dimension(500, 25));
        textField.setMinimumSize(textField.getPreferredSize());
        textField.setMaximumSize(textField.getPreferredSize());
    }

    //This method is required, as this class implements ActionListener
    //This method is called whenever a button assigned this actionlistener is clicked, therefore it is important
    //to use e.getSource() to determine which button was clicked before deciding what code to run
    @Override
    public void actionPerformed(ActionEvent e) {
        //if the Browse button was clicked
        if(e.getSource() == csvPathButton) {
            //Open up the File Chooser
            JFileChooser fc = new JFileChooser();
            int option = fc.showDialog(this, "Open");
            //If the Open button is clicked retrieve the path and set it to the csvPathField
            //If the Cancel button is clicked, the File Chooser will close
            if (option == JFileChooser.APPROVE_OPTION) {
                String path = fc.getSelectedFile().getPath();
                path = path.replaceAll("\\\\", "/");
                csvPathField.setText(path);
            }
        } else if(e.getSource() == importButton) {
            //If the Import button was clicked get the MySQLManager and run the import method with all of the data
            //the user has input into the GUI
            CSVManager csvManager = CSVManager.getInstance();
            String[] headers = csvManager.validateCSVFile(csvPathField.getText());
            if(headers != null) {
                MySQLManager mySQLManager = MySQLManager.getInstance();
                boolean result = mySQLManager.importCSVFileToDatabase(csvPathField.getText(), mySqlPortField.getText(),
                        mySqlDBField.getText(), mySqlTableField.getText(), mySqlUserField.getText(), mySqlPassField.getText(),
                        headers, csvIsJobData.isSelected());
                if (result) {
                    logger.info("Import Process Ended Successfully");
                    //If the import was ran without any exceptions
                    JOptionPane.showMessageDialog(this, "CSV File Successfully imported into the database!",
                            "Import Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    //If an exception occurred
                    JOptionPane.showMessageDialog(this, "CSV File Failed to import into the database.\n" +
                                    "Please see the log file for more information.",
                            "Import Failed", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                //If the CSV file validation failed
                JOptionPane.showMessageDialog(this, "CSV File Path is invalid.\nPlease enter a valid" +
                                "path to a CSV File that contains valid data",
                        "CSV File Path Invalid", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            //If the Export button was clicked get the MySQLManager and run the export method with all of the data
            //the user has input into the GUI
            MySQLManager mySQLManager = MySQLManager.getInstance();
            String filename = exportFileNameField.getText() + ".csv";
            boolean result = mySQLManager.exportTableToCSVFile(filename, mySqlPortField.getText(), mySqlDBField.getText(),
                    mySqlTableField.getText(), mySqlUserField.getText(), mySqlPassField.getText());
            if(result) {
                logger.info("Export Process Ended Successfully");
                //If the export was ran without any exceptions
                JOptionPane.showMessageDialog(this, "Database Table Successfully exported" +
                                " into the CSV File!",
                        "Export Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                //If an exception occurred
                JOptionPane.showMessageDialog(this, "Database Table failed to Export into CSV File.\n" +
                                "Please see the log file for more information.",
                        "Export Failed", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
