package com.boot.service;

import com.boot.pojo.loginLog;

import java.util.List;

/**
 * @author 游政杰
 */
public interface LoginLogService {


    void insertLoginLog(loginLog loginLog);

    List<loginLog> selectLoginLogAll();

    int loginLogCount();
}
