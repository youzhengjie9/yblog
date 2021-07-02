package com.boot.service;

import com.boot.pojo.blacklist;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface blacklistService {

    List<blacklist> selectBlackList();

    void deleteBlackListByIp(String ip);

    void addBlackList(blacklist blacklist);

    boolean checkIpHasBlack(String ip); //检查ip是否在黑名单

    int selectBlackCount();

    void updateBlackIp(String oldIp,String newIp);
}
