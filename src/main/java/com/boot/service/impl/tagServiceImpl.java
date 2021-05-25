package com.boot.service.impl;

import com.boot.dao.tagMapper;
import com.boot.pojo.tag;
import com.boot.service.tagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class tagServiceImpl implements tagService {

    @Autowired
    private tagMapper tagMapper;

    @Override
    public void addTag(tag tag) {
        tagMapper.addTag(tag);
    }
}
