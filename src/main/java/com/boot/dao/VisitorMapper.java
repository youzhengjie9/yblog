package com.boot.dao;

import com.boot.pojo.Visitor;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface VisitorMapper {


    void insertVisitor(Visitor visitor); //插入访问者

    List<Visitor> selectVisitor(); //查询所有访问者







   //echarts，获取近7天日期
    List<String> selectDaysBy7();

    //echarts ,查询一天的访问量
    int selectOneDayVisitor(@Param("day") String day);

    @Select("select count(*) from t_visitor")
    int selectVistorCount();

}
