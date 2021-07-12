package com.boot.dao;

import com.boot.pojo.MenuData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface MenuMapper {

    @Select("select menu from t_menu where authority=#{authority}")
    String selectMenuDataByAuthority(int authority);



}
