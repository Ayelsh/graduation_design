# graduation_design

### 关于数据库
  * 数据库建表语句存在于DB文件夹，直接执行sql文件即可建立数据库
  * 同时修改application.yml和application-dev.yml的datasource配置和redis为服务器配置
  * 其中通过将数据插入button表，可以增加liunx界面的按钮

### 关于模块
  * 后端代码耦合度不高，理论上可以删去moduled的Blog模块和File模块，只保留login模块和liunx模块，对整个系统影响不大，同时可以简化很多配置
  * 如果不删除或者删除后影响运行的，就不删除，同时配置包括Minio/MongoDB在内的中间件

### 关于部署
  * 实测前后端均可以通过宝塔界面进行图形化部署
  * 部署前请在部署环境安装好JDK17+MySql+maven+Redis+Minio+MongoDB,修改application.yml和application-dev.yml里面的配置为部署环境的配置，通过maven打成Jar包即可在宝塔界面部署

### 其他
  * 请确保链接的kali-liunx开放了ssh端口,后端的链接模块做了多线程异步处理，可以拓展成链接多台机器，但是前端当时没完成这个功能
  * 链接liunx的信息写死在前端页面，可以修改成动态的
