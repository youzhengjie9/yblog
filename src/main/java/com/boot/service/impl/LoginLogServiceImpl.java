package com.boot.service.impl;

import com.boot.dao.loginLogMapper;
import com.boot.pojo.loginLog;
import com.boot.service.LoginLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoginLogServiceImpl implements LoginLogService {

    @Autowired
    private loginLogMapper loginLogMapper;

    @Override
    public void insertLoginLog(loginLog loginLog) {

        loginLogMapper.insertLoginLog(loginLog);
    }

    @Override
    public List<loginLog> selectLoginLogAll() {
        return loginLogMapper.selectLoginLogAll();
    }

    @Override
    public int loginLogCount() {
        return loginLogMapper.loginLogCount();
    }
}
