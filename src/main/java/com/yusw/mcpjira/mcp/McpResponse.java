package com.yusw.mcpjira.mcp;


/**
 * MCP 响应类
 * 
 * @author yusw
 */
public class McpResponse extends McpMessage {
    
    private Object id;
    private Object result;
    private McpError error;
    
    public McpResponse() {
        super("response");
    }
    
    public McpResponse(Object id, Object result) {
        super("response");
        this.id = id;
        this.result = result;
    }
    
    public McpResponse(Object id, McpError error) {
        super("response");
        this.id = id;
        this.error = error;
    }
    
    public Object getId() {
        return id;
    }
    
    public void setId(Object id) {
        this.id = id;
    }
    
    public Object getResult() {
        return result;
    }
    
    public void setResult(Object result) {
        this.result = result;
    }
    
    public McpError getError() {
        return error;
    }
    
    public void setError(McpError error) {
        this.error = error;
    }
    
    public static class McpError {
        private int code;
        private String message;
        private Object data;
        
        public McpError() {}
        
        public McpError(int code, String message) {
            this.code = code;
            this.message = message;
        }
        
        public int getCode() {
            return code;
        }
        
        public void setCode(int code) {
            this.code = code;
        }
        
        public String getMessage() {
            return message;
        }
        
        public void setMessage(String message) {
            this.message = message;
        }
        
        public Object getData() {
            return data;
        }
        
        public void setData(Object data) {
            this.data = data;
        }
    }
} 