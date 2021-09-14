项目简介

1、框架使用springboot security权限搭建

2、支持docker镜像打包命令

mvn命令 mvn clean package dockerfile:build

3、postman测试登陆

http://localhost:9002/login GET/POST

{
    "loginName":"admin",
    "password":"2015bi"
}