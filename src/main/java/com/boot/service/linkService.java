package com.boot.service;

import com.boot.pojo.link;

import java.util.List;

public interface linkService {

    List<link> selectAllLink(); //查询所有友链

    void updateLink(link link); //修改友链

    void insertLink(link link); //添加友链
}
