# 多阶段构建 Dockerfile for MCP JIRA Service
# 使用 OpenJDK 21 和 Maven 进行构建

# ================================
# 构建阶段 (Build Stage)
# ================================
FROM maven:3.9-openjdk-21-slim AS builder

# 设置工作目录
WORKDIR /app

# 复制 Maven 配置文件
COPY pom.xml .
COPY .mvn .mvn
COPY mvnw .

# 下载依赖（利用 Docker 缓存层）
RUN mvn dependency:go-offline -B

# 复制源代码
COPY src ./src

# 构建应用
RUN mvn clean package -DskipTests -B

# ================================
# 运行阶段 (Runtime Stage)
# ================================
FROM openjdk:21-jre-slim

# 安装必要的工具和字体
RUN apt-get update && apt-get install -y \
    curl \
    wget \
    dumb-init \
    && rm -rf /var/lib/apt/lists/*

# 创建应用用户（非 root 用户运行，提高安全性）
RUN groupadd -r mcpjira && useradd -r -g mcpjira mcpjira

# 设置工作目录
WORKDIR /app

# 创建应用相关目录
RUN mkdir -p logs generated-code templates && \
    chown -R mcpjira:mcpjira /app

# 从构建阶段复制 JAR 文件
COPY --from=builder /app/target/mcp-jira-service-*.jar app.jar

# 复制启动脚本
COPY docker/entrypoint.sh /entrypoint.sh
RUN chmod +x /entrypoint.sh && chown mcpjira:mcpjira /entrypoint.sh

# 设置环境变量
ENV JAVA_OPTS="-Xmx512m -Xms256m" \
    SPRING_PROFILES_ACTIVE="docker" \
    SERVER_PORT=8010 \
    MANAGEMENT_PORT=8011

# 暴露端口
EXPOSE 8010 8011

# 健康检查
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
    CMD curl -f http://localhost:8011/actuator/health || exit 1

# 切换到非 root 用户
USER mcpjira

# 使用 dumb-init 作为入口点（处理信号）
ENTRYPOINT ["/usr/bin/dumb-init", "--"]

# 启动应用
CMD ["/entrypoint.sh"] 