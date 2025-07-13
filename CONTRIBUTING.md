# 贡献指南 (Contributing Guide)

感谢您对 MCP JIRA Service 项目的关注！我们欢迎任何形式的贡献。

## 🤝 如何贡献

### 报告问题 (Reporting Issues)

如果您发现了 bug 或有功能建议，请：

1. 在 [Issues](../../issues) 页面搜索，确保问题尚未被报告
2. 使用适当的 issue 模板创建新的 issue
3. 提供详细的描述和重现步骤
4. 如果是 bug，请包含：
   - 运行环境信息（Java 版本、操作系统等）
   - 错误日志或堆栈跟踪
   - 最小重现示例

### 提交代码 (Code Contributions)

#### 开发环境设置

1. **Fork 项目**
   ```bash
   # 克隆您的 fork
   git clone https://github.com/YOUR_USERNAME/mcp-jira-service.git
   cd mcp-jira-service
   
   # 添加原始仓库为 upstream
   git remote add upstream https://github.com/ORIGINAL_OWNER/mcp-jira-service.git
   ```

2. **环境要求**
   - Java 21 或更高版本
   - Maven 3.8 或更高版本
   - Git 2.x

3. **安装依赖**
   ```bash
   mvn clean install
   ```

4. **配置环境**
   ```bash
   # 复制配置文件模板
   cp env.example .env
   
   # 编辑 .env 文件，填入您的 JIRA 连接信息
   ```

#### 开发流程

1. **创建功能分支**
   ```bash
   git checkout -b feature/your-feature-name
   # 或
   git checkout -b fix/your-bug-fix
   ```

2. **编写代码**
   - 遵循现有的代码风格
   - 编写清晰的提交消息
   - 为新功能添加测试
   - 确保所有测试通过

3. **运行测试**
   ```bash
   # 运行所有测试
   mvn clean test
   
   # 运行特定测试
   mvn test -Dtest=YourTestClass
   
   # 查看测试覆盖率
   mvn jacoco:report
   ```

4. **代码质量检查**
   ```bash
   # 编译检查
   mvn clean compile
   
   # 代码格式检查
   mvn checkstyle:check
   
   # 静态分析
   mvn spotbugs:check
   ```

5. **提交更改**
   ```bash
   git add .
   git commit -m "feat: add new feature description"
   git push origin feature/your-feature-name
   ```

6. **创建 Pull Request**
   - 使用清晰的标题和描述
   - 引用相关的 issue
   - 确保 CI/CD 检查通过

## 📋 代码规范

### Java 代码风格

- 使用 4 个空格缩进
- 类名使用 PascalCase
- 方法和变量名使用 camelCase
- 常量使用 ALL_CAPS
- 行长度限制为 120 字符

### 注释规范

```java
/**
 * 服务类说明
 * 
 * @author 作者名
 * @since 版本号
 */
public class ExampleService {
    
    /**
     * 方法功能描述
     * 
     * @param param 参数说明
     * @return 返回值说明
     * @throws Exception 异常说明
     */
    public String exampleMethod(String param) throws Exception {
        // 单行注释
        return "result";
    }
}
```

### 提交消息规范

使用 [Conventional Commits](https://www.conventionalcommits.org/) 格式：

```
<type>[optional scope]: <description>

[optional body]

[optional footer(s)]
```

类型 (type)：
- `feat`: 新功能
- `fix`: bug 修复
- `docs`: 文档更新
- `style`: 代码格式化
- `refactor`: 代码重构
- `test`: 测试相关
- `chore`: 构建过程或辅助工具的变动

示例：
```
feat(jira): add support for JIRA API v3

- Implement new JiraClientV3Service
- Add configuration for API version selection
- Update tests for new API endpoints

Closes #123
```

## 🧪 测试要求

### 测试类型

1. **单元测试**
   - 所有公共方法都应有对应的单元测试
   - 测试覆盖率应保持在 80% 以上
   - 使用 JUnit 5 和 Mockito

2. **集成测试**
   - 对 REST API 端点进行集成测试
   - 使用 @SpringBootTest 注解
   - 测试真实的组件交互

3. **契约测试**
   - 对外部 API 调用进行契约测试
   - 使用 WireMock 模拟外部服务

### 测试命名规范

```java
@Test
void shouldReturnValidResponse_WhenValidInputProvided() {
    // Given
    String validInput = "test";
    
    // When
    String result = service.process(validInput);
    
    // Then
    assertThat(result).isNotNull();
}
```

## 📚 文档贡献

### API 文档

- 使用 Swagger/OpenAPI 注解
- 为所有 REST 端点提供示例
- 包含错误响应的说明

### README 更新

如果您的更改影响到：
- 安装过程
- 配置方式
- 使用方法

请同时更新 README.md 文件。

### 代码注释

- 所有公共 API 必须有 Javadoc 注释
- 复杂的业务逻辑需要详细注释
- 支持中英文双语注释

## 🔍 代码审查

### 审查流程

1. 所有 PR 需要至少一个维护者的审查
2. 确保 CI/CD 检查全部通过
3. 代码必须遵循项目的编码规范
4. 新功能必须包含适当的测试

### 审查标准

- 代码质量和可读性
- 测试覆盖率和测试质量
- 性能影响
- 安全性考虑
- 文档完整性

## 🏷️ 发布流程

### 版本命名

使用 [语义化版本](https://semver.org/lang/zh-CN/)：
- `MAJOR.MINOR.PATCH`
- 示例：`1.2.3`

### 发布检查清单

- [ ] 所有测试通过
- [ ] 更新 CHANGELOG.md
- [ ] 更新版本号
- [ ] 创建 Git 标签
- [ ] 发布 GitHub Release

## 🆘 获取帮助

如果您在贡献过程中遇到问题：

1. 查看 [FAQ](../../wiki/FAQ)
2. 搜索现有的 [Issues](../../issues)
3. 创建新的 issue 寻求帮助
4. 在 PR 中 @mention 维护者

## 🙏 感谢

感谢所有为这个项目做出贡献的开发者！您的贡献使这个项目变得更好。

---

## English Version

Thank you for your interest in contributing to MCP JIRA Service! We welcome contributions of all kinds.

For the English version of this guide, please see our [Contributing Guide (English)](CONTRIBUTING_EN.md). 