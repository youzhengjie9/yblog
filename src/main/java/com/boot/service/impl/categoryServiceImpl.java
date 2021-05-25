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
}
