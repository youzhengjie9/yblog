package com.boot.controller;

import com.boot.pojo.img;
import com.boot.pojo.userDetail;
import com.boot.service.imgService;
import com.boot.service.userDetailService;
import com.boot.utils.Commons;
import com.boot.utils.SpringSecurityUtil;
import com.boot.utils.bootstrap;
import com.boot.utils.fileUtil;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping(path = "/img")
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

//    private static final Object lock=new Object(); //悲观锁

    @RequestMapping(path = "/list")
    public String toFileList(HttpSession session, Model model){


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



}
