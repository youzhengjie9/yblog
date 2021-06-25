package com.boot.service.impl;

import com.boot.dao.categoryMapper;
import com.boot.pojo.category;
import com.boot.service.articleService;
import com.boot.service.categoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class categoryServiceImpl implements categoryService {

    @Autowired
    private categoryMapper categoryMapper;

    @Autowired
    private categoryService categoryService;

    @Autowired
    private articleService articleService;

    @Override
    public void updateCategoryCount(String categoryName) {
        categoryMapper.updateCategoryCount(categoryName);
    }

    @Override
    public void updateCategory_service(String oldName,String newName) {
        try {
            //修改分类表的分类名
            categoryService.updateCategory(oldName, newName);
            //修改article表中有关oldName的分类，修改成newName
            articleService.updateCategory(oldName, newName);
        }catch (Exception e){
            throw new RuntimeException();
        }


    }

    @Override
    public void deleteCategory_service(String n, String DEFAULT_CATEGORY) {

        try {
            //把article的分类n改成默认分类
            articleService.updateCategory(n, "默认分类");
            //删除category表的分类n
            int count = categoryService.selectCategoryCountByName(n); //先查再删,获取删除分类的数量
            categoryService.deleteCategoryByName(n);
            //并且把删除的数量加到默认分类的数量上
            categoryService.updateCategoryCountByName(DEFAULT_CATEGORY, count);

        }catch (Exception e){
            throw new RuntimeException();
        }

    }

    @Override
    public List<category> selectCategories_echarts() {
        return categoryMapper.selectCategories_echarts();
    }

    @Override
    public int selectCategoryCount() {
        return categoryMapper.selectCategoryCount();
    }


    @Override
    public List<category> selectCategories() {
        return categoryMapper.selectCategories();
    }

    @Override
    public void updateCategoryCountDecr(String categoryName) {
        categoryMapper.updateCategoryCountDecr(categoryName);
    }

    @Override
    public void updateCategory(String oldName, String newName) {
        categoryMapper.updateCategory(oldName, newName);
    }

    @Override
    public void deleteCategoryByName(String categoryName) {
        categoryMapper.deleteCategoryByName(categoryName);
    }

    @Override
    public int selectCategoryCountByName(String categoryName) {
        return categoryMapper.selectCategoryCountByName(categoryName);
    }

    @Override
    public void updateCategoryCountByName(String categoryName, int count) {
        categoryMapper.updateCategoryCountByName(categoryName, count);
    }

    @Override
    public void addCategory(category category) {
        categoryMapper.addCategory(category);
    }


}
