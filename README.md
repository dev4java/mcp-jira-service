# MCP JIRA Service

[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://openjdk.java.net/projects/jdk/21/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-green.svg)](https://spring.io/projects/spring-boot)
[![Maven](https://img.shields.io/badge/Maven-3.8+-blue.svg)](https://maven.apache.org/)
[![License](https://img.shields.io/badge/License-Apache%202.0-brightgreen.svg)](https://opensource.org/licenses/Apache-2.0)
[![Build Status](https://github.com/dev4java/mcp-jira-service/workflows/CI/badge.svg)](https://github.com/dev4java/mcp-jira-service/actions)

[English](#english) | [中文](#中文)

---

## 中文

> 🚀 一个功能强大的 Spring Boot 应用，提供 JIRA 集成和基于 MCP 协议的代码生成服务

### ✨ 特性

- 🔗 **JIRA 集成** - 支持 API Token 和密码认证
- 🛠️ **代码生成** - 从 JIRA 问题自动生成 Java 代码
- 🌐 **RESTful API** - 完整的 REST API 接口
- 📡 **MCP 协议** - 实现 Model Context Protocol 支持
- 🐳 **Docker 支持** - 容器化部署
- 🔄 **CI/CD** - GitHub Actions 自动化流水线
- 📚 **丰富文档** - 完整的使用指南和 API 文档

### 🚀 快速开始

#### 一键启动

```bash
# 克隆项目
git clone https://github.com/dev4java/mcp-jira-service.git
cd mcp-jira-service

# 运行启动脚本
./start.sh
```

#### 最小配置

只需配置 3 个 JIRA 连接参数：

```bash
# 复制并编辑配置文件
cp env.example .env

# 编辑以下配置
JIRA_BASE_URL=https://your-jira-server.com
JIRA_USERNAME=your-username
JIRA_API_TOKEN=your-api-token
```

#### 访问服务

- **主应用**: http://localhost:8010
- **API 文档**: http://localhost:8010/swagger-ui.html
- **健康检查**: http://localhost:8011/actuator/health

#### 默认认证

- 用户名: `admin`
- 密码: `admin123`

> ⚠️ **安全提醒**: 生产环境请务必修改默认密码

### 📖 详细文档

- 📋 [快速开始指南](QUICK_START.md) - 详细的启动步骤
- 🔧 [配置指南](docs/configuration.md) - 完整的配置选项
- 🚀 [部署指南](docs/deployment.md) - 生产环境部署
- 🔌 [API 文档](docs/api.md) - REST API 接口说明
- 💡 [使用示例](examples/) - 代码示例和最佳实践

### 🛠️ 技术栈

- **后端框架**: Spring Boot 3.x + WebFlux
- **编程语言**: Java 21
- **构建工具**: Maven 3.8+
- **容器化**: Docker + Docker Compose
- **CI/CD**: GitHub Actions
- **API 文档**: OpenAPI 3.0 + Swagger UI
- **监控**: Spring Boot Actuator

### 🤝 贡献指南

我们欢迎所有形式的贡献！请查看 [贡献指南](CONTRIBUTING.md) 了解如何参与项目开发。

#### 开发流程

1. Fork 项目
2. 创建功能分支 (`git checkout -b feature/amazing-feature`)
3. 提交更改 (`git commit -m 'Add some amazing feature'`)
4. 推送到分支 (`git push origin feature/amazing-feature`)
5. 创建 Pull Request

### 📄 许可证

本项目基于 [Apache License 2.0](LICENSE) 开源许可协议。

### 🙏 致谢

感谢以下项目和社区的支持：

- [Spring Boot](https://spring.io/projects/spring-boot)
- [JIRA REST API](https://developer.atlassian.com/cloud/jira/platform/rest/v3/)
- [Model Context Protocol](https://spec.modelcontextprotocol.io/)

### 📞 联系方式

- 🐛 [问题反馈](https://github.com/dev4java/mcp-jira-service/issues)
- 💬 [讨论交流](https://github.com/dev4java/mcp-jira-service/discussions)
- 📧 邮件: [项目维护者](mailto:120171383@qq.com)

### ⭐ 如果这个项目对您有帮助，请给我们一个 Star！

[![Star History Chart](https://api.star-history.com/svg?repos=dev4java/mcp-jira-service&type=Date)](https://star-history.com/#dev4java/mcp-jira-service&Date)

---

## English

> 🚀 A powerful Spring Boot application providing JIRA integration and MCP protocol-based code generation services

### ✨ Features

- 🔗 **JIRA Integration** - Support for API Token and password authentication
- 🛠️ **Code Generation** - Automatically generate Java code from JIRA issues
- 🌐 **RESTful API** - Complete REST API interfaces
- 📡 **MCP Protocol** - Model Context Protocol implementation
- 🐳 **Docker Support** - Containerized deployment
- 🔄 **CI/CD** - GitHub Actions automation pipeline
- 📚 **Rich Documentation** - Complete usage guides and API documentation

### 🚀 Quick Start

#### One-Click Launch

```bash
# Clone the project
git clone https://github.com/dev4java/mcp-jira-service.git
cd mcp-jira-service

# Run the startup script
./start.sh
```

#### Minimal Configuration

Only 3 JIRA connection parameters needed:

```bash
# Copy and edit configuration file
cp env.example .env

# Edit the following configuration
JIRA_BASE_URL=https://your-jira-server.com
JIRA_USERNAME=your-username
JIRA_API_TOKEN=your-api-token
```

#### Access Services

- **Main Application**: http://localhost:8010
- **API Documentation**: http://localhost:8010/swagger-ui.html
- **Health Check**: http://localhost:8011/actuator/health

#### Default Authentication

- Username: `admin`
- Password: `admin123`

> ⚠️ **Security Warning**: Please change default credentials in production

### 📖 Documentation

- 📋 [Quick Start Guide](QUICK_START.md) - Detailed startup steps
- 🔧 [Configuration Guide](docs/configuration.md) - Complete configuration options
- 🚀 [Deployment Guide](docs/deployment.md) - Production environment deployment
- 🔌 [API Documentation](docs/api.md) - REST API interface specifications
- 💡 [Usage Examples](examples/) - Code examples and best practices

### 🛠️ Tech Stack

- **Backend Framework**: Spring Boot 3.x + WebFlux
- **Programming Language**: Java 21
- **Build Tool**: Maven 3.8+
- **Containerization**: Docker + Docker Compose
- **CI/CD**: GitHub Actions
- **API Documentation**: OpenAPI 3.0 + Swagger UI
- **Monitoring**: Spring Boot Actuator

### 🤝 Contributing

We welcome all forms of contributions! Please check the [Contributing Guide](CONTRIBUTING.md) to learn how to participate in project development.

#### Development Workflow

1. Fork the project
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Create a Pull Request

### 📄 License

This project is licensed under the [Apache License 2.0](LICENSE).

### 🙏 Acknowledgments

Thanks to the following projects and communities for their support:

- [Spring Boot](https://spring.io/projects/spring-boot)
- [JIRA REST API](https://developer.atlassian.com/cloud/jira/platform/rest/v3/)
- [Model Context Protocol](https://spec.modelcontextprotocol.io/)

### 📞 Contact

- 🐛 [Issue Reports](https://github.com/dev4java/mcp-jira-service/issues)
- 💬 [Discussions](https://github.com/dev4java/mcp-jira-service/discussions)
- 📧 Email: [Project Maintainers](mailto:120171383@qq.com)

### ⭐ If this project helps you, please give us a Star!

[![Star History Chart](https://api.star-history.com/svg?repos=dev4java/mcp-jira-service&type=Date)](https://star-history.com/#dev4java/mcp-jira-service&Date) 