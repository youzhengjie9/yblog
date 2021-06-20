package com.boot.utils;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.Map;

public class HttpUtil {

    /**
     * 发送POST请求
     * @param url 请求的接口路径
     * @param params 参数
     * @return
     * @throws IOException
     */
    public static String post(String url, Map<String, String> params) throws IOException {
        CloseableHttpClient client = HttpClients.createDefault();
        StringBuilder stringBuilder = new StringBuilder(url);


        stringBuilder.append("?grant_type=");
        stringBuilder.append(params.get("grant_type"));


        stringBuilder.append("&code=");
        stringBuilder.append(params.get("code"));

        stringBuilder.append("&client_id=");
        stringBuilder.append(params.get("client_id"));

        stringBuilder.append("&redirect_uri=");
        stringBuilder.append(params.get("redirect_uri"));

        stringBuilder.append("&client_secret=");
        stringBuilder.append(params.get("client_secret"));

//        System.out.println("stringBuilder.toString():"+stringBuilder.toString());

        HttpPost httpPost = new HttpPost(stringBuilder.toString());
        httpPost.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36)");
        //发送请求返回响应的信息
        CloseableHttpResponse response = client.execute(httpPost);
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            String result = EntityUtils.toString(entity, "UTF-8");
            return result;
        }
        return null;
    }

    public static String get(String url, Object access_token) throws IOException {
        CloseableHttpClient client = HttpClients.createDefault();
        StringBuilder stringBuilder = new StringBuilder(url);
        //第一个参数
        stringBuilder.append("?access_token=");
        stringBuilder.append(access_token);
//        System.out.println("stringBuilder.toString():"+stringBuilder.toString());
        HttpGet httpGet = new HttpGet(stringBuilder.toString());
        httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36)");
        //发送请求返回响应的信息
        CloseableHttpResponse response = client.execute(httpGet);
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            String result = EntityUtils.toString(entity, "UTF-8");
            return result;
        }
        return null;
    }
}
