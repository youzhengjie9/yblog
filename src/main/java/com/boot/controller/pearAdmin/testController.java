package com.boot.controller.pearAdmin;

import io.swagger.annotations.Api;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Api("调试接入新的后台管理系统控制器_仅仅是调试专用")
@Controller
@RequestMapping(path = "/pear")
public class testController {


    @RequestMapping(path = "/welcome")
    public String towelcome(){

        return "back/newback/article/welcome";
    }
    //控制后台，非常重要，访问后台时，会内嵌这个url
    @RequestMapping(path = "/toconsole")
    public String toconsole(){



        return "back/newback/article/console1";
    }
    //控制后台
    @RequestMapping(path = "/admin")
    public String toAdmin(){



        return "back/newback/index";
    }
    //登录
    @RequestMapping(path = "/login")
    public String tologin(){


        return "back/newback/login";
    }

    //文章
    //发布文章
    @RequestMapping(path = "/topublish")
    public String toPublishArticle(){


        return "back/newback/article/article_edit";
    }

    //文章管理
    @RequestMapping(path = "/toArticleManager")
    public String toArticleManager(){


        return "back/newback/article/article_list";
    }

    //分类管理
    @RequestMapping(path = "/toCategory")
    public String toCategory(){


        return "back/newback/article/categories";
    }

    //标签管理
    @RequestMapping(path = "/toTag")
    public String toTag(){


        return "back/newback/article/tag_list";
    }

    //附件管理
    @RequestMapping(path = "/toFileUpload")
    public String toFileUpload(){


        return "back/newback/article/img_list";
    }

    //爬取数据
    @RequestMapping(path = "/toCatchData")
    public String toCatchData(){


        return "back/newback/article/catch_list";
    }
    //用户管理
    @RequestMapping(path = "/toUserManager")
    public String toUserManager(){


        return "back/newback/article/userManager";
    }

    //友链管理
    @RequestMapping(path = "/toLink")
    public String toLink(){


        return "back/newback/article/link_list";
    }
    //个人资料
    @RequestMapping(path = "/touser")
    public String touser(){


        return "back/newback/article/user_list";
    }

    //访客记录
    @RequestMapping(path = "/toVisitor")
    public String toVisitor(){


        return "back/newback/article/visitor_list";
    }
    //黑名单
    @RequestMapping(path = "/toBlack")
    public String toBlack(){


        return "back/newback/article/black_list";
    }
//    //ip查询
//    @RequestMapping(path = "/toIpsearch")
//    public String toIpsearch(){
//
//
//        return "back/newback/system/user";
//    }
    //拦截记录
    @RequestMapping(path = "/toInterceptorLog")
    public String toInterceptorLog(){


        return "back/newback/article/interceptor_list";
    }
    //行为日志
    @RequestMapping(path = "/toLog")
    public String toLog(){


        return "back/newback/article/log_list";
    }

    //数据监控
    @RequestMapping(path = "/toMonitor")
    public String toMonitor(){


        return "back/newback/article/monitor";
    }
    //数据图表
    @RequestMapping(path = "/toEcharts")
    public String toEcharts(){


        return "back/newback/article/statistic_charts";
    }
    //系统设置
    @RequestMapping(path = "/toSetting")
    public String toSetting(){


        return "back/newback/article/setting";
    }





}
