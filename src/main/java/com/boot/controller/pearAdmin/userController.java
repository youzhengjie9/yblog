package com.boot.controller.pearAdmin;

import com.alibaba.fastjson.JSON;
import com.boot.data.ResponseData.layuiJSON;
import com.boot.pojo.password;
import com.boot.pojo.userDetail;
import com.boot.service.userDetailService;
import com.boot.service.userService;
import com.boot.utils.SpringSecurityUtil;
import com.boot.utils.fileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author 游政杰
 *
 */
@Controller("pearUserController")
@RequestMapping(path = "/pear")
@CrossOrigin
public class userController {


    @Autowired
    private SpringSecurityUtil springSecurityUtil;

    @Autowired
    private userDetailService userDetailService;

    @Autowired
    private userService userService;

    @ResponseBody
    @RequestMapping(path = "/userInfo")
    public String userInfo(userDetail userDetail, String email, MultipartFile file, HttpSession session) throws IOException {

        layuiJSON json = new layuiJSON();

        try {

            boolean flag = file.isEmpty(); //判断是否是空
            String name = springSecurityUtil.currentUser(session);
            if(!flag){ //如果不是空的,说明传入了头像
                InputStream inputStream = file.getInputStream(); //获取文件流
                byte fileByteArray[] =new byte[inputStream.available()];
                inputStream.read(fileByteArray);//读取到一个字节数组中
                String userImagePath = fileUtil.writeImage(file.getOriginalFilename(),fileByteArray); //传入头像名

                userDetail.setIcon(userImagePath); //把头像路径设置进去



                //删除之前的头像
                com.boot.pojo.userDetail userDetail1 = userDetailService.selectUserDetailByUserName(name);

                String icon = userDetail1.getIcon();
                String iconPath=fileUtil.getStaticPath()+icon; //拼接头像地址
                File file1 = new File(iconPath);
                try {
                    if (file1.exists()){
                        file1.delete();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            userDetailService.updateUserDetail(userDetail);
            if(email!=null && !email.equals("")){
                userService.updateEmail(email,userDetail.getName());
            }

            json.setSuccess(true);
            json.setMsg("修改个人资料成功");

        }catch (Exception e){
            e.printStackTrace();
            json.setSuccess(false);
            json.setMsg("修改个人资料失败");
        }

        return JSON.toJSONString(json);
    }
    @ResponseBody
    @RequestMapping(path = "/updatePassword")
    public String updatePassword(password password,HttpSession session){

        layuiJSON layuiJSON = new layuiJSON();

        try {


            boolean mt= (password.getNewPassword().equals(password.getCheckPassword()))?true:false;

            String name = springSecurityUtil.currentUser(session);
            if(mt){
                //创建BCrypt加密器
                BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
                String password1 = userService.selectPasswordByuserName(name); //获取数据库已加密的密码
                boolean matches = passwordEncoder.matches(password.getOldPassword(), password1); //进行密码校对
                if(matches){ //如果匹配成功

                    //进行修改
                    String encodePassword = passwordEncoder.encode(password.getNewPassword()); //放入数据库
                    userService.updatePassword(name,encodePassword);

                    layuiJSON.setSuccess(true);
                    layuiJSON.setMsg("修改密码成功");
                    return JSON.toJSONString(layuiJSON);
                }else { //匹配失败（旧密码和数据库密码不一致）

                    layuiJSON.setSuccess(false);
                    layuiJSON.setMsg("输入的旧密码和数据库保存的密码不一致,修改失败");
                    return JSON.toJSONString(layuiJSON);
                }
            }else {
                layuiJSON.setSuccess(false);
                layuiJSON.setMsg("两次输入的新密码不一致,修改密码失败");
                return JSON.toJSONString(layuiJSON);
            }


        }catch (Exception e){
            e.printStackTrace();

            layuiJSON.setSuccess(false);
            layuiJSON.setMsg("修改密码失败");
            return JSON.toJSONString(layuiJSON);
        }


    }
}
