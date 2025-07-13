package com.yusw.mcpjira.service;

import com.yusw.mcpjira.config.JiraProperties;
import com.yusw.mcpjira.model.JiraIssue;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

















/**
 * JIRA 客户端服务(JIRA Client Service)
 * 
 * @author yusw
 */
@Service
public class JiraClientService {
    
    private static final Logger logger = LoggerFactory.getLogger(JiraClientService.class);
    
    private final JiraProperties jiraProperties;
    private final WebClient webClient;
    
    @Autowired
    public JiraClientService(JiraProperties jiraProperties) {
        this.jiraProperties = jiraProperties;
        this.webClient = createWebClient();
    }
    
    /**
     * 创建 WebClient 实例(Create WebClient instance)
     */
    private WebClient createWebClient() {
        String authCredential;
        
        // 优先使用 API Token，如果没有则使用密码
        if (jiraProperties.getApiToken() != null && !jiraProperties.getApiToken().trim().isEmpty()) {
            // API Token 认证（推荐方式）
            authCredential = jiraProperties.getUsername() + ":" + jiraProperties.getApiToken();
            logger.info("Using API Token for JIRA authentication");
        } else {
            // 密码认证（传统方式）
            authCredential = jiraProperties.getUsername() + ":" + jiraProperties.getPassword();
            logger.info("Using password for JIRA authentication");
        }
        
        String encodedAuth = Base64.getEncoder().encodeToString(authCredential.getBytes(StandardCharsets.UTF_8));
        
        return WebClient.builder()
                .baseUrl(jiraProperties.getBaseUrl())
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Basic " + encodedAuth)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
    
    /**
     * 根据问题键获取 JIRA 问题(Get JIRA issue by key)
     */
    public Mono<JiraIssue> getIssueByKey(String issueKey) {
        logger.info("Getting JIRA issue: {}", issueKey);
        
        return webClient.get()
                .uri("/rest/api/2/issue/{issueKey}", issueKey)
                .retrieve()
                .bodyToMono(Map.class)
                .timeout(Duration.ofMillis(jiraProperties.getTimeout()))
                .map(this::mapToJiraIssue)
                .doOnSuccess(issue -> logger.info("Successfully retrieved JIRA issue: {}", issue.getKey()))
                .doOnError(error -> logger.error("Failed to get JIRA issue: {}", error.getMessage()));
    }
    
    /**
     * 搜索 JIRA 问题(Search JIRA issues)
     */
    public Flux<JiraIssue> searchIssues(String jql, int maxResults) {
        logger.info("Searching JIRA issues, JQL: {}, max results: {}", jql, maxResults);
        
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/rest/api/2/search")
                        .queryParam("jql", jql)
                        .queryParam("maxResults", maxResults)
                        .build())
                .retrieve()
                .bodyToMono(Map.class)
                .timeout(Duration.ofMillis(jiraProperties.getTimeout()))
                .flatMapMany(response -> {
                    List<Map<String, Object>> issues = (List<Map<String, Object>>) response.get("issues");
                    return Flux.fromIterable(issues)
                            .map(this::mapToJiraIssue);
                })
                .doOnComplete(() -> logger.info("JIRA issue search completed"))
                .doOnError(error -> logger.error("Failed to search JIRA issues: {}", error.getMessage()));
    }
    
    /**
     * 获取项目中的所有问题(Get all issues in project)
     */
    public Flux<JiraIssue> getIssuesByProject(String projectKey, int maxResults) {
        String jql = "project = " + projectKey + " ORDER BY created DESC";
        return searchIssues(jql, maxResults);
    }
    
    /**
     * 获取指定状态的问题(Get issues by status)
     */
    public Flux<JiraIssue> getIssuesByStatus(String status, int maxResults) {
        String jql = "status = \"" + status + "\" ORDER BY updated DESC";
        return searchIssues(jql, maxResults);
    }
    
    /**
     * 获取指定用户分配的问题(Get issues assigned to user)
     */
    public Flux<JiraIssue> getIssuesByAssignee(String assignee, int maxResults) {
        String jql = "assignee = \"" + assignee + "\" ORDER BY updated DESC";
        return searchIssues(jql, maxResults);
    }
    
    /**
     * 将 API 响应映射为 JiraIssue 对象(Map API response to JiraIssue object)
     */
    private JiraIssue mapToJiraIssue(Map<String, Object> issueData) {
        JiraIssue issue = new JiraIssue();
        
        issue.setKey((String) issueData.get("key"));
        
        Map<String, Object> fields = (Map<String, Object>) issueData.get("fields");
        if (fields != null) {
            issue.setSummary((String) fields.get("summary"));
            issue.setDescription((String) fields.get("description"));
            
            // 状态(Status)
            Map<String, Object> status = (Map<String, Object>) fields.get("status");
            if (status != null) {
                issue.setStatus((String) status.get("name"));
            }
            
            // 优先级(Priority)
            Map<String, Object> priority = (Map<String, Object>) fields.get("priority");
            if (priority != null) {
                issue.setPriority((String) priority.get("name"));
            }
            
            // 问题类型(Issue Type)
            Map<String, Object> issueType = (Map<String, Object>) fields.get("issuetype");
            if (issueType != null) {
                issue.setIssueType((String) issueType.get("name"));
            }
            
            // 指派人(Assignee)
            Map<String, Object> assignee = (Map<String, Object>) fields.get("assignee");
            if (assignee != null) {
                issue.setAssignee((String) assignee.get("displayName"));
            }
            
            // 报告人(Reporter)
            Map<String, Object> reporter = (Map<String, Object>) fields.get("reporter");
            if (reporter != null) {
                issue.setReporter((String) reporter.get("displayName"));
            }
            
            // 标签(Labels)
            List<String> labels = (List<String>) fields.get("labels");
            issue.setLabels(labels);
        }
        
        return issue;
    }
    
    /**
     * 测试 JIRA 连接(Test JIRA connection)
     */
    public Mono<Boolean> testConnection() {
        logger.info("Testing JIRA connection to: {}", jiraProperties.getBaseUrl());
        logger.info("Using username: {}", jiraProperties.getUsername());
        
        return webClient.get()
                .uri("/rest/api/2/myself")
                .retrieve()
                .onStatus(
                    status -> status.is4xxClientError(),
                    response -> {
                        logger.error("JIRA API authentication failed (4xx): Check username and password");
                        return Mono.error(new RuntimeException("Authentication failed: Invalid username or password"));
                    }
                )
                .onStatus(
                    status -> status.is5xxServerError(),
                    response -> {
                        logger.error("JIRA server error (5xx): Internal server error");
                        return Mono.error(new RuntimeException("Server error: JIRA internal server error"));
                    }
                )
                .bodyToMono(Map.class)
                .timeout(Duration.ofMillis(jiraProperties.getTimeout()))
                .map(response -> {
                    logger.info("✅ JIRA connection successful!");
                    logger.info("Logged in user: {}", response.get("displayName"));
                    logger.info("User email: {}", response.get("emailAddress"));
                    return true;
                })
                .doOnError(error -> {
                    logger.error("❌ JIRA connection failed: {}", error.getMessage());
                    logger.error("Please check:");
                    logger.error("1. JIRA server URL: {}", jiraProperties.getBaseUrl());
                    logger.error("2. Username: {}", jiraProperties.getUsername());
                    logger.error("3. Password is correct");
                    logger.error("4. Network connection is normal");
                })
                .onErrorReturn(false);
    }
} 