package com.boot.dao;

import com.boot.pojo.Category;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface CategoryMapper {


    public void updateCategoryCount(@Param("categoryName") String categoryName);

    public void updateCategoryCountDecr(@Param("categoryName") String categoryName);

    public List<Category> selectCategories();

    public void updateCategory(@Param("oldName") String oldName,@Param("newName") String newName);

    public void deleteCategoryByName(@Param("categoryName") String categoryName);

    public int selectCategoryCountByName(@Param("categoryName") String categoryName);

    public void updateCategoryCountByName(@Param("categoryName") String categoryName,
                                          @Param("count") int count);

    public void addCategory(Category category);

    //echarts
    List<Category> selectCategories_echarts();


    //查询分类数量
    int selectCategoryCount();

    Category selectCategoryByName(@Param("categoryName") String categoryName);


    int selectCountByName(@Param("categoryName") String categoryName);

    @Select("select categoryName from t_categories")
    List<String> selectCategoryName();

}
