package com.boot.controller;

import com.boot.pojo.userDetail;
import com.boot.pojo.visitor;
import com.boot.service.userDetailService;
import com.boot.service.visitorService;
import com.boot.utils.Commons;
import com.boot.utils.SpringSecurityUtil;
import com.boot.utils.visitorUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Controller
@RequestMapping(path = "/visitor")
@Api(value = "访客控制器")
public class visitorController {

    private final int type = 1;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private visitorService visitorService;

    @Autowired
    private SpringSecurityUtil springSecurityUtil;

    @Autowired
    private userDetailService userDetailService;


    @GetMapping(path = "/list")
    public String toVisitorList(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum, Model model,
                                HttpServletRequest request, @Value("进入访客管理界面") String desc
                              , HttpSession session) {

        //添加访客信息
        visitor visitor = visitorUtil.getVisitor(request, desc);
        String key = "visit_ip_" + visitor.getVisit_ip() + "_type_" + type;
        String s = (String) redisTemplate.opsForValue().get(key);
        if (StringUtils.isEmpty(s)) {
            visitorService.insertVisitor(visitor);
            //由ip和type组成的key放入redis缓存,5分钟内访问过的不再添加访客
            redisTemplate.opsForValue().set(key, "1", 60 * 5, TimeUnit.SECONDS);
        }

        PageHelper.startPage(pageNum, 8);
        List<com.boot.pojo.visitor> visitors = visitorService.selectVisitor();

        PageInfo pageInfo = new PageInfo(visitors);

        String username = springSecurityUtil.currentUser(session);
        userDetail userDetail = userDetailService.selectUserDetailByUserName(username);
        model.addAttribute("userDetail", userDetail);

        model.addAttribute("pageInfo", pageInfo);
        model.addAttribute("visitors", visitors);
        model.addAttribute("commons", Commons.getInstance());
        return "back/visitor_list";
    }


}
