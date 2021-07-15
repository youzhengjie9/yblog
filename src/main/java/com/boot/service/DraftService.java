package com.boot.service;

import com.boot.pojo.Draft;

import java.util.List;

public interface DraftService {


    List<Draft> selectAllDraft(String username);

}
