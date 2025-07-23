# MCPTools Service

[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://openjdk.java.net/projects/jdk/21/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-green.svg)](https://spring.io/projects/spring-boot)
[![Maven](https://img.shields.io/badge/Maven-3.8+-blue.svg)](https://maven.apache.org/)
[![License](https://img.shields.io/badge/License-Apache%202.0-brightgreen.svg)](https://opensource.org/licenses/Apache-2.0)

---

## 简介

**MCPTools Service** 是一个基于 Spring Boot 3.x 和 Java 21 的现代化微服务，提供 JIRA 集成、GitLab MR 代码建议、自动代码生成等能力，适用于企业级 DevOps 场景。

- 🚀 **JIRA 集成**：支持 API Token/密码认证，自动获取和处理 JIRA 问题。
- 🔗 **GitLab 集成**：支持单个项目ID配置，获取 MR 及其代码建议，无需每次传递 projectId。
- 🛠️ **代码生成**：根据 JIRA 问题自动生成 Java 代码。
- 🌐 **RESTful API**：标准 REST API 设计，易于对接前端和自动化工具。
- 📦 **一键部署**：支持本地、Docker、云原生环境快速部署。

---

## 快速开始

### 1. 克隆项目

```bash
git clone https://github.com/dev4java/mcptools-service.git
cd mcptools-service
```

### 2. 配置环境

编辑 `src/main/resources/application.yml` 或 `.env` 文件，**只需配置一个 GitLab 项目ID**：

```yaml
gitlab:
  base-url: https://your-gitlab-server.com
  token: <your GitLab Token>
  project-id: <your GitLab project id>
```

JIRA 配置同理，支持 API Token 或密码。

### 3. 启动服务

```bash
./start.sh
```
或
```bash
mvn spring-boot:run
```

### 4. 访问服务

- 主应用: [http://localhost:8010](http://localhost:8010)
- API 文档: [http://localhost:8010/swagger-ui.html](http://localhost:8010/swagger-ui.html)
- 健康检查: [http://localhost:8011/actuator/health](http://localhost:8011/actuator/health)

---

## 典型API示例

- 获取 MR 代码建议（无需 projectId 参数）：
  ```
  GET /api/gitlab/mr/{mrIid}/code-suggestions/structured
  ```

- 获取 JIRA 问题详情：
  ```
  GET /api/jira/issue/{issueKey}
  ```

---

## 主要特性

- Spring Boot 3.x + Java 21，现代开发体验
- 配置简单，支持环境变量和 yml
- 只需配置一个 GitLab 项目ID，接口自动使用，无需每次传参
- 完善的异常处理、日志、监控
- 支持 Docker 一键部署

---

## 贡献与支持

欢迎提交 Issue、PR 或参与讨论！

---

# English

## Introduction

**MCPTools Service** is a modern Spring Boot 3.x microservice for JIRA & GitLab integration, code suggestion, and code generation.  
- **Single projectId config** for GitLab, no need to pass projectId in every API call.
- Out-of-the-box deployment, RESTful APIs, and easy extension.

## Quick Start

1. Clone the repo and enter the directory.
2. Edit `application.yml` to set your GitLab/JIRA info (only one projectId supported).
3. Run `./start.sh` or `mvn spring-boot:run`.
4. Visit [http://localhost:8010](http://localhost:8010). 