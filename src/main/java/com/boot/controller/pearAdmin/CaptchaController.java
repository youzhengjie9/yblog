package com.boot.controller.pearAdmin;

import com.wf.captcha.ArithmeticCaptcha;
import com.wf.captcha.utils.CaptchaUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller("Captcha")
@RequestMapping(path = "/pear")
public class CaptchaController {

    @RequestMapping("/captcha")
    public void captcha(HttpServletRequest request, HttpServletResponse response) throws Exception {

        // 设置位数
        CaptchaUtil.out(5, request, response);
        // 设置宽、高、位数
        CaptchaUtil.out(130, 48, 5, request, response);

        // 使用算数验证码
        ArithmeticCaptcha  captcha = new ArithmeticCaptcha(130,48,4);
        captcha.setLen(3);  // 几位数运算，默认是两位
        captcha.getArithmeticString();  // 获取运算的公式：3+2=?
        captcha.text();  // 获取运算的结果：5

        CaptchaUtil.out(request, response); //输出验证码
    }
}
