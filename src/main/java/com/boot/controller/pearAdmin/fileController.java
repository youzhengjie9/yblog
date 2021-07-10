package com.boot.controller.pearAdmin;

import com.alibaba.fastjson.JSON;
import com.boot.annotation.Operation;
import com.boot.annotation.Visitor;
import com.boot.data.ResponseData.layuiJSON;
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
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.List;

@Controller("pearFileController")
@RequestMapping(path = "/pear")
@CrossOrigin
public class fileController {

    @Autowired
    private imgService imgService;

    @Autowired
    private SpringSecurityUtil springSecurityUtil;

    @Autowired
    private userDetailService userDetailService;

    //附件管理
    @Operation("进入附件管理页面")
    @Visitor(desc = "附件管理")
    @RequestMapping(path = "/toFileUpload")
    public String toFileUpload(Model model) {

        List<img> imgs = imgService.selectAllImg();
        model.addAttribute("imgs", imgs);


        return "back/newback/article/img_list";
    }


    @Operation("附件上传")
    @RequestMapping(path = "/file/upload")
    @ResponseBody
    public String fileupload(MultipartFile[] files, HttpSession session, Model model) throws IOException {

        layuiJSON json = new layuiJSON();
        if (files.length > 0) { //如果有文件


            try {

                for (MultipartFile file : files) {
                    String bigImgPath = fileUtil.getBigImgPath();
                    String smallImgPath = fileUtil.getSmallImgPath();
                    if (!file.isEmpty()) {
                        //处理大图
                        InputStream inputStream = file.getInputStream();
                        byte bytes[] = new byte[inputStream.available()];
                        inputStream.read(bytes);

                        String randomName = fileUtil.getRandomName();
                        String fileSuffix = fileUtil.getFileSuffix(file.getOriginalFilename()); //后缀名
                        bigImgPath += randomName + "." + fileSuffix;
                        fileUtil.write(bigImgPath, bytes); //写入大图


                        //用google的图像处理工具处理缩略图

                        //缩略图文件名
                        String randomName2 = fileUtil.getRandomName(); //缩略图随机名
                        String fileSuffix2 = fileUtil.getFileSuffix(file.getOriginalFilename());
                        smallImgPath += randomName2 + "." + fileSuffix2;

                        Thumbnails.of(bigImgPath)
                                //图片尺寸
                                .size(256, 256)
                                //输出质量，0-1 ，越大图片质量越好
                                .outputQuality(0.9f)
                                /**
                                 * keepAspectRatio(false) 默认是按照比例缩放的,所以把它关掉
                                 */
                                .keepAspectRatio(false)
                                //输出到的文件全名
                                .toFile(smallImgPath);

                        String i1 = "/big_img/" + randomName + "." + fileSuffix; //大图，存入数据库的地址
                        String i2 = "/small_img/" + randomName2 + "." + fileSuffix;
                        imgService.addImgPath(i1, i2);
                    }
                }
                json.setMsg("上传附件成功");
                json.setSuccess(true);
                return JSON.toJSONString(json);
            } catch (Exception e) {
                e.printStackTrace();
                json.setMsg("上传附件失败");
                json.setSuccess(false);
                return JSON.toJSONString(json);
            }


        } else { //如果没有文件

            json.setMsg("没有添加附件，上传失败");
            json.setSuccess(false);
            return JSON.toJSONString(json);
        }

    }


    /**
     * 文件下载
     */
    @RequestMapping(path = "/file/download")
    public ResponseEntity<byte[]> downloadFile(@RequestParam(value = "f") int f) throws IOException {

        img img = imgService.selectImgByid(f);
        String staticPath = fileUtil.getStaticPath();
        String big_img = img.getBig_img();
        staticPath += big_img;
        File file = new File(staticPath);
        FileInputStream fileInputStream = new FileInputStream(file);
        byte[] bytes = new byte[fileInputStream.available()];
        fileInputStream.read(bytes);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment;filename=" + file.getName());
        ResponseEntity<byte[]> responseEntity = new ResponseEntity<byte[]>(bytes, headers, HttpStatus.OK);
        return responseEntity;
    }

    /**
     * 附件删除(待实现）=========================
     */
    @Operation("附件删除")
    @ResponseBody
    @RequestMapping(path = "/file/delete")
    public String fileDelete(int id) throws FileNotFoundException {
        layuiJSON json=new layuiJSON();


        try {
            img img = imgService.selectImgByid(id);
            String big_img = img.getBig_img();
            String small_img = img.getSmall_img();

            String p = ResourceUtils.getURL("classpath:static").getPath();
            String path=p.substring(1,p.length());
            String big=path+big_img;
            String small=path+small_img;

            File bigFile = new File(big);
            File smallFile = new File(small);

            //删除服务器中的大图和缩略图文件
            if(bigFile.exists()){
                bigFile.delete();
            }
            if(smallFile.exists()){
                smallFile.delete();
            }

            //删除对应数据库记录
            imgService.deleteImgByid(id);
            json.setMsg("删除附件成功");
            json.setSuccess(true);
        }catch (Exception e){
            e.printStackTrace();
            json.setMsg("删除附件失败");
            json.setSuccess(false);
        }


        return JSON.toJSONString(json);
    }




}
