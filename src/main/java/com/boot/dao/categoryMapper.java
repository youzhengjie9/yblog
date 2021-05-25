package com.boot.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface categoryMapper {


    public void updateCategoryCount(@Param("categoryName") String categoryName);





}
