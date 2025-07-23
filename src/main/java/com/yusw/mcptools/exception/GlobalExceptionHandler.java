package com.yusw.mcptools.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;


/**
 * 全局异常处理器
 * 
 * @author yusw
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    
    /**
     * 处理 WebClient 响应异常
     */
    @ExceptionHandler(WebClientResponseException.class)
    public Mono<ResponseEntity<Map<String, Object>>> handleWebClientResponseException(WebClientResponseException ex) {
        logger.error("WebClient response exception: {}", ex.getMessage(), ex);
        
        Map<String, Object> errorResponse = createErrorResponse(
            "JIRA_API_ERROR",
            "JIRA API call failed: " + ex.getMessage(),
            ex.getStatusCode().value()
        );
        
        return Mono.just(ResponseEntity.status(ex.getStatusCode()).body(errorResponse));
    }
    
    /**
     * 处理 JIRA 连接异常
     */
    @ExceptionHandler(JiraConnectionException.class)
    public Mono<ResponseEntity<Map<String, Object>>> handleJiraConnectionException(JiraConnectionException ex) {
        logger.error("JIRA connection exception: {}", ex.getMessage(), ex);
        
        Map<String, Object> errorResponse = createErrorResponse(
            "JIRA_CONNECTION_ERROR",
            "JIRA connection failed: " + ex.getMessage(),
            HttpStatus.SERVICE_UNAVAILABLE.value()
        );
        
        return Mono.just(ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(errorResponse));
    }
    
    /**
     * 处理代码生成异常
     */
    @ExceptionHandler(CodeGenerationException.class)
    public Mono<ResponseEntity<Map<String, Object>>> handleCodeGenerationException(CodeGenerationException ex) {
        logger.error("Code generation exception: {}", ex.getMessage(), ex);
        
        Map<String, Object> errorResponse = createErrorResponse(
            "CODE_GENERATION_ERROR",
            "Code generation failed: " + ex.getMessage(),
            HttpStatus.INTERNAL_SERVER_ERROR.value()
        );
        
        return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse));
    }
    
    /**
     * 处理参数验证异常
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public Mono<ResponseEntity<Map<String, Object>>> handleIllegalArgumentException(IllegalArgumentException ex) {
        logger.error("Parameter validation exception: {}", ex.getMessage(), ex);
        
        Map<String, Object> errorResponse = createErrorResponse(
            "VALIDATION_ERROR",
            "Parameter validation failed: " + ex.getMessage(),
            HttpStatus.BAD_REQUEST.value()
        );
        
        return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse));
    }
    
    /**
     * 处理运行时异常
     */
    @ExceptionHandler(RuntimeException.class)
    public Mono<ResponseEntity<Map<String, Object>>> handleRuntimeException(RuntimeException ex) {
        logger.error("Runtime exception: {}", ex.getMessage(), ex);
        
        Map<String, Object> errorResponse = createErrorResponse(
            "RUNTIME_ERROR",
            "Runtime error: " + ex.getMessage(),
            HttpStatus.INTERNAL_SERVER_ERROR.value()
        );
        
        return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse));
    }
    
    /**
     * 处理通用异常
     */
    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<Map<String, Object>>> handleGenericException(Exception ex) {
        logger.error("Unknown exception: {}", ex.getMessage(), ex);
        
        Map<String, Object> errorResponse = createErrorResponse(
            "UNKNOWN_ERROR",
            "Internal server error",
            HttpStatus.INTERNAL_SERVER_ERROR.value()
        );
        
        return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse));
    }
    
    /**
     * 创建错误响应
     */
    private Map<String, Object> createErrorResponse(String errorCode, String message, int statusCode) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("success", false);
        errorResponse.put("errorCode", errorCode);
        errorResponse.put("message", message);
        errorResponse.put("statusCode", statusCode);
        errorResponse.put("timestamp", LocalDateTime.now().toString());
        return errorResponse;
    }
} 