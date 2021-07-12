package com.boot.controller.pearAdmin;


import com.alibaba.fastjson.JSON;
import com.boot.pojo.ChildrenMenu;
import com.boot.pojo.Menu;
import com.boot.pojo.MenuData;
import com.boot.service.MenuService;
import com.boot.service.userAuthorityService;
import com.boot.service.userService;
import com.boot.utils.SpringSecurityUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 游政杰
 */
@RestController
@RequestMapping(path = "/pear")
@Api("动态生成后台管理系统菜单的接口")
public class MenuController {

    @Autowired
    private MenuService menuService;

    @Autowired
    private SpringSecurityUtil springSecurityUtil;

    @Autowired
    private HttpSession session;

    @Autowired
    private userAuthorityService userAuthorityService;

    @Autowired
    private userService userService;

    //生成菜单JSON串
    //这里一定要指定produces为application/json，因为默认转发json串是text/plain（不可以用这种）
    @RequestMapping(path = "/menu/json", produces = "application/json")
    @ApiOperation("生成菜单JSON串")
    public String menujson() {
//        Menu[] arr=new Menu[1];
//
//        Menu menu = new Menu();
//
//        menu.setId(1);
//        menu.setTitle("工作空间");
//        menu.setType(0);
//        menu.setIcon("layui-icon layui-icon-console");
//        menu.setHref("");
//
//        ChildrenMenu[] childrenMenus=new ChildrenMenu[1];
//        ChildrenMenu c1 = new ChildrenMenu();
//
//        c1.setId(10);
//        c1.setTitle("控制后台");
//        c1.setIcon("layui-icon layui-icon-console");
//        c1.setType(1);
//        c1.setOpenType("_iframe");
//        c1.setHref("/pear/toconsole");
//
//        childrenMenus[0]=c1;
//        menu.setChildren(childrenMenus);
//
//        arr[0]=menu;
//
//        return JSON.toJSONString(arr);


        String username = springSecurityUtil.currentUser(session);
        int userid = userService.selectUseridByUserName(username);

        int authority = userAuthorityService.selectAuthorityID(userid);

        String menuData = menuService.selectMenuDataByAuthority(authority);

        String jsonData = JSON.toJSONString(menuData);
        String s = jsonData.replaceAll("\\\\", "");
         s= s.substring(1,s.length()-1);

        return s;
    }


}
