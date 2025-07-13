package com.yusw.mcpjira.mcp;

import java.util.Map;

/**
 * MCP 通知类
 * 
 * @author yusw
 */
public class McpNotification extends McpMessage {
    
    private String method;
    private Map<String, Object> params;
    
    public McpNotification() {
        super("notification");
    }
    
    public McpNotification(String method, Map<String, Object> params) {
        super("notification");
        this.method = method;
        this.params = params;
    }
    
    public String getMethod() {
        return method;
    }
    
    public void setMethod(String method) {
        this.method = method;
    }
    
    public Map<String, Object> getParams() {
        return params;
    }
    
    public void setParams(Map<String, Object> params) {
        this.params = params;
    }
} 