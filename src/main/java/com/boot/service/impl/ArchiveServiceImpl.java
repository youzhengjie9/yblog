package com.boot.service.impl;

import com.boot.dao.ArchiveMapper;
import com.boot.pojo.Article;
import com.boot.pojo.Archive;
import com.boot.service.ArchiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArchiveServiceImpl implements ArchiveService {

    @Autowired
    private ArchiveMapper archiveMapper;


    @Override
    public List<Archive> selectAllArchiveGroup() {
        return archiveMapper.selectAllArchiveGroup();
    }

    @Override
    public List<Article> selectArticleByarchiveTime(String archiveTime) {
        return archiveMapper.selectArticleByarchiveTime(archiveTime);
    }


}
