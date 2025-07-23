# 🚀 快速开始指南 (Quick Start Guide)

## 1. 环境要求

- Java 21+
- Maven 3.8+
- 推荐 Linux/macOS/WSL

## 2. 克隆与配置

```bash
git clone https://github.com/dev4java/mcptools-service.git
cd mcptools-service
```

编辑 `src/main/resources/application.yml`，**只需配置一个 GitLab 项目ID**：

```yaml
gitlab:
  base-url: https://your-gitlab-server.com
  token: <your GitLab Token>
  project-id: <your GitLab project id>
```

JIRA 配置示例：

```yaml
jira:
  base-url: https://your-jira-server.com
  username: your username
  api-token: your api-token
```

## 3. 启动服务

```bash
./start.sh
```
或
```bash
mvn spring-boot:run
```

## 4. 典型API调用

- 获取 MR 结构化建议（无需 projectId 参数）：
  ```
  GET http://localhost:8010/mcptools/api/gitlab/mr/327/code-suggestions/structured
  ```

- 获取 JIRA 问题详情：
  ```
  GET http://localhost:8010/mcptools/api/jira/issue/ABC-123
  ```

## 5. 常见问题

- **Q: 可以配置多个 projectId 吗？**  
  A: 当前仅支持单个 projectId，配置为字符串即可。

- **Q: 404 Not Found from GET /projects/[1029]/...?**  
  A: 请确保 project-id 配置为字符串（如 `1029`），不要用数组或带中括号。

## 6. 其它

- 支持 Docker 部署，详见 `docker-compose.yml`
- 日志、监控、健康检查等均已内置

---

# English Quick Start

1. Java 21+, Maven 3.8+
2. Clone the repo and enter the directory.
3. Edit `application.yml` to set your GitLab/JIRA info (only one projectId supported).
4. Run `./start.sh` or `mvn spring-boot:run`.
5. Visit [http://localhost:8010](http://localhost:8010). 