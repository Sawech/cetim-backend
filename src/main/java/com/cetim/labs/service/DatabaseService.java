package com.cetim.labs.service;


import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.cetim.labs.model.DatabaseConfig;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;


@Service
public class DatabaseService {

    /**
     * Checks the database connection using the configured DataSource.
     * 
     */
    public boolean checkconfig() {
        
        File config = new File("src/main/resources/config.properties");
        
        if (config.exists()) {
            return true; 
        } else {
        	return false;
        }
    }

    /**
     * Tests a database connection using the provided DatabaseConfig.
     *
     * @param config The database configuration to test.
     * @return A map containing the connection status and message.
     */
    public Map<String, String> checkConnection(DatabaseConfig config) {
        Map<String, String> response = new HashMap<>();

        if (config == null) {
            response.put("status", "error");
            response.put("message", "Invalid database configuration.");
            return response;
        }

        String jdbcUrl = buildJdbcUrl(config);
        try (Connection connection = DriverManager.getConnection(jdbcUrl, config.getUser(), config.getPassword())) {
            if (connection.isValid(0)) {
                response.put("status", "success");
                response.put("message", "Database connection successful.");
            } else {
                response.put("status", "error");
                response.put("message", "Database connection failed.");
            }
        } catch (SQLException e) {
            response.put("status", "error");
            response.put("message", "Failed to connect to the database: " + e.getMessage());
        }

        return response;
    }
    
    public ResponseEntity<String> saveConfiguration(DatabaseConfig configI) {
 
        String driverClassName = "";
        switch (configI.getDbType().toLowerCase()) {
            case "mysql":
            case "mariadb":
                driverClassName = "com.mysql.cj.jdbc.Driver";
                break;
            case "postgresql":
                driverClassName = "org.postgresql.Driver";
                break;
        }

        String url = buildJdbcUrl(configI);
    
        String config = String.format(
    		"spring.application.name=CETIM\n" +
            "spring.datasource.url=%s\n" +
            "spring.datasource.username=%s\n" +
            "spring.datasource.password=%s\n" +
            "spring.datasource.driver-class-name=%s\n" +
            "spring.profiles.active=db-configured\n" +
            "spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect\n"+  
            "jwt.secret=3244EFFFFESXCBY42126GS3SD3DF6FHT2SQ2\n"+
            "jwt.expiration=86400000\n" +
            "spring.jpa.hibernate.ddl-auto=update\n"+
            "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect\n"+
            "auth.app.jwtCookieName= auth\n"+
            "auth.app.jwtSecret= authSecretKey\n"+
            "auth.app.jwtExpirationMs= 86400000\n",
            url, configI.getUser(), configI.getPassword(), driverClassName
        );

        try {
            Path configPath = Paths.get("src/main/resources/config.properties");
            Files.write(configPath, config.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            return ResponseEntity.ok("Database configured. Please restart the application.");
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Failed to save configuration.");
        }
    }


    private String buildJdbcUrl(DatabaseConfig config) {
        String baseUrl = config.getUrl().trim();
        if (!baseUrl.endsWith("/")) {
            baseUrl += "/";
        }
        return "jdbc:" + config.getDbType() + "://" + baseUrl + config.getDbName();
    }
}