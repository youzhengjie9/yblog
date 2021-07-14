# yblog

<p align=center>
  <a href="#">
    <img src="https://gitee.com/youzhengjie/springBootBlog/raw/master/image/bloglogo.jpg" alt="yblog" style="width:200px;height:200px">
  </a>
</p>
<p align=center>
   yblog,一个简单、功能齐全的SpringBoot博客系统
</p>

&emsp;&emsp;yblog是基于SpringBoot架构开发的博客：**博文管理**、**统计图表**、**接口监控**、**访问记录**、**附件管理**、**用户管理**、**友链管理**、**监控管理**、**抓取博文**，以及**第三方登录**等功能，且一直会对本项目进行加强，请各位大佬多多指点，一起共同进步。
文章无需自己写，可以使用作者自己编写的全自动爬虫工具即可，只需轻轻一点，万千文章到手。

### 仓库地址
GitHub: https://github.com/youzhengjie9/yblog <br/>
Gitee: https://gitee.com/youzhengjie/springBootBlog

### 目标
> **虽说博客系统已不是新鲜玩意，但是我觉得把一个普通的项目做的更完善、功能更多的话你就不普通，我也会秉承着这个理念一直把项目完善下去**<br/>
> **本人是大二的菜鸟，项目中如有不足，请大佬赐教，一起共同进步吧！！！**

### 技术栈
#### 后端

| 名称                | 官网                                                         |
| -----------------   | ------------------------------------------------------------ |
| Spring Boot             | https://spring.io/projects/spring-boot               | 
| Redis             | http://www.redis.cn/               | 
| RabbitMQ                   |  https://www.rabbitmq.com/                                  |
| elasticSearch           |    https://www.elastic.co/cn/elasticsearch/                  |
| MyBatis             | http://www.mybatis.org/mybatis-3/zh/index.html               |         
| Spring Security        | https://spring.io/projects/spring-security/                                   |
| PageHelper         | http://git.oschina.net/free/Mybatis_PageHelper               |
| Maven              | http://maven.apache.org/                                     |
| MySQL              | https://www.mysql.com/                                       |                                  |
| Swagger2                  | https://swagger.io/               |
| Druid                       |    https://github.com/alibaba/druid                    |
| fastjson                          |   https://github.com/alibaba/fastjson/                |
| log4j                     |   http://logging.apache.org/log4j/1.2/  |
| thumbnailator                         |   https://github.com/coobird/thumbnailator                   |
| Nginx                       |     http://nginx.org/en/download.html
#### 前端

| 名称            | 描述       | 官网                                                     |
| --------------- | ---------- | -------------------------------------------------------- |
| jQuery          | 函数库     | http://jquery.com/                                       |
| Bootstrap       | 前端框架   | https://v3.bootcss.com/                                |
| echarts         | 可视化图表库       | https://echarts.apache.org/zh/index.html        |                        |                             |
| Thymeleaf     | 模板引擎                | https://www.thymeleaf.org/      |
| TinyMCE        |  富文本编辑器         |  http://tinymce.ax-z.cn/  |
| alertJs          |弹框插件          |  https://gitee.com/ydq/alertjs
| layui           | 模块化前端UI框架        | https://www.layui.com/         |
#### 安装教程
##### Windows部署
* 1.找到sql包的sql文件并导入到自己的数据库中
* 2.修改application.yml中的redis、mysql、elasticsearch、邮件发送校验码
、rabbitmq即可
* 3.启动redis、mysql、elasticsearch、rabbitmq的服务
* 4.执行elasticSearchTest的es创建索引命令和执行下面的添加数据代码

* 第一点注意：如需使用爬虫功能，则需要对mysql做出如下配置。
* 1.修改mysql的配置文件mysql/bin/my.ini, 添加如下内容：
```text
[client]
default-character-set=utf8mb4
 
[mysql]
default-character-set=utf8mb4
 
[mysqld]
character-set-client-handshake=FALSE
character-set-server=utf8mb4
collation-server=utf8mb4_unicode_ci
init_connect='SET NAMES utf8mb4'
```
* 2.重启数据库服务。点击此电脑，右键打开管理，点击服务和应用程序、点击服务、找到MYSQL服务
，右键重新启动即可
* 3.进入mysql命令行，输入ALTER TABLE TABLE_NAME CONVERT TO CHARACTER SET utf8mb4;
把mysql的utf8编码切换成utf8mb4，以支持爬取的emoji表情，不然遇到4字节的宽字符就会插入异常了。

