# 构建和运行

  在根目录下执行`./up.sh`，会使用docker-compose构建整套环境然后启动，包括：
  
  - MySQL：8.x
  
  - Server: 
    使用mvn在docker容器中build并部署和启动Java springboot application
  
  - Nginx: 
    在docker容器中build并部署Vue App，并配置8000端口上的前端静态资源服务器, 并反向代理REST API到Java server端

# 关于数据库初始化：

  Java springboot application中使用flyway 进行数据库初始化，flyway会自动读取`server/main/resources/db/migration`目录下的sql文件，并执行。

  默认添加了一个用户：用户名admin, 密码123456A@b

# 技术栈：
  
  - server端（java):
    
    - Framework: springboot 3.x

    - Database: MySQL (runtime), H2 (unit test)

    - ORM: spring data JPA (Hibernate), QueryDSL

    - Authentication (JWT): spring security
    
    - Bean copy: mapstruct

    - Unit test: JUnit5, Mockito

  - web端：
    
    - Vue3 + Pinia + Vue-router

    - 构建：Vite

    - 组件库：Element Plus

    - Unit test：无

    - HTTP client：axios

# 代码简要说明：

  ## server端：
     
  - 使用Spring Initializr（https://start.spring.io）创建的使用maven构建结构的项目

  - src中分两部分package：
       
   1. io.github.qiangyt.common: 自用的一些基础代码，复制自https://github.com/qiangyt/java-common（做了一些调整）

   2. com.example.demo: 本次示例直接使用的代码

      - sdk: 定义了client端和server端的公用的模型和接口

      - server: server端，常规的springmvc RestController + facade/service (管理事务边界) + dao/repository

  此外，src/main/resources下：
       
   1. /db/migration: flyway管理的数据库初始化和升级脚本
       
   2. app.key和app.pub: JWT 签名的公私钥

   3. application.yml: springboot配置文件
      
   4. logback.xml: logback配置文件

  web端是常规的vite + vue3项目结构，不再赘述。页面直接用了Element Plus的Tree等组件，整体上未做美观细节的处理。
  
  ## 部分实现细节的说明：
        
   1. 登录（/rest/signin）没有使用session/cookie，而是使用了JWT，参见io.github.qiangyt.common.security.JwtAuthFilter。登录成功后，会返回一个accessToken，简单起见，未实现refresh token，而是直接设置accessToken的expiry为1个月，配合前端实现rememberMe功能。服务端会校验token，如果校验失败，会返回401。在前端这里，登录成功后，登录API返回的token和user对象都会保存到Pina store里，并且，如果选中rememberMe，会同时写入local storage。登录成功后，在axios的request interceptor里为Authorization header里统一加上这个bearer ${token}。
   退出登录状态无须调用API，而是直接删除store和local storage里的token等数据。

   2. 关于深层次的评论：
      
      1）数据库表里统一使用message表存储评论和留言，每个message既有parentId（回复目标的id），又有postId（留言的id）。留言的parentId为null，而postId是留言自身的id。

      2）为了在实现一次性取得所有评论的同时避免根据parentId层层执行SQL查询，demo里简单粗暴的用单条SQL一次性读入全表，然后在内存中遍历所有message并建立它们的树形结构。