package com.yusw.mcptools.service;

import com.yusw.mcptools.config.McpProperties;
import com.yusw.mcptools.model.CodeGenerationRequest;
import com.yusw.mcptools.model.GeneratedCode;
import com.yusw.mcptools.model.JiraIssue;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;


import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/**
 * 代码生成服务测试类
 * 
 * @author yusw
 */
@ExtendWith(MockitoExtension.class)
class CodeGeneratorServiceTest {

    @Mock
    private McpProperties mcpProperties;

    @Mock
    private JiraClientService jiraClientService;

    private CodeGeneratorService codeGeneratorService;

    @BeforeEach
    void setUp() {
        McpProperties.CodeGenerationConfig codeGenConfig = new McpProperties.CodeGenerationConfig();
        codeGenConfig.setOutputDirectory("./test-output");
        codeGenConfig.setDefaultPackage("com.test");
        
        when(mcpProperties.getCodeGeneration()).thenReturn(codeGenConfig);
        
        codeGeneratorService = new CodeGeneratorService(mcpProperties, jiraClientService);
    }

    @Test
    void generateCodeFromJira_ShouldGenerateEntityCode() {
        // 准备测试数据
        JiraIssue testIssue = new JiraIssue();
        testIssue.setKey("TEST-123");
        testIssue.setSummary("创建用户管理功能");
        testIssue.setDescription("用户管理功能包含基本的CRUD操作");
        
        CodeGenerationRequest request = new CodeGenerationRequest();
        request.setJiraKey("TEST-123");
        request.setCodeType("entity");
        request.setPackageName("com.test");
        request.setFramework("spring-boot");
        
        when(jiraClientService.getIssueByKey(anyString())).thenReturn(Mono.just(testIssue));
        
        // 执行测试
        Mono<List<GeneratedCode>> result = codeGeneratorService.generateCodeFromJira(request);
        
        // 验证结果
        StepVerifier.create(result)
                .expectNextMatches(codes -> {
                    return codes.size() == 1 && 
                           codes.get(0).getCodeType().equals("entity") &&
                           codes.get(0).getClassName().contains("Entity");
                })
                .verifyComplete();
    }

    @Test
    void generateCodeFromJira_ShouldGenerateAllCode() {
        // 准备测试数据
        JiraIssue testIssue = new JiraIssue();
        testIssue.setKey("TEST-456");
        testIssue.setSummary("订单处理系统");
        testIssue.setDescription("订单处理系统需要完整的业务逻辑");
        
        CodeGenerationRequest request = new CodeGenerationRequest();
        request.setJiraKey("TEST-456");
        request.setCodeType("all");
        request.setPackageName("com.test");
        request.setFramework("spring-boot");
        
        when(jiraClientService.getIssueByKey(anyString())).thenReturn(Mono.just(testIssue));
        
        // 执行测试
        Mono<List<GeneratedCode>> result = codeGeneratorService.generateCodeFromJira(request);
        
        // 验证结果
        StepVerifier.create(result)
                .expectNextMatches(codes -> {
                    return codes.size() == 3 
                    && codes.stream().anyMatch(c -> c.getCodeType().equals("entity")) 
                    && codes.stream().anyMatch(c -> c.getCodeType().equals("service")) 
                    && codes.stream().anyMatch(c -> c.getCodeType().equals("controller"));
                })
                .verifyComplete();
    }
} 