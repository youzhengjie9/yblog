package com.boot.service;


import com.boot.pojo.Category;

import java.util.List;

public interface CategoryService {

    public void updateCategoryCount(String categoryName);

    public List<Category> selectCategories();

    public void updateCategoryCountDecr(String categoryName);

    public void updateCategory(String oldName, String newName);

    public void deleteCategoryByName(String categoryName);

    public int selectCategoryCountByName(String categoryName);

    public void updateCategoryCountByName(String categoryName,
                                          int count);

    public void addCategory(Category category);


    //代码重构

    public void updateCategory_service(String oldName,String newName);

    public void deleteCategory_service(String n, String DEFAULT_CATEGORY);

    //echarts
    List<Category> selectCategories_echarts();

    //查询分类数量
    int selectCategoryCount();

    Category selectCategoryByName(String categoryName);

    int selectCountByName(String categoryName);

    List<String> selectCategoryName();
}
