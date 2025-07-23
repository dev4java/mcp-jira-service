package com.yusw.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Duration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;


/**
 * JIRA MCP 客户端配置
 */
@Configuration
@EnableConfigurationProperties(JiraMcpConfig.JiraMcpProperties.class)
public class JiraMcpConfig {

    /**
     * 配置 RestTemplate Bean，用于 JIRA MCP 服务调用
     */
    @Bean
    public RestTemplate jiraMcpRestTemplate(JiraMcpProperties properties) {
        // 配置 HTTP 客户端
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(Timeout.ofMilliseconds(properties.getConnectionTimeout()))
                .setResponseTimeout(Timeout.ofMilliseconds(properties.getReadTimeout()))
                .build();

        HttpComponentsClientHttpRequestFactory factory = 
            new HttpComponentsClientHttpRequestFactory(
                HttpClients.custom()
                    .setDefaultRequestConfig(requestConfig)
                    .build()
            );

        return new RestTemplateBuilder()
                .requestFactory(() -> factory)
                .setConnectTimeout(Duration.ofMillis(properties.getConnectionTimeout()))
                .setReadTimeout(Duration.ofMillis(properties.getReadTimeout()))
                .build();
    }

    /**
     * 配置 ObjectMapper Bean
     */
    @Bean
    public ObjectMapper jiraMcpObjectMapper() {
        return new ObjectMapper();
    }

    /**
     * JIRA MCP 配置属性
     */
    @ConfigurationProperties(prefix = "jira.mcp")
    public static class JiraMcpProperties {
        
        /**
         * JIRA MCP 服务 URL
         */
        private String serviceUrl = "http://localhost:8010/mcptools/api";
        
        /**
         * 连接超时时间（毫秒）
         */
        private int connectionTimeout = 5000;
        
        /**
         * 读取超时时间（毫秒）
         */
        private int readTimeout = 30000;
        
        /**
         * 重试次数
         */
        private int retryAttempts = 3;
        
        /**
         * 健康检查配置
         */
        private HealthCheck healthCheck = new HealthCheck();
        
        /**
         * 缓存配置
         */
        private Cache cache = new Cache();

        // Getters and Setters
        public String getServiceUrl() {
            return serviceUrl;
        }

        public void setServiceUrl(String serviceUrl) {
            this.serviceUrl = serviceUrl;
        }

        public int getConnectionTimeout() {
            return connectionTimeout;
        }

        public void setConnectionTimeout(int connectionTimeout) {
            this.connectionTimeout = connectionTimeout;
        }

        public int getReadTimeout() {
            return readTimeout;
        }

        public void setReadTimeout(int readTimeout) {
            this.readTimeout = readTimeout;
        }

        public int getRetryAttempts() {
            return retryAttempts;
        }

        public void setRetryAttempts(int retryAttempts) {
            this.retryAttempts = retryAttempts;
        }

        public HealthCheck getHealthCheck() {
            return healthCheck;
        }

        public void setHealthCheck(HealthCheck healthCheck) {
            this.healthCheck = healthCheck;
        }

        public Cache getCache() {
            return cache;
        }

        public void setCache(Cache cache) {
            this.cache = cache;
        }

        /**
         * 健康检查配置
         */
        public static class HealthCheck {
            private boolean enabled = true;
            private long interval = 60000; // 60秒

            public boolean isEnabled() {
                return enabled;
            }

            public void setEnabled(boolean enabled) {
                this.enabled = enabled;
            }

            public long getInterval() {
                return interval;
            }

            public void setInterval(long interval) {
                this.interval = interval;
            }
        }

        /**
         * 缓存配置
         */
        public static class Cache {
            private boolean enabled = true;
            private long ttl = 300000; // 5分钟

            public boolean isEnabled() {
                return enabled;
            }

            public void setEnabled(boolean enabled) {
                this.enabled = enabled;
            }

            public long getTtl() {
                return ttl;
            }

            public void setTtl(long ttl) {
                this.ttl = ttl;
            }
        }
    }
} 