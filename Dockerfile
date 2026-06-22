# 第一阶段：Maven 构建 WAR
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn package -DskipTests -B

# 第二阶段：Tomcat 运行
FROM tomcat:9.0-jdk17-temurin-jammy
# 删除默认 ROOT 应用
RUN rm -rf /usr/local/tomcat/webapps/ROOT
# 部署 WAR 包
COPY --from=build /app/target/JPetStore.war /usr/local/tomcat/webapps/ROOT.war
EXPOSE 8080
CMD ["catalina.sh", "run"]
