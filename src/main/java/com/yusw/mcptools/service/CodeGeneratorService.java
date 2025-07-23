package com.yusw.mcptools.service;

import com.squareup.javapoet.*;
import com.yusw.mcptools.config.McpProperties;
import com.yusw.mcptools.model.CodeGenerationRequest;
import com.yusw.mcptools.model.GeneratedCode;
import com.yusw.mcptools.model.JiraIssue;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.lang.model.element.Modifier;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;


/**
 * 代码生成器服务
 * 
 * @author yusw
 */
@Service
public class CodeGeneratorService {
    
    private static final Logger logger = LoggerFactory.getLogger(CodeGeneratorService.class);
    
    private final McpProperties mcpProperties;
    private final JiraClientService jiraClientService;
    
    @Autowired
    public CodeGeneratorService(McpProperties mcpProperties, JiraClientService jiraClientService) {
        this.mcpProperties = mcpProperties;
        this.jiraClientService = jiraClientService;
    }
    
    /**
     * 根据 JIRA 问题生成代码
     */
    public Mono<List<GeneratedCode>> generateCodeFromJira(CodeGenerationRequest request) {
        logger.info("Starting code generation for JIRA issue: {}", request.getJiraKey());
        
        return jiraClientService.getIssueByKey(request.getJiraKey())
                .flatMap(issue -> {
                    try {
                        List<GeneratedCode> generatedCodes = new ArrayList<>();
                        
                        // 根据代码类型生成对应的代码
                        switch (request.getCodeType().toLowerCase()) {
                            case "entity":
                                generatedCodes.add(generateEntityCode(issue, request));
                                break;
                            case "service":
                                generatedCodes.add(generateServiceCode(issue, request));
                                break;
                            case "controller":
                                generatedCodes.add(generateControllerCode(issue, request));
                                break;
                            case "all":
                                generatedCodes.add(generateEntityCode(issue, request));
                                generatedCodes.add(generateServiceCode(issue, request));
                                generatedCodes.add(generateControllerCode(issue, request));
                                break;
                            default:
                                generatedCodes.add(generateGenericCode(issue, request));
                        }
                        
                        // 保存生成的代码到文件
                        return saveGeneratedCodes(generatedCodes)
                                .thenReturn(generatedCodes);
                        
                    } catch (Exception e) {
                        logger.error("Code generation failed", e);
                        return Mono.error(e);
                    }
                });
    }
    
    /**
     * 生成实体类代码
     */
    private GeneratedCode generateEntityCode(JiraIssue issue, CodeGenerationRequest request) {
        String className = extractClassName(issue.getSummary()) + "Entity";
        String packageName = request.getPackageName() + ".entity";
        
        TypeSpec.Builder classBuilder = TypeSpec.classBuilder(className)
                .addModifiers(Modifier.PUBLIC)
                .addJavadoc("根据 JIRA 问题 $L 生成的实体类: $L\n", issue.getKey(), issue.getSummary())
                .addJavadoc("@author yusw\n");
        
        // 添加基础字段
        classBuilder.addField(FieldSpec.builder(String.class, "id")
                .addModifiers(Modifier.PRIVATE)
                .addJavadoc("主键ID\n")
                .build());
        
        // 根据 JIRA 描述解析字段
        List<FieldInfo> fields = parseFieldsFromDescription(issue.getDescription());
        for (FieldInfo field : fields) {
            classBuilder.addField(FieldSpec.builder(field.type, field.name)
                    .addModifiers(Modifier.PRIVATE)
                    .addJavadoc("$L\n", field.description)
                    .build());
        }
        
        // 添加构造函数
        classBuilder.addMethod(MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .build());
        
        // 添加 getter 和 setter 方法
        classBuilder.addMethod(MethodSpec.methodBuilder("getId")
                .addModifiers(Modifier.PUBLIC)
                .returns(String.class)
                .addStatement("return id")
                .build());
        
        classBuilder.addMethod(MethodSpec.methodBuilder("setId")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(String.class, "id")
                .addStatement("this.id = id")
                .build());
        
        TypeSpec entityClass = classBuilder.build();
        
        JavaFile javaFile = JavaFile.builder(packageName, entityClass)
                .build();
        
        GeneratedCode generatedCode = new GeneratedCode();
        generatedCode.setFileName(className + ".java");
        generatedCode.setPackageName(packageName);
        generatedCode.setClassName(className);
        generatedCode.setCodeContent(javaFile.toString());
        generatedCode.setCodeType("entity");
        generatedCode.setFramework(request.getFramework());
        generatedCode.setJiraKey(issue.getKey());
        
        return generatedCode;
    }
    
