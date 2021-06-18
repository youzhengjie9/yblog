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

    @Override
    public void addSettingByUser(setting setting) {
        settingMapper.addSettingByUser(setting);
    }

    @Override
    public void changeSettingByUser(setting setting) {
        settingMapper.changeSettingByUser(setting);
    }

    @Override
    public void deleteSettingByUser(String name) {
        settingMapper.deleteSettingByUser(name);
    }
}