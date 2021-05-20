package com.boot.controller;

import com.boot.dao.articleMapper;
import com.boot.data.ResponseData.ArticleResponseData;
import com.boot.pojo.Article;
import com.boot.pojo.Comment;
import com.boot.service.CommentService;
import com.boot.service.articleService;
import com.boot.utils.Commons;
import com.boot.utils.SpringSecurityUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Controller
public class clientController {

    @Autowired
    private articleService articleService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private SpringSecurityUtil securityUtil;


    //前10排行
    private static final List<Article> ArticleOrder_10(List<Article> articleList){
        List<Article> list=new ArrayList<>(10);
        for (int i = 0; i < 10; i++) {
            list.add(articleList.get(i));
        }
        return list;
    }

    @RequestMapping(path = {"/"})
    public ModelAndView toIndex1(){
        PageHelper.startPage(1,5);
        List<Article> list = articleService.selectAllArticle();
        PageInfo pageInfo = new PageInfo(list);

        List<Article> articleOrders = ArticleOrder_10(articleService.selectAllArticleOrderByDesc());


//        System.out.println(pageInfo);
        //PageInfo{pageNum=1, pageSize=5, size=5, startRow=1, endRow=5, total=12, pages=3, list=Page{count=true, pageNum=1, pageSize=5, startRow=0, endRow=5, total=12, pages=3, reasonable=false, pageSizeZero=false}[Article{id=1, title='2018新版Java学习路线图', content='&ensp;&ensp;&ensp;&ensp;播妞深知广大爱好Java的人学习是多么困难，没视频没资源，上网花钱还老担心被骗。因此专门整理了新版的学习路线图，不管你是不懂电脑的小白，还是已经步入开发的大牛，这套路线路绝对不容错过！12年传智播客黑马程序员分享免费视频教程长达10余万小时，累计下载量3000余万次，受益人数达千万。2018年我们不忘初心，继续前行。 路线图的宗旨就是分享，专业，便利，让喜爱Java的人，都能平等的学习。从今天起不要再找借口，不要再说想学Java却没有资源，赶快行动起来，Java等你来探索，高薪距你只差一步！
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("articles",list);
        modelAndView.addObject("commons",new Commons());
        modelAndView.addObject("pageInfo",pageInfo);
        modelAndView.addObject("articleOrders",articleOrders);
        modelAndView.setViewName("client/index");
        return modelAndView;
    }

    /**
     * public class PageInfo<T> extends PageSerializable<T> {
     *     private int pageNum;
     *     private int pageSize;
     *     private int size;
     *     private int startRow;
     *     private int endRow;
     *     private int pages;
     *     private int prePage;
     *     private int nextPage;
     *     private boolean isFirstPage;
     *     private boolean isLastPage;
     *     private boolean hasPreviousPage;
     *     private boolean hasNextPage;
     *     private int navigatePages;
     *     private int[] navigatepageNums;
     *     private int navigateFirstPage;
     *     private int navigateLastPage;
     * @param pageNum
     * @return
     */
    @RequestMapping(path = {"/page/{pageNum}"})
    public ModelAndView toIndex2(@PathVariable("pageNum")int pageNum){
        PageHelper.startPage(pageNum,5);
        List<Article> list = articleService.selectAllArticle();
        PageInfo pageInfo = new PageInfo(list);

//        System.out.println(pageInfo);


        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("articles",list);
        modelAndView.addObject("commons",new Commons());
        modelAndView.addObject("pageInfo",pageInfo);

        modelAndView.setViewName("client/index");
        return modelAndView;
    }



    @GetMapping(path = "/article/{articleId}")
    public ModelAndView toArticleDetailByID(@PathVariable("articleId") Integer articleId, HttpServletRequest request){
        ModelAndView modelAndView = new ModelAndView();
        boolean res=false;
        Boolean hasComment = commentService.hasComment(articleId);
//        System.out.println(hasComment);
        Article article=articleService.selectArticleByArticleIdNoComment(articleId);;
        String c = request.getParameter("c");
        int pageNum=0;
        if(c==null||c.equals("")){
            res=false;
        }else {
            try {
                pageNum = Integer.parseInt(c);
                res=true;
            } catch (NumberFormatException e) {
                System.out.println(e.getMessage());
                res=false;
            }
        }
        if(res){
            if(hasComment){
//            article = articleService.selectArticleByArticleId(articleId);
                PageHelper.startPage(pageNum,3);
                List<Comment> comments = commentService.CommentByArticleId(articleId); //一定是要mybatis查询才行，get方法获取不行
                PageInfo pageInfo=new PageInfo(comments);
                modelAndView.addObject("pageInfo",pageInfo);
                modelAndView.addObject("comments",comments);
            }
            modelAndView.addObject("article",article);
            modelAndView.addObject("commons",new Commons());
            modelAndView.addObject("articleId",articleId);
            modelAndView.setViewName("client/articleDetails");
            return modelAndView;


        }else{
            if(hasComment){
//            article = articleService.selectArticleByArticleId(articleId);
                PageHelper.startPage(1,3);
                List<Comment> comments = commentService.CommentByArticleId(articleId); //一定是要mybatis查询才行，get方法获取不行
                PageInfo pageInfo=new PageInfo(comments);
                modelAndView.addObject("pageInfo",pageInfo);
                modelAndView.addObject("comments",comments);
            }
            modelAndView.addObject("article",article);
            modelAndView.addObject("commons",new Commons());
            modelAndView.addObject("articleId",articleId);
            modelAndView.setViewName("client/articleDetails");
            return modelAndView;

        }
    }

    @ResponseBody
    @PostMapping(path = "/publishComment")
    public ArticleResponseData publishComment(Integer aid,String text,HttpSession session){
        try{
            //发布评论
            Comment comment = new Comment();
            comment.setArticleId(aid);
            comment.setC_content(text);
            comment.setStatus("approved");
            comment.setIp("0:0:0:0:0:0:0:1"); //获取ip的功能未完善，之后可能会进行完善，暂且给一个默认值
            Date date = new Date(new java.util.Date().getTime());
            comment.setCreated(date);
            String username = securityUtil.currentUser(session);
            comment.setAuthor(username); //这里的用户名最好加唯一索引
            commentService.publishComment(comment);
            return ArticleResponseData.ok();
        }catch (Exception e){
            e.printStackTrace();
            return ArticleResponseData.fail();
        }

    }






}
