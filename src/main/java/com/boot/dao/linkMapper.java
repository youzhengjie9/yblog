package com.boot.dao;

import com.boot.pojo.link;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface linkMapper {


    List<link> selectAllLink(); //查询所有友链



}
