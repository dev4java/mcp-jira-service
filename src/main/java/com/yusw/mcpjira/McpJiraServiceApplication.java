package com.yusw.mcpjira;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * MCP JIRA 服务主应用类(MCP JIRA Service Main Application Class)
 * 
 * @author yusw
 */
@SpringBootApplication
@EnableConfigurationProperties
@EnableAsync
public class McpJiraServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(McpJiraServiceApplication.class, args);
    }
} 
