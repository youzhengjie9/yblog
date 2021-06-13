# yblog
&emsp;&emsp;基于SpringBoot架构开发的博客：**博文管理**、**统计图表**、**访问记录**、**附件管理**、**用户管理**、**友链管理**、**监控管理**、**抓取博文**等功能，且一直会对本项目进行加强，请各位大佬多多指点，一起共同进步。
文章无需自己写，可以使用作者自己编写的全自动爬虫工具即可，只需轻轻一点，万千文章到手。
### 前言
> 本人目前大二，由于学业繁重，自己抽出空来做项目，基本上所有东西都是靠自己学，可能有很多不足之处，将来自己能力足够会进行改进。<br>
> 需求的设计、数据库的设计，以及前端、后端都是自己一个人完成，据统计，目前Java类的代码量在**4000**-**5000**行。<br>
> 在这段时间里，我基本每天都在思考，思考如何做出一个较为成熟的系统，也借鉴过一些大佬的idea，并自己将其转换成代码，呈现于项目之中。<br/>
> 自行完成项目会让我们学到很多，做项目不能光想不做，我在开始的时候认为做项目很简单，但并不是，我在做项目时遇到了很多困难和挫折，每解决一个问题，就会让我提升很多。<br>
> **让我们一起进步吧！！！**

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
| Thymeleaf                  | 模板引擎                | https://www.thymeleaf.org/      |
| Markdown        |  富文本编辑器         |  http://markdown.p2hp.com/  |
| alertJs          |弹框插件          |  https://gitee.com/ydq/alertjs

#### 安装教程
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
        listen       8888;
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
* 所以我们只需要把项目的8080、8081、8082端口打开，然后访问localhost:8888即可实现负载均衡。

#### 使用教程
* 输入http://localhost:8080/swagger-ui.html,可进入swagger接口文档
* Druid监控帐号：admin 密码： 123456
* RabbitMQ帐号：guest 密码： guest
