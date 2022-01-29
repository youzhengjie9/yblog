package com.boot.service;

import com.boot.pojo.BlackList;

import java.util.List;

public interface BlackListService {

    List<BlackList> selectBlackList();

    void deleteBlackListByIp(String ip);

    void addBlackList(BlackList blacklist);

    boolean checkIpHasBlack(String ip); //检查ip是否在黑名单

    int selectBlackCount();

    void updateBlackIp(String oldIp,String newIp);
}
