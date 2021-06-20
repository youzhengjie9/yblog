package com.boot.dao;

import com.boot.pojo.authority;
import com.boot.pojo.user_authority;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface authorityMapper {


    List<authority> selectUserAuthority();

    String selectAuthorityByid(@Param("id") int id);

}
