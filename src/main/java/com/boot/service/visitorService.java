package com.boot.service;

import com.boot.pojo.onedayVisitor;
import com.boot.pojo.visitor;

import java.util.List;

public interface visitorService {

    void insertVisitor(visitor visitor); //插入访问者

    List<visitor> selectVisitor(); //查询所有访问者

    //echarts ,统计近7天的每一天的访问量
    List<onedayVisitor> selectOneDayVisitor();

}
