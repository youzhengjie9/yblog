package com.boot.service;

import com.boot.pojo.user_authority;
import org.apache.ibatis.annotations.Param;

public interface userAuthorityService {

    void changeUserAuthority(user_authority user_authority);

    int selectAuthorityID(int userid);
}
