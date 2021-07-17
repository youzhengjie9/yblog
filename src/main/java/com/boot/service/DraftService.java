package com.boot.service;

import com.boot.pojo.Article;
import com.boot.pojo.Draft;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DraftService {


    List<Draft> selectAllDraft(String username);

    int selectDraftCount(String username);

    Draft selectDraftByID(int id);

    void deleteDraftByID(int id);

    void publishDraft(Article article,int draftid);

    void modifyDraft(Draft draft);

    void addDraft(Draft draft);

    List<Draft> selectDraftByTitle(String title);
}
