#!/bin/bash

# MCP JIRA 服务 API 调用示例
# GitHub: https://github.com/dev4java/mcp-jira-service
# 文档: https://github.com/dev4java/mcp-jira-service/wiki

BASE_URL="http://localhost:8010"

# 确保已经配置了 JIRA 连接信息
# 查看配置指南: https://github.com/dev4java/mcp-jira-service/blob/main/QUICK_START.md

echo "=== MCP JIRA 服务 API 调用示例 ==="
echo "项目地址: https://github.com/dev4java/mcp-jira-service"
echo

# 0. 检查环境变量
echo "0. 检查环境配置"
if [ -n "$JIRA_BASE_URL" ] && [ -n "$JIRA_USERNAME" ] && [ -n "$JIRA_PASSWORD" ]; then
    echo "✅ JIRA 环境变量已配置"
    echo "   JIRA_BASE_URL: $JIRA_BASE_URL"
    echo "   JIRA_USERNAME: $JIRA_USERNAME"
    echo "   JIRA_PASSWORD: [已设置]"
else
    echo "⚠️  JIRA 环境变量未配置，请确保在 application.yml 中配置了 JIRA 连接信息"
fi
echo

# 1. 健康检查
echo "1. 检查服务健康状态"
curl -X GET "$BASE_URL/api/mcp/health" \
  -H "Content-Type: application/json" | jq .
echo

# 2. 获取服务信息
echo "2. 获取服务信息"
curl -X GET "$BASE_URL/api/mcp/info" \
  -H "Content-Type: application/json" | jq .
echo

# 3. 测试 JIRA 连接
echo "3. 测试 JIRA 连接"
curl -X GET "$BASE_URL/api/jira/test-connection" \
  -H "Content-Type: application/json" | jq .
echo

# 4. 获取单个 JIRA 问题
echo "4. 获取单个 JIRA 问题"
curl -X GET "$BASE_URL/api/jira/issue/PROJECT-123" \
  -H "Content-Type: application/json" | jq .
echo

# 5. 搜索 JIRA 问题
echo "5. 搜索 JIRA 问题"
curl -X GET "$BASE_URL/api/jira/search?jql=project=PROJECT&maxResults=10" \
  -H "Content-Type: application/json" | jq .
echo

# 6. 快速生成代码
echo "6. 快速生成代码"
curl -X POST "$BASE_URL/api/code/generate/PROJECT-123?codeType=entity&packageName=com.yusw" \
  -H "Content-Type: application/json" | jq .
echo

# 7. 完整的代码生成请求
echo "7. 完整的代码生成请求"
curl -X POST "$BASE_URL/api/code/generate" \
  -H "Content-Type: application/json" \
  -d '{
    "jiraKey": "PROJECT-123",
    "codeType": "all",
    "packageName": "com.yusw.user",
    "framework": "spring-boot",
    "projectName": "user-management"
  }' | jq .
echo

# 8. MCP 协议请求 - 获取 JIRA 问题
echo "8. MCP 协议请求 - 获取 JIRA 问题"
curl -X POST "$BASE_URL/api/mcp/request" \
  -H "Content-Type: application/json" \
  -d '{
    "jsonrpc": "2.0",
    "id": 1,
    "method": "jira.getIssue",
    "params": {
      "issueKey": "PROJECT-123"
    }
  }' | jq .
echo

# 9. MCP 协议请求 - 生成代码
echo "9. MCP 协议请求 - 生成代码"
curl -X POST "$BASE_URL/api/mcp/request" \
  -H "Content-Type: application/json" \
  -d '{
    "jsonrpc": "2.0",
    "id": 2,
    "method": "code.generateFromJira",
    "params": {
      "jiraKey": "PROJECT-123",
      "codeType": "service",
      "packageName": "com.yusw.user",
      "framework": "spring-boot"
    }
  }' | jq .
echo

# 10. 获取代码生成选项
echo "10. 获取代码生成选项"
curl -X GET "$BASE_URL/api/code/options" \
  -H "Content-Type: application/json" | jq .
echo

echo "=== 示例完成 ===" 