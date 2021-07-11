package com.boot.service.impl;

import com.boot.dao.TimeCalcMapper;
import com.boot.pojo.TimeCalc;
import com.boot.service.TimeCalcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 游政杰
 */
@Service
public class TimeCalcServiceImpl implements TimeCalcService {

    @Autowired
    private TimeCalcMapper timeCalcMapper;

    @Override
    public void insertTimeCalc(TimeCalc timeCalc) {
        timeCalcMapper.insertTimeCalc(timeCalc);
    }
}
