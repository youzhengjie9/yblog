package com.boot.utils;

import com.boot.pojo.visitor;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

import java.sql.Date;
import java.text.SimpleDateFormat;

import static com.boot.utils.browserOS.getBrowserName;
import static com.boot.utils.browserOS.getOsName;

@Component
public final class visitorUtil {


    /**
     * 生成visitor对象
     *
     * @param request
     * @return
     */
    public static final visitor getVisitor(HttpServletRequest request, String desc) {
        String ipAddr = ipUtils.getIpAddr(request); //获取ip
        String cityInfo = IpToAddressUtil.getCityInfo(ipAddr); //获取访问者城市信息
        String browserName = getBrowserName(request); //获取浏览器名
        String osName = getOsName(request); //获取访问者操作系统

        visitor visitor = new visitor();
        visitor.setVisit_ip(ipAddr);
        /**
         * 本地访问（也就是没有部署到外网）的情况下，会检测不到所在地址，这时候给它一个默认值
         */
        if (cityInfo == null || cityInfo.equals("")) {
            visitor.setVisit_address("暂未检测到所在地址");
        } else {
            visitor.setVisit_address(cityInfo);
        }
        visitor.setBrowser(browserName);
        visitor.setOs(osName);
        visitor.setVisit_describe(desc);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        java.sql.Date date = new Date(new java.util.Date().getTime());
        String time = simpleDateFormat.format(date);
        visitor.setVisit_time(time);
        return visitor;
    }


}
