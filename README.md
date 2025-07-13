# MCP JIRA 服务(MCP JIRA Service)

这是一个基于 Spring Boot 的 MCP (Model Context Protocol) 服务，用于连接 JIRA 并根据 JIRA 内容生成功能代码。(This is a Spring Boot-based MCP (Model Context Protocol) service for connecting to JIRA and generating functional code based on JIRA content.)

## 功能特性(Features)

- **JIRA 集成**: 连接私有化部署的 JIRA 服务，获取问题详情和搜索功能(Connect to privately deployed JIRA services, get issue details and search functionality)
- **MCP 协议支持**: 完整的 MCP 协议实现，支持请求/响应和通知(Complete MCP protocol implementation, supporting request/response and notifications)
- **代码生成**: 根据 JIRA 问题自动生成 Java 代码（Entity、Service、Controller）(Automatically generate Java code based on JIRA issues - Entity, Service, Controller)
- **RESTful API**: 提供完整的 REST API 端点(Provide complete REST API endpoints)
- **异步处理**: 使用 Spring WebFlux 实现响应式编程(Asynchronous processing using Spring WebFlux for reactive programming)
- **错误处理**: 全局异常处理和错误响应(Global exception handling and error responses)
- **日志记录**: 完整的日志记录和监控(Complete logging and monitoring)

## 技术栈(Technology Stack)

- **Java 21**: 使用最新的 Java 语言特性(Using the latest Java language features)
- **Spring Boot 3.2**: 现代化的 Spring Boot 框架(Modern Spring Boot framework)
- **Spring WebFlux**: 响应式编程模型(Reactive programming model)
- **Maven**: 项目构建和依赖管理(Project build and dependency management)
- **JavaPoet**: 代码生成库(Code generation library)
- **JIRA REST API**: JIRA 官方 REST 客户端(Official JIRA REST client)

## 🚀 开箱即用快速开始 (Out-of-the-Box Quick Start)

> **新特性**：只需一个命令即可启动！无需复杂配置，提供完整的演示环境。

### ⚡ 一键启动
```bash
git clone https://github.com/yourusername/mcp-jira-service.git
cd mcp-jira-service
./start.sh  # 自动引导配置和启动
```

### 📋 最小配置（仅需 3 项）
```bash
# 编辑 .env 文件中的 JIRA 连接信息
JIRA_BASE_URL=https://your-jira-server.com
JIRA_USERNAME=your-username  
JIRA_PASSWORD=your-password
```

### 🎯 三种启动模式
1. **本地开发模式** - 快速开发调试
2. **Docker 部署模式** - 生产环境部署  
3. **无安全测试模式** - 快速功能验证

### 🌐 立即访问
- **应用服务**: http://localhost:8010
- **API 文档**: http://localhost:8010/swagger-ui.html
- **健康检查**: http://localhost:8011/actuator/health

### 🔐 默认登录
- 用户名: `admin`
- 密码: `admin123`

> 📚 **详细指南**: 查看 [快速开始指南](QUICK_START.md) 了解更多启动选项

## 传统安装方式 (Traditional Setup)

### 1. 环境要求(Environment Requirements)

- Java 21 或更高版本(Java 21 or higher)
- Maven 3.8 或更高版本(Maven 3.8 or higher)
- JIRA 服务器访问权限(JIRA server access permissions)

### 2. 手动配置(Manual Configuration)

在 `.env` 文件中配置 JIRA 连接信息：(Configure JIRA connection information in `.env` file:)

```bash
JIRA_BASE_URL=https://your-jira-server.com
JIRA_USERNAME=your-username
JIRA_PASSWORD=your-password
```

### 3. 构建和运行(Build and Run)

```bash
# 构建项目(Build project)
mvn clean package

# 运行服务(Run service)
mvn spring-boot:run

# 或者直接运行 JAR 文件(Or run JAR file directly)
java -jar target/mcp-jira-service-1.0.0.jar
```

