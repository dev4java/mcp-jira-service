package com.yusw.mcptools.mcp;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * MCP 消息基类
 * 
 * @author yusw
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = McpRequest.class, name = "request"),
    @JsonSubTypes.Type(value = McpResponse.class, name = "response"),
    @JsonSubTypes.Type(value = McpNotification.class, name = "notification")
})
public abstract class McpMessage {
    
    @JsonProperty("jsonrpc")
    private String jsonRpc = "2.0";
    
    private String type;
    
    public McpMessage() {}
    
    public McpMessage(String type) {
        this.type = type;
    }
    
    public String getJsonRpc() {
        return jsonRpc;
    }
    
    public void setJsonRpc(String jsonRpc) {
        this.jsonRpc = jsonRpc;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
} 