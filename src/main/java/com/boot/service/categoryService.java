package com.boot.service;


import com.boot.pojo.category;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface categoryService {

    public void updateCategoryCount(String categoryName);

    public List<category> selectCategories();

    public void updateCategoryCountDecr(String categoryName);

    public void updateCategory(String oldName, String newName);

    public void deleteCategoryByName(String categoryName);

    public int selectCategoryCountByName(String categoryName);

    public void updateCategoryCountByName(String categoryName,
                                          int count);

    public void addCategory(category category);


    //代码重构

    public void updateCategory_service(String oldName,String newName);

    public void deleteCategory_service(String n, String DEFAULT_CATEGORY);

    //echarts
    List<category> selectCategories_echarts();

    //查询分类数量
    int selectCategoryCount();

    category selectCategoryByName(String categoryName);

    int selectCountByName(String categoryName);
}
