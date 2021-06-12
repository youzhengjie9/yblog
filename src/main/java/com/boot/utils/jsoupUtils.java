package com.boot.utils;

import com.boot.pojo.Article;
import com.boot.pojo.Statistic;
import com.boot.service.articleService;
import com.boot.service.statisticService;
import io.swagger.annotations.ApiModel;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@ApiModel("jsoup爬取数据")
@Component
public class jsoupUtils {

    @Autowired
    private articleService articleService;

    @Autowired
    private statisticService statisticService;


    /**
     * 爬取CSDN博客*****记得注意版权问题，小心爬取
     * 可抓取数据，并存入数据库中
     * 需要传入准确的文章url ,例如：https://blog.csdn.net/liyuanjinglyj/article/details/117656787?utm_medium=distribute.pc_category.none-task-blog-hot-1.nonecase&depth_1-utm_source=distribute.pc_category.none-task-blog-hot-1.nonecase
     */
    public void catchData_csdn(String url) throws MalformedURLException {


        Document document = null;
        try {
            document = Jsoup.parse(new URL(url), 1000 * 20);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Element titleElement = document.getElementById("articleContentId");
        String title = titleElement.html(); //爬取标题
        Element contentElement = document.getElementById("content_views");
        String content = contentElement.toString(); //爬取内容

        Article article = new Article(); //创建文章

        //放入标题和内容
        article.setTitle(title);
        article.setContent(content);
        java.util.Date date1 = new java.util.Date();
        Date date = new Date(date1.getTime());
        article.setCreated(date);
        article.setCategories("默认分类");
        article.setTags("");
        article.setAllowComment(true);
        articleService.addArticle(article);
        Integer id = article.getId();

        Statistic statistic = new Statistic();
        statistic.setArticleId(id);
        statistic.setHits(0);
        statistic.setCommentsNum(0);
        statisticService.addStatistic(statistic);
    }




    /**
     * 抓取一个csdn模块的url
     *
     * @param modelUrl
     * @param links
     * @throws IOException
     */
    private static void catchModelHref_csdn(String modelUrl, List<String> links) throws IOException {

        Document document = Jsoup.parse(new URL(modelUrl), 1000 * 15);

        Element feedlist_id = document.getElementById("feedlist_id");

        Elements li = feedlist_id.getElementsByTag("li");
        for (int i = 0; i < li.size(); i++) {
            Element element = li.get(i);
            Elements e1 = element.getElementsByClass("title");

            if (e1 != null && e1.size() > 0) {
                Elements aElement = e1.get(0).getElementsByTag("a");

                if (aElement != null && aElement.size() > 0) {

                    String link = aElement.get(0).attr("href");

                    links.add(link); //放入集合中

                }

            }

        }

    }

    /**
     * 批量抓取一个csdn模块的所有文章
     * 需要传入一个模块的url ，例如：https://blog.csdn.net/nav/java
     */
    public void batchCatchArticleByModel_csdn(String modelUrl) throws IOException {
        List<String> links = new ArrayList<>();
        catchModelHref_csdn(modelUrl, links);

        if (links != null && links.size() > 0) {

            for (String link : links) {

                this.catchData_csdn(link);

            }


        }

    }


}
