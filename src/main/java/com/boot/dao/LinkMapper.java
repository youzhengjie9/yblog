package com.boot.dao;

import com.boot.pojo.Link;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface LinkMapper {


    List<Link> selectAllLink(); //查询所有友链

    void updateLink(Link link); //修改友链

    void insertLink(Link link); //添加友链

    void deleteLink(@Param("id") int id); //删除友链

    @Select("select count(*) from t_link")
    int linkCount();


    List<Link> selectLinkByTitle(@Param("title") String title);

    int selectCountByTitle(@Param("title") String title);


}