    /**
     * 生成服务类代码
     */
    private GeneratedCode generateServiceCode(JiraIssue issue, CodeGenerationRequest request) {
        String className = extractClassName(issue.getSummary()) + "Service";
        String packageName = request.getPackageName() + ".service";
        
        TypeSpec.Builder classBuilder = TypeSpec.classBuilder(className)
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Service.class)
                .addJavadoc("根据 JIRA 问题 $L 生成的服务类: $L\n", issue.getKey(), issue.getSummary())
                .addJavadoc("@author yusw\n");
        
        // 添加日志字段
        classBuilder.addField(FieldSpec.builder(Logger.class, "logger")
                .addModifiers(Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL)
                .initializer("$T.getLogger($L.class)", LoggerFactory.class, className)
                .build());
        
        // 添加基础方法
        classBuilder.addMethod(MethodSpec.methodBuilder("process")
                .addModifiers(Modifier.PUBLIC)
                .returns(String.class)
                .addJavadoc("Process $L business logic\n", issue.getSummary())
                .addStatement("logger.info(\"Processing business logic: $L\")", issue.getSummary())
                .addStatement("return \"Processing completed\"")
                .build());
        
        TypeSpec serviceClass = classBuilder.build();
        
        JavaFile javaFile = JavaFile.builder(packageName, serviceClass)
                .build();
        
        GeneratedCode generatedCode = new GeneratedCode();
        generatedCode.setFileName(className + ".java");
        generatedCode.setPackageName(packageName);
        generatedCode.setClassName(className);
        generatedCode.setCodeContent(javaFile.toString());
        generatedCode.setCodeType("service");
        generatedCode.setFramework(request.getFramework());
        generatedCode.setJiraKey(issue.getKey());
        
