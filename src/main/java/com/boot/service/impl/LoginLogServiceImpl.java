package com.boot.service.impl;

import com.boot.dao.LoginLogMapper;
import com.boot.pojo.LoginLog;
import com.boot.service.LoginLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoginLogServiceImpl implements LoginLogService {

    @Autowired
    private LoginLogMapper loginLogMapper;

    @Override
    public void insertLoginLog(LoginLog loginLog) {

        loginLogMapper.insertLoginLog(loginLog);
    }

    @Override
    public List<LoginLog> selectLoginLogAll() {
        return loginLogMapper.selectLoginLogAll();
    }

    @Override
    public int loginLogCount() {
        return loginLogMapper.loginLogCount();
    }
}
