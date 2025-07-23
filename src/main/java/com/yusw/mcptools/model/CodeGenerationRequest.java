package com.yusw.mcptools.model;

import java.util.Map;

/**
 * 代码生成请求模型
 * 
 * @author yusw
 */
public class CodeGenerationRequest {
    
    private String jiraKey;
    private String projectName;
    private String packageName;
    private String codeType; // entity, service, controller, etc.
    private String framework; // spring-boot, react, etc.
    private Map<String, Object> additionalOptions;
    
    public CodeGenerationRequest() {}
    
    public CodeGenerationRequest(String jiraKey, String projectName, String packageName, String codeType) {
        this.jiraKey = jiraKey;
        this.projectName = projectName;
        this.packageName = packageName;
        this.codeType = codeType;
    }
    
    public String getJiraKey() {
        return jiraKey;
    }
    
    public void setJiraKey(String jiraKey) {
        this.jiraKey = jiraKey;
    }
    
    public String getProjectName() {
        return projectName;
    }
    
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
    
    public String getPackageName() {
        return packageName;
    }
    
    public void setPackageName(String packageName) {
        this.packageName = packageName;
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
    
    public Map<String, Object> getAdditionalOptions() {
        return additionalOptions;
    }
    
    public void setAdditionalOptions(Map<String, Object> additionalOptions) {
        this.additionalOptions = additionalOptions;
    }
} 