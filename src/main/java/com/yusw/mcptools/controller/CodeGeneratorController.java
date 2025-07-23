package com.yusw.mcptools.controller;

import com.yusw.mcptools.model.CodeGenerationRequest;
import com.yusw.mcptools.service.CodeGeneratorService;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;


/**
 * 代码生成控制器
 * 
 * @author yusw
 */
@RestController
@RequestMapping("/api/code")
@CrossOrigin(origins = "*")
public class CodeGeneratorController {
    
    private static final Logger logger = LoggerFactory.getLogger(CodeGeneratorController.class);
    
    private final CodeGeneratorService codeGeneratorService;
    
    @Autowired
    public CodeGeneratorController(CodeGeneratorService codeGeneratorService) {
        this.codeGeneratorService = codeGeneratorService;
    }
    
    /**
     * 根据 JIRA 问题生成代码
     */
    @PostMapping(value = "/generate", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Map<String, Object>> generateCode(@RequestBody CodeGenerationRequest request) {
        logger.info("Code generation request: JIRA={}, type={}", request.getJiraKey(), request.getCodeType());
        
        return codeGeneratorService.generateCodeFromJira(request)
                .map(generatedCodes -> {
                    Map<String, Object> result = new HashMap<>();
                    result.put("success", true);
                    result.put("jiraKey", request.getJiraKey());
                    result.put("generatedFiles", generatedCodes.size());
                    result.put("files", generatedCodes);
                    result.put("message", "Code generation successful");
                    return result;
                })
                .onErrorReturn(createErrorResponse("Code generation failed"));
    }
    
    /**
     * 快速生成代码（通过 URL 参数）
     */
    @PostMapping(value = "/generate/{jiraKey}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Map<String, Object>> generateCodeQuick(
            @PathVariable String jiraKey,
            @RequestParam(defaultValue = "all") String codeType,
            @RequestParam(defaultValue = "com.generated") String packageName,
            @RequestParam(defaultValue = "spring-boot") String framework,
            @RequestParam(defaultValue = "generated-project") String projectName) {
        
        logger.info("Quick code generation: JIRA={}, type={}", jiraKey, codeType);
        
        CodeGenerationRequest request = new CodeGenerationRequest();
        request.setJiraKey(jiraKey);
        request.setCodeType(codeType);
        request.setPackageName(packageName);
        request.setFramework(framework);
        request.setProjectName(projectName);
        
        return codeGeneratorService.generateCodeFromJira(request)
                .map(generatedCodes -> {
                    Map<String, Object> result = new HashMap<>();
                    result.put("success", true);
                    result.put("jiraKey", jiraKey);
                    result.put("generatedFiles", generatedCodes.size());
                    result.put("files", generatedCodes);
                    result.put("message", "代码生成成功");
                    return result;
                })
                .onErrorReturn(createErrorResponse("代码生成失败"));
    }
    
    /**
     * 获取代码生成选项
     */
    @GetMapping("/options")
    public Mono<Map<String, Object>> getGenerationOptions() {
        Map<String, Object> options = new HashMap<>();
        options.put("codeTypes", new String[]{"entity", "service", "controller", "all"});
        options.put("frameworks", new String[]{"spring-boot", "spring-web", "plain-java"});
        options.put("defaultPackage", "com.generated");
        options.put("supportedLanguages", new String[]{"java"});
        return Mono.just(options);
    }
    
    /**
     * 创建错误响应
     */
    private Map<String, Object> createErrorResponse(String message) {
        Map<String, Object> error = new HashMap<>();
        error.put("success", false);
        error.put("message", message);
        error.put("timestamp", System.currentTimeMillis());
        return error;
    }
} 