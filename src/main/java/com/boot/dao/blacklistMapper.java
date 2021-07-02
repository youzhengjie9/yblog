package com.boot.dao;

import com.boot.pojo.blacklist;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface blacklistMapper {

    List<blacklist> selectBlackList();

    void deleteBlackListByIp(@Param("ip") String ip);

    void addBlackList(blacklist blacklist);

    String selectBlackListByIp(@Param("ip") String ip);

    @Select("select count(*) from t_blacklist")
    int selectBlackCount();

    @Update("update t_blacklist set black_ip=#{newIp} where black_ip=#{oldIp}")
    void updateBlackIp(@Param("oldIp") String oldIp,@Param("newIp") String newIp);


}
