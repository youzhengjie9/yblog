package com.boot.service;

import com.boot.pojo.Setting;

public interface SettingService {


    Setting selectUserSetting(String name);

    void addSettingByUser(Setting setting);

    void changeSettingByUser(Setting setting);

    void deleteSettingByUser(String name);
}
