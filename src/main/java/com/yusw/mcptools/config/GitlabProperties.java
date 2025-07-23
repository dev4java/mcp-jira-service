package com.yusw.mcptools.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


/**
 * GitLab 配置属性类
 * GitLab properties configuration class
 */
@Component
@ConfigurationProperties(prefix = "gitlab")
public class GitlabProperties {
    /**
     * GitLab 服务器 API 地址
     * GitLab server API base url
     */
    private String baseUrl;
    /**
     * GitLab 访问 Token
     * GitLab access token
     */
    private String token;
    /**
     * GitLab 项目 ID
     * GitLab project ID list
     */
    private String projectId;

    public String getBaseUrl() {
        return baseUrl;
    }
    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
    public String getProjectId() {
        return projectId;
    }
    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }
} 