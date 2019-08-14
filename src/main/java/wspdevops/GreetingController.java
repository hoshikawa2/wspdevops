package wspdevops;

import java.util.concurrent.atomic.AtomicLong;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import wspdevops.*;

import java.io.File;
// ATP Library - 2019-08-07
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import oracle.jdbc.pool.OracleDataSource;
import oracle.jdbc.OracleConnection;
import java.sql.DatabaseMetaData;

@RestController
public class GreetingController {

    public static String msg = System.getenv("MSG");
     
    //final static String DB_URL="jdbc:oracle:thin:@atp_medium?TNS_ADMIN=/src/main/resources/wallet_atp"; 
    //DB_URL= "jdbc:oracle:thin:@adb.us-ashburn-1.oraclecloud.com:1522/raqdkqsvxzgrlb9_atp_high.atp.oraclecloud.com:1521/atp";
    // For ATP and ADW - use the TNS Alias name along with the TNS_ADMIN when using 18.3 JDBC driver
    final static String DB_URL="jdbc:oracle:thin:@atp_tpurgent?TNS_ADMIN=/pipeline/source/target/classes/wallet_atp";
    // In case of windows, use the following URL 
    // final static String DB_URL="jdbc:oracle:thin:@wallet_dbname?TNS_ADMIN=C:\\Users\\test\\wallet_dbname";
    final static String DB_USER = "admin";
  final static String DB_PASSWORD = "Oracle123456";
  
    public String template = msg + ", %s!";
    private final AtomicLong counter = new AtomicLong();
    
    @RequestMapping("/greeting")
    public Greeting greeting(@RequestParam(value="name", defaultValue="World") String name) {   

    	if(true)
    	{

    		System.out.println("Working Directory = " +
    	              System.getProperty("user.dir"));
            
    		//startATP();    		
    	}

    	if(true)
    	{
            new BusyThread().start();
    	}
    	
    	return new Greeting(counter.incrementAndGet(),
                            String.format(template, name));
    }
    
    
    //Main Process
    public static void startATP()
    {
        Properties info = new Properties();     
        info.put(OracleConnection.CONNECTION_PROPERTY_USER_NAME, DB_USER);
        info.put(OracleConnection.CONNECTION_PROPERTY_PASSWORD, DB_PASSWORD); 
        info.put(OracleConnection.CONNECTION_PROPERTY_DEFAULT_ROW_PREFETCH, "20");    
        
        //Call to ATP
        try {
            OracleDataSource ods = new OracleDataSource();

            if(true)
            {
                ods.setURL(DB_URL);    
                ods.setConnectionProperties(info);

                // With AutoCloseable, the connection is closed automatically.
                try (OracleConnection connection = (OracleConnection) ods.getConnection()) {
                  // Get the JDBC driver name and version 
                  DatabaseMetaData dbmd = connection.getMetaData();       
                  //System.out.println("Driver Name: " + dbmd.getDriverName());
                  //System.out.println("Driver Version: " + dbmd.getDriverVersion());
                  // Print some connection properties
                  //System.out.println("Default Row Prefetch Value is: " + 
                  //   connection.getDefaultRowPrefetch());
                  //System.out.println("Database Username is: " + connection.getUserName());
                  //System.out.println();
                  // Perform a database operation 
                  printEmployees(connection);
                  //createEmployees(connection);
                }     	        	
            	
            }
        } catch (Exception e) {
			// TODO: handle exception
        	System.out.println(e.getMessage());
		}
    }
    
    
    //ATP SOURCE-CODE
    public static void printEmployees(Connection connection) throws SQLException {
        // Statement and ResultSet are AutoCloseable and closed automatically. 
        try (Statement statement = connection.createStatement()) {      
          try (ResultSet resultSet = statement
              .executeQuery("select first_name, last_name from employees")) {
            System.out.println("FIRST_NAME" + "  " + "LAST_NAME");
            System.out.println("---------------------");
            while (resultSet.next())
              System.out.println(resultSet.getString(1) + " "
                  + resultSet.getString(2) + " ");       
          }
        }   
    }
    
    public static void createEmployees(Connection connection) throws SQLException {
        // Statement and ResultSet are AutoCloseable and closed automatically. 
        try (Statement statement = connection.createStatement()) {      
          try (ResultSet resultSet = statement
              .executeQuery("create table employees (first_name varchar(255), last_name varchar(255))")) {
            System.out.println("table employees created");
            System.out.println("---------------------");
          }
        }   
    }
    
    //THREAD SOURCE-CODE
    private static class BusyThread extends Thread {

        public BusyThread() {
        }

        /**
         * Generates the load when run
         */
        @Override
        public void run() {
            try {
                // Loop for the given duration
                while (true) {
                    // call ATP
                	startATP();

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
}
