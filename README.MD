# graduation_design

## 数据库建表语句存在于DB文件夹，直接执行sql文件即可建立数据库
## 同时修改application.yml和application-dev.yml的datasource配置和redis为服务器配置，包括其他中间件也需要修改配置
## 后端代码耦合度不高，理论上可以删去moduled的Blog模块和File模块，只保留login模块和liunx模块，对整个系统影响不大，同时可以简化很多配置
## 实测可以直接通过宝塔界面部署在liunx
## 请确保链接的kali-liunx开放了ssh端口,后端的链接模块做了多线程异步处理，可以拓展成链接多台机器，但是前端当时没完成这个功能
## 链接liunx的信息写死在前端页面，可以修改成动态的
