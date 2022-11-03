# dockerfile:build 之前设置DOCKER_HOST=tcp://172.17.51.71:2375   DOCKER_CERT_PATH=

# 添加 Java 8 镜像来源
FROM java:8

# 添加参数
ARG JAR_FILE

# 添加 Spring Boot 包
ADD ${JAR_FILE} app.jar

# 执行启动命令
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]