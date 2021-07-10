package com.boot.service;

import com.boot.pojo.operationLog;

import java.util.List;

/**
 * @author 游政杰
 */
public interface OperationService {

    void insertOperationLog(operationLog operationLog);

    List<operationLog> selectAllOperationLog();

    int selectOperationCount();
}
