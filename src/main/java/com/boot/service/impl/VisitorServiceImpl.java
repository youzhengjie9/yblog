package com.boot.service.impl;

import com.boot.dao.VisitorMapper;
import com.boot.pojo.Visitor;
import com.boot.service.VisitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VisitorServiceImpl implements VisitorService {

    @Autowired
    private VisitorMapper visitorMapper;

    @Override
    public void insertVisitor(Visitor visitor) {
        visitorMapper.insertVisitor(visitor);
    }

    @Override
    public List<Visitor> selectVisitor() {
        return visitorMapper.selectVisitor();
    }

    @Override
    public List<String> selectDaysBy7() {
        return visitorMapper.selectDaysBy7();
    }

    @Override
    public int selectOneDayVisitor(String day) {
        return visitorMapper.selectOneDayVisitor(day);
    }

    @Override
    public int selectVistorCount() {
        return visitorMapper.selectVistorCount();
    }


}
