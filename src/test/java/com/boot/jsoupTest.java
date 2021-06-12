package com.boot;

import com.boot.service.catchDataService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.net.URL;

@SpringBootTest
public class jsoupTest {

    //测试博客的url
    private String url1="https://blog.csdn.net/weixin_55932383/article/details/117752978?utm_medium=distribute.pc_category.none-task-blog-hot-6.nonecase&depth_1-utm_source=distribute.pc_category.none-task-blog-hot-6.nonecase";

    private String url2="https://blog.csdn.net/l1028386804/article/details/117740163?utm_medium=distribute.pc_category.none-task-blog-hot-4.nonecase&depth_1-utm_source=distribute.pc_category.none-task-blog-hot-4.nonecase";


    /**
     * jsoup爬取csdn标题和内容
     */
    @Test
    public void jsoup_csdn() throws IOException {
        Document document = Jsoup.parse(new URL(url2), 1000 * 10);
        Element titleElement = document.getElementById("articleContentId");
        String title = titleElement.html(); //爬取标题
        Element contentElement = document.getElementById("content_views");
        String content = contentElement.toString(); //爬取内容
        System.out.println(title);
        System.out.println("===========");
        System.out.println(content);
    }

    /**
     * jsoup批量获取一个模块的文章url
     */

    @Test
    public void jsoup_csdn_url() throws IOException {

        Document document = Jsoup.parse(new URL("https://blog.csdn.net/nav/java"), 1000 * 10);

        Element feedlist_id = document.getElementById("feedlist_id");

        Elements li = feedlist_id.getElementsByTag("li");
        for (int i = 0; i < li.size(); i++) {
            Element element = li.get(i);
            Elements e1 = element.getElementsByClass("title");

            if(e1!=null&&e1.size()>0){
                Elements aElement = e1.get(0).getElementsByTag("a");

                if(aElement!=null&&aElement.size()>0){

                    String link = aElement.get(0).attr("href");

                    System.out.println(link); //获取到文章url

                }


            }

        }


    }










}
