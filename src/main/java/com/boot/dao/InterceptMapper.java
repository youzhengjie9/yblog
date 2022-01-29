package com.boot.dao;

import com.boot.pojo.Intercept;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface InterceptMapper {

    //插入拦截记录
    void insertIntercept(Intercept intercept);
    //查询所有拦截记录
    List<Intercept> selectIntercepts();

    int selectInterceptCount();

}
