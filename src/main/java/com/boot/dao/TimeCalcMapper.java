package com.boot.dao;

import com.boot.pojo.TimeCalc;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface TimeCalcMapper {


    void insertTimeCalc(TimeCalc timeCalc);


    List<TimeCalc> selectAllTimeCalc();


    int selectAllCount();


    //根据uri去查询接口监控的数据
    @Select("select * from t_timecalc where uri=#{uri}")
    List<TimeCalc> selectAllTimeCalcByUri(String uri);

    //根据uri去查询接口监控的数据的数量
    int selectCountByUri(@Param("uri") String uri);


}
