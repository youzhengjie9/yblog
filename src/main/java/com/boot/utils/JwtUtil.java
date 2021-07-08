package com.boot.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtUtil {

    private static final long EXPIRE = 200; //过期时间

    public static final SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);//密钥，动态生成的密钥

    /**
     * 生成token
     *
     * @param claims 要传送消息map
     * @return
     */
    public static String generate(Map<String, Object> claims) {
        Date nowDate = new Date();
        //过期时间
        Date expireDate = new Date(System.currentTimeMillis() + EXPIRE);
        //头部信息,可有可无
        Map<String, Object> header = new HashMap<>(2);
        header.put("typ", "jwt");

        //更强的密钥,JDK11起才能用
        //  KeyPair keyPair = Keys.keyPairFor(SignatureAlgorithm.RS256);
        //  PrivateKey key1 =  keyPair.getPrivate();  // 私钥
        //PublicKey key2 =  keyPair.getPublic();  //公钥

        return Jwts.builder().setHeader(header)
                // .setSubject("weimi")//主题
                // .setIssuer("weimi") //发送方
                .setClaims(claims)  //自定义claims
                .setIssuedAt(nowDate)//当前时间
                .setExpiration(expireDate) //过期时间
                .signWith(key)//签名算法和key
                .compact();
    }

    /**
     * 生成token
     *
     * @param header 传入头部信息map
     * @param claims 要传送消息map
     * @return
     */
    public static String generate(Map<String, Object> header, Map<String, Object> claims) {
        Date nowDate = new Date();
        //过期时间
        Date expireDate = new Date(System.currentTimeMillis() + EXPIRE);

        return Jwts.builder().setHeader(header)
                // .setSubject("weimi")//主题
                //    .setIssuer("weimi") //发送方
                .setClaims(claims)  //自定义claims
                .setIssuedAt(nowDate)//当前时间
                .setExpiration(expireDate) //过期时间
                .signWith(key)//签名算法和key
                .compact();
    }

    /**
     * 校验是不是jwt签名
     *
     * @param token
     * @return
     */
    public static boolean isSigned(String token) {
        return Jwts.parser()
                .setSigningKey(key)
                .isSigned(token);
    }

    /**
     * 校验签名是否正确
     *
     * @param token
     * @return
     */
    public static boolean verify(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(key)
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    /**
     * 获取payload 部分内容（即要传的信息）
     * 使用方法：如获取userId：getClaim(token).get("userId");
     *
     * @param token
     * @return
     */
    public static Claims getClaim(String token) {
        Claims claims = null;
        try {
            claims = Jwts.parser()
                    .setSigningKey(key)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return claims;
    }

    /**
     * 获取头部信息map
     * 使用方法 : getHeader(token).get("alg");
     *
     * @param token
     * @return
     */
    public static JwsHeader getHeader(String token) {
        JwsHeader header = null;
        try {
            header = Jwts.parser()
                    .setSigningKey(key)
                    .parseClaimsJws(token)
                    .getHeader();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return header;
    }

    /**
     * 获取jwt发布时间
     */
    public static Date getIssuedAt(String token) {
        return getClaim(token).getIssuedAt();
    }

    /**
     * 获取jwt失效时间
     */
    public static Date getExpiration(String token) {
        return getClaim(token).getExpiration();
    }

    /**
     * 验证token是否失效
     *
     * @param token
     * @return true:过期   false:没过期
     */
    public static boolean isExpired(String token) {
        try {
            final Date expiration = getExpiration(token);
            return expiration.before(new Date());
        } catch (ExpiredJwtException expiredJwtException) {
            return true;
        }
    }

    /**
     * 直接Base64解密获取header内容
     *
     * @param token
     * @return
     */
    public static String getHeaderByBase64(String token) {
        String header = null;
        if (isSigned(token)) {
            try {
                byte[] header_byte = Base64.getDecoder().decode(token.split("\\.")[0]);
                header = new String(header_byte);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return header;
    }

    /**
     * 直接Base64解密获取payload内容
     *
     * @param token
     * @return
     */
    public static String getPayloadByBase64(String token) {
        String payload = null;
        if (isSigned(token)) {
            try {
                byte[] payload_byte = Base64.getDecoder().decode(token.split("\\.")[1]);
                payload = new String(payload_byte);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return payload;
    }

//    public static void main(String[] args) {
//        //用户自定义信息claims
//        Map<String, Object> map = new HashMap<>();
//        map.put("userId", "test122");
//        String token = generate(map);
//        System.out.println(token);
//
//        System.out.println("claim:" + getClaim(token).get("userId"));
//        System.out.println("header:" + getHeader(token));
//        //    System.out.println(getIssuedAt(token));
//        Claims claims = getClaim(token);
//
//        //  System.out.println(getHeaderByBase64(token));
//        System.out.println(getPayloadByBase64(token));
//
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy‐MM‐dd hh:mm:ss");
//        System.out.println("签发时间:" + sdf.format(claims.getIssuedAt()));
//        System.out.println("过期时间:" + sdf.format(claims.getExpiration()));
//        System.out.println("当前时间:" + sdf.format(new Date()));
//
//    }

}
