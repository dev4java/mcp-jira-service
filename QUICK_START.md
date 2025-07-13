# 🚀 快速开始指南 (Quick Start Guide)

MCP JIRA Service 现在提供**开箱即用**的体验！只需几分钟即可启动运行。

## ⚡ 一键启动

```bash
# 克隆项目
git clone https://github.com/yourusername/mcp-jira-service.git
cd mcp-jira-service

# 运行启动脚本（自动引导配置）
./start.sh
```

## 🎯 三种启动模式

### 1. 🔧 本地开发模式
- **适用场景**：日常开发、调试
- **优势**：快速重启、代码热更新
- **命令**：选择选项 `1`

### 2. 🐳 Docker 部署模式  
- **适用场景**：生产环境、团队协作
- **优势**：环境一致性、便于部署
- **命令**：选择选项 `2`

### 3. 🔓 无安全测试模式
- **适用场景**：快速功能验证、API 测试
- **优势**：无需认证、快速体验
- **命令**：选择选项 `3`

## 📋 最小配置

### 必须配置（仅 3 项）
```bash
# 编辑 .env 文件
JIRA_BASE_URL=https://your-jira-server.com
JIRA_USERNAME=your-username  
JIRA_PASSWORD=your-password
```

### 可选配置
```bash
# 禁用安全认证（测试用）
SECURITY_ENABLED=false

# 调整日志级别
LOG_LEVEL_APP=DEBUG

# 自定义端口
SERVER_PORT=8080
```

## 🌐 访问地址

启动成功后，您可以访问：

| 服务 | 地址 | 说明 |
|------|------|------|
| **主应用** | http://localhost:8010 | 核心 MCP JIRA 服务 |
| **API 文档** | http://localhost:8010/swagger-ui.html | 完整的 API 文档 |
| **健康检查** | http://localhost:8011/actuator/health | 服务状态监控 |

## 🔐 默认认证

| 用户名 | 密码 | 权限 |
|--------|------|------|
| `admin` | `admin123` | 完整访问权限 |

> 💡 **提示**：生产环境请务必修改默认密码！

## ⚡ 功能验证

### 1. 健康检查
```bash
curl http://localhost:8011/actuator/health
```

### 2. API 测试（需认证）
```bash
curl -u admin:admin123 http://localhost:8010/api/jira/projects
```

### 3. 无认证测试（无安全模式）
```bash
curl http://localhost:8010/api/jira/projects
```

## 🎉 成功标志

看到以下输出说明启动成功：

```
========================================
🎉 启动完成！

📖 访问地址：
   - 应用服务: http://localhost:8010
   - API 文档: http://localhost:8010/swagger-ui.html
   - 健康检查: http://localhost:8011/actuator/health

🔐 默认登录:
   - 用户名: admin
   - 密码: admin123
========================================
```

## 🔧 故障排除

### 常见问题

1. **端口被占用**
   ```bash
   # 修改端口
   export SERVER_PORT=8020
   ./start.sh
   ```

2. **JIRA 连接失败**
   ```bash
   # 检查配置
   cat .env
   # 测试连接
   curl -u username:password https://your-jira-server.com/rest/api/2/serverInfo
   ```

3. **内存不足**
   ```bash
   # 减少内存使用
   export JAVA_OPTS="-Xmx256m -Xms128m"
   ./start.sh
   ```

## 📚 下一步

- 📖 查看 [完整文档](README.md)
- 🔧 学习 [配置选项](CONTRIBUTING.md)
- 🚀 体验 [代码生成功能](examples/)
- 💡 查看 [最佳实践](docs/)

---

## 🎯 设计理念

**开箱即用** - 最少配置，最快启动
**渐进增强** - 从简单到复杂，按需启用功能  
**开发友好** - 详细日志，清晰错误提示 