package com.boot.controller.pearAdmin;

import com.alibaba.fastjson.JSON;
import com.boot.data.ResponseData.layuiJSON;
import com.boot.pojo.password;
import com.boot.pojo.userDetail;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller("pearUserController")
@RequestMapping(path = "/pear")
@CrossOrigin
public class userController {



    @ResponseBody
    @RequestMapping(path = "/userInfo")
    public String userInfo(userDetail userDetail, String email, MultipartFile file){
        System.out.println("============="+userDetail.toString()+email+"==file"+file);
        layuiJSON layuiJSON = new layuiJSON();
        layuiJSON.setMsg("666");
//        Map map = JSONObject.parseObject(jsonData, Map.class); //把前端传来的json转换成Map
//        System.out.println(map);
        layuiJSON.setSuccess(true);

        return JSON.toJSONString(layuiJSON);
    }
    @ResponseBody
    @RequestMapping(path = "/updatePassword")
    public String updatePassword(password password){

        System.out.println(password);

        layuiJSON layuiJSON = new layuiJSON();
        layuiJSON.setMsg("666");
        if(password.getNewPassword().equals(password.getCheckPassword())){
            layuiJSON.setSuccess(true);
        }else {
            layuiJSON.setSuccess(false);
        }
        return JSON.toJSONString(layuiJSON);
    }
}
