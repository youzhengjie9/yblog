package com.boot.service;

import com.boot.pojo.intercept;

import java.util.List;

public interface interceptService {


    void insertIntercept(intercept intercept);

    //查询所有拦截记录
    List<intercept> selectIntercepts();

    int selectInterceptCount();

}
