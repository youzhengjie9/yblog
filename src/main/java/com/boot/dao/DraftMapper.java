package com.boot.dao;

import com.boot.pojo.Draft;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author 游政杰
 */
@Mapper
@Repository
public interface DraftMapper {


    List<Draft> selectAllDraft(@Param("username") String username);






}
