package com.boot.service.impl;

import com.boot.dao.blacklistMapper;
import com.boot.pojo.blacklist;
import com.boot.service.blacklistService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class blacklistServiceImpl implements blacklistService {

    @Autowired
    private blacklistMapper blacklistMapper;

    @Override
    public List<blacklist> selectBlackList() {
        return blacklistMapper.selectBlackList();
    }

    @Override
    public void deleteBlackListByIp(String ip) {
        blacklistMapper.deleteBlackListByIp(ip);
    }

    @Override
    public void addBlackList(blacklist blacklist) {
        blacklistMapper.addBlackList(blacklist);
    }

    @Override
    public boolean checkIpHasBlack(String ip) {
        String blackip = blacklistMapper.selectBlackListByIp(ip);
        if (blackip==null||blackip.equals("")) { //为true，说明这个ip不在黑名单
            return false;
        } else {
            return true;
        }

    }

    @Override
    public int selectBlackCount() {
        return blacklistMapper.selectBlackCount();
    }
}
