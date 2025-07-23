package com.yusw.mcptools.mcp;

import java.util.Map;

/**
 * MCP 请求类
 * 
 * @author yusw
 */
public class McpRequest extends McpMessage {
    
    private Object id;
    private String method;
    private Map<String, Object> params;
    
    public McpRequest() {
        super("request");
    }
    
    public McpRequest(Object id, String method, Map<String, Object> params) {
        super("request");
        this.id = id;
        this.method = method;
        this.params = params;
    }
    
    public Object getId() {
        return id;
    }
    
    public void setId(Object id) {
        this.id = id;
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