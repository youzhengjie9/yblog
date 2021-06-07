package com.boot.service.impl;

import com.boot.dao.visitorMapper;
import com.boot.pojo.visitor;
import com.boot.service.visitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class visitorServiceImpl implements visitorService {

    @Autowired
    private visitorMapper visitorMapper;

    @Override
    public void insertVisitor(visitor visitor) {
        visitorMapper.insertVisitor(visitor);
    }

    @Override
    public List<visitor> selectVisitor() {
        return visitorMapper.selectVisitor();
    }
}
