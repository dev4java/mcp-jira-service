#!/bin/bash

# MCP JIRA Service Docker 启动脚本
# 设置错误时退出
set -e

# 打印启动信息
echo "=========================================="
echo "Starting MCP JIRA Service"
echo "=========================================="
echo "Java Version: $(java -version 2>&1 | head -1)"
echo "Java Options: ${JAVA_OPTS}"
echo "Spring Profiles: ${SPRING_PROFILES_ACTIVE}"
echo "Server Port: ${SERVER_PORT}"
echo "Management Port: ${MANAGEMENT_PORT}"
echo "=========================================="

# 等待依赖服务（如果需要）
if [ ! -z "$WAIT_FOR_SERVICES" ]; then
    echo "Waiting for dependent services..."
    for service in $WAIT_FOR_SERVICES; do
        echo "Checking $service..."
        while ! nc -z $service; do
            echo "Waiting for $service to be ready..."
            sleep 2
        done
        echo "$service is ready!"
    done
fi

# 检查必要的环境变量
if [ -z "$JIRA_BASE_URL" ]; then
    echo "WARNING: JIRA_BASE_URL is not set"
fi

if [ -z "$JIRA_USERNAME" ]; then
    echo "WARNING: JIRA_USERNAME is not set"
fi

if [ -z "$JIRA_PASSWORD" ] && [ -z "$JIRA_API_TOKEN" ]; then
    echo "WARNING: Neither JIRA_PASSWORD nor JIRA_API_TOKEN is set"
fi

# 创建必要的目录
mkdir -p logs generated-code templates

# 设置 JVM 参数
JAVA_OPTS="${JAVA_OPTS} -Djava.security.egd=file:/dev/./urandom"
JAVA_OPTS="${JAVA_OPTS} -Dspring.profiles.active=${SPRING_PROFILES_ACTIVE}"

# 如果是开发模式，启用调试
if [ "$SPRING_PROFILES_ACTIVE" = "dev" ] || [ "$ENABLE_DEBUG" = "true" ]; then
    JAVA_OPTS="${JAVA_OPTS} -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005"
    echo "Debug mode enabled on port 5005"
fi

# 启动应用
echo "Starting MCP JIRA Service..."
exec java $JAVA_OPTS -jar app.jar 