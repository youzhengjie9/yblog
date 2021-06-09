package com.boot.controller;

import com.boot.pojo.img;
import com.boot.pojo.userDetail;
import com.boot.pojo.visitor;
import com.boot.service.imgService;
import com.boot.service.userDetailService;
import com.boot.service.visitorService;
import com.boot.utils.*;
import io.swagger.annotations.Api;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author 游政杰
 * 2021/5/29
 */
@Controller
@RequestMapping(path = "/img")
@Api("图片附件控制器")
public class ImgController {

    /**
     * 文件附件上传
     */
    @Autowired
    private SpringSecurityUtil springSecurityUtil;

    @Autowired
    private userDetailService userDetailService;

    @Autowired
    private imgService imgService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private visitorService visitorService;

    private final int type=1;

//    private static final Object lock=new Object(); //悲观锁

    @RequestMapping(path = "/list")
    public String toFileList(HttpSession session, Model model, HttpServletRequest request,@Value("进入后台界面") String desc){

        //添加访客信息
        visitor visitor = visitorUtil.getVisitor(request, desc);
        String key = "visit_ip_" + visitor.getVisit_ip() + "_type_" + type;
        String s = (String) redisTemplate.opsForValue().get(key);
        if (StringUtils.isEmpty(s)) {
            visitorService.insertVisitor(visitor);
            //由ip和type组成的key放入redis缓存,5分钟内访问过的不再添加访客
            redisTemplate.opsForValue().set(key, "1", 60 * 5, TimeUnit.SECONDS);
        }

        List<img> imgs = imgService.selectAllImg();
        System.out.println(imgs);
        model.addAttribute("imgs",imgs);
        String username = springSecurityUtil.currentUser(session);
        userDetail userDetail = userDetailService.selectUserDetailByUserName(username);
        model.addAttribute("userDetail",userDetail);
        model.addAttribute("bootstrap",new bootstrap());
        model.addAttribute("commons", Commons.getInstance());
        return "back/img_list";
    }


    @RequestMapping(path = "/imgupload")
    public String fileupload(MultipartFile[] files,HttpSession session,Model model) throws IOException {


        if(files.length>0){ //如果有文件


                for (MultipartFile file : files) {
                    String bigImgPath = fileUtil.getBigImgPath();
                    String smallImgPath = fileUtil.getSmallImgPath();
                    if(!file.isEmpty()){
                        //处理大图
                        InputStream inputStream = file.getInputStream();
                        byte bytes[]=new byte[inputStream.available()];
                        inputStream.read(bytes);

                        String randomName = fileUtil.getRandomName();
                        String fileSuffix = fileUtil.getFileSuffix(file.getOriginalFilename()); //后缀名
                        bigImgPath+=randomName+"."+fileSuffix;
                        fileUtil.write(bigImgPath,bytes); //写入大图


                        //用google的图像处理工具处理缩略图

                        //缩略图文件名
                        String randomName2 = fileUtil.getRandomName(); //缩略图随机名
                        String fileSuffix2 = fileUtil.getFileSuffix(file.getOriginalFilename());
                        smallImgPath+=randomName2+"."+fileSuffix2;

                        Thumbnails.of(bigImgPath)
                                //图片尺寸
                                .size(256,256)
                                //输出质量，0-1 ，越大图片质量越好
                                .outputQuality(0.9f)
                                /**
                                 * keepAspectRatio(false) 默认是按照比例缩放的,所以把它关掉
                                 */
                                .keepAspectRatio(false)
                                //输出到的文件全名
                                .toFile(smallImgPath);

                        String i1="/big_img/"+randomName+"."+fileSuffix; //大图，存入数据库的地址
                        String i2="/small_img/"+randomName2+"."+fileSuffix;
                        imgService.addImgPath(i1,i2);
                    }
                }

            }




        List<img> imgs = imgService.selectAllImg();
        model.addAttribute("imgs",imgs);
        String username = springSecurityUtil.currentUser(session);
        userDetail userDetail = userDetailService.selectUserDetailByUserName(username);
        model.addAttribute("userDetail",userDetail);
        model.addAttribute("bootstrap",new bootstrap());
        model.addAttribute("commons", Commons.getInstance());
        return "back/img_list";
    }


    /**
     * 文件下载
     */
    @RequestMapping(path = "/dowmloadImg")
    public ResponseEntity<byte[]> dowmloadImg(@RequestParam(value = "f") int f) throws IOException {

        img img = imgService.selectImgByid(f);
        String staticPath = fileUtil.getStaticPath();
        String big_img = img.getBig_img();
        staticPath+=big_img;
        File file = new File(staticPath);
        FileInputStream fileInputStream = new FileInputStream(file);
        byte[] bytes=new byte[fileInputStream.available()];
        fileInputStream.read(bytes);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition","attachment;filename="+file.getName());
        ResponseEntity<byte[]> responseEntity = new ResponseEntity<byte[]>(bytes,headers, HttpStatus.OK);
        return responseEntity;
    }





}
