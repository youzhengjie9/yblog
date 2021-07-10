package com.boot.service.impl;

import com.boot.dao.OperationMapper;
import com.boot.pojo.operationLog;
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
    public void insertOperationLog(operationLog operationLog) {
        operationMapper.insertOperationLog(operationLog);
    }

    @Override
    public List<operationLog> selectAllOperationLog() {
        return operationMapper.selectAllOperationLog();
    }

    @Override
    public int selectOperationCount() {
        return operationMapper.selectOperationCount();
    }
}
