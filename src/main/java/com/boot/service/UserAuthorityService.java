package com.boot.service;

import com.boot.pojo.UserAuthority;

public interface UserAuthorityService {

    void changeUserAuthority(UserAuthority user_authority);

    int selectAuthorityID(int userid);
}
