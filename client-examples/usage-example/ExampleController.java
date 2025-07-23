package com.yusw.controller;

import com.yusw.mcptools.model.CodeGenerationRequest;
import com.yusw.mcptools.model.GeneratedCode;
import com.yusw.mcptools.model.JiraIssue;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


/**
 * 示例控制器：展示如何使用 JIRA MCP 客户端
 */
@RestController
@RequestMapping("/api/example")
@CrossOrigin(origins = "*")
public class ExampleController {

    private final JiraMcpClient jiraMcpClient;

    @Autowired
    public ExampleController(JiraMcpClient jiraMcpClient) {
        this.jiraMcpClient = jiraMcpClient;
    }

    /**
     * 获取 JIRA 问题详情
     * GET /api/example/jira/{issueKey}
     */
    @GetMapping("/jira/{issueKey}")
    public ResponseEntity<?> getJiraIssue(@PathVariable String issueKey) {
        try {
            JiraIssue issue = jiraMcpClient.getJiraIssue(issueKey);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", issue);
            response.put("message", "成功获取 JIRA 问题: " + issueKey);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "获取 JIRA 问题失败: " + e.getMessage());
            errorResponse.put("issueKey", issueKey);
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse);
        }
    }

    /**
     * 搜索 JIRA 问题
     * GET /api/example/jira/search?jql=...
     */
    @GetMapping("/jira/search")
    public ResponseEntity<?> searchJiraIssues(@RequestParam String jql) {
        try {
            List<JiraIssue> issues = jiraMcpClient.searchJiraIssues(jql);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", issues);
            response.put("count", issues.size());
            response.put("jql", jql);
            response.put("message", "搜索完成，找到 " + issues.size() + " 个问题");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "搜索 JIRA 问题失败: " + e.getMessage());
            errorResponse.put("jql", jql);
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse);
        }
    }

    /**
     * 根据项目获取问题
     * GET /api/example/jira/project/{project}
     */
    @GetMapping("/jira/project/{project}")
    public ResponseEntity<?> getIssuesByProject(@PathVariable String project) {
        try {
            List<JiraIssue> issues = jiraMcpClient.getIssuesByProject(project);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", issues);
            response.put("project", project);
            response.put("count", issues.size());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "获取项目问题失败: " + e.getMessage());
            errorResponse.put("project", project);
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse);
        }
    }

    /**
     * 根据 JIRA 问题生成代码
     * POST /api/example/generate-code
     */
    @PostMapping("/generate-code")
    public ResponseEntity<?> generateCode(@RequestBody CodeGenerationRequest request) {
        try {
            GeneratedCode code = jiraMcpClient.generateCodeFromJira(
                    request.getIssueKey(), 
                    request.getCodeType()
            );
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", code);
            response.put("message", "代码生成成功");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "代码生成失败: " + e.getMessage());
            errorResponse.put("request", request);
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse);
        }
    }

    /**
     * 生成 Entity 代码
     * POST /api/example/generate-entity/{issueKey}
     */
    @PostMapping("/generate-entity/{issueKey}")
    public ResponseEntity<?> generateEntity(@PathVariable String issueKey) {
        try {
            GeneratedCode code = jiraMcpClient.generateEntity(issueKey);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", code);
            response.put("codeType", "entity");
            response.put("issueKey", issueKey);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "message", "Entity 代码生成失败: " + e.getMessage()));
        }
    }

    /**
     * 生成 Service 代码
     * POST /api/example/generate-service/{issueKey}
     */
    @PostMapping("/generate-service/{issueKey}")
    public ResponseEntity<?> generateService(@PathVariable String issueKey) {
        try {
            GeneratedCode code = jiraMcpClient.generateService(issueKey);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", code);
            response.put("codeType", "service");
            response.put("issueKey", issueKey);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "message", "Service 代码生成失败: " + e.getMessage()));
        }
    }

    /**
     * 生成 Controller 代码
     * POST /api/example/generate-controller/{issueKey}
     */
    @PostMapping("/generate-controller/{issueKey}")
    public ResponseEntity<?> generateController(@PathVariable String issueKey) {
        try {
            GeneratedCode code = jiraMcpClient.generateController(issueKey);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", code);
            response.put("codeType", "controller");
            response.put("issueKey", issueKey);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "message", "Controller 代码生成失败: " + e.getMessage()));
        }
    }

    /**
     * 健康检查
     * GET /api/example/health
     */
    @GetMapping("/health")
    public ResponseEntity<?> healthCheck() {
        try {
            Map<String, Object> mcpHealth = jiraMcpClient.checkHealth();
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("status", "UP");
            response.put("mcpService", mcpHealth);
            response.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("status", "DOWN");
            errorResponse.put("message", "MCP 服务连接失败: " + e.getMessage());
            errorResponse.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body(errorResponse);
        }
    }

    /**
     * 获取服务信息
     * GET /api/example/info
     */
    @GetMapping("/info")
    public ResponseEntity<?> getServiceInfo() {
        try {
            Map<String, Object> mcpInfo = jiraMcpClient.getServiceInfo();
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("clientInfo", Map.of(
                    "name", "JIRA MCP Client Example",
                    "version", "1.0.0",
                    "description", "示例应用，展示如何集成 JIRA MCP 服务"
            ));
            response.put("mcpService", mcpInfo);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "message", "获取服务信息失败: " + e.getMessage()));
        }
    }

    /**
     * 批量操作示例：获取项目中的所有问题并生成代码
     * POST /api/example/batch-generate/{project}
     */
    @PostMapping("/batch-generate/{project}")
    public ResponseEntity<?> batchGenerateCode(
            @PathVariable String project,
            @RequestParam(defaultValue = "entity") String codeType) {
        try {
            // 1. 获取项目中的所有问题
            List<JiraIssue> issues = jiraMcpClient.getIssuesByProject(project);
            
            // 2. 为每个问题生成代码
            Map<String, GeneratedCode> generatedCodes = new HashMap<>();
            for (JiraIssue issue : issues) {
                try {
                    GeneratedCode code = jiraMcpClient.generateCodeFromJira(
                            issue.getKey(), codeType);
                    generatedCodes.put(issue.getKey(), code);
                } catch (Exception e) {
                    // 记录失败的问题，但继续处理其他问题
                    System.err.println("为问题 " + issue.getKey() + " 生成代码失败: " + e.getMessage());
                }
            }
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("project", project);
            response.put("codeType", codeType);
            response.put("totalIssues", issues.size());
            response.put("generatedCount", generatedCodes.size());
            response.put("generatedCodes", generatedCodes);
            response.put("message", "批量代码生成完成");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "message", "批量代码生成失败: " + e.getMessage()));
        }
    }
} 