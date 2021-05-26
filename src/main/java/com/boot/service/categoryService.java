package com.boot.service;


import com.boot.pojo.category;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface categoryService {

    public void updateCategoryCount(String categoryName);

    public List<category> selectCategories();

    public void updateCategoryCountDecr(String categoryName);

}
