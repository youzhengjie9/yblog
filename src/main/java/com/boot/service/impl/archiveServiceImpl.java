package com.boot.service.impl;

import com.boot.dao.archiveMapper;
import com.boot.pojo.Article;
import com.boot.pojo.archive;
import com.boot.service.archiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class archiveServiceImpl implements archiveService {

    @Autowired
    private archiveMapper archiveMapper;


    @Override
    public List<archive> selectAllArchiveGroup() {
        return archiveMapper.selectAllArchiveGroup();
    }

    @Override
    public List<Article> selectArticleByarchiveTime(String archiveTime) {
        return archiveMapper.selectArticleByarchiveTime(archiveTime);
    }


}
