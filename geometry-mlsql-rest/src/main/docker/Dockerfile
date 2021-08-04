FROM java:8

#定义工作目录
ENV WORK_PATH /usr/local/work
#设置环境，默认为：prod
ENV SPRING_PROFILES_ACTIVE prod

#创建文件夹
RUN mkdir -p $WORK_PATH

#拷贝jar并修改名称为bi
COPY *.jar $WORK_PATH/geometry-mlsql-rest.jar

#设置端口
CMD ["--server.port=9002"]

#暴露端口
EXPOSE 9002

#设置环境变量
ENV JVM_OPTS=""

#执行jar
ENTRYPOINT java ${JVM_OPTS} -jar $WORK_PATH/geometry-mlsql-rest.jar --spring.profiles.active=${SPRING_PROFILES_ACTIVE}