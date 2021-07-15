package com.boot.service.impl;

import com.boot.dao.DraftMapper;
import com.boot.pojo.Draft;
import com.boot.service.DraftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DraftServiceImpl implements DraftService {

    @Autowired
    private DraftMapper draftMapper;

    @Override
    public List<Draft> selectAllDraft(String username) {
        return draftMapper.selectAllDraft(username);
    }
}
