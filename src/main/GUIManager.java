import MySQL.MySQLManager;

import javax.swing.*;
import javax.swing.plaf.FileChooserUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUIManager extends JFrame implements ActionListener {

    private JLabel csvPathLabel = new JLabel("Path to CSV File", SwingConstants.LEFT);
    private JTextField csvPathField = new JTextField("C:/Users/Ben/IdeaProjects/My_Mobile_Workers_Technical_Test/src/resources/jobdata.csv");
    private JButton csvPathButton = new JButton("Browse...");

    private JLabel mySqlPortLabel = new JLabel("MySQL Port Number");
    private JTextField mySqlPortField = new JTextField("8080");

    private JLabel mySqlDBLabel = new JLabel("MySQL Database Name");
    private JTextField mySqlDBField = new JTextField("jobsdb");
    private JLabel mySqlTableLabel = new JLabel("MySQL Table Name");
    private JTextField mySqlTableField = new JTextField("jobs");
    private JLabel mySqlUserLabel = new JLabel("MySQL Username");
    private JTextField mySqlUserField = new JTextField("root");
    private JLabel mySqlPassLabel = new JLabel("MySQL Password");
    private JPasswordField mySqlPassField = new JPasswordField("G0ldf1sh");

    GUIManager() {
        init();
    }

    private void init() {
        setDefaultLookAndFeelDecorated(true);
        setTitle("Job Import Java Utility");
        //setSize(1000, 500);
        //setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);

        JPanel panel = new JPanel();
        add(panel);
        setContentPane(panel);
        setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        JButton importButton = new JButton("Import");
        setFieldSize(csvPathLabel);
        panel.add(csvPathLabel);
        setFieldSize(csvPathField);
        panel.add(csvPathField);
        panel.add(csvPathButton);
        panel.add(mySqlPortLabel);
        panel.add(mySqlPortField);
        setFieldSize(mySqlPortField);
        panel.add(mySqlDBLabel);
        panel.add(mySqlDBField);
        setFieldSize(mySqlDBField);
        panel.add(mySqlTableLabel);
        panel.add(mySqlTableField);
        setFieldSize(mySqlTableField);
        panel.add(mySqlUserLabel);
        panel.add(mySqlUserField);
        setFieldSize(mySqlUserField);
        panel.add(mySqlPassLabel);
        panel.add(mySqlPassField);
        setFieldSize(mySqlPassField);
        panel.add(importButton);
        csvPathButton.addActionListener(this);
        importButton.addActionListener(this);
        pack();
    }

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

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == csvPathButton) {
            JFileChooser fc = new JFileChooser();
            int option = fc.showDialog(this, "Open");
            if (option == JFileChooser.APPROVE_OPTION) {
                String path = fc.getSelectedFile().getPath();
                path = path.replaceAll("\\\\", "/");
                csvPathField.setText(path);
            }
        } else {
            MySQLManager mySQLManager = MySQLManager.getInstance();
            boolean result = mySQLManager.importCSVFileToDatabase(csvPathField.getText(), mySqlPortField.getText(),
                    mySqlDBField.getText(), mySqlTableField.getText(), mySqlUserField.getText(), mySqlPassField.getText());
            if (result) {
                JOptionPane.showMessageDialog(this, "CSV File Successfully imported into the database",
                        "Import Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "CSV File Failed to import into the database",
                        "Import Failed", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
