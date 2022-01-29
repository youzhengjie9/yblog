package com.boot.service.impl;

import com.boot.dao.InterceptMapper;
import com.boot.pojo.Intercept;
import com.boot.service.InterceptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InterceptServiceImpl implements InterceptService {

    @Autowired
    private InterceptMapper interceptMapper;

    @Override
    public void insertIntercept(Intercept intercept) {
        interceptMapper.insertIntercept(intercept);
    }

    @Override
    public List<Intercept> selectIntercepts() {
        return interceptMapper.selectIntercepts();
    }

    @Override
    public int selectInterceptCount() {
        return interceptMapper.selectInterceptCount();
    }
}
