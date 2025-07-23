package com.yusw.mcptools.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * JIRA 问题实体类
 * 
 * @author yusw
 */
public class JiraIssue {
    
    private String key;
    private String summary;
    private String description;
    private String status;
    private String priority;
    private String issueType;
    private String assignee;
    private String reporter;
    private LocalDateTime created;
    private LocalDateTime updated;
    private List<String> labels;
    private Map<String, Object> customFields;
    
    // Constructors
    public JiraIssue() {}
    
    public JiraIssue(String key, String summary, String description) {
        this.key = key;
        this.summary = summary;
        this.description = description;
    }
    
    // Getters and Setters
    public String getKey() {
        return key;
    }
    
    public void setKey(String key) {
        this.key = key;
    }
    
    public String getSummary() {
        return summary;
    }
    
    public void setSummary(String summary) {
        this.summary = summary;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getPriority() {
        return priority;
    }
    
    public void setPriority(String priority) {
        this.priority = priority;
    }
    
    @JsonProperty("issue_type")
    public String getIssueType() {
        return issueType;
    }
    
    public void setIssueType(String issueType) {
        this.issueType = issueType;
    }
    
    public String getAssignee() {
        return assignee;
    }
    
    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }
    
    public String getReporter() {
        return reporter;
    }
    
    public void setReporter(String reporter) {
        this.reporter = reporter;
    }
    
    public LocalDateTime getCreated() {
        return created;
    }
    
    public void setCreated(LocalDateTime created) {
        this.created = created;
    }
    
    public LocalDateTime getUpdated() {
        return updated;
    }
    
    public void setUpdated(LocalDateTime updated) {
        this.updated = updated;
    }
    
    public List<String> getLabels() {
        return labels;
    }
    
    public void setLabels(List<String> labels) {
        this.labels = labels;
    }
    
    @JsonProperty("custom_fields")
    public Map<String, Object> getCustomFields() {
        return customFields;
    }
    
    public void setCustomFields(Map<String, Object> customFields) {
        this.customFields = customFields;
    }
    
    @Override
    public String toString() {
        return "JiraIssue{" +
                "key='" + key + '\'' +
                ", summary='" + summary + '\'' +
                ", status='" + status + '\'' +
                ", priority='" + priority + '\'' +
                ", issueType='" + issueType + '\'' +
                '}';
    }
} 