package com.yusw.mcpjira.controller;

import com.yusw.mcpjira.model.JiraIssue;
import com.yusw.mcpjira.service.JiraClientService;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


/**
 * JIRA 控制器
 * 
 * @author yusw
 */
@RestController
@RequestMapping("/api/jira")
@CrossOrigin(origins = "*")
public class JiraController {
    
    private static final Logger logger = LoggerFactory.getLogger(JiraController.class);
    
    private final JiraClientService jiraClientService;
    
    @Autowired
    public JiraController(JiraClientService jiraClientService) {
        this.jiraClientService = jiraClientService;
    }
    
    /**
     * 获取单个 JIRA 问题
     */
    @GetMapping(value = "/issue/{issueKey}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<JiraIssue> getIssue(@PathVariable String issueKey) {
        logger.info("Getting JIRA issue: {}", issueKey);
        return jiraClientService.getIssueByKey(issueKey);
    }
    
    /**
     * 搜索 JIRA 问题
     */
    @GetMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<JiraIssue> searchIssues(
            @RequestParam String jql,
            @RequestParam(defaultValue = "50") int maxResults) {
        logger.info("Searching JIRA issues, JQL: {}", jql);
        return jiraClientService.searchIssues(jql, maxResults);
    }
    
    /**
     * 获取项目中的问题
     */
    @GetMapping(value = "/project/{projectKey}/issues", produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<JiraIssue> getProjectIssues(
            @PathVariable String projectKey,
            @RequestParam(defaultValue = "50") int maxResults) {
        logger.info("Getting issues for project: {}", projectKey);
        return jiraClientService.getIssuesByProject(projectKey, maxResults);
    }
    
    /**
     * 获取指定状态的问题
     */
    @GetMapping(value = "/status/{status}/issues", produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<JiraIssue> getIssuesByStatus(
            @PathVariable String status,
            @RequestParam(defaultValue = "50") int maxResults) {
        logger.info("Getting issues with status: {}", status);
        return jiraClientService.getIssuesByStatus(status, maxResults);
    }
    
    /**
     * 获取指定用户的问题
     */
    @GetMapping(value = "/assignee/{assignee}/issues", produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<JiraIssue> getIssuesByAssignee(
            @PathVariable String assignee,
            @RequestParam(defaultValue = "50") int maxResults) {
        logger.info("Getting issues assigned to: {}", assignee);
        return jiraClientService.getIssuesByAssignee(assignee, maxResults);
    }
    
    /**
     * 测试 JIRA 连接
     */
    @GetMapping("/test-connection")
    public Mono<Map<String, Object>> testConnection() {
        logger.info("Testing JIRA connection");
        return jiraClientService.testConnection()
                .map(connected -> {
                    Map<String, Object> result = new HashMap<>();
                    result.put("connected", connected);
                    result.put("message", connected ? "JIRA 连接成功" : "JIRA 连接失败");
                    result.put("timestamp", System.currentTimeMillis());
                    return result;
                });
    }
} 