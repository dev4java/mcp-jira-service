package com.yusw.simple;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;



/**
 * 简单的 JIRA MCP 客户端
 * 不需要 Node.js，直接通过 HTTP 调用服务
 */
@Component
public class SimpleJiraClient {
    
    private final RestTemplate restTemplate;
    private final String baseUrl;
    
    public SimpleJiraClient() {
        this.restTemplate = new RestTemplate();
        this.baseUrl = "http://localhost:8010/mcp-jira/api";
    }
    
    /**
     * 获取 JIRA 问题
     */
    public Map<String, Object> getJiraIssue(String issueKey) {
        String url = baseUrl + "/jira/issue/" + issueKey;
        return restTemplate.getForObject(url, Map.class);
    }
    
    /**
     * 搜索 JIRA 问题
     */
    public Map<String, Object> searchJiraIssues(String jql) {
        String url = baseUrl + "/jira/search?jql=" + jql;
        return restTemplate.getForObject(url, Map.class);
    }
    
    /**
     * 生成代码
     */
    public Map<String, Object> generateCode(String issueKey, String codeType) {
        String url = baseUrl + "/code/generate-from-jira";
        
        Map<String, Object> request = new HashMap<>();
        request.put("issueKey", issueKey);
        request.put("codeType", codeType);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);
        
        return restTemplate.postForObject(url, entity, Map.class);
    }
    
    /**
     * 使用示例
     */
    public static void main(String[] args) {
        SimpleJiraClient client = new SimpleJiraClient();
        
        try {
            // 获取 JIRA 问题
            Map<String, Object> issue = client.getJiraIssue("IOP-6269");
            System.out.println("JIRA 问题: " + issue);
            
            // 生成 Entity 代码
            Map<String, Object> code = client.generateCode("IOP-6269", "entity");
            System.out.println("生成的代码: " + code.get("code"));
            
        } catch (Exception e) {
            System.err.println("调用失败: " + e.getMessage());
        }
    }
} 