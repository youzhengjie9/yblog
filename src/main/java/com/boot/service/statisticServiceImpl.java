package com.boot.service;

import com.boot.dao.statisticMapper;
import com.boot.pojo.Statistic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class statisticServiceImpl implements statisticService {

    @Autowired
    private statisticMapper statisticMapper;


    @Override
    public int addStatistic(Statistic statistic) {
        return statisticMapper.addStatistic(statistic);
    }

    @Override
    public int deleteStatisticByArticleId(Integer id) {
        return statisticMapper.deleteStatisticByArticleId(id);
    }
}
