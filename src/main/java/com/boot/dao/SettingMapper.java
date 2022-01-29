package com.boot.dao;

import com.boot.pojo.Setting;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface SettingMapper {

    Setting selectUserSetting(@Param("name") String name);

    void addSettingByUser(Setting setting);

    void changeSettingByUser(Setting setting);

    void deleteSettingByUser(@Param("name") String name);


}
