package com.boot.dao;

import com.boot.pojo.onedayVisitor;
import com.boot.pojo.visitor;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface visitorMapper {


    void insertVisitor(visitor visitor); //插入访问者

    List<visitor> selectVisitor(); //查询所有访问者

    /**
     * SELECT
     * 	DATE_FORMAT( visit_time, '%Y-%m-%d' ) day,
     * 	COUNT( id ) count
     * FROM
     * 	t_visitor
     * WHERE
     * 	date_sub(CURDATE(),INTERVAL 7 DAY) <= DATE(visit_time)
     * GROUP BY
     * 	day
     * ORDER BY
     * 	count;
     */
    //echarts ,统计近7天的每一天的访问量
    List<onedayVisitor> selectOneDayVisitor();

}
