package com.boot.interceptor;

import com.boot.config.scanClassProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

@Component
public class visitorInterceptor implements HandlerInterceptor {

    @Autowired
    private scanClassProperties scanClassProperties;


    //遍历文件夹
    private void listFiles(String packageName) {
        File file = new File(packageName);

        while (true) {
            try {
                if(!file.exists()){
                    return;
                }else {

                    if(file.isDirectory()){
                        String name = file.getName();
                        listFiles(packageName+"\\"+name); //递归
                    }else if(file.isFile()){
                        System.out.println("文件："+file.getName());
                    }

                }



            }catch (Exception e){
                e.printStackTrace();
            }



        }

    }


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String p = scanClassProperties.getPackageName(); //获取全包名
        StringBuilder stringBuilder=new StringBuilder(); //封装格式化后的全包名
        for (int i = 0; i < p.length(); i++) { //进行格式化
            if(p.charAt(i)=='.'){
                stringBuilder.append("\\");
            }else {
                stringBuilder.append(p.charAt(i));
            }
        }
        String packageName = stringBuilder.toString(); //格式化后的全包名
        String s = (String) System.getProperties().get("user.dir"); //项目路径
        s+="\\src\\main\\java\\";
        String scanPackage=s+packageName; //最终结果

        listFiles(scanPackage);



        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
