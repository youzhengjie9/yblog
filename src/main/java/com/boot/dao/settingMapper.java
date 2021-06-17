package com.boot.dao;

import com.boot.pojo.setting;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface settingMapper {

    @Select("select * from t_setting where name=#{name}")
    setting selectUserSetting(@Param("name") String name);





}
