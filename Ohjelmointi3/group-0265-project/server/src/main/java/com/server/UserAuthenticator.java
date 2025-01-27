package com.server;


import java.sql.SQLException;

import org.json.JSONException;
import org.json.JSONObject;

import com.sun.net.httpserver.BasicAuthenticator;

public class UserAuthenticator extends BasicAuthenticator {

    private MessageDb db = null;

    public UserAuthenticator() {
        super("info");
        db = MessageDb.getInstance();
    }

    @Override
    public boolean checkCredentials(String username, String password) {

        System.out.println("checking user: " + username + " " + password + "\n");

        boolean isValidUser;
        try {
            isValidUser = db.authenticateUser(username, password);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        return isValidUser;
    }

    public boolean addUser(String userName, String password, String email, String userNickname) throws JSONException, SQLException {
        
        boolean result = db.setUser(new JSONObject().put("username", userName).put("password", password).put("email", email).put("userNickname", userNickname));
        if(!result){
            return false;
        }            
        return true;

    }

}
