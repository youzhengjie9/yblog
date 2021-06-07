package com.boot.dao;

import com.boot.pojo.visitor;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface visitorMapper {


    void insertVisitor(visitor visitor); //插入访问者

    List<visitor> selectVisitor(); //查询所有访问者


}
