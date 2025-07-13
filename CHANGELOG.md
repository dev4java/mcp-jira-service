# 更新日志 (Changelog)

本文件记录了 MCP JIRA Service 项目的所有重要变更。

格式基于 [Keep a Changelog](https://keepachangelog.com/zh-CN/1.0.0/)，
项目遵循 [语义化版本](https://semver.org/lang/zh-CN/)。

## [未发布 (Unreleased)]

### 新增 (Added)
- **开箱即用特性** ⚡
  - 一键启动脚本 (`./start.sh`)
  - 三种启动模式（本地/Docker/无安全）
  - 最小配置要求（仅需 3 个 JIRA 配置项）
  - 快速开始指南 (QUICK_START.md)
- 企业级开源项目治理文件
  - Apache 2.0 开源许可证
  - 贡献指南和行为准则
  - 环境变量配置示例
- CI/CD 流水线
  - GitHub Actions 自动化构建
  - 代码质量检查（Checkstyle、SpotBugs）
  - 测试覆盖率报告（JaCoCo）
  - 安全漏洞扫描（OWASP Dependency Check）
- 容器化部署支持
  - 多阶段构建 Dockerfile
  - 简化版 Docker Compose（仅核心服务）
- API 文档和监控
  - OpenAPI 3.0 文档集成
  - Swagger UI 接口文档
  - 基础健康检查

### 改进 (Changed)
- **简化架构设计** 🎯
  - 移除 Redis 缓存依赖
  - 移除复杂监控组件（Prometheus、Grafana）
  - 减少必须配置项
  - 优化资源使用（内存、线程池）
- 安全配置优化
  - 可选的安全认证（可完全禁用）
  - 简化用户管理（默认 admin/admin123）
  - JIRA API Token 支持
  - 开发友好的 CORS 配置
- 配置管理增强
  - 提供合理默认值
  - 演示 JIRA 配置示例
  - 环境变量优先级
  - 简化日志输出


### 移除 (Removed)
- Redis 缓存依赖和相关配置
- Prometheus 和 Grafana 监控组件
- 复杂的权限控制和用户管理
- 非必要的依赖组件

### 修复 (Fixed)
- 配置文件安全性问题
- 依赖版本兼容性
- 内存和资源使用优化

### 安全 (Security)
- 可配置的安全认证机制
- 简化但安全的默认配置
- 开发环境友好的安全策略

## [1.0.0] - 2024-XX-XX

### 新增 (Added)
- 基础 MCP 协议服务框架
- JIRA 客户端集成
- 代码生成功能
- RESTful API 端点
- 响应式编程支持
- 基础监控和日志

### 技术栈 (Technology Stack)
- Java 21
- Spring Boot 3.2.0
- Spring WebFlux
- Maven 构建工具
- JIRA REST API 客户端

---

## 版本说明 (Version Notes)

### 版本格式
- **主版本号 (MAJOR)**：不兼容的 API 修改
- **次版本号 (MINOR)**：向下兼容的功能性新增
- **修订号 (PATCH)**：向下兼容的问题修正

### 更新类型
- **新增 (Added)**：新功能
- **改进 (Changed)**：现有功能的变更
- **弃用 (Deprecated)**：即将移除的功能
- **移除 (Removed)**：已移除的功能
- **修复 (Fixed)**：Bug 修复
- **安全 (Security)**：安全相关的修复

### 链接格式
[未发布 (Unreleased)]: https://github.com/yourusername/mcp-jira-service/compare/v1.0.0...HEAD
[1.0.0]: https://github.com/yourusername/mcp-jira-service/releases/tag/v1.0.0 