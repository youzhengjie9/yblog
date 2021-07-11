package com.boot.service;

import com.boot.pojo.TimeCalc;

import java.util.List;

/**
 * @author 游政杰
 */
public interface TimeCalcService {


    void insertTimeCalc(TimeCalc timeCalc);

    List<TimeCalc> selectAllTimeCalc();

}
