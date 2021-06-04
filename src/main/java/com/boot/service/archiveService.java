package com.boot.service;

import com.boot.pojo.Article;
import com.boot.pojo.archive;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface archiveService {

    List<archive> selectAllArchiveGroup();  //进行归档分组

    //注意：这是查询文章信息
    List<Article> selectArticleByarchiveTime(String archiveTime); //根据归档时间查询文章


}
