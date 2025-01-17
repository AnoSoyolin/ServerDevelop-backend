# 使用官方 OpenJDK 运行时作为基础镜像
FROM openjdk:8-jdk-alpine

# 设置容器内的工作目录
WORKDIR /app

# 将 Maven Wrapper 和 pom.xml 文件复制到容器内
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# 下载 Maven 依赖（但不构建项目）
RUN ./mvnw dependency:go-offline

# 将整个项目复制到容器内
COPY src ./src

# 构建应用程序（这会在 target 目录下生成一个 jar 文件）
RUN ./mvnw clean package -DskipTests

# 暴露应用运行的端口
EXPOSE 8080

# 启动应用
CMD ["java", "-jar", "target/BlueWhale-0.0.1-SNAPSHOT.jar"]
