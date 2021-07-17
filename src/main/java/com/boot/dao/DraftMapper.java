package com.boot.dao;

import com.boot.pojo.Draft;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author 游政杰
 */
@Mapper
@Repository
public interface DraftMapper {


    List<Draft> selectAllDraft(@Param("username") String username);


    int selectDraftCount(@Param("username") String username);


    Draft selectDraftByID(@Param("id") int id);


    void deleteDraftByID(@Param("id") int id);


    void modifyDraft(Draft draft);

    void addDraft(Draft draft);

    @Select("select * from t_draft where title=#{title}")
    List<Draft> selectDraftByTitle(String title);

}
