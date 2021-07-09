package com.boot.interceptor;

import com.boot.annotation.Visitor;
import com.boot.config.scanClassProperties;
import com.boot.pojo.visitor;
import com.boot.service.visitorService;
import com.boot.utils.visitorUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.concurrent.TimeUnit;

/**
 * @author 游政杰
 * @Time 2021/6/18 15:31
 */
@Component
public class visitorInterceptor implements HandlerInterceptor {

    /**
     * @Visitor注解的实现
     */

    @Autowired
    private scanClassProperties scanClassProperties;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private visitorService visitorService;

    private final int type = 1;

//    /**
//     * 处理文件后缀
//     *
//     * @param name
//     * @return
//     */
//    private String cutFileName(String name) {
//        int index = -1; //记录最后一个点的index
//        //获取到最后一个'.'的index
//        for (int i = name.length() - 1; i >= 0; i--) {
//            if (name.charAt(i) == '.') {
//                index = i;
//                break;
//            }
//        }
//        String substring = name.substring(0, index);
//
//        return substring;
//    }
//
//
//    /**
//     * 遍历文件夹，获取每个类的全类名
//     *
//     * @param packageName 操作系统中目录的绝对路径
//     * @param scanPackage Java中的全包名，com.boot.controller
//     */
//    private void listFiles(String packageName, String scanPackage) {
//        File file = new File(packageName);
//
//        if (!file.exists()) {
//            return;
//        }
//        File[] files = file.listFiles(); //获取子文件
//        for (File f : files) {
//
//            if (f.isFile()) {
//                String fileName = packageName + "\\" + f.getName(); //文件的绝对路径
//
//                String name = f.getName();
//                //这里的文件名是有后缀的,比如有.java,所以我们要进行处理
//                String newName = cutFileName(name);
//                String javaClassFileName = scanPackage + "." + newName; //Java中文件的全类名
//
//                //处理每个类的Visitor注解
//
//                handleAnnotation(javaClassFileName);
//
//
//            } else if (f.isDirectory()) {
//                listFiles(packageName + "\\" + f.getName(), scanPackage + "." + f.getName()); //递归
//            }
//
//        }
//
//    }
//
//    /**
//     * 处理扫描到的类的Visitor注解
//     *
//     * @param javaClassFileName
//     */
//    private void handleAnnotation(String javaClassFileName) throws ClassNotFoundException {
//
//        Class<?> aClass = Class.forName(javaClassFileName); //获取到该类的Class对象
//
//        Method[] methods = aClass.getMethods(); //获取这个类所有的方法
//
//        for (Method method : methods) {
//
//            Visitor visitor = method.getAnnotation(Visitor.class);
//            System.out.println("visitor:" + visitor + "method");
//
//        }
//
//
//    }


    private void addVisitor(HttpServletRequest request, String desc) {
        //添加访客信息
        visitor visitor = visitorUtil.getVisitor(request, desc);
        String key = "visit_ip_" + visitor.getVisit_ip() + "_type_" + type;
        String s = (String) redisTemplate.opsForValue().get(key);
        if (StringUtils.isEmpty(s)) {
            visitorService.insertVisitor(visitor);
            //由ip和type组成的key放入redis缓存,5分钟内访问过的不再添加访客
            redisTemplate.opsForValue().set(key, "1", 60 * 5, TimeUnit.SECONDS);
        }

    }


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object o, ModelAndView modelAndView) throws Exception {

//        String p = scanClassProperties.getPackageName(); //获取全包名
//        StringBuilder stringBuilder = new StringBuilder(); //封装格式化后的全包名
//        for (int i = 0; i < p.length(); i++) { //进行格式化
//            if (p.charAt(i) == '.') {
//                stringBuilder.append("\\");
//            } else {
//                stringBuilder.append(p.charAt(i));
//            }
//        }
//        String packageName = stringBuilder.toString(); //格式化后的全包名
//        String s = (String) System.getProperties().get("user.dir"); //项目路径
//        s += "\\src\\main\\java\\";
//        String scanPackage = s + packageName; //最终结果
//
//        listFiles(scanPackage, p);



        //获取控制器的名字（全类名）
            String controllerName = ((HandlerMethod) o).getBean().getClass().getName();
            //获取方法名
            String methodName = ((HandlerMethod) o).getMethod().getName();

        //        System.out.println(methodName);

            Class<?> aClass = Class.forName(controllerName);

            Method[] methods = aClass.getMethods();


            for (Method method : methods) {
                if (method.getName().equals(methodName)) { //指定方法名

                    Visitor visitor = method.getAnnotation(Visitor.class);

                    if (visitor != null) { //如果有这个注解

                        String desc = visitor.desc(); //获取注解值

                        this.addVisitor(request, desc);

                    }

                }





        }


    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
