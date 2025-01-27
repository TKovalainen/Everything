package com.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.stream.Collectors;

import com.sun.net.httpserver.HttpHandler;

import org.json.JSONException;
import org.json.JSONObject;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

public class RegistrationHandler implements HttpHandler{

    private final UserAuthenticator user;
   

    public RegistrationHandler(UserAuthenticator user){
        this.user = user;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        Headers headers = exchange.getRequestHeaders();
        String contentType = "";
        final int codeOk = 200;
        final int codeBadRequest = 400;
        final int internalErrorCode = 500;
        final int forbiddenCode = 403;
        JSONObject object = null;
        String username = "";
        String password = "";
        String email = "";
        String userNickname = "";

        if (exchange.getRequestMethod().equalsIgnoreCase("POST")) {

            try{
                contentType = headers.getFirst("Content-Type");
                if(contentType.equalsIgnoreCase("application/json")){
                    contentType = headers.get("Content-Type").get(0);
                }else{
                    sendResponse(exchange, codeBadRequest, "No content type available");
                    return;
                }
                if(contentType.equalsIgnoreCase("application/json")){

                    InputStream stream = exchange.getRequestBody();

                    String newUser = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8)).lines().collect(Collectors.joining("\n"));


                    if(newUser == null || newUser.length() == 0){
                        sendResponse(exchange, codeBadRequest, "Invalid user");
                        return;
                    }else{
                        try{
                            object = new JSONObject(newUser);

                        }catch (JSONException e){
                            sendResponse(exchange, internalErrorCode, "Internal server error");
                            return;
                        }
                        if (object.getString("username").length() == 0 || object.getString("password").length() == 0 || object.getString("email").length() == 0 || object.getString("userNickname").length() == 0){
                            sendResponse(exchange, codeBadRequest, "Invalid credentials");
                        }else{
                            username = object.getString("username");
                            password = object.getString("password");
                            email = object.getString("email");
                            userNickname = object.getString("userNickname");
                            Boolean result = user.addUser(username, password, email, userNickname);
                            if(result == false){
                                sendResponse(exchange, codeBadRequest, "User already registered");
                                return;
                            }
                            sendResponse(exchange, codeOk, "User registered successfully");
                            stream.close();
                        }
                    }                    

            } 
        }catch(Exception e){
            sendResponse(exchange, internalErrorCode, "Internal server error");
            return;
        }  

    }

    if (exchange.getRequestMethod().equalsIgnoreCase("GET")) {
            sendResponse(exchange, forbiddenCode, "Not supported");          
        }
    }

    public void sendResponse(HttpExchange exchange, int code, String message) throws IOException{
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(code, message.getBytes(StandardCharsets.UTF_8).length);
        OutputStream output = exchange.getResponseBody();
        output.write(message.getBytes());
        output.flush();
        output.close();
    }
    
}
