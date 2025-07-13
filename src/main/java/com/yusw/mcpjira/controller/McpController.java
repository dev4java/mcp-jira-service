package com.yusw.mcpjira.controller;

import com.yusw.mcpjira.mcp.McpRequest;
import com.yusw.mcpjira.mcp.McpResponse;
import com.yusw.mcpjira.service.McpJiraService;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;





/**
 * MCP 协议控制器(MCP Protocol Controller)(MCP Protocol Controller)
 * 
 * @author yusw
 */
@RestController
@RequestMapping("/api/mcp")
@CrossOrigin(origins = "*")
public class McpController {
    
    private static final Logger logger = LoggerFactory.getLogger(McpController.class);
    
    private final McpJiraService mcpJiraService;
    
    @Autowired
    public McpController(McpJiraService mcpJiraService) {
        this.mcpJiraService = mcpJiraService;
    }
    
    /**
     * 处理 MCP 请求(Handle MCP request)(Handle MCP request)
     */
    @PostMapping(value = "/request", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<McpResponse> handleRequest(@RequestBody McpRequest request) {
        logger.info("Received MCP request: {}", request.getMethod());
        return mcpJiraService.handleRequest(request);
    }
    
    /**
     * 获取 MCP 服务信息(Get MCP service information)(Get MCP service information)
     */
    @GetMapping("/info")
    public Mono<Map<String, Object>> getServiceInfo() {
        Map<String, Object> info = new HashMap<>();
        info.put("service", "MCP JIRA Service");
        info.put("version", "1.0.0");
        info.put("description", "MCP 服务，用于连接 JIRA 并生成功能代码(MCP service for connecting to JIRA and generating functional code)");
        info.put("supportedMethods", new String[]{
            "jira.getIssue",
            "jira.searchIssues", 
            "jira.testConnection",
            "code.generate",
            "code.generateFromJira"
        });
        return Mono.just(info);
    }
    
    /**
     * 健康检查端点(Health check endpoint)(Health check endpoint)
     */
    @GetMapping("/health")
    public Mono<Map<String, Object>> health() {
        Map<String, Object> health = new HashMap<>();
        health.put("status", "UP");
        health.put("service", "mcp-jira-service");
        health.put("timestamp", System.currentTimeMillis());
        return Mono.just(health);
    }
} 