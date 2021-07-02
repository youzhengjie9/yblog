package com.boot.controller.pearAdmin;

import com.alibaba.fastjson.JSON;
import com.boot.data.ResponseData.layuiData;
import com.boot.data.ResponseData.layuiJSON;
import com.boot.pojo.blacklist;
import com.boot.pojo.userDetail;
import com.boot.service.blacklistService;
import com.boot.service.userDetailService;
import com.boot.utils.Commons;
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
public class blackListController {

    @Autowired
    private blacklistService blacklistService;

    private final String key = "intercept_ip_"; //拦截ip的key前缀

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private SpringSecurityUtil springSecurityUtil;

    @Autowired
    private userDetailService userDetailService;


    @ResponseBody
    @RequestMapping(path = "/blackData")
    public String blackData(@RequestParam(value = "page", defaultValue = "1") int page,
                            @RequestParam(value = "limit", defaultValue = "10") int limit) {

        layuiData<blacklist> blacklistlayuiData = new layuiData<>();

        PageHelper.startPage(page, limit);
        List<blacklist> blacklists = blacklistService.selectBlackList();

        int count = blacklistService.selectBlackCount();

        blacklistlayuiData.setCode(0);
        blacklistlayuiData.setMsg("");
        blacklistlayuiData.setData(blacklists);
        blacklistlayuiData.setCount(count);

        return JSON.toJSONString(blacklistlayuiData);
    }


    @GetMapping(path = "/delete/BlackList")
    @ResponseBody
    public String deleteBlackList(String ip, HttpSession session, HttpServletRequest request) {

        layuiJSON json = new layuiJSON();

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

    @PostMapping(path = "/add/BlackList")
    @ResponseBody
    public String addBlackList(String ip1, String ip2, String ip3, String ip4,
                               HttpSession session,
                               HttpServletRequest request) {

        layuiJSON json = new layuiJSON();
        try {
            String ip = ip1 + "." + ip2 + "." + ip3 + "." + ip4; //真正的ip
//        System.out.println(ip);
            blacklist blacklist = new blacklist();
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


    @ResponseBody
    @RequestMapping(path = "/batchRemove/blacklist/{ips}")
    public String batchRemoveBlackList(@PathVariable("ips") String ips) {

        layuiJSON json = new layuiJSON();

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

    //待实现（编辑黑名单）
    @ResponseBody
    @RequestMapping(path = "/update/blacklist")
    public String updateBlacklist(String oldIp,String newIp){

        layuiJSON json=new layuiJSON();
        try {

            blacklistService.updateBlackIp(oldIp, newIp);

            json.setSuccess(true);
            json.setMsg("修改成功");
            return JSON.toJSONString(json);
        }catch (Exception e){
            e.printStackTrace();
            json.setMsg("修改失败");
            json.setSuccess(false);
            return JSON.toJSONString(json);
        }

    }



}
