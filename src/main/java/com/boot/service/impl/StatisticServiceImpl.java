package com.boot.service.impl;

import com.boot.dao.StatisticMapper;
import com.boot.pojo.Statistic;
import com.boot.service.StatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StatisticServiceImpl implements StatisticService {

    @Autowired
    private StatisticMapper statisticMapper;


    @Override
    public int addStatistic(Statistic statistic) {
        return statisticMapper.addStatistic(statistic);
    }

    @Override
    public int deleteStatisticByArticleId(Integer id) {
        return statisticMapper.deleteStatisticByArticleId(id);
    }
}