服务将在 `http://localhost:8010/mcp-jira` 上启动。(The service will start at `http://localhost:8010/mcp-jira`.)

> **私有化部署用户**(Private Deployment Users)：如果您使用的是企业内部部署的 JIRA 服务器，请参考 [私有化部署配置指南](docs/private-jira-setup.md) 获取详细的配置说明和故障排除方法。(If you are using an enterprise internally deployed JIRA server, please refer to the [Private Deployment Configuration Guide](docs/private-jira-setup.md) for detailed configuration instructions and troubleshooting methods.)

## API 文档(API Documentation)

### MCP 端点(MCP Endpoints)

#### 处理 MCP 请求(Handle MCP Request)
```
POST /mcp-jira/api/mcp/request
Content-Type: application/json

{
    "jsonrpc": "2.0",
    "id": 1,
    "method": "jira.getIssue",
    "params": {
        "issueKey": "PROJECT-123"
    }
}
```

#### 获取服务信息(Get Service Information)
```
GET /mcp-jira/api/mcp/info
```

#### 健康检查(Health Check)
```
GET /mcp-jira/api/mcp/health
```

### JIRA 端点(JIRA Endpoints)

#### 获取单个问题(Get Single Issue)
```
GET /mcp-jira/api/jira/issue/{issueKey}
```

#### 搜索问题(Search Issues)
```
GET /mcp-jira/api/jira/search?jql=project=PROJECT&maxResults=50
```

#### 获取项目问题(Get Project Issues)
```
GET /mcp-jira/api/jira/project/{projectKey}/issues?maxResults=50
```

#### 测试连接(Test Connection)
```
GET /mcp-jira/api/jira/test-connection
```

### 代码生成端点(Code Generation Endpoints)

#### 生成代码(Generate Code)
```
POST /mcp-jira/api/code/generate
Content-Type: application/json

{
    "jiraKey": "PROJECT-123",
    "codeType": "all",
    "packageName": "com.yusw.generated",
    "framework": "spring-boot",
    "projectName": "my-project"
}
```

#### 快速生成(Quick Generate)
```
POST /mcp-jira/api/code/generate/{jiraKey}?codeType=entity&packageName=com.yusw
```

#### 获取生成选项(Get Generation Options)
```
GET /mcp-jira/api/code/options
```

## MCP 协议支持(MCP Protocol Support)

服务支持以下 MCP 方法：(The service supports the following MCP methods:)

- `jira.getIssue`: 获取指定的 JIRA 问题(Get specified JIRA issue)
- `jira.searchIssues`: 搜索 JIRA 问题(Search JIRA issues)
- `jira.testConnection`: 测试 JIRA 连接(Test JIRA connection)
- `code.generate`: 生成代码(Generate code)
- `code.generateFromJira`: 从 JIRA 问题生成代码(Generate code from JIRA issues)

### 请求示例(Request Example)

```json
{
    "jsonrpc": "2.0",
    "id": 1,
    "method": "code.generateFromJira",
    "params": {
        "jiraKey": "PROJECT-123",
        "codeType": "all",
        "packageName": "com.yusw.generated",
        "framework": "spring-boot"
    }
}
```

### 响应示例(Response Example)

```json
{
    "jsonrpc": "2.0",
    "id": 1,
    "result": {
        "jiraKey": "PROJECT-123",
        "generatedFiles": 3,
        "files": [
            {
                "fileName": "UserEntity.java",
                "className": "UserEntity",
                "packageName": "com.yusw.generated.entity",
                "codeType": "entity",
                "content": "package com.yusw.generated.entity;\n\npublic class UserEntity {\n..."
            }
        ],
        "message": "代码生成成功(Code generation successful)"
    }
}
```

## 代码生成(Code Generation)

### 支持的代码类型(Supported Code Types)

- **entity**: 生成 JPA 实体类(Generate JPA entity classes)
- **service**: 生成 Spring Service 类(Generate Spring Service classes)
- **controller**: 生成 Spring REST Controller 类(Generate Spring REST Controller classes)
- **all**: 生成所有类型的代码(Generate all types of code)

