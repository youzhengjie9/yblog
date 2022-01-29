package com.boot.dao;

import com.boot.pojo.Article;
import com.boot.pojo.Archive;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ArchiveMapper {


    List<Archive> selectAllArchiveGroup();  //进行归档分组

    //注意：这是查询文章信息
    List<Article> selectArticleByarchiveTime(@Param("archiveTime") String archiveTime); //根据归档时间查询文章




}
