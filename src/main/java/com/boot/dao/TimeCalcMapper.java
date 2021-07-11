package com.boot.dao;

import com.boot.pojo.TimeCalc;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface TimeCalcMapper {


    void insertTimeCalc(TimeCalc timeCalc);




}
