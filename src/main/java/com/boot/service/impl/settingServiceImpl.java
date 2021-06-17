package com.boot.service.impl;

import com.boot.dao.settingMapper;
import com.boot.pojo.setting;
import com.boot.service.settingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class settingServiceImpl implements settingService {

    @Autowired
    private settingMapper settingMapper;

    @Override
    public setting selectUserSetting(String name) {
        return settingMapper.selectUserSetting(name);
    }
}