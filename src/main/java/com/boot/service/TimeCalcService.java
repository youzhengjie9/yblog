package com.boot.service;

import com.boot.pojo.TimeCalc;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 游政杰
 */
public interface TimeCalcService {


    void insertTimeCalc(TimeCalc timeCalc);

    List<TimeCalc> selectAllTimeCalc();

    int selectAllCount();

    //根据uri去查询接口监控的数据
    List<TimeCalc> selectAllTimeCalcByUri(String uri);

    //根据uri去查询接口监控的数据的数量
    int selectCountByUri(String uri);


}
