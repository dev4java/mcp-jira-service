package com.yusw.mcpjira.model;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 生成的代码模型
 * 
 * @author yusw
 */
public class GeneratedCode {
    
    private String fileName;
    private String packageName;
    private String className;
    private String codeContent;
    private String codeType;
    private String framework;
    private LocalDateTime generatedAt;
    private String jiraKey;
    private Map<String, Object> metadata;
    
    public GeneratedCode() {
        this.generatedAt = LocalDateTime.now();
    }
    
    public GeneratedCode(String fileName, String packageName, String className, String codeContent) {
        this();
        this.fileName = fileName;
        this.packageName = packageName;
        this.className = className;
        this.codeContent = codeContent;
    }
    
    public String getFileName() {
        return fileName;
    }
    
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    
    public String getPackageName() {
        return packageName;
    }
    
    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
    
    public String getClassName() {
        return className;
    }
    
    public void setClassName(String className) {
        this.className = className;
    }
    
    public String getCodeContent() {
        return codeContent;
    }
    
    public void setCodeContent(String codeContent) {
        this.codeContent = codeContent;
    }
    
    public String getCodeType() {
        return codeType;
    }
    
    public void setCodeType(String codeType) {
        this.codeType = codeType;
    }
    
    public String getFramework() {
        return framework;
    }
    
    public void setFramework(String framework) {
        this.framework = framework;
    }
    
    public LocalDateTime getGeneratedAt() {
        return generatedAt;
    }
    
    public void setGeneratedAt(LocalDateTime generatedAt) {
        this.generatedAt = generatedAt;
    }
    
    public String getJiraKey() {
        return jiraKey;
    }
    
    public void setJiraKey(String jiraKey) {
        this.jiraKey = jiraKey;
    }
    
    public Map<String, Object> getMetadata() {
        return metadata;
    }
    
    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }
} 