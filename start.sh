#!/bin/bash

# MCP JIRA Service 开箱即用启动脚本
# Quick Start Script for MCP JIRA Service
# GitHub: https://github.com/dev4java/mcptools-service

set -e

echo "=========================================="
echo "🚀 MCP JIRA Service 快速启动"
echo "🚀 Quick Start for MCP JIRA Service"
echo "📚 GitHub: https://github.com/dev4java/mcptools-service"
echo "=========================================="

# 检查 Java 环境
if ! command -v java &> /dev/null; then
    echo "❌ Java 21+ 未安装，请先安装 Java"
    echo "❌ Java 21+ is not installed, please install Java first"
    exit 1
fi

echo "✅ Java 版本: $(java -version 2>&1 | head -1)"

# 检查 Maven 环境
if ! command -v mvn &> /dev/null; then
    echo "❌ Maven 未安装，请先安装 Maven"
    echo "❌ Maven is not installed, please install Maven first"
    exit 1
fi

echo "✅ Maven 版本: $(mvn -version 2>&1 | head -1)"

# 创建必要的目录
echo "📁 创建必要的目录..."
mkdir -p logs generated-code

# 检查配置文件
if [ ! -f ".env" ]; then
    echo "📝 创建配置文件..."
    cp env.example .env
    echo "⚠️  请编辑 .env 文件，配置您的 JIRA 连接信息"
    echo "⚠️  Please edit .env file to configure your JIRA connection"
    echo ""
    echo "📖 主要配置项："
    echo "   - JIRA_BASE_URL: 您的 JIRA 服务器地址"
    echo "   - JIRA_USERNAME: JIRA 用户名"
    echo "   - JIRA_PASSWORD: JIRA 密码或 API Token"
    echo ""
    read -p "🤔 是否现在配置 JIRA 连接？(y/n): " config_now
    if [ "$config_now" = "y" ] || [ "$config_now" = "Y" ]; then
        nano .env 2>/dev/null || vi .env 2>/dev/null || echo "请手动编辑 .env 文件"
    fi
fi

# 选择启动方式
echo ""
echo "🎯 选择启动方式："
echo "1. 本地启动 (推荐开发)"
echo "2. Docker 启动 (推荐生产)"
echo "3. 无安全模式启动 (快速测试)"
read -p "请选择 (1-3): " choice

case $choice in
    1)
        echo "🔧 本地启动模式..."
        echo "📦 编译项目..."
        mvn clean compile -q
        echo "🚀 启动应用..."
        mvn spring-boot:run -Dspring-boot.run.profiles=dev
        ;;
    2)
        echo "🐳 Docker 启动模式..."
        if ! command -v docker &> /dev/null; then
            echo "❌ Docker 未安装"
            exit 1
        fi
        echo "🏗️  构建 Docker 镜像..."
        docker-compose build
        echo "🚀 启动 Docker 服务..."
        docker-compose up
        ;;
    3)
        echo "🔓 无安全模式启动..."
        echo "⚠️  注意：此模式禁用了身份认证，仅用于测试"
        export SECURITY_ENABLED=false
        mvn clean compile -q
        mvn spring-boot:run -Dspring-boot.run.profiles=dev
        ;;
    *)
        echo "❌ 无效选择"
        exit 1
        ;;
esac

echo ""
echo "=========================================="
echo "🎉 启动完成！"
echo ""
echo "📖 访问地址："
echo "   - 应用服务: http://localhost:8010"
echo "   - API 文档: http://localhost:8010/swagger-ui.html"
echo "   - 健康检查: http://localhost:8011/actuator/health"
echo ""
echo "🔐 默认登录:"
echo "   - 用户名: admin"
echo "   - 密码: admin123"
echo ""
echo "📚 更多信息："
echo "   - 项目主页: https://github.com/dev4java/mcptools-service"
echo "   - 使用文档: https://github.com/dev4java/mcptools-service/wiki"
echo "   - 问题反馈: https://github.com/dev4java/mcptools-service/issues"
echo "==========================================" 