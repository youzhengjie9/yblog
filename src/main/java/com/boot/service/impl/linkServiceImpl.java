package com.boot.service.impl;

import com.boot.dao.linkMapper;
import com.boot.pojo.link;
import com.boot.service.linkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class linkServiceImpl implements linkService {

    @Autowired
    private linkMapper linkMapper;

    @Override
    public List<link> selectAllLink() {
        return linkMapper.selectAllLink();
    }
}
