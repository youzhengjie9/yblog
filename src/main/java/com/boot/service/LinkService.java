package com.boot.service;

import com.boot.pojo.Link;

import java.util.List;

public interface LinkService {

    List<Link> selectAllLink(); //查询所有友链

    void updateLink(Link link); //修改友链

    void insertLink(Link link); //添加友链

    void deleteLink(int id); //删除友链

    int linkCount();

    List<Link> selectLinkByTitle(String title);

    int selectCountByTitle(String title);
}