* 第二点注意：本项目使用了Nginx做负载均衡，本机器的nginx.conf文件如下：
```text
worker_processes  1;

events {
    worker_connections  1024;
}

http {
    include       mime.types;
    default_type  application/octet-stream;

    sendfile        on;
    keepalive_timeout  65;

upstream yblog.cn{

	server localhost:8080 weight=1;
	server localhost:8081 weight=1;
	server localhost:8082 weight=1;
}
    server {
        listen       80;
        server_name  localhost;


        location / {
            root   html;
            index  index.html index.htm;
			proxy_pass http://yblog.cn;
        }      
        error_page   500 502 503 504  /50x.html;
        location = /50x.html {
            root   html;
        }
    
    }
}
```
* 在Run/Debug Configurations的SpringBoot environment设置VM options
* 把三个服务分别设置成-Dserver.port=8080、-Dserver.port=8081、-Dserver.port=8082
* 所以我们只需要把项目的8080、8081、8082端口打开，然后访问localhost:80即可实现负载均衡。


* 评论模块需要自己去https://www.leancloud.cn/ 注册，获取AppID、AppKey，并加入到下面对应的js
```js
new Valine({
        el: '#vcomments',
        appId: 'xxx',
        appKey: 'yyy',
        placeholder: '请输入内容',
        pageSize: 3 ,
        recordIP: true,
        avatar:'',
        requiredFields: ['nick']
    });
````
，弄好了之后评论功能就能运行了。


* 执行下面的SQL语句，往数据库添加后台菜单数据：
  ```sql
  INSERT INTO `t_menu` VALUES (1, 1, '[{\"id\":1,\"title\":\"工作空间\",\"type\":0,\"icon\":\"layui-icon layui-icon-console\",\"href\":\"\",\"children\":[{\"id\":10,\"title\":\"控制后台\",\"icon\":\"layui-icon layui-icon-console\",\"type\":1,\"openType\":\"_iframe\",\"href\":\"/pear/toconsole\"},{\"id\":14,\"title\":\"百度一下\",\"icon\":\"layui-icon layui-icon-console\",\"type\":1,\"openType\":\"_iframe\",\"href\":\"http://www.baidu.com\"}]},{\"id\":\"component\",\"title\":\"文章管理\",\"icon\":\"layui-icon layui-icon-component\",\"type\":0,\"href\":\"\",\"children\":[{\"id\":203,\"title\":\"发布文章\",\"icon\":\"layui-icon layui-icon-console\",\"type\":1,\"openType\":\"_iframe\",\"href\":\"/pear/topublish\"},{\"id\":205,\"title\":\"文章管理\",\"icon\":\"layui-icon layui-icon-console\",\"type\":1,\"openType\":\"_iframe\",\"href\":\"/pear/toArticleManager\"},{\"id\":207,\"title\":\"分类管理\",\"icon\":\"layui-icon layui-icon-console\",\"type\":1,\"openType\":\"_iframe\",\"href\":\"/pear/toCategory\"},{\"id\":208,\"title\":\"标签管理\",\"icon\":\"layui-icon layui-icon-console\",\"type\":1,\"openType\":\"_iframe\",\"href\":\"/pear/toTag\"}]},{\"id\":\"result\",\"title\":\"常用工具\",\"icon\":\"layui-icon layui-icon-auz\",\"type\":0,\"href\":\"\",\"children\":[{\"id\":\"success\",\"title\":\"附件管理\",\"icon\":\"layui-icon layui-icon-face-smile\",\"type\":1,\"openType\":\"_iframe\",\"href\":\"/pear/toFileUpload\"},{\"id\":\"failure\",\"title\":\"爬取数据\",\"icon\":\"layui-icon layui-icon-face-cry\",\"type\":1,\"openType\":\"_iframe\",\"href\":\"/pear/toCatchData\"}]},{\"id\":\"error\",\"title\":\"用户管理\",\"icon\":\"layui-icon layui-icon-face-cry\",\"type\":0,\"href\":\"\",\"children\":[{\"id\":403,\"title\":\"用户管理\",\"icon\":\"layui-icon layui-icon-face-smile\",\"type\":1,\"openType\":\"_iframe\",\"href\":\"/pear/toUserManager\"},{\"id\":404,\"title\":\"友链管理\",\"icon\":\"layui-icon layui-icon-face-cry\",\"type\":1,\"openType\":\"_iframe\",\"href\":\"/pear/toLink\"},{\"id\":500,\"title\":\"个人资料\",\"icon\":\"layui-icon layui-icon-face-cry\",\"type\":1,\"openType\":\"_iframe\",\"href\":\"/pear/touser\"}]},{\"id\":\"system\",\"title\":\"访客管理\",\"icon\":\"layui-icon layui-icon-set-fill\",\"type\":0,\"href\":\"\",\"children\":[{\"id\":601,\"title\":\"访客记录\",\"icon\":\"layui-icon layui-icon-face-smile\",\"type\":1,\"openType\":\"_iframe\",\"href\":\"/pear/toVisitor\"},{\"id\":602,\"title\":\"黑名单\",\"icon\":\"layui-icon layui-icon-face-cry\",\"type\":1,\"openType\":\"_iframe\",\"href\":\"/pear/toBlack\"},{\"id\":604,\"title\":\"拦截记录\",\"icon\":\"layui-icon layui-icon-face-cry\",\"type\":1,\"openType\":\"_iframe\",\"href\":\"/pear/toInterceptorLog\"},{\"id\":605,\"title\":\"行为日志\",\"icon\":\"layui-icon layui-icon-face-cry\",\"type\":1,\"openType\":\"_iframe\",\"href\":\"/pear/toLog\"}]},{\"id\":\"common\",\"title\":\"监控管理\",\"icon\":\"layui-icon layui-icon-component\",\"type\":0,\"href\":\"\",\"children\":[{\"id\":701,\"title\":\"数据监控\",\"icon\":\"layui-icon layui-icon-console\",\"type\":0,\"children\":[{\"id\":2011,\"title\":\"RabbitMQ\",\"icon\":\"layui-icon layui-icon-face-smile\",\"type\":1,\"openType\":\"_iframe\",\"href\":\"http://localhost:15672/\"},{\"id\":2014,\"title\":\"ElasticSearch\",\"icon\":\"layui-icon layui-icon-face-cry\",\"type\":1,\"openType\":\"_iframe\",\"href\":\"http://localhost:5601/\"},{\"id\":2010,\"title\":\"Druid\",\"icon\":\"layui-icon layui-icon-face-cry\",\"type\":1,\"openType\":\"_iframe\",\"href\":\"http://127.0.0.1:8080/druid/index.html\"}]},{\"id\":2017,\"title\":\"接口监控\",\"icon\":\"layui-icon layui-icon-face-smile\",\"type\":1,\"openType\":\"_iframe\",\"href\":\"/pear/monitorInterface\"}]},{\"id\":\"echarts\",\"title\":\"数据图表\",\"icon\":\"layui-icon layui-icon-chart\",\"type\":0,\"href\":\"\",\"children\":[{\"id\":12121,\"title\":\"数据图表\",\"icon\":\"layui-icon layui-icon-face-smile\",\"type\":1,\"openType\":\"_iframe\",\"href\":\"/pear/toEcharts\"}]},{\"id\":\"code\",\"title\":\"系统设置\",\"icon\":\"layui-icon layui-icon-util\",\"type\":0,\"href\":\"\",\"children\":[{\"id\":801,\"title\":\"系统设置\",\"icon\":\"layui-icon layui-icon-util\",\"type\":1,\"openType\":\"_iframe\",\"href\":\"/pear/toSetting\"}]}]');
  INSERT INTO `t_menu` VALUES (2, 2, '[{\"id\":1,\"title\":\"工作空间\",\"type\":0,\"icon\":\"layui-icon layui-icon-console\",\"href\":\"\",\"children\":[{\"id\":10,\"title\":\"控制后台\",\"icon\":\"layui-icon layui-icon-console\",\"type\":1,\"openType\":\"_iframe\",\"href\":\"/pear/toconsole\"},{\"id\":14,\"title\":\"百度一下\",\"icon\":\"layui-icon layui-icon-console\",\"type\":1,\"openType\":\"_iframe\",\"href\":\"http://www.baidu.com\"}]},{\"id\":\"component\",\"title\":\"文章管理\",\"icon\":\"layui-icon layui-icon-component\",\"type\":0,\"href\":\"\",\"children\":[{\"id\":203,\"title\":\"发布文章\",\"icon\":\"layui-icon layui-icon-console\",\"type\":1,\"openType\":\"_iframe\",\"href\":\"/pear/topublish\"},{\"id\":208,\"title\":\"标签管理\",\"icon\":\"layui-icon layui-icon-console\",\"type\":1,\"openType\":\"_iframe\",\"href\":\"/pear/toTag\"}]},{\"id\":\"result\",\"title\":\"常用工具\",\"icon\":\"layui-icon layui-icon-auz\",\"type\":0,\"href\":\"\",\"children\":[{\"id\":\"success\",\"title\":\"附件管理\",\"icon\":\"layui-icon layui-icon-face-smile\",\"type\":1,\"openType\":\"_iframe\",\"href\":\"/pear/toFileUpload\"},{\"id\":\"failure\",\"title\":\"爬取数据\",\"icon\":\"layui-icon layui-icon-face-cry\",\"type\":1,\"openType\":\"_iframe\",\"href\":\"/pear/toCatchData\"}]},{\"id\":\"error\",\"title\":\"用户管理\",\"icon\":\"layui-icon layui-icon-face-cry\",\"type\":0,\"href\":\"\",\"children\":[{\"id\":500,\"title\":\"个人资料\",\"icon\":\"layui-icon layui-icon-face-cry\",\"type\":1,\"openType\":\"_iframe\",\"href\":\"/pear/touser\"}]},{\"id\":\"echarts\",\"title\":\"数据图表\",\"icon\":\"layui-icon layui-icon-chart\",\"type\":0,\"href\":\"\",\"children\":[{\"id\":12121,\"title\":\"数据图表\",\"icon\":\"layui-icon layui-icon-face-smile\",\"type\":1,\"openType\":\"_iframe\",\"href\":\"/pear/toEcharts\"}]},{\"id\":\"code\",\"title\":\"系统设置\",\"icon\":\"layui-icon layui-icon-util\",\"type\":0,\"href\":\"\",\"children\":[{\"id\":801,\"title\":\"系统设置\",\"icon\":\"layui-icon layui-icon-util\",\"type\":1,\"openType\":\"_iframe\",\"href\":\"/pear/toSetting\"}]}]');
  ```

##### 第三方登录
###### gitee
* 接入gitee第三方授权配置,先在gitee的第三方应用上对网站进行授权，获得Client ID和Client Secret，并且要设置回调地址
* 然后把springBoot配置文件的gitee.oauth.callback的端口改成项目的端口

###### GitHub （需要用nginx 80端口）
* GitHub第三方配置有点复杂，也弄了一两天，主要还是GitHub中国地区登录很慢
* 我们只需要把com.boot.GitHubConstant的CLIENT_ID和CLIENT_SECRET换成自己的
* 然后在login.html的GitHub第三方登录超链接中client_id=xxx换成自己的id，即可

##### Linux+Docker容器化部署
* 前提：你的Linux系统必须要安装了Docker。（然后按顺序执行下面的命令）
* 首先要启动docker,因为docker默认是关闭的，需要自己去开启
* ```shell script
  systemctl start docker    #启动docker容器
  systemctl start firewalld  #打开防火墙
  firewall-cmd --list-port   #查看防火墙已经打开的端口
  ######重要：记得打开端口
  firewall-cmd --zone=public --add-port=8080/tcp --permanent  #打开8080端口
  firewall-cmd --zone=public --add-port=9090/tcp --permanent  #打开9090端口
  firewall-cmd --zone=public --add-port=5672/tcp --permanent  #打开5672端口
  firewall-cmd --zone=public --add-port=15672/tcp --permanent #打开15672端口
  firewall-cmd --zone=public --add-port=3306/tcp --permanent
  firewall-cmd --zone=public --add-port=6379/tcp --permanent
  firewall-cmd --zone=public --add-port=5601/tcp --permanent
  firewall-cmd --zone=public --add-port=9100/tcp --permanent
  firewall-cmd --zone=public --add-port=9200/tcp --permanent
  
  
  ####重要：记得重新加载防火墙（不然上面的设置不会生效）
  firewall-cmd --reload
  
  ```
* =>  **1:Docker安装关系型数据库--MySQL 教程**  <=
* ```shell script
    docker pull mysql:5.7  #从从Docker仓库里面拉取mysql5.7
    docker images          #查看从Docker镜像
    docker run -itd --name yblog-mysql -p 3306:3306 -e MYSQL_ROOT_PASSWORD=18420163207 mysql:5.7   #执行从Docker镜像，生成从Docker的mysql容器，返回mysql的实例
    docker ps              #查看从Docker容器实例
    docker exec -it yblog-mysql /bin/bash  #执行mysql容器实例
    mysql -u root -p       #紧接着执行这句命令，然后就输入你的密码就行了
  ```
* =>  **2:Docker安装非关系型数据库--Redis 教程**  <=
* ```shell script
    docker pull redis:latest   #从Docker仓库拉取最新的Redis镜像
    docker run -itd --name yblog-redis -p 6379:6379 redis  #运行redis镜像，生成对应容器实例
    docker exec -it yblog-redis /bin/bash  
    redis-cli    #输入就能进入redis客户端
  ```

* =>  **3:Docker安装消息队列--RabbitMQ 教程**  <=
* ```shell script
    docker pull rabbitmq    #从Docker仓库拉取最新的RabbitMQ镜像
    docker run -d --hostname my-rabbit --name yblog-rabbit -p 15672:15672 -p 5672:5672 rabbitmq  #运行RabbitMQ镜像
    docker exec -it yblog-rabbit /bin/bash   #进入RabbitMQ容器
    rabbitmq-plugins enable rabbitmq_management   #安装RabbitMQ的插件
  ```
* **注意：要打开RabbitMQ的可视化界面，创建一个虚拟主机为：/ems**

* **4:用Navicat把yblog的项目的数据库导入到Docker的mysql中，即可！！！**

* **5:修改配置文件的IP为你的Linux系统的IP然后打成jar包，并且把yblog打出来的Jar包传入到Linux系统，然后通过java -jar jar包名  的命令启动即可**


#### 使用教程
* 输入http://localhost:8080/swagger-ui.html,可进入swagger接口文档
* 后台管理账号:admin 密码:123456
* Druid监控帐号：admin 密码： 123456
* RabbitMQ帐号：guest 密码： guest

### 图片演示
后台管理(第二套界面)👇
![01.png](https://gitee.com/youzhengjie/springBootBlog/raw/master/image/01.PNG)
![02.png](https://gitee.com/youzhengjie/springBootBlog/raw/master/image/02.PNG)
![03.png](https://gitee.com/youzhengjie/springBootBlog/raw/master/image/03.PNG)
![04.png](https://gitee.com/youzhengjie/springBootBlog/raw/master/image/04.PNG)
![05.png](https://gitee.com/youzhengjie/springBootBlog/raw/master/image/05.PNG)
![06.png](https://gitee.com/youzhengjie/springBootBlog/raw/master/image/06.PNG)
![07.png](https://gitee.com/youzhengjie/springBootBlog/raw/master/image/07.PNG)
![08.png](https://gitee.com/youzhengjie/springBootBlog/raw/master/image/08.PNG)
![09.png](https://gitee.com/youzhengjie/springBootBlog/raw/master/image/09.PNG)
![10.png](https://gitee.com/youzhengjie/springBootBlog/raw/master/image/10.PNG)
![11.png](https://gitee.com/youzhengjie/springBootBlog/raw/master/image/11.PNG)
![12.png](https://gitee.com/youzhengjie/springBootBlog/raw/master/image/12.PNG)
![13.png](https://gitee.com/youzhengjie/springBootBlog/raw/master/image/13.PNG)
![14.png](https://gitee.com/youzhengjie/springBootBlog/raw/master/image/14.PNG)
![25.png](https://gitee.com/youzhengjie/springBootBlog/raw/master/image/25.PNG)
![26.png](https://gitee.com/youzhengjie/springBootBlog/raw/master/image/26.PNG)
![27.png](https://gitee.com/youzhengjie/springBootBlog/raw/master/image/27.PNG)
前台界面(第二套界面)👇
![21.png](https://gitee.com/youzhengjie/springBootBlog/raw/master/image/21.PNG)
![22.png](https://gitee.com/youzhengjie/springBootBlog/raw/master/image/22.PNG)
![23.png](https://gitee.com/youzhengjie/springBootBlog/raw/master/image/23.PNG)
![24.png](https://gitee.com/youzhengjie/springBootBlog/raw/master/image/24.PNG)
登录界面👇
![login.png](https://gitee.com/youzhengjie/springBootBlog/raw/master/image/login.png)
注册界面👇
![register.png](https://gitee.com/youzhengjie/springBootBlog/raw/master/image/register.png)


