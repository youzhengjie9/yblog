package com.boot.utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

@Component
public class IpToAddressUtil {

    //使用腾讯位置服务的接口通过ip拿到城市信息
    private static final String KEY = "W57BZ-TVM3R-6PJWD-W5UG6-4LC4K-J2BKI"; //腾讯位置服务接口key

    public static String getCityInfo(String ip) {
        String s = sendGet(ip, KEY);
        Map map = JSONObject.parseObject(s, Map.class);
        String message = (String) map.get("message");
        if ("query ok".equals(message)) {
            Map result = (Map) map.get("result");
            Map addressInfo = (Map) result.get("ad_info");
            //下面的值可能查询不出来
            String nation = (String) addressInfo.get("nation");
            String province = (String) addressInfo.get("province");
            String city = (String) addressInfo.get("city");
            String district = (String) addressInfo.get("district");

            StringBuilder stringBuilder = new StringBuilder();

            if (StringUtils.isEmpty(nation)) {

                stringBuilder.append("暂未检测到所在地址");

            } else {

                stringBuilder.append(nation);

                if (!StringUtils.isEmpty(province)) {
                    stringBuilder.append("-" + province);

                    if (!StringUtils.isEmpty(city)) {
                        stringBuilder.append("-" + city);

                        if (!StringUtils.isEmpty(district)) {
                            stringBuilder.append("-" + district);
                        }
                    }

                }

            }

            return stringBuilder.toString();
        } else {
            System.out.println(message);
            return null;
        }
    }

    //根据在腾讯位置服务上申请的key进行请求操作
    public static String sendGet(String ip, String key) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = "https://apis.map.qq.com/ws/location/v1/ip?ip=" + ip + "&key=" + key;
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }

}
