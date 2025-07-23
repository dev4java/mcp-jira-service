package com.yusw.mcptools.exception;

/**
 * JIRA 连接异常
 * 
 * @author yusw
 */
public class JiraConnectionException extends RuntimeException {
    
    public JiraConnectionException(String message) {
        super(message);
    }
    
    public JiraConnectionException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public JiraConnectionException(Throwable cause) {
        super(cause);
    }
} 