package com.boot.dao;

import com.boot.pojo.operationLog;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author 游政杰
 */
@Mapper
@Repository
public interface OperationMapper {


    void insertOperationLog(operationLog operationLog);

    List<operationLog> selectAllOperationLog();

    int selectOperationCount();


}
