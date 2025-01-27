package com.server;

import java.io.File;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

import org.apache.commons.codec.digest.Crypt;
import org.json.JSONObject;

public class MessageDb{

    private Connection dbConnection = null;
    private static MessageDb dbInstance = null;
    private SecureRandom secureRandom = new SecureRandom();

	public static synchronized MessageDb getInstance() {
		if (null == dbInstance) {
			dbInstance = new MessageDb();
		}
        return dbInstance;
    }

    private MessageDb(){
        try {
            initialize();
        } catch (SQLException e) {

        }
    }

    public void open(String dbName) throws SQLException {
        boolean databaseExists = new File(dbName).exists();
        if(databaseExists){
            String connectionString = "jdbc:sqlite:"+dbName;
            dbConnection = DriverManager.getConnection(connectionString);            
        }
        else{
            initialize();
        }
    }

    private boolean initialize() throws SQLException {
        String dbName = "Database";
        String database = "jdbc:sqlite:" + dbName;
        dbConnection = DriverManager.getConnection(database);

        if (null != dbConnection) {
            String createUserTable = "create table users (username varchar(50) NOT NULL PRIMARY KEY, password varchar(50) NOT NULL, email varchar(50) NOT NULL, userNickname varchar(50) NOT NULL)";
            String createMessageTable = 
            "create table messages (locationName varchar(50) NOT NULL PRIMARY KEY, locationDescription varchar(50) NOT NULL, locationCity varchar(50) NOT NULL, locationCountry varchar(50) NOT NULL, locationStreetAddress varchar(50) NOT NULL, originalPoster varchar(50) NOT NULL, originalPostingTime INTEGER NOT NULL, latitude DOUBLE, longitude DOUBLE)";
            Statement createStatement = dbConnection.createStatement();
            createStatement.executeUpdate(createUserTable);
            createStatement.executeUpdate(createMessageTable);
            createStatement.close();
        return true;
        }
        return false;
        }

    public void closeDB() throws SQLException {
		if (null != dbConnection) {
			dbConnection.close();
            System.out.println("closing db connection");
			dbConnection = null;
		}
    }

    public boolean setUser(JSONObject user) throws SQLException {
        if (checkIfUserExists(user.getString("username"))) {
            return false;
        }
        byte bytes[] = new byte[13];
        secureRandom.nextBytes(bytes);
        String saltBytes = new String(Base64.getEncoder().encode(bytes));
        String salt = "$6$" + saltBytes;
        String hashedPassword = Crypt.crypt(user.getString("password"), salt);
        String setUserString = "INSERT INTO users " + "VALUES('" + user.getString("username") + "','" + hashedPassword + "','" + user.getString("email") + "','"+ user.getString("userNickname")+"')";
        Statement createStatement = dbConnection.createStatement();
        createStatement.executeUpdate(setUserString);
        createStatement.close();
        return true;
    }

    public boolean checkIfUserExists(String givenUserName) throws SQLException{
        Statement queryStatement = null;
        ResultSet rs;
        String checkUser = "select username from users where username = '" + givenUserName + "'";
        queryStatement = dbConnection.createStatement();
		rs = queryStatement.executeQuery(checkUser);
        
        if(rs.next()){
            return true;
        }else{
            return false;
        }
    }
    
    public boolean authenticateUser(String givenUserName, String givenPassword) throws SQLException {

        Statement queryStatement = null;
        ResultSet rs;

        String getMessagesString = "select username, password from users where username = '" + givenUserName + "'";
        queryStatement = dbConnection.createStatement();
		rs = queryStatement.executeQuery(getMessagesString);

        if (rs.next() == false) {
            return false;
        }else{
            String hashedPasswd = rs.getString("password");
            givenPassword = Crypt.crypt(givenPassword, hashedPasswd);
            if (hashedPasswd.equals(givenPassword)) {
                return true;
            } else {
                return false;
            }
        }

    }

    public void setMessage(String locationName, String locationDescription, String locationCity, String locationCountry, String locationStreetAddress, String originalPoster, ZonedDateTime postingTime, double latitude, double longitude) throws SQLException {
        UserMessage message = new UserMessage(locationName, locationDescription, locationCity, locationCountry, locationStreetAddress, originalPoster, postingTime, latitude, longitude);
        String username = message.getOriginalPoster();
        String getUsernickname = "SELECT userNickname FROM users WHERE username = '"+username+"'";
        Statement query = dbConnection.createStatement();
        ResultSet results = query.executeQuery(getUsernickname);
        while(results.next()){
            message.setOriginalPoster(results.getString("userNickname"));
        }
		String setMessageString = "INSERT or REPLACE into messages " + 
        "VALUES('"+ message.getLocationName() +"','"+ message.getLocationDescription() +"','"+ message.getLocationCity() +"','"+message.getLocationCountry()+"','"+message.getLocationStreetAddress()+"','"+message.getOriginalPoster()+"','"+message.dateAsInt()+"','"+message.getLatitude()+"','"+message.getLongitude()+"')";
		Statement createStatement = dbConnection.createStatement();
		createStatement.executeUpdate(setMessageString);
		createStatement.close();

    }

    public JSONObject getMessages() throws SQLException {

        JSONObject message = null;
        String getMessages = "SELECT * FROM messages";
        Statement query = dbConnection.createStatement();
        ResultSet results = query.executeQuery(getMessages);
        
        while(results.next()){
            message = new JSONObject();
            message.put("locationName", results.getString("locationName"));
            message.put("locationDescription", results.getString("locationDescription"));
            message.put("locationCity", results.getString("locationCity"));
            message.put("locationCountry", results.getString("locationCountry"));
            message.put("locationStreetAddress", results.getString("locationStreetAddress"));
            message.put("originalPoster", results.getString("originalPoster"));                
            ZonedDateTime time = getSentDate(results.getLong("originalPostingTime"));
            String timestamp = time.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
            message.put("originalPostingTime", timestamp);
            if(results.getDouble("latitude") > 0.0){
                message.put("latitude", results.getDouble("latitude"));
            }
            if(results.getDouble("longitude") > 0.0){
                message.put("longitude", results.getDouble("longitude"));
            }

        }
        return message;

    }

    public ZonedDateTime getSentDate(long epoch) {
        return ZonedDateTime.ofInstant(Instant.ofEpochMilli(epoch), ZoneOffset.UTC);
    }

}