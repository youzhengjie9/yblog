package com.boot.service;

import com.boot.pojo.setting;
import org.apache.ibatis.annotations.Param;

public interface settingService {


    setting selectUserSetting(String name);

    void addSettingByUser(setting setting);

    void changeSettingByUser(setting setting);

    void deleteSettingByUser(String name);
}
