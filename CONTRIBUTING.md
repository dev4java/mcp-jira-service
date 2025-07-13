# 贡献指南 (Contributing Guide)

首先，感谢您考虑为 MCP JIRA Service 做出贡献！🎉

这个文档将指导您如何为项目做出贡献。

## 📋 目录

- [代码行为准则](#代码行为准则)
- [如何贡献](#如何贡献)
- [开发环境设置](#开发环境设置)
- [提交流程](#提交流程)
- [代码规范](#代码规范)
- [问题反馈](#问题反馈)

## 📜 代码行为准则

请阅读并遵守我们的 [行为准则](CODE_OF_CONDUCT.md)。

## 🤝 如何贡献

### 🐛 报告 Bug

在报告 Bug 之前：
1. 搜索 [现有 Issues](https://github.com/dev4java/mcp-jira-service/issues) 确认问题未被报告
2. 使用最新版本重现问题

报告 Bug 时请包含：
- 详细的问题描述
- 重现步骤
- 预期行为 vs 实际行为
- 环境信息（操作系统、Java 版本等）
- 错误日志

### 💡 建议新功能

在建议新功能前：
1. 搜索 [现有 Issues](https://github.com/dev4java/mcp-jira-service/issues) 确认功能未被提出
2. 考虑功能是否符合项目目标

建议功能时请包含：
- 功能的详细描述
- 使用场景
- 可能的实现方案
- 是否愿意自己实现

### 🔧 代码贡献

1. **Fork 项目**
   ```bash
   # Fork 项目到您的 GitHub 账户
   # 然后克隆到本地
   git clone https://github.com/YOUR_USERNAME/mcp-jira-service.git
   cd mcp-jira-service
   ```

2. **设置上游仓库**
   ```bash
   git remote add upstream https://github.com/dev4java/mcp-jira-service.git
   ```

3. **创建功能分支**
   ```bash
   git checkout -b feature/your-feature-name
   ```

4. **开发和测试**
   - 编写代码
   - 添加测试
   - 确保所有测试通过

5. **提交更改**
   ```bash
   git commit -m "feat: add your feature description"
   ```

6. **推送分支**
   ```bash
   git push origin feature/your-feature-name
   ```

7. **创建 Pull Request**
   - 在 GitHub 上创建 PR
   - 填写 PR 模板
   - 等待代码审查

## 🛠️ 开发环境设置

### 环境要求

- Java 21 或更高版本
- Maven 3.8 或更高版本
- Git
- Docker (可选，用于容器化测试)

### 设置步骤

```bash
# 1. 克隆项目
git clone https://github.com/dev4java/mcp-jira-service.git
cd mcp-jira-service

# 2. 复制配置文件
cp env.example .env
# 编辑 .env 文件，配置您的 JIRA 连接信息

# 3. 构建项目
mvn clean install

# 4. 运行测试
mvn test

# 5. 启动应用
mvn spring-boot:run
```

### IDE 配置

推荐使用 IntelliJ IDEA 或 Eclipse，配置建议：

- 导入 `checkstyle.xml` 代码检查规则
- 配置 Java 21
- 安装 Spring Boot 插件

## 📝 提交流程

### 提交信息格式

使用 [Conventional Commits](https://www.conventionalcommits.org/) 格式：

```
type(scope): description

[optional body]

[optional footer]
```

**类型 (type):**
- `feat`: 新功能
- `fix`: Bug 修复
- `docs`: 文档更新
- `style`: 代码格式化
- `refactor`: 代码重构
- `test`: 测试相关
- `chore`: 构建过程或辅助工具的变动

**示例:**
```
feat(auth): add JWT token support
fix(api): resolve JIRA connection timeout issue
docs(readme): update installation guide
```

### Pull Request 要求

- PR 标题清晰描述变更内容
- 包含相关的 Issue 编号
- 添加必要的测试
- 更新相关文档
- 确保 CI 检查通过

## 📋 代码规范

### Java 代码规范

- 遵循 [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html)
- 使用项目提供的 `checkstyle.xml` 配置
- 类和方法需要 Javadoc 注释
- 变量命名采用驼峰命名法

### 测试规范

- 为新功能编写单元测试
- 测试覆盖率不低于 80%
- 使用 JUnit 5 和 Spring Boot Test
- 测试方法命名清晰，描述测试场景

### 文档规范

- 使用 Markdown 格式
- 中英文混合时注意空格
- 代码示例需要完整可运行
- 及时更新 API 文档

## 🔍 代码审查

### 审查标准

- 代码功能正确
- 代码风格一致
- 测试充分
- 文档完整
- 性能考虑

### 审查流程

1. 提交 PR 后，维护者将进行代码审查
2. 根据审查意见修改代码
3. 所有检查通过后，PR 将被合并

## 🐛 问题反馈

### 报告 Bug

使用 [Bug 报告模板](https://github.com/dev4java/mcp-jira-service/issues/new?template=bug_report.md)

### 功能请求

使用 [功能请求模板](https://github.com/dev4java/mcp-jira-service/issues/new?template=feature_request.md)

### 讨论交流

使用 [GitHub Discussions](https://github.com/dev4java/mcp-jira-service/discussions) 进行：
- 技术讨论
- 使用问题
- 项目规划

## 🏷️ 版本发布

项目使用 [语义化版本](https://semver.org/)：

- `MAJOR.MINOR.PATCH`
- 主要版本：不兼容的 API 修改
- 次要版本：向后兼容的功能性新增
- 修订版本：向后兼容的问题修正

## 🎉 贡献者

感谢所有贡献者的努力！

[![Contributors](https://contrib.rocks/image?repo=dev4java/mcp-jira-service)](https://github.com/dev4java/mcp-jira-service/graphs/contributors)

## 📞 联系我们

- 📧 邮件: [dev4java@example.com](mailto:dev4java@example.com)
- 💬 讨论: [GitHub Discussions](https://github.com/dev4java/mcp-jira-service/discussions)
- 🐛 问题: [GitHub Issues](https://github.com/dev4java/mcp-jira-service/issues)

---

再次感谢您的贡献！每一个贡献都让这个项目变得更好。🙏 