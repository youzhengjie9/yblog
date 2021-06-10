package com.boot.service;

import com.boot.pojo.link;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface linkService {

    List<link> selectAllLink(); //查询所有友链

    void updateLink(link link); //修改友链

    void insertLink(link link); //添加友链

    void deleteLink(int id); //删除友链

}