### 生成规则(Generation Rules)

代码生成器会根据 JIRA 问题的内容自动生成相应的代码：(The code generator automatically generates corresponding code based on JIRA issue content:)

1. **类名**(Class Name): 从 JIRA 问题标题中提取并转换为驼峰命名(Extract from JIRA issue title and convert to camelCase)
2. **字段**(Fields): 从 JIRA 问题描述中解析字段信息(Parse field information from JIRA issue description)
3. **注释**(Comments): 包含 JIRA 问题的相关信息(Include relevant information from JIRA issue)
4. **框架**(Framework): 根据指定的框架添加相应的注解(Add corresponding annotations based on specified framework)

### 生成的文件结构(Generated File Structure)

```
generated-code/
├── UserEntity.java
├── UserService.java
└── UserController.java
```

## 配置选项(Configuration Options)

### 服务器配置(Server Configuration)

```yaml
server:
  port: 8010                          # 服务端口(Service port)
  servlet:
    context-path: /mcp-jira           # 上下文路径(Context path)
```

### JIRA 配置(JIRA Configuration)

```yaml
jira:
  base-url: https://your-jira-server.com        # JIRA 服务器地址(JIRA server URL)
  username: your-username                       # JIRA 用户名(JIRA username)
  password: your-password                       # JIRA 密码(JIRA password)
  timeout: 30000                                # 超时时间（毫秒）(Timeout in milliseconds)
  max-connections: 50                           # 最大连接数(Maximum connections)
```

### MCP 配置(MCP Configuration)

```yaml
mcp:
  server:
    host: localhost      # MCP 服务器主机(MCP server host)
    port: 8021          # MCP 服务器端口（用于配置，实际HTTP服务运行在8010）(MCP server port - for configuration, actual HTTP service runs on 8010)
    protocol: websocket # 协议类型（配置项，实际实现为HTTP REST）(Protocol type - configuration item, actual implementation is HTTP REST)
  
  code-generation:
    output-directory: ./generated-code    # 代码输出目录(Code output directory)
    template-directory: ./templates       # 模板目录(Template directory)
    default-package: com.generated        # 默认包名(Default package name)
```

### 日志配置(Logging Configuration)

```yaml
logging:
  level:
    com.yusw.mcpjira: DEBUG
    org.springframework.web: INFO
  file:
    name: logs/mcp-jira-service.log
```

## 错误处理(Error Handling)

服务提供了完整的错误处理机制：(The service provides complete error handling mechanisms:)

- **JIRA_API_ERROR**: JIRA API 调用错误(JIRA API call error)
- **JIRA_CONNECTION_ERROR**: JIRA 连接错误(JIRA connection error)
- **CODE_GENERATION_ERROR**: 代码生成错误(Code generation error)
- **VALIDATION_ERROR**: 参数验证错误(Parameter validation error)
- **RUNTIME_ERROR**: 运行时错误(Runtime error)

错误响应格式：(Error response format:)

```json
{
    "success": false,
    "errorCode": "JIRA_API_ERROR",
    "message": "JIRA API 调用失败: Issue not found",
    "statusCode": 404,
    "timestamp": "2024-01-01T12:00:00"
}
```

## 监控和健康检查(Monitoring and Health Check)

### 健康检查端点(Health Check Endpoints)

```
GET /mcp-jira/actuator/health
GET /mcp-jira/api/mcp/health
```

### 监控端点(Monitoring Endpoints)

```
GET /mcp-jira/actuator/metrics
GET /mcp-jira/actuator/info
GET /mcp-jira/actuator/prometheus
```

## 开发指南(Development Guide)

### 添加新的 MCP 方法(Adding New MCP Methods)

1. 在 `McpJiraService` 中添加新的方法处理逻辑(Add new method handling logic in `McpJiraService`)
2. 更新 `handleRequest` 方法中的 switch 语句(Update switch statement in `handleRequest` method)
3. 添加相应的测试用例(Add corresponding test cases)

