package com.boot.service.impl;

import com.boot.dao.OperationMapper;
import com.boot.pojo.OperationLog;
import com.boot.service.OperationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 游政杰
 */
@Service
public class OperationServiceImpl implements OperationService {

    @Autowired
    private OperationMapper operationMapper;

    @Override
    public void insertOperationLog(OperationLog operationLog) {
        operationMapper.insertOperationLog(operationLog);
    }

    @Override
    public List<OperationLog> selectAllOperationLog() {
        return operationMapper.selectAllOperationLog();
    }

    @Override
    public int selectOperationCount() {
        return operationMapper.selectOperationCount();
    }

    @Override
    public List<OperationLog> selectOperationLogByLimit(int limit) {
        return operationMapper.selectOperationLogByLimit(limit);
    }
}