        return generatedCode;
    }
    
    /**
     * 生成控制器代码
     */
    private GeneratedCode generateControllerCode(JiraIssue issue, CodeGenerationRequest request) {
        String className = extractClassName(issue.getSummary()) + "Controller";
        String packageName = request.getPackageName() + ".controller";
        
        TypeSpec.Builder classBuilder = TypeSpec.classBuilder(className)
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(ClassName.get("org.springframework.web.bind.annotation", "RestController"))
                .addAnnotation(AnnotationSpec.builder(ClassName.get("org.springframework.web.bind.annotation", "RequestMapping"))
                        .addMember("value", "\"/" + extractClassName(issue.getSummary()).toLowerCase() + "\"")
                        .build())
                .addJavadoc("根据 JIRA 问题 $L 生成的控制器类: $L\n", issue.getKey(), issue.getSummary())
                .addJavadoc("@author yusw\n");
        
        // 添加基础 GET 方法
        classBuilder.addMethod(MethodSpec.methodBuilder("get")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(ClassName.get("org.springframework.web.bind.annotation", "GetMapping"))
                .returns(String.class)
                .addJavadoc("获取 $L 信息\n", issue.getSummary())
                .addStatement("return \"$L API\"", issue.getSummary())
                .build());
        
        TypeSpec controllerClass = classBuilder.build();
        
        JavaFile javaFile = JavaFile.builder(packageName, controllerClass)
                .build();
        
        GeneratedCode generatedCode = new GeneratedCode();
        generatedCode.setFileName(className + ".java");
        generatedCode.setPackageName(packageName);
        generatedCode.setClassName(className);
        generatedCode.setCodeContent(javaFile.toString());
        generatedCode.setCodeType("controller");
        generatedCode.setFramework(request.getFramework());
        generatedCode.setJiraKey(issue.getKey());
        
        return generatedCode;
    }
    
    /**
     * 生成通用代码
     */
    private GeneratedCode generateGenericCode(JiraIssue issue, CodeGenerationRequest request) {
        String className = extractClassName(issue.getSummary());
        String packageName = request.getPackageName();
        
        TypeSpec.Builder classBuilder = TypeSpec.classBuilder(className)
                .addModifiers(Modifier.PUBLIC)
                .addJavadoc("根据 JIRA 问题 $L 生成的类: $L\n", issue.getKey(), issue.getSummary())
                .addJavadoc("@author yusw\n");
        
        // 添加基础方法
        classBuilder.addMethod(MethodSpec.methodBuilder("execute")
                .addModifiers(Modifier.PUBLIC)
                .returns(void.class)
                .addJavadoc("执行 $L 相关功能\n", issue.getSummary())
                .addComment("TODO: 实现具体功能")
                .build());
        
        TypeSpec genericClass = classBuilder.build();
        
        JavaFile javaFile = JavaFile.builder(packageName, genericClass)
                .build();
        
        GeneratedCode generatedCode = new GeneratedCode();
        generatedCode.setFileName(className + ".java");
        generatedCode.setPackageName(packageName);
        generatedCode.setClassName(className);
        generatedCode.setCodeContent(javaFile.toString());
        generatedCode.setCodeType("generic");
        generatedCode.setFramework(request.getFramework());
        generatedCode.setJiraKey(issue.getKey());
        
        return generatedCode;
    }
    
    /**
     * 从标题中提取类名
     */
    private String extractClassName(String title) {
        if (StringUtils.isBlank(title)) {
            return "GeneratedClass";
        }
        
        // 移除特殊字符，只保留字母和数字
        String cleaned = title.replaceAll("[^a-zA-Z0-9\\s]", "");
        
        // 转换为驼峰命名
        String[] words = cleaned.split("\\s+");
        StringBuilder className = new StringBuilder();
        
        for (String word : words) {
            if (StringUtils.isNotBlank(word)) {
                className.append(StringUtils.capitalize(word.toLowerCase()));
            }
        }
        
        return className.length() > 0 ? className.toString() : "GeneratedClass";
    }
    
    /**
     * 从描述中解析字段信息
     */
    private List<FieldInfo> parseFieldsFromDescription(String description) {
        List<FieldInfo> fields = new ArrayList<>();
        
        if (StringUtils.isBlank(description)) {
            return fields;
        }
        
        // 简单的字段解析逻辑，查找类似 "字段名: 类型" 的模式
        Pattern pattern = Pattern.compile("(\\w+):\\s*(\\w+)");
        Matcher matcher = pattern.matcher(description);
        
        while (matcher.find()) {
            String fieldName = matcher.group(1);
            String fieldType = matcher.group(2);
            
            Class<?> javaType = mapStringToJavaType(fieldType);
            fields.add(new FieldInfo(fieldName, javaType, "从 JIRA 描述中解析的字段"));
        }
        
        return fields;
    }
    
    /**
     * 将字符串类型映射为 Java 类型
     */
    private Class<?> mapStringToJavaType(String typeString) {
        return switch (typeString.toLowerCase()) {
            case "string", "text" -> String.class;
            case "int", "integer" -> Integer.class;
            case "long" -> Long.class;
            case "double" -> Double.class;
            case "boolean" -> Boolean.class;
            case "date" -> java.time.LocalDate.class;
            case "datetime" -> java.time.LocalDateTime.class;
            default -> String.class;
        };
    }
    
    /**
     * 保存生成的代码到文件
     */
    private Mono<Void> saveGeneratedCodes(List<GeneratedCode> generatedCodes) {
        return Mono.fromRunnable(() -> {
            try {
                String outputDir = mcpProperties.getCodeGeneration().getOutputDirectory();
                Path outputPath = Paths.get(outputDir);
                
                if (!Files.exists(outputPath)) {
                    Files.createDirectories(outputPath);
                }
                
                for (GeneratedCode code : generatedCodes) {
                    Path filePath = outputPath.resolve(code.getFileName());
                    Files.write(filePath, code.getCodeContent().getBytes());
                    logger.info("Code file saved: {}", filePath);
                }
            } catch (IOException e) {
                logger.error("Failed to save code files", e);
                throw new RuntimeException("Failed to save code files", e);
            }
        });
    }
    
    /**
     * 字段信息类
     */
    private static class FieldInfo {
        final String name;
        final Class<?> type;
        final String description;
        
        FieldInfo(String name, Class<?> type, String description) {
            this.name = name;
            this.type = type;
            this.description = description;
        }
    }
} 