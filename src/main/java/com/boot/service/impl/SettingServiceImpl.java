package com.boot.service.impl;

import com.boot.dao.SettingMapper;
import com.boot.pojo.Setting;
import com.boot.service.SettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SettingServiceImpl implements SettingService {

    @Autowired
    private SettingMapper settingMapper;

    @Override
    public Setting selectUserSetting(String name) {
        return settingMapper.selectUserSetting(name);
    }

    @Override
    public void addSettingByUser(Setting setting) {
        settingMapper.addSettingByUser(setting);
    }

    @Override
    public void changeSettingByUser(Setting setting) {
        settingMapper.changeSettingByUser(setting);
    }

    @Override
    public void deleteSettingByUser(String name) {
        settingMapper.deleteSettingByUser(name);
    }
}