package com.boot.dao;

import com.boot.pojo.setting;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface settingMapper {

    setting selectUserSetting(@Param("name") String name);

    void addSettingByUser(setting setting);

    void changeSettingByUser(setting setting);

    void deleteSettingByUser(@Param("name") String name);


}
