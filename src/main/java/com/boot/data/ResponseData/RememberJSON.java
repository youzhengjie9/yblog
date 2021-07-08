package com.boot.data.ResponseData;

public class RememberJSON {

    private String username;
    private String password; //加密后的


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "RememberJSON{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
