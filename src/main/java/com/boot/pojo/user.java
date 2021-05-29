package com.boot.pojo;

import java.io.Serializable;
import java.sql.Date;

public class user implements Serializable {

    private int id;
    private String username; //用户名
    private String password; //密码
    private String email; //邮箱
    private Date date; //创建日期
    private int valid;  //是否有效

    private userDetail userDetail;

    public com.boot.pojo.userDetail getUserDetail() {
        return userDetail;
    }

    public void setUserDetail(com.boot.pojo.userDetail userDetail) {
        this.userDetail = userDetail;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getValid() {
        return valid;
    }

    public void setValid(int valid) {
        this.valid = valid;
    }


    @Override
    public String toString() {
        return "user{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", date=" + date +
                ", valid=" + valid +
                ", userDetail=" + userDetail +
                '}';
    }
}
