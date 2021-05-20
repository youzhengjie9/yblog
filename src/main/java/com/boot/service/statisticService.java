package com.boot.service;


import com.boot.pojo.Statistic;
import org.apache.ibatis.annotations.Param;

public interface statisticService {



    public int addStatistic(Statistic statistic);


    public int deleteStatisticByArticleId(Integer id);



}
