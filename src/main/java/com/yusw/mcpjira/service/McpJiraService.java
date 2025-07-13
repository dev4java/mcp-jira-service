package com.yusw.mcpjira.service;

import com.yusw.mcpjira.mcp.McpRequest;
import com.yusw.mcpjira.mcp.McpResponse;
import com.yusw.mcpjira.model.CodeGenerationRequest;
import com.yusw.mcpjira.model.GeneratedCode;
import com.yusw.mcpjira.model.JiraIssue;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


/**
 * MCP JIRA 服务主要业务逻辑
 * 
 * @author yusw
 */
@Service
public class McpJiraService {
    
    private static final Logger logger = LoggerFactory.getLogger(McpJiraService.class);
    
    private final JiraClientService jiraClientService;
    private final CodeGeneratorService codeGeneratorService;
    
    @Autowired
    public McpJiraService(JiraClientService jiraClientService, CodeGeneratorService codeGeneratorService) {
        this.jiraClientService = jiraClientService;
        this.codeGeneratorService = codeGeneratorService;
    }
    
    /**
     * 处理 MCP 请求(Handle MCP request)
     */
    public Mono<McpResponse> handleRequest(McpRequest request) {
        logger.info("Handle MCP request: {}", request.getMethod());
        
        return switch (request.getMethod()) {
            case "jira.getIssue" -> handleGetIssue(request);
            case "jira.searchIssues" -> handleSearchIssues(request);
            case "jira.testConnection" -> handleTestConnection(request);
            case "code.generate" -> handleGenerateCode(request);
            case "code.generateFromJira" -> handleGenerateCodeFromJira(request);
            default -> Mono.just(new McpResponse(request.getId(), 
                    new McpResponse.McpError(-32601, "Method not found: " + request.getMethod())));
        };
    }
    
    /**
     * 处理获取单个 JIRA 问题的请求
     */
    private Mono<McpResponse> handleGetIssue(McpRequest request) {
        String issueKey = (String) request.getParams().get("issueKey");
        
        if (issueKey == null) {
            return Mono.just(new McpResponse(request.getId(), 
                    new McpResponse.McpError(-32602, "Missing parameter: issueKey")));
        }
        
        return jiraClientService.getIssueByKey(issueKey)
                .map(issue -> new McpResponse(request.getId(), createIssueResponse(issue)))
                .onErrorReturn(new McpResponse(request.getId(), 
                        new McpResponse.McpError(-32603, "Failed to get JIRA issue")));
    }
    
    /**
     * 处理搜索 JIRA 问题的请求
     */
    private Mono<McpResponse> handleSearchIssues(McpRequest request) {
        String jql = (String) request.getParams().get("jql");
        Integer maxResults = (Integer) request.getParams().getOrDefault("maxResults", 50);
        
        if (jql == null) {
            return Mono.just(new McpResponse(request.getId(), 
                    new McpResponse.McpError(-32602, "Missing parameter: jql")));
        }
        
        return jiraClientService.searchIssues(jql, maxResults)
                .collectList()
                .map(issues -> {
                    Map<String, Object> result = new HashMap<>();
                    result.put("total", issues.size());
                    result.put("issues", issues.stream().map(this::createIssueResponse).toList());
                    return new McpResponse(request.getId(), result);
                })
                .onErrorReturn(new McpResponse(request.getId(), 
                        new McpResponse.McpError(-32603, "Failed to search JIRA issues")));
    }
    
    /**
     * 处理测试 JIRA 连接的请求
     */
    private Mono<McpResponse> handleTestConnection(McpRequest request) {
        return jiraClientService.testConnection()
                .map(connected -> {
                    Map<String, Object> result = new HashMap<>();
                    result.put("connected", connected);
                    result.put("message", connected ? "连接成功" : "连接失败");
                    return new McpResponse(request.getId(), result);
                })
                .onErrorReturn(new McpResponse(request.getId(), 
                        new McpResponse.McpError(-32603, "Failed to test JIRA connection")));
    }
    
    /**
     * 处理生成代码的请求
     */
    private Mono<McpResponse> handleGenerateCode(McpRequest request) {
        try {
            CodeGenerationRequest codeRequest = mapToCodeGenerationRequest(request.getParams());
            
            return codeGeneratorService.generateCodeFromJira(codeRequest)
                    .map(generatedCodes -> {
                        Map<String, Object> result = new HashMap<>();
                        result.put("generatedFiles", generatedCodes.size());
                        result.put("files", generatedCodes.stream().map(this::createCodeResponse).toList());
                        return new McpResponse(request.getId(), result);
                    })
                    .onErrorReturn(new McpResponse(request.getId(), 
                            new McpResponse.McpError(-32603, "Failed to generate code")));
        } catch (Exception e) {
            return Mono.just(new McpResponse(request.getId(), 
                    new McpResponse.McpError(-32602, "Invalid parameters: " + e.getMessage())));
        }
    }
    
