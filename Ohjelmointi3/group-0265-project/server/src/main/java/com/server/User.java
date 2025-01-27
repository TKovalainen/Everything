package com.server;

public class User {
    
    private String userName = null;
    private String password = null;
    private String email = null;
    private String nickname = null;

    public User(){

    }

    public User(String userName, String password, String email, String nickname){
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.nickname = nickname;
    }

    public String getUserName(){
        return this.userName;
    }

    public String getPassword(){
        return this.password;
    }

    public String getEmail(){
        return this.email;
    }
    
    public String getNickname(){
        return this.nickname;
    }

}
