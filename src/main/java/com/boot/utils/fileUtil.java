package com.boot.utils;

import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.util.UUID;

@Component
public final class fileUtil {


    /**
     * 文件工具类
     *
     * @return
     */



    //获取文件后缀名（会转换成小写）
    private static final String getFileSuffix(String originalFilename){

        //获取头像后缀名 ,采用导入遍历，遇到“.”就break ，保存后缀名
        StringBuilder stringBuilder=new StringBuilder();
        for (int x=originalFilename.length()-1;x>=0;x--){
            if(originalFilename.charAt(x)=='.'){
                break;
            }else {
                stringBuilder.append(originalFilename.charAt(x));
            }
        }
        //因为上面获取的后缀名是倒序的，所以我们要反转字符串
        String post="";
        String s = stringBuilder.toString();
        for (int i = s.length()-1; i >=0 ; i--) {
            post+=s.charAt(i);
        }
        //后缀名变成小写
        String post_lower = post.toLowerCase();


        return post_lower;

    }

    private static final String getRandomName(){
        String s = UUID.randomUUID().toString();
        String s1 = s.replaceAll("-", "");
        return s1;
    }

    //获取静态路径
    private static final String getStaticPath() throws FileNotFoundException {
        String path = ResourceUtils.getURL("classpath:static").getPath();
        String substring = path.substring(1, path.length());
        return substring;
    }

    private static final void write(String path,byte[] fileByteArray) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(new File(path));
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);

        bufferedOutputStream.write(fileByteArray);


        bufferedOutputStream.flush(); //刷新缓冲区
        bufferedOutputStream.close();
        fileOutputStream.close();
    }


    //获取用户头像所在地址
    public static final String writeImage(String originalFilename, byte[] fileByteArray) throws IOException {

        String staticPath = fileUtil.getStaticPath();

        String usericonPath_datebase="/user_img/"; //存入数据库的地址
        String randomName = fileUtil.getRandomName();
        String fileSuffix = fileUtil.getFileSuffix(originalFilename); //获取文件后缀名
        String fileName=randomName+"."+fileSuffix;
        usericonPath_datebase+=fileName;


        staticPath+=usericonPath_datebase;

        //写文件
        write(staticPath,fileByteArray);

        return usericonPath_datebase; //返回需要存储到数据库的图片地址
    }






}