    /**
     * 处理从 JIRA 生成代码的请求
     */
    private Mono<McpResponse> handleGenerateCodeFromJira(McpRequest request) {
        String jiraKey = (String) request.getParams().get("jiraKey");
        String codeType = (String) request.getParams().getOrDefault("codeType", "all");
        String packageName = (String) request.getParams().getOrDefault("packageName", "com.generated");
        String framework = (String) request.getParams().getOrDefault("framework", "spring-boot");
        
        if (jiraKey == null) {
            return Mono.just(new McpResponse(request.getId(), 
                    new McpResponse.McpError(-32602, "Missing parameter: jiraKey")));
        }
        
        CodeGenerationRequest codeRequest = new CodeGenerationRequest();
        codeRequest.setJiraKey(jiraKey);
        codeRequest.setCodeType(codeType);
        codeRequest.setPackageName(packageName);
        codeRequest.setFramework(framework);
        codeRequest.setProjectName("generated-project");
        
        return codeGeneratorService.generateCodeFromJira(codeRequest)
                .map(generatedCodes -> {
                    Map<String, Object> result = new HashMap<>();
                    result.put("jiraKey", jiraKey);
                    result.put("generatedFiles", generatedCodes.size());
                    result.put("files", generatedCodes.stream().map(this::createCodeResponse).toList());
                    result.put("message", "代码生成成功");
                    return new McpResponse(request.getId(), result);
                })
                .onErrorReturn(new McpResponse(request.getId(), 
                        new McpResponse.McpError(-32603, "Failed to generate code from JIRA")));
    }
    
    /**
     * 获取项目中的所有问题
     */
    public Flux<JiraIssue> getProjectIssues(String projectKey, int maxResults) {
        return jiraClientService.getIssuesByProject(projectKey, maxResults);
    }
    
    /**
     * 批量生成代码
     */
    public Mono<List<GeneratedCode>> batchGenerateCode(List<CodeGenerationRequest> requests) {
        return Flux.fromIterable(requests)
                .flatMap(request -> codeGeneratorService.generateCodeFromJira(request))
                .collectList()
                .map(lists -> lists.stream()
                        .flatMap(List::stream)
                        .toList());
    }
    
    /**
     * 创建问题响应对象
     */
    private Map<String, Object> createIssueResponse(JiraIssue issue) {
        Map<String, Object> response = new HashMap<>();
        response.put("key", issue.getKey());
        response.put("summary", issue.getSummary());
        response.put("description", issue.getDescription());
        response.put("status", issue.getStatus());
        response.put("priority", issue.getPriority());
        response.put("issueType", issue.getIssueType());
        response.put("assignee", issue.getAssignee());
        response.put("reporter", issue.getReporter());
        response.put("labels", issue.getLabels());
        return response;
    }
    
    /**
     * 创建代码响应对象
     */
    private Map<String, Object> createCodeResponse(GeneratedCode code) {
        Map<String, Object> response = new HashMap<>();
        response.put("fileName", code.getFileName());
        response.put("packageName", code.getPackageName());
        response.put("className", code.getClassName());
        response.put("codeType", code.getCodeType());
        response.put("framework", code.getFramework());
        response.put("generatedAt", code.getGeneratedAt().toString());
        response.put("jiraKey", code.getJiraKey());
        response.put("content", code.getCodeContent());
        return response;
    }
    
    /**
     * 将参数映射为代码生成请求
     */
    private CodeGenerationRequest mapToCodeGenerationRequest(Map<String, Object> params) {
        CodeGenerationRequest request = new CodeGenerationRequest();
        request.setJiraKey((String) params.get("jiraKey"));
        request.setProjectName((String) params.getOrDefault("projectName", "generated-project"));
        request.setPackageName((String) params.getOrDefault("packageName", "com.generated"));
        request.setCodeType((String) params.getOrDefault("codeType", "all"));
        request.setFramework((String) params.getOrDefault("framework", "spring-boot"));
        
        @SuppressWarnings("unchecked")
        Map<String, Object> additionalOptions = (Map<String, Object>) params.get("additionalOptions");
        request.setAdditionalOptions(additionalOptions);
        
        return request;
    }
} 