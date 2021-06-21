package com.boot.dao;

import com.boot.pojo.like;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface likeMapper {

    @Insert("insert into t_like (username,article_id) values(#{username},#{article_id})")
    void addLike(like like);

    @Select("select username from t_like where username=#{username} and article_id=#{article_id}")
    String selectLikeExsit(like like);

}
