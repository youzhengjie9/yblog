package com.boot.dao;

import com.boot.pojo.Statistic;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface StatisticMapper {


    public int addStatistic(Statistic statistic);

    public int deleteStatisticByArticleId(@Param("id") Integer id);
}
