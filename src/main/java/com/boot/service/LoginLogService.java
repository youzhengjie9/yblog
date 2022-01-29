package com.boot.service;

import com.boot.pojo.LoginLog;

import java.util.List;

/**
 * @author 游政杰
 */
public interface LoginLogService {


    void insertLoginLog(LoginLog loginLog);

    List<LoginLog> selectLoginLogAll();

    int loginLogCount();
}
