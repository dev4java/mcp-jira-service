package com.yusw.mcpjira;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

/**
 * Spring Boot 应用测试类(Spring Boot Application Test Class)
 * 
 * @author yusw
 */
@SpringBootTest
@TestPropertySource(properties = {
    "jira.base-url=https://test-jira-server.com",
    "jira.username=test-user",
    "jira.password=test-password"
})
class McpJiraServiceApplicationTests {

    @Test
    void contextLoads() {
        // 测试 Spring 应用上下文是否能正常加载
    }
} 