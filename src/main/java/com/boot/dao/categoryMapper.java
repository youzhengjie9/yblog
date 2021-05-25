package com.boot.dao;

import com.boot.pojo.category;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface categoryMapper {


    public void updateCategoryCount(@Param("categoryName") String categoryName);


    public List<category> selectCategories();



}
