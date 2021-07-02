package com.boot.controller.pearAdmin;

import com.github.pagehelper.util.MSUtils;
import com.wf.captcha.ArithmeticCaptcha;
import com.wf.captcha.utils.CaptchaUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller("Captcha")
@RequestMapping(path = "/pear")
public class CaptchaController {


    /**
     * public static void out(Captcha captcha, HttpServletRequest request, HttpServletResponse response)
     * throws IOException {
     * setHeader(response);
     * request.getSession().setAttribute(SESSION_KEY, captcha.text().toLowerCase());
     * captcha.out(response.getOutputStream());
     * }
     */

    @RequestMapping("/captcha")
    public void captcha(HttpServletRequest request, HttpServletResponse response) throws Exception {

        // 设置请求头为输出图片类型
        response.setContentType("image/gif");
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);

        //字母类型
        //SpecCaptcha captcha = new SpecCaptcha(130, 48, 4);

        // 中文gif类型
        //ChineseGifCaptcha captcha = new ChineseGifCaptcha(130, 48);

        // 中文类型
        //ChineseCaptcha captcha = new ChineseCaptcha(130, 48);

        // 算术类型
        ArithmeticCaptcha captcha = new ArithmeticCaptcha(130, 48); // 图片的宽高
        captcha.setLen(2);  // 几位数运算，默认是两位
        captcha.getArithmeticString();  // 获取运算的公式：3+2=?
        captcha.text();  // 获取运算的结果：5

        //验证码存入session或存入redis
        //存redis通过UUID.randomUUID()生成key，specCaptcha.text().toLowerCase()生成value，存入redis并将key返回给前端，在登录时从redis中获取并验证
        //存session则如下，
        request.getSession().setAttribute("captcha", captcha.text().toLowerCase());
        // 输出图片流
        captcha.out(response.getOutputStream());
    }
}
