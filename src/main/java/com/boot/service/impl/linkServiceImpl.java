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

    @Override
    public void updateLink(link link) {
        linkMapper.updateLink(link);
    }

    @Override
    public void insertLink(link link) {
        linkMapper.insertLink(link);
    }

    @Override
    public void deleteLink(int id) {
        linkMapper.deleteLink(id);
    }

    @Override
    public int linkCount() {
        return linkMapper.linkCount();
    }

    @Override
    public List<link> selectLinkByTitle(String title) {
        return linkMapper.selectLinkByTitle(title);
    }

    @Override
    public int selectCountByTitle(String title) {
        return linkMapper.selectCountByTitle(title);
    }
}
