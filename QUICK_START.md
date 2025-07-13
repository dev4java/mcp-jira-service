# 🚀 快速开始指南 (Quick Start Guide)

[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://openjdk.java.net/projects/jdk/21/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-green.svg)](https://spring.io/projects/spring-boot)
[![GitHub](https://img.shields.io/github/stars/dev4java/mcp-jira-service?style=social)](https://github.com/dev4java/mcp-jira-service)

[English](#english) | [中文](#中文)

---

## 中文

欢迎使用 MCP JIRA Service！本指南将帮助您在几分钟内启动并运行服务。

### ⚡ 一键启动

```bash
# 克隆项目
git clone https://github.com/dev4java/mcp-jira-service.git
cd mcp-jira-service

# 运行启动脚本（自动引导配置）
./start.sh
```

### 🎯 三种启动模式

#### 1. 🔧 本地开发模式
- **适用场景**：日常开发、调试
- **优势**：快速重启、代码热更新
- **命令**：选择选项 `1`

#### 2. 🐳 Docker 部署模式  
- **适用场景**：生产环境、团队协作
- **优势**：环境一致性、便于部署
- **命令**：选择选项 `2`

#### 3. 🔓 无安全测试模式
- **适用场景**：快速功能验证、API 测试
- **优势**：无需认证、快速体验
- **命令**：选择选项 `3`

### 📋 最小配置

#### 必须配置（仅 3 项）
```bash
# 编辑 .env 文件
JIRA_BASE_URL=https://your-jira-server.com
JIRA_USERNAME=your-username  
JIRA_API_TOKEN=your-api-token
```

> 💡 **提示**: 推荐使用 API Token 而非密码，更安全且支持双因素认证

#### 🔑 如何获取 JIRA API Token

1. 登录您的 JIRA 实例
2. 点击右上角头像 → **Account Settings**
3. 导航到 **Security** → **Create and manage API tokens**
4. 点击 **Create API Token**
5. 复制生成的 Token 到配置文件

#### 可选配置
```bash
# 禁用安全认证（测试用）
SECURITY_ENABLED=false

# 调整日志级别
LOG_LEVEL_APP=DEBUG

# 自定义端口
SERVER_PORT=8080
MANAGEMENT_PORT=8081
```

### 🌐 访问地址

启动成功后，您可以访问：

| 服务 | 地址 | 说明 |
|------|------|------|
| **主应用** | http://localhost:8010 | 核心 MCP JIRA 服务 |
| **API 文档** | http://localhost:8010/swagger-ui.html | 完整的 API 文档 |
| **健康检查** | http://localhost:8011/actuator/health | 服务状态监控 |

### 🔐 默认认证

| 用户名 | 密码 | 权限 |
|--------|------|------|
| `admin` | `admin123` | 完整访问权限 |

> ⚠️ **安全提醒**: 生产环境请务必修改默认密码！

### ⚡ 功能验证

#### 1. 健康检查
```bash
curl http://localhost:8011/actuator/health
```

**预期响应:**
```json
{
  "status": "UP",
  "groups": ["liveness", "readiness"]
}
```

#### 2. API 测试（需认证）
```bash
curl -u admin:admin123 http://localhost:8010/api/jira/test-connection
```

#### 3. 无认证测试（无安全模式）
```bash
curl http://localhost:8010/api/jira/test-connection
```

#### 4. 代码生成测试
```bash
curl -X POST http://localhost:8010/api/code/generate/PROJECT-123 \
  -H "Content-Type: application/json" \
  -d '{"codeType": "entity", "packageName": "com.example"}'
```

### 🎉 成功标志

看到以下输出说明启动成功：

```
==========================================
🎉 启动完成！

📖 访问地址：
   - 应用服务: http://localhost:8010
   - API 文档: http://localhost:8010/swagger-ui.html
   - 健康检查: http://localhost:8011/actuator/health

🔐 默认登录:
   - 用户名: admin
   - 密码: admin123
==========================================
```

### 🔧 故障排除

#### 常见问题

##### 1. 端口被占用
```bash
# 查看端口占用
lsof -i :8010
# 或者修改端口
export SERVER_PORT=8020
./start.sh
```

##### 2. JIRA 连接失败
```bash
# 检查配置
cat .env

# 测试连接
curl -u your-username:your-token \
  https://your-jira-server.com/rest/api/2/serverInfo
```

##### 3. Java 版本问题
```bash
# 检查 Java 版本
java -version
# 需要 Java 21 或更高版本
```

##### 4. 内存不足
```bash
# 减少内存使用
export JAVA_OPTS="-Xmx256m -Xms128m"
./start.sh
```

##### 5. Docker 相关问题
```bash
# 检查 Docker 状态
docker --version
docker-compose --version

# 清理 Docker 资源
docker system prune -f
```

### 📚 下一步

探索更多功能和配置选项：

- 📖 [完整文档](README.md) - 详细的功能介绍
- 🔧 [配置指南](docs/configuration.md) - 高级配置选项
- 🚀 [部署指南](docs/deployment.md) - 生产环境部署
- 💡 [使用示例](examples/) - 代码示例和最佳实践
- 🤝 [贡献指南](CONTRIBUTING.md) - 如何参与项目开发

### 📞 获取帮助

如果遇到问题，请查看：

- 🐛 [Issue 反馈](https://github.com/dev4java/mcp-jira-service/issues) - 报告问题
- 💬 [讨论区](https://github.com/dev4java/mcp-jira-service/discussions) - 技术讨论
- 📧 [邮件支持](mailto:dev4java@example.com) - 直接联系

### 🎯 设计理念

**开箱即用** - 最少配置，最快启动  
**渐进增强** - 从简单到复杂，按需启用功能  
**开发友好** - 详细日志，清晰错误提示  
**生产就绪** - 内置监控，容器化支持

### 📺 视频教程

🎬 [观看视频教程](https://github.com/dev4java/mcp-jira-service/wiki/Video-Tutorials) - 可视化安装和配置指南

### 🌟 特色功能预览

#### 🔗 JIRA 集成
- 支持 JIRA Cloud 和 Server 版本
- API Token 和密码双重认证
- 自动连接测试和故障诊断

#### 🛠️ 代码生成
- 从 JIRA 问题自动生成 Java 代码
- 支持 Entity、Service、Controller 模板
- 自定义包名和框架选择

#### 📡 MCP 协议
- 完整的 Model Context Protocol 实现
- JSON-RPC 2.0 兼容
- 丰富的 API 端点

#### 🐳 容器化
- 多阶段 Docker 构建
- Docker Compose 一键部署
- 健康检查和自动重启

感谢使用 MCP JIRA Service！🙏

---

## English

Welcome to MCP JIRA Service! This guide will help you get the service up and running in minutes.

### ⚡ One-Click Launch

```bash
# Clone the project
git clone https://github.com/dev4java/mcp-jira-service.git
cd mcp-jira-service

# Run the startup script (automatic configuration guide)
./start.sh
```

### 🎯 Three Launch Modes

#### 1. 🔧 Local Development Mode
- **Use Cases**: Daily development, debugging
- **Advantages**: Quick restart, hot code reload
- **Command**: Select option `1`

#### 2. 🐳 Docker Deployment Mode  
- **Use Cases**: Production environment, team collaboration
- **Advantages**: Environment consistency, easy deployment
- **Command**: Select option `2`

#### 3. 🔓 No-Security Test Mode
- **Use Cases**: Quick feature validation, API testing
- **Advantages**: No authentication required, fast experience
- **Command**: Select option `3`

### 📋 Minimal Configuration

#### Required Configuration (Only 3 items)
```bash
# Edit .env file
JIRA_BASE_URL=https://your-jira-server.com
JIRA_USERNAME=your-username  
JIRA_API_TOKEN=your-api-token
```

> 💡 **Tip**: We recommend using API Token instead of password for better security and 2FA support

#### 🔑 How to Get JIRA API Token

1. Log in to your JIRA instance
2. Click your avatar in the top right → **Account Settings**
3. Navigate to **Security** → **Create and manage API tokens**
4. Click **Create API Token**
5. Copy the generated token to your configuration file

#### Optional Configuration
```bash
# Disable security authentication (for testing)
SECURITY_ENABLED=false

# Adjust log level
LOG_LEVEL_APP=DEBUG

# Custom ports
SERVER_PORT=8080
MANAGEMENT_PORT=8081
```

### 🌐 Access URLs

After successful startup, you can access:

| Service | URL | Description |
|---------|-----|-------------|
| **Main Application** | http://localhost:8010 | Core MCP JIRA Service |
| **API Documentation** | http://localhost:8010/swagger-ui.html | Complete API Documentation |
| **Health Check** | http://localhost:8011/actuator/health | Service Status Monitoring |

### 🔐 Default Authentication

| Username | Password | Permissions |
|----------|----------|-------------|
| `admin` | `admin123` | Full Access |

> ⚠️ **Security Warning**: Please change default credentials in production!

### ⚡ Feature Verification

#### 1. Health Check
```bash
curl http://localhost:8011/actuator/health
```

**Expected Response:**
```json
{
  "status": "UP",
  "groups": ["liveness", "readiness"]
}
```

#### 2. API Test (Authentication Required)
```bash
curl -u admin:admin123 http://localhost:8010/api/jira/test-connection
```

#### 3. No-Auth Test (No-Security Mode)
```bash
curl http://localhost:8010/api/jira/test-connection
```

#### 4. Code Generation Test
```bash
curl -X POST http://localhost:8010/api/code/generate/PROJECT-123 \
  -H "Content-Type: application/json" \
  -d '{"codeType": "entity", "packageName": "com.example"}'
```

### 🎉 Success Indicators

Seeing the following output indicates successful startup:

```
==========================================
🎉 Startup Complete!

📖 Access URLs:
   - Application Service: http://localhost:8010
   - API Documentation: http://localhost:8010/swagger-ui.html
   - Health Check: http://localhost:8011/actuator/health

🔐 Default Login:
   - Username: admin
   - Password: admin123
==========================================
```

### 🔧 Troubleshooting

#### Common Issues

##### 1. Port Already in Use
```bash
# Check port usage
lsof -i :8010
# Or modify port
export SERVER_PORT=8020
./start.sh
```

##### 2. JIRA Connection Failed
```bash
# Check configuration
cat .env

# Test connection
curl -u your-username:your-token \
  https://your-jira-server.com/rest/api/2/serverInfo
```

##### 3. Java Version Issues
```bash
# Check Java version
java -version
# Requires Java 21 or higher
```

##### 4. Insufficient Memory
```bash
# Reduce memory usage
export JAVA_OPTS="-Xmx256m -Xms128m"
./start.sh
```

##### 5. Docker Related Issues
```bash
# Check Docker status
docker --version
docker-compose --version

# Clean Docker resources
docker system prune -f
```

### 📚 Next Steps

Explore more features and configuration options:

- 📖 [Complete Documentation](README.md) - Detailed feature introduction
- 🔧 [Configuration Guide](docs/configuration.md) - Advanced configuration options
- 🚀 [Deployment Guide](docs/deployment.md) - Production environment deployment
- 💡 [Usage Examples](examples/) - Code examples and best practices
- 🤝 [Contributing Guide](CONTRIBUTING.md) - How to participate in project development

### 📞 Getting Help

If you encounter issues, please check:

- 🐛 [Issue Reports](https://github.com/dev4java/mcp-jira-service/issues) - Report problems
- 💬 [Discussions](https://github.com/dev4java/mcp-jira-service/discussions) - Technical discussions
- 📧 [Email Support](mailto:dev4java@example.com) - Direct contact

### 🎯 Design Philosophy

**Out-of-the-Box** - Minimal configuration, fastest startup  
**Progressive Enhancement** - From simple to complex, enable features as needed  
**Developer Friendly** - Detailed logs, clear error messages  
**Production Ready** - Built-in monitoring, containerization support

### 📺 Video Tutorials

🎬 [Watch Video Tutorials](https://github.com/dev4java/mcp-jira-service/wiki/Video-Tutorials) - Visual installation and configuration guide

### 🌟 Feature Preview

#### 🔗 JIRA Integration
- Support for JIRA Cloud and Server versions
- API Token and password dual authentication
- Automatic connection testing and diagnostics

#### 🛠️ Code Generation
- Automatically generate Java code from JIRA issues
- Support for Entity, Service, Controller templates
- Custom package names and framework selection

#### 📡 MCP Protocol
- Complete Model Context Protocol implementation
- JSON-RPC 2.0 compatible
- Rich API endpoints

#### 🐳 Containerization
- Multi-stage Docker builds
- Docker Compose one-click deployment
- Health checks and auto-restart

Thank you for using MCP JIRA Service! 🙏 