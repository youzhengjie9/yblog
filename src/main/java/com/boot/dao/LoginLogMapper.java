package com.boot.dao;

import com.boot.pojo.LoginLog;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author 游政杰
 * @Date  2021/7/9 1:24
 */
@Mapper
@Repository
public interface LoginLogMapper {

    void insertLoginLog(LoginLog loginLog);

    List<LoginLog> selectLoginLogAll();

    int loginLogCount();



}
