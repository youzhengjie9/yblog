package com.boot.service;

import java.io.IOException;

public interface catchDataService {


    /**
     * 爬取CSDN博客*****记得注意版权问题，小心爬取
     * 可抓取数据，并存入数据库中
     * 需要传入准确的文章url ,例如：https://blog.csdn.net/liyuanjinglyj/article/details/117656787?utm_medium=distribute.pc_category.none-task-blog-hot-1.nonecase&depth_1-utm_source=distribute.pc_category.none-task-blog-hot-1.nonecase
     */
    public void catchData_csdn(String url);

    /**
     * 批量抓取一个csdn模块的所有文章
     * 需要传入一个模块的url ，例如：https://blog.csdn.net/nav/java
     */
    public void batchCatchArticleByModel_csdn(String modelUrl) throws IOException;



}
