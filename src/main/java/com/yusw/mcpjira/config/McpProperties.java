package com.yusw.mcpjira.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * MCP 配置属性类
 * 
 * @author yusw
 */
@Component
@ConfigurationProperties(prefix = "mcp")
public class McpProperties {
    
    private ServerConfig server = new ServerConfig();
    private CodeGenerationConfig codeGeneration = new CodeGenerationConfig();
    
    public ServerConfig getServer() {
        return server;
    }
    
    public void setServer(ServerConfig server) {
        this.server = server;
    }
    
    public CodeGenerationConfig getCodeGeneration() {
        return codeGeneration;
    }
    
    public void setCodeGeneration(CodeGenerationConfig codeGeneration) {
        this.codeGeneration = codeGeneration;
    }
    
    public static class ServerConfig {
        private String host = "localhost";
        private int port = 8081;
        private String protocol = "websocket";
        
        public String getHost() {
            return host;
        }
        
        public void setHost(String host) {
            this.host = host;
        }
        
        public int getPort() {
            return port;
        }
        
        public void setPort(int port) {
            this.port = port;
        }
        
        public String getProtocol() {
            return protocol;
        }
        
        public void setProtocol(String protocol) {
            this.protocol = protocol;
        }
    }
    
    public static class CodeGenerationConfig {
        private String outputDirectory = "./generated-code";
        private String templateDirectory = "./templates";
        private String defaultPackage = "com.generated";
        
        public String getOutputDirectory() {
            return outputDirectory;
        }
        
        public void setOutputDirectory(String outputDirectory) {
            this.outputDirectory = outputDirectory;
        }
        
        public String getTemplateDirectory() {
            return templateDirectory;
        }
        
        public void setTemplateDirectory(String templateDirectory) {
            this.templateDirectory = templateDirectory;
        }
        
        public String getDefaultPackage() {
            return defaultPackage;
        }
        
        public void setDefaultPackage(String defaultPackage) {
            this.defaultPackage = defaultPackage;
        }
    }
} 