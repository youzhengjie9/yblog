package com.boot.dao;

import com.boot.pojo.intercept;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface interceptMapper {

    //插入拦截记录
    void insertIntercept(intercept intercept);
    //查询所有拦截记录
    List<intercept> selectIntercepts();

    int selectInterceptCount();

}
