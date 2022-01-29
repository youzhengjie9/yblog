package com.boot.service;

import com.boot.pojo.Visitor;

import java.util.List;

public interface VisitorService {

    void insertVisitor(Visitor visitor); //插入访问者

    List<Visitor> selectVisitor(); //查询所有访问者


    //echarts，获取近7天日期
    List<String> selectDaysBy7();

    //echarts ,查询一天的访问量
    int selectOneDayVisitor(String day);

    int selectVistorCount();


}
