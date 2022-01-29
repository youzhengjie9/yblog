package com.boot.service;

import com.boot.pojo.OperationLog;

import java.util.List;

/**
 * @author 游政杰
 */
public interface OperationService {

    void insertOperationLog(OperationLog operationLog);

    List<OperationLog> selectAllOperationLog();

    int selectOperationCount();

    //查询指定数量的操作日志
    List<OperationLog> selectOperationLogByLimit(int limit);
}
