package com.boot.dao;

import com.boot.pojo.onedayVisitor;
import com.boot.pojo.visitor;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface visitorMapper {


    void insertVisitor(visitor visitor); //插入访问者

    List<visitor> selectVisitor(); //查询所有访问者







   //echarts，获取近7天日期
    List<String> selectDaysBy7();

    //echarts ,查询一天的访问量
    int selectOneDayVisitor(@Param("day") String day);




}
