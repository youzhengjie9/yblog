package com.boot.service;

import com.boot.pojo.operationLog;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 游政杰
 */
public interface OperationService {

    void insertOperationLog(operationLog operationLog);

    List<operationLog> selectAllOperationLog();

    int selectOperationCount();

    //查询指定数量的操作日志
    List<operationLog> selectOperationLogByLimit(int limit);
}
