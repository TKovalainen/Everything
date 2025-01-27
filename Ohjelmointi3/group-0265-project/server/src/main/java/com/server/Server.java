package com.server;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.sql.SQLException;
import java.time.DateTimeException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.TrustManagerFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.sun.net.httpserver.*;
/**
 * Hello world!
 *aaaaaa
 */
public class Server implements HttpHandler {
    
    StringBuilder textDump = new StringBuilder("");
    ArrayList<String> array = new ArrayList<String>();
    ArrayList<UserMessage> messages = new ArrayList<UserMessage>();
    MessageDb db = null;

    private Server(){
        db = MessageDb.getInstance();
    }
    
    public static void main( String[] args ) throws Exception{
    
        try{

            HttpsServer server = HttpsServer.create(new InetSocketAddress(8001), 0);
            UserAuthenticator users = new UserAuthenticator();
            SSLContext sslContext = myServerSSLContext();
            server.setHttpsConfigurator (new HttpsConfigurator(sslContext) {
                public void configure (HttpsParameters params) {
                InetSocketAddress remote = params.getClientAddress();
                SSLContext c = getSSLContext();
                SSLParameters sslparams = c.getDefaultSSLParameters();
                params.setSSLParameters(sslparams);
                }
            });
            server.createContext("/registration", new RegistrationHandler(users));
            HttpContext context = server.createContext("/info", new Server());
            context.setAuthenticator(users);
            server.setExecutor(Executors.newCachedThreadPool());
            server.start();
        }catch (FileNotFoundException e){
            System.out.println("Certificate not found!");
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        String requestParamValue = null;

        if(exchange.getRequestMethod().equalsIgnoreCase("POST")){
            handleResponsePOST(exchange);
            
        }else if(exchange.getRequestMethod().equalsIgnoreCase("GET")){
            handleResponseGET(exchange, requestParamValue);

        }else{
            handleResponse(exchange, " only GET and POST supported.");
        }
    }

    private void handleResponsePOST(HttpExchange exchange) throws IOException{

        Headers headers = exchange.getRequestHeaders();
        String contentType = "";
        int code = 200;
        String response = "";
        JSONObject object = null;
        OutputStream output = exchange.getResponseBody();
        StringBuilder builder = new StringBuilder();

        try{
            if(headers.containsKey("Content-Type")){
                contentType = headers.get("Content-Type").get(0);
            }else{
                builder.setLength(0);
                builder.append("No content type available");
                String htmlResponse = builder.toString();
                byte[] bytes = htmlResponse.getBytes(StandardCharsets.UTF_8);
                exchange.sendResponseHeaders(400, bytes.length);
                output.write(bytes);
            }
            if(contentType.equalsIgnoreCase("application/json")){

                InputStream stream = exchange.getRequestBody();
                UserMessage message = null;
                String newMessage = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8)).lines().collect(Collectors.joining("\n"));

                stream.close();

                if(newMessage == null || newMessage.length() == 0){
                    code = 412;
                    response = "Faulty message";
                }else{
                    try{
                        object = new JSONObject(newMessage);

                    }catch (JSONException e){
                        builder.setLength(0);
                        builder.append("Faulty Json");
                        String htmlResponse = builder.toString();
                        byte[] bytes = htmlResponse.getBytes(StandardCharsets.UTF_8);
                        exchange.sendResponseHeaders(500, bytes.length);
                        output.write(bytes);
                    }
                    if (object.getString("locationCity").length() == 0 || object.getString("locationDescription").length() == 0 ||
                        object.getString("locationName").length() == 0 || object.getString("locationStreetAddress").length() == 0 ||
                        object.getString("locationCountry").length() == 0){
                        code = 413;
                        response = "Faulty message";
                    }else{
                        Double latitude = 0.0;
                        Double longitude = 0.0;
                        String locationName = object.getString("locationName");
                        String locationDescription = object.getString("locationDescription");
                        String locationCity = object.getString("locationCity");
                        if(object.has("latitude")){
                            latitude = object.getDouble("latitude");                            
                        }
                        if(object.has("longitude")){
                            longitude = object.getDouble("longitude");
                        }
                        String locationStreetAddress = object.getString("locationStreetAddress");
                        String locationCountry = object.getString("locationCountry");
                        String originalPoster = exchange.getPrincipal().getUsername();
                        ZonedDateTime postingTime = null;
                        
                        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX");

                        try{
                            postingTime = ZonedDateTime.parse(object.getString("originalPostingTime"), format);
                        }catch (DateTimeException e){
                            code = 400;
                            response = "invalid time format";
                        }
                        if(locationName != null || locationDescription != null || locationCity != null || postingTime != null){

                            try{
                                db.setMessage(locationName, locationDescription, locationCity, locationCountry, locationStreetAddress, originalPoster, postingTime, latitude, longitude);
                            }catch(SQLException e){
                                e.printStackTrace();
                            }
                            code = 200;
                            response = "message sent";
                        }

                    }
                }                    
                builder.setLength(0);
                builder.append(response);
                String htmlResponse = builder.toString();
                byte[] bytes = htmlResponse.getBytes(StandardCharsets.UTF_8);
                exchange.sendResponseHeaders(code, htmlResponse.length());
                output.write(bytes);

        } 

    }catch(Exception e){
        builder.setLength(0);
        builder.append("Internal server error");
        String htmlResponse = builder.toString();
        byte[] bytes = htmlResponse.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(500, bytes.length);
        output.write(bytes);
    }

    }


    private void handleResponseGET(HttpExchange exchange, String request) throws IOException{

        try{
            
            JSONArray messageArray = new JSONArray();
            messageArray.put(db.getMessages());
            String response = messageArray.toString();
            OutputStream output = exchange.getResponseBody();
            byte[] bytes = response.getBytes("UTF-8");
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, bytes.length);
            output.write(bytes);
            output.flush();
            output.close();

        }catch(SQLException e){
            e.printStackTrace();
        }

    }

    private void handleResponse(HttpExchange exchange, String requestParamValue) throws IOException{

       
        OutputStream outputStream = exchange.getResponseBody();
        StringBuilder htmlBuilder = new StringBuilder();        
        htmlBuilder.append("<html>")
            .append("<body>")
            .append("<h1>")
            .append("Returning payload ")
            .append(requestParamValue)
            .append("</h1>")
            .append("</body>")
            .append("</html>");

        String response = htmlBuilder.toString();
        exchange.sendResponseHeaders(400, response.length());
        outputStream.write(response.getBytes("UTF-8"));
        outputStream.flush();
        outputStream.close();

    }

    private static SSLContext myServerSSLContext() throws Exception{

        char[] passphrase = "salasana123".toCharArray();
        KeyStore ks = KeyStore.getInstance("JKS");
        ks.load(new FileInputStream("C:/Users/tuukk/Ohjelmointi3/group-0265-project/server/src/main/java/com/server/keystore.jks"), passphrase);

        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
        kmf.init(ks, passphrase);
            
        TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
        tmf.init(ks);
            
        SSLContext ssl = SSLContext.getInstance("TLS");
        ssl.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

        return ssl;
    }
}
