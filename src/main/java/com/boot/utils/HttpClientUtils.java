package com.boot.utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;
import java.util.*;

/**
 * @author 游政杰
 */
@Component
public class HttpClientUtils {

  /**
   * 发送get请求，利用java代码发送请求
   *
   * @param url
   * @return
   * @throws Exception
   */
  public static String doGet(String url, Map<String, String> headers) throws Exception {

    CloseableHttpClient httpclient = HttpClients.createDefault();

    HttpGet httpGet = new HttpGet(url);

    // 添加请求头
    if (headers != null) {
      Set<String> keys = headers.keySet();
      for (String key : keys) {
        httpGet.addHeader(key, headers.get(key));
      }
    }

    // 发送了一个http请求
    CloseableHttpResponse response = httpclient.execute(httpGet);
    // 如果响应200成功,解析响应结果
    if (response.getStatusLine().getStatusCode() == 200) {
      // 获取响应的内容
      HttpEntity responseEntity = response.getEntity();

      return EntityUtils.toString(responseEntity);
    }
    return null;
  }
}
