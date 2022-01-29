package com.boot.service;

import com.boot.pojo.Intercept;

import java.util.List;

public interface InterceptService {


    void insertIntercept(Intercept intercept);

    //查询所有拦截记录
    List<Intercept> selectIntercepts();

    int selectInterceptCount();

}
