package wspdevops;

import java.util.concurrent.atomic.AtomicLong;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

// ATP Library - 2019-08-07
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import oracle.jdbc.pool.OracleDataSource;
import oracle.jdbc.OracleConnection;
import java.sql.DatabaseMetaData;

@RestController
public class GreetingController {

    public static String msg = System.getenv("MSG");
        
    public String template = msg + ", %s!";
    private final AtomicLong counter = new AtomicLong();

    @RequestMapping("/greeting")
    public Greeting greeting(@RequestParam(value="name", defaultValue="World") String name) {   
    	startATP();
    	return new Greeting(counter.incrementAndGet(),
                            String.format(template, name));
    }
    
    public static void startATP()
    {
        Properties info = new Properties();     
        info.put(OracleConnection.CONNECTION_PROPERTY_USER_NAME, "");
        info.put(OracleConnection.CONNECTION_PROPERTY_PASSWORD, "");          
        info.put(OracleConnection.CONNECTION_PROPERTY_DEFAULT_ROW_PREFETCH, "20");    
      
        try {
            OracleDataSource ods = new OracleDataSource();
            ods.setURL("");    
            ods.setConnectionProperties(info);

            // With AutoCloseable, the connection is closed automatically.
            try (OracleConnection connection = (OracleConnection) ods.getConnection()) {
              // Get the JDBC driver name and version 
              DatabaseMetaData dbmd = connection.getMetaData();       
              System.out.println("Driver Name: " + dbmd.getDriverName());
              System.out.println("Driver Version: " + dbmd.getDriverVersion());
              // Print some connection properties
              System.out.println("Default Row Prefetch Value is: " + 
                 connection.getDefaultRowPrefetch());
              System.out.println("Database Username is: " + connection.getUserName());
              System.out.println();
              // Perform a database operation 
              printEmployees(connection);
            }     	        	
        } catch (Exception e) {
			// TODO: handle exception
        	System.out.println("ERRO DE BANCO");
		}
    }
    
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
    
    
}
