# My_Mobile_Workers_Technical_Test
Developed using IntelliJ IDEA Community 2017.2.2

Prerequisites to running the program:
- Must have a running MySQL Server Service on a port within localhost (This can be downloaded and set up from the SQL Installer https://dev.mysql.com/downloads/installer/).
- You must know which port the MySQL Server is running on.
- You must know the root username and password for the MySQL Server you are running.

To complete the given task:
- The jobdata.csv file can be found in src/resources

External Libraries Required:
- JDK (1.8.0_144)
- JUnit4
- mysql-connector-java-5.1.44-bin

To Build the code, import the project into IntelliJ and ensure that all of the External Libraries have been added. 
A copy of the mysql-connector Library can be found in the src/resources folder.
The project can then be built and started by running the main method within the Main class.

Alternatively, you could create an artifact to store the project in a jar file.
This has been done in advance and the jar file can be found in out/artifact/My_Mobile_Workers_Technical_Test_jar.
Simply double-clicking the My_Mobile_Workers_Technical_Test.jar file will start the program and this program can be moved to other directories on your system and still work as intended.

Example GUI input for given task:
- MySQL Port Number - 3306
- MySQL Database Name - jobsdb
- MySQL Table Name - jobs
- MySQL Username - root
- MySQL Password - root
- Path to CSV File - C:/Users/Ben/IdeaProjects/My_Mobile_Workers_Technical_Test/src/resources/jobdata.csv
- Is Selected CSV File jobdata.csv - selected = true
- Export File Name (.csv) - Export

The GUI is split into 3 sections the MySQL Parameters are required fields for both the Import and Export functions.
The Import and Export sections do not rely on each other's parameters and therefore aren't required when using the other function.