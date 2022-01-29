package com.boot.dao;

import com.boot.pojo.OperationLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author 游政杰
 */
@Mapper
@Repository
public interface OperationMapper {


    void insertOperationLog(OperationLog operationLog);

    List<OperationLog> selectAllOperationLog();

    int selectOperationCount();

    //查询指定数量的操作日志
    List<OperationLog> selectOperationLogByLimit(@Param("limit") int limit);


}
