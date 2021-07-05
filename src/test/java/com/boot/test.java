package com.boot;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.boot.config.scanClassProperties;
import com.boot.pojo.*;
import com.boot.service.*;
import com.boot.utils.IpToAddressUtil;
import com.boot.utils.SpringSecurityUtil;
import com.boot.utils.timeUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;
import javax.swing.*;
import java.io.File;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

@SpringBootTest
public class test {


    @Autowired
    private articleService articleService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private userDetailService userDetailService;

//    @Test
//    public void test(){
//
//        Random random = new Random();
//        for (int i = 0; i < 10; i++) {
//            int x = random.nextInt(5);
//            System.out.println(x);
//        }
//
//    }

    //删除缓存数据，初始化
//    @Test
//    public void deleteRedis() {
//        Set keys = redisTemplate.keys("*");
//        for (Object key : keys) {
//            redisTemplate.delete(key);
//        }
//    }


    /**
     * 导入tag数据
     */
    private Map<String, Integer> map = new LinkedHashMap<>();

    @Autowired
    private tagService tagService;

//    @Test
//    public void test1() {
//        List<Article> articles = articleService.selectTagsByArticle();
//        for (Article article : articles) {
//            String tags = article.getTags();
//            String[] split = tags.split(",");
//            for (String s : split) {
//
//                if (map.containsKey(s)) {
//                    int i = map.get(s);
//                    i++;
//                    map.put(s, i);
//
//                } else {
//                    map.put(s, 1);
//                }
//            }
//
//        }
//        Set<String> strings = map.keySet();
//        for (String string : strings) {
//            tag tag = new tag();
//            tag.setTagName(string);
//            tag.setTagCount(map.get(string));
//            tagService.addTag(tag);
//
//        }
//
//    }

    @Autowired
    private categoryService categoryService;

    @Autowired
    private SpringSecurityUtil springSecurityUtil;

    @Autowired
    private archiveService archiveService;

    @Test
    public void test2() {

//        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
//        String encode = bCryptPasswordEncoder.encode("123456");
//
//        boolean matches = bCryptPasswordEncoder.matches("123456", "$2a$10$5ooQI8dir8jv0/gCa1Six.GpzAdIPf6pMqdminZ/3ijYzivCyPlfK");
//        System.out.println(matches);
//

        List<archive> archives = archiveService.selectAllArchiveGroup();
        System.out.println(archives);

    }

    @Autowired
    private visitorService visitorService;


//    @Test
//    public void test3() {
////        String ipInfo = IpToAddressUtil.sendGet("120.85.62.189", "W57BZ-TVM3R-6PJWD-W5UG6-4LC4K-J2BKI");
////
////        JSONObject jsonObject = JSONObject.parseObject(ipInfo); //转换成json对象
////        System.out.println(jsonObject.getJSONObject("result").get("ip")); //ip
////        System.out.println(jsonObject.getJSONObject("result").getJSONObject("location").get("lng")); //经度
////        System.out.println(jsonObject.getJSONObject("result").getJSONObject("location").get("lat")); //纬度
////        System.out.println(jsonObject.getJSONObject("result").getJSONObject("ad_info").get("nation")); //国家
////        System.out.println(jsonObject.getJSONObject("result").getJSONObject("ad_info").get("province")); //省
////        System.out.println(jsonObject.getJSONObject("result").getJSONObject("ad_info").get("city")); //市
////        System.out.println(jsonObject.getJSONObject("result").getJSONObject("ad_info").get("district")); //区
////        System.out.println(jsonObject.getJSONObject("result").getJSONObject("ad_info").get("adcode")); //城市码
//
//        List<onedayVisitor> onedayVisitors = visitorService.selectOneDayVisitor();
//        System.out.println(onedayVisitors.get(0).getDay());
//        System.out.println(onedayVisitors.get(0).getCount());
//
//    }

//    @Test
//    public void test4() {
//        userDetail admin = userDetailService.selectUserDetailByUserName("admin");
//        userDetail adminsss = userDetailService.selectUserDetailByUserName("adminsss");
//        System.out.println("admin:" + admin);
//        System.out.println("adminsss:" + adminsss);
//    }

    @Autowired
    private scanClassProperties scanClassProperties;


//    /**
//     * 处理文件后缀
//     * @param name
//     * @return
//     */
//    private String cutFileName(String name) {
//        int index=-1; //记录最后一个点的index
//        //获取到最后一个'.'的index
//        for (int i = name.length()-1; i >=0 ; i--) {
//            if(name.charAt(i)=='.'){
//                index=i;
//                break;
//            }
//        }
//        String substring = name.substring(0, index);
//
//        return substring;
//    }
//
//
//    //遍历文件夹
//    private void listFiles(String packageName,String scanPackage) {
//        File file = new File(packageName);
//
//        if(!file.exists()){
//            return;
//        }
//        File[] files = file.listFiles(); //获取子文件
//        for (File f : files) {
//
//            if(f.isFile()){
//                String fileName=packageName+"\\"+f.getName(); //文件的绝对路径
//
//                String name = f.getName();
//                //这里的文件名是有后缀的,比如有.java,所以我们要进行处理
//                 String newName= cutFileName(name);
//
//                String javaClassFileName=scanPackage+"."+newName; //Java中文件的全类名
//                System.out.println(javaClassFileName);
//
//            }else if(f.isDirectory()){
//                listFiles(packageName+"\\"+f.getName(),scanPackage+"."+f.getName()); //递归
//            }
//
//        }
//
//    }
//
//
//
//    @Test
//    public void test5() {
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
//        listFiles(scanPackage,p);
//
//    }


    @Test
    public void test1(){
        //统计图表的访问记录


        List<String> days = visitorService.selectDaysBy7();
        Map<String,Integer> maps=new ConcurrentHashMap<>(); //维护时间--对应的访问量
        for (String day : days) {
            int count = visitorService.selectOneDayVisitor(day);
            maps.put(day,count);
        }

        Set<String> strings = maps.keySet();
        for (String string : strings) {
            System.out.println(string+"===>"+maps.get(string));
        }
    }

    @Test
    public void test3(){
        Object var1 =redisTemplate.opsForValue().get("echarts_days");
        Object var2 = redisTemplate.opsForValue().get("echarts_counts");

        List<String> ds = JSON.parseArray((String) var1, String.class);
        List<Integer> cs = JSON.parseArray((String) var2, Integer.class);

        System.out.println(ds);
        System.out.println(cs);
    }


    @Test
    public void test4(){

        userDetail userDetail = new userDetail();
        userDetail.setName("99110");
        userDetailService.addUserDetail(userDetail);

    }
    



}
