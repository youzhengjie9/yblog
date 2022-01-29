package com.boot.controller.pearAdmin;

import com.alibaba.fastjson.JSON;
import com.boot.annotation.Operation;
import com.boot.data.ResponseData.LayuiData;
import com.boot.data.ResponseData.LayuiJSON;
import com.boot.pojo.BlackList;
import com.boot.service.BlackListService;
import com.boot.service.UserDetailService;
import com.boot.utils.IpToAddressUtil;
import com.boot.utils.SpringSecurityUtil;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @author 游政杰
 * @Date 2021/6/30
 */
@Controller(value = "pearBlackListController")
@RequestMapping(path = "/pear")
@CrossOrigin
public class BlackListController {

    @Autowired
    private BlackListService blacklistService;

    private final String key = "intercept_ip_"; //拦截ip的key前缀

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private SpringSecurityUtil springSecurityUtil;

    @Autowired
    private UserDetailService userDetailService;


    @ResponseBody
    @RequestMapping(path = "/blackData")
    public String blackData(@RequestParam(value = "page", defaultValue = "1") int page,
                            @RequestParam(value = "limit", defaultValue = "10") int limit) {

        LayuiData<BlackList> blacklistlayuiData = new LayuiData<>();

        PageHelper.startPage(page, limit);
        List<BlackList> blacklists = blacklistService.selectBlackList();

        int count = blacklistService.selectBlackCount();

        blacklistlayuiData.setCode(0);
        blacklistlayuiData.setMsg("");
        blacklistlayuiData.setData(blacklists);
        blacklistlayuiData.setCount(count);

        return JSON.toJSONString(blacklistlayuiData);
    }


    @Operation("删除黑名单IP")
    @GetMapping(path = "/delete/BlackList")
    @ResponseBody
    public String deleteBlackList(String ip, HttpSession session, HttpServletRequest request) {

        LayuiJSON json = new LayuiJSON();

        try {

            blacklistService.deleteBlackListByIp(ip);
            redisTemplate.delete(key + ip); //从缓存中删除这个key

            json.setMsg("该用户被移除黑名单");
            json.setSuccess(true);
            return JSON.toJSONString(json);
        } catch (Exception e) {
            e.printStackTrace();
            json.setMsg("移除黑名单失败");
            json.setSuccess(false);
            return JSON.toJSONString(json);
        }

    }

    @RequestMapping(path = "/toModifyBlackPage")
    public String toModifyBlackPage(String oldIP, Model model) {

        model.addAttribute("oldIP", oldIP);
        return "back/newback/article/module/modifyBlack";
    }

    @RequestMapping(path = "/toaddBlackPage")
    public String toaddBlackPage(Model model) {

        return "back/newback/article/module/addBlack";
    }

    @Operation("新增黑名单IP")
    @PostMapping(path = "/add/BlackList")
    @ResponseBody
    public String addBlackList(String ip1, String ip2, String ip3, String ip4,
                               HttpSession session,
                               HttpServletRequest request) {

        LayuiJSON json = new LayuiJSON();
        try {
            String ip = ip1 + "." + ip2 + "." + ip3 + "." + ip4; //真正的ip
//        System.out.println(ip);
            BlackList blacklist = new BlackList();
            blacklist.setBlack_ip(ip);
            //通过前端传来的ip去获取地址
            String cityInfo = IpToAddressUtil.getCityInfo(ip);

            blacklist.setBlack_address(cityInfo);
            blacklistService.addBlackList(blacklist);

            json.setSuccess(true);
            json.setMsg("新增黑名单IP成功");
            return JSON.toJSONString(json);
        } catch (Exception e) {
            e.printStackTrace();
            json.setMsg("新增黑名单IP失败");
            json.setSuccess(false);
            return JSON.toJSONString(json);
        }
    }


    @Operation("批量删除黑名单IP")
    @ResponseBody
    @RequestMapping(path = "/batchRemove/blacklist/{ips}")
    public String batchRemoveBlackList(@PathVariable("ips") String ips) {

        LayuiJSON json = new LayuiJSON();

        try {

            String[] split = ips.split(",");

            for (String ip : split) {
                blacklistService.deleteBlackListByIp(ip);
            }

            json.setMsg("批量移除黑名单IP成功");
            json.setSuccess(true);
            return JSON.toJSONString(json);
        } catch (Exception e) {
            e.printStackTrace();
            json.setMsg("批量移除黑名单IP失败");
            json.setSuccess(false);
            return JSON.toJSONString(json);
        }

    }

    @Operation("修改黑名单IP")
    @ResponseBody
    @RequestMapping(path = "/update/blacklist")
    public String updateBlacklist(String oldIp, String ip1, String ip2, String ip3, String ip4) {

        String newIp = ip1 + "." + ip2 + "." + ip3 + "." + ip4;


        System.out.println(oldIp+"===>"+newIp);

        LayuiJSON json = new LayuiJSON();
        try {

            blacklistService.updateBlackIp(oldIp, newIp);
            redisTemplate.delete(key + oldIp);

            json.setSuccess(true);
            json.setMsg("修改成功");
            return JSON.toJSONString(json);
        } catch (Exception e) {
            e.printStackTrace();
            json.setMsg("修改失败");
            json.setSuccess(false);
            return JSON.toJSONString(json);
        }

    }


}
