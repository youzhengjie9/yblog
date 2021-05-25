package com.boot.dao;

import com.boot.pojo.tag;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface tagMapper {


    public void addTag(tag tag);





}
