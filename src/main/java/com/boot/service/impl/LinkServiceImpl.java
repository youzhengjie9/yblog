package com.boot.service.impl;

import com.boot.dao.LinkMapper;
import com.boot.pojo.Link;
import com.boot.service.LinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LinkServiceImpl implements LinkService {

    @Autowired
    private LinkMapper linkMapper;

    @Override
    public List<Link> selectAllLink() {
        return linkMapper.selectAllLink();
    }

    @Override
    public void updateLink(Link link) {
        linkMapper.updateLink(link);
    }

    @Override
    public void insertLink(Link link) {
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
    public List<Link> selectLinkByTitle(String title) {
        return linkMapper.selectLinkByTitle(title);
    }

    @Override
    public int selectCountByTitle(String title) {
        return linkMapper.selectCountByTitle(title);
    }
}
