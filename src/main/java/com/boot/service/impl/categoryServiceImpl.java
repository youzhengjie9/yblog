package com.boot.service.impl;

import com.boot.dao.categoryMapper;
import com.boot.pojo.category;
import com.boot.service.categoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class categoryServiceImpl implements categoryService {

    @Autowired
    private categoryMapper categoryMapper;

    @Override
    public void updateCategoryCount(String categoryName) {
        categoryMapper.updateCategoryCount(categoryName);

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
        categoryMapper.updateCategoryCountByName(categoryName,count);
    }

    @Override
    public void addCategory(category category) {
        categoryMapper.addCategory(category);
    }
}