### 添加新的代码生成类型(Adding New Code Generation Types)

1. 在 `CodeGeneratorService` 中添加新的生成方法(Add new generation methods in `CodeGeneratorService`)
2. 更新 `generateCodeFromJira` 方法中的 switch 语句(Update switch statement in `generateCodeFromJira` method)
3. 创建相应的代码模板(Create corresponding code templates)

### 扩展 JIRA 功能(Extending JIRA Functionality)

1. 在 `JiraClientService` 中添加新的 API 调用方法(Add new API call methods in `JiraClientService`)
2. 更新 `JiraIssue` 模型以支持新的字段(Update `JiraIssue` model to support new fields)
3. 在控制器中添加新的端点(Add new endpoints in controllers)

## 故障排除(Troubleshooting)

### 常见问题(Common Issues)

1. **JIRA 连接失败**(JIRA Connection Failed)
   - 检查 JIRA 服务器地址是否正确(Check if JIRA server URL is correct)
   - 确认用户名和密码有效(Confirm username and password are valid)
   - 检查网络连接和防火墙设置(Check network connection and firewall settings)

2. **代码生成失败**(Code Generation Failed)
   - 确认输出目录有写权限(Ensure output directory has write permissions)
   - 检查 JIRA 问题描述格式(Check JIRA issue description format)
   - 查看日志文件获取详细错误信息(Check log files for detailed error information)

3. **MCP 协议错误**(MCP Protocol Error)
   - 检查请求格式是否正确(Check if request format is correct)
   - 确认方法名是否支持(Confirm method name is supported)
   - 验证参数是否完整(Verify parameters are complete)

4. **无法访问服务**(Cannot Access Service)
   - 确认服务运行在正确端口：8010(Confirm service is running on correct port: 8010)
   - 检查访问地址包含context-path：`/mcp-jira`(Check access URL includes context-path: `/mcp-jira`)
   - 完整访问地址：`http://localhost:8010/mcp-jira`(Complete access URL: `http://localhost:8010/mcp-jira`)

### 日志文件(Log Files)

日志文件位置：`logs/mcp-jira-service.log`(Log file location: `logs/mcp-jira-service.log`)

可以通过调整日志级别来获取更多调试信息：(You can get more debugging information by adjusting log levels:)

```yaml
logging:
  level:
    com.yusw.mcpjira: DEBUG
```

## 重要说明(Important Notes)

### 服务访问地址(Service Access URL)

- **服务端口**(Service Port): 8010
- **Context Path**: /mcp-jira
- **完整地址**(Complete URL): http://localhost:8010/mcp-jira
- **API端点示例**(API Endpoint Example): http://localhost:8010/mcp-jira/api/mcp/info

### MCP协议实现(MCP Protocol Implementation)

当前版本采用HTTP REST实现MCP协议，而非WebSocket。配置中的websocket设置为兼容性配置项。(The current version implements MCP protocol using HTTP REST, not WebSocket. The websocket setting in configuration is for compatibility.)

## 贡献指南(Contributing Guide)

1. Fork 项目(Fork the project)
2. 创建功能分支(Create feature branch)
3. 提交更改(Commit changes)
4. 推送到分支(Push to branch)
5. 创建 Pull Request(Create Pull Request)

## 许可证(License)

本项目使用 MIT 许可证，详情请参见 LICENSE 文件。(This project uses MIT license, please see LICENSE file for details.)

## 联系方式(Contact)

如有问题或建议，请通过以下方式联系：(For questions or suggestions, please contact through the following methods:)

- 创建 GitHub Issue(Create GitHub Issue)
- 发送邮件至项目维护者(Send email to project maintainers)

---

**注意**(Note): 请确保在生产环境中使用 HTTPS 并配置适当的安全措施。(Please ensure to use HTTPS and configure appropriate security measures in production environment.) 