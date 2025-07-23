package com.yusw.mcptools.service;

import com.yusw.mcptools.config.GitlabProperties;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;

/**
 * GitLab 客户端服务
 * GitLab client service
 */
@Service
public class GitlabClientService {
    private static final Logger log = LoggerFactory.getLogger(GitlabClientService.class);
    private final WebClient webClient;
    private final GitlabProperties gitlabProperties;

    public GitlabClientService(GitlabProperties gitlabProperties) {
        this.gitlabProperties = gitlabProperties;
        this.webClient = WebClient.builder()
                .baseUrl(gitlabProperties.getBaseUrl())
                .defaultHeader("PRIVATE-TOKEN", gitlabProperties.getToken())
                .build();
    }

    /**
     * 获取所有开放的 Merge Request
     * Get all open Merge Requests
     */
    public Flux<Map<String, Object>> getOpenMergeRequests() {
        return webClient.get()
                .uri("/projects/{projectId}/merge_requests?state=opened", gitlabProperties.getProjectId())
                .retrieve()
                .bodyToFlux(new ParameterizedTypeReference<Map<String, Object>>() {})
                .onErrorResume(WebClientResponseException.class, e -> {
                    log.error("[GitLab] 获取MR失败: {}", e.getMessage());
                    return Flux.empty();
                });
    }

    /**
     * 获取指定MR的所有建议（discussions/notes）
     * Get all suggestions (discussions/notes) for a specific MR
     * @param mrIid MR的内部ID
     * @return discussions 列表
     */
    public Flux<Map<String, Object>> getMergeRequestDiscussions(String mrIid) {
        return webClient.get()
                .uri("/projects/{projectId}/merge_requests/{mrIid}/discussions", gitlabProperties.getProjectId(), mrIid)
                .retrieve()
                .bodyToFlux(new ParameterizedTypeReference<Map<String, Object>>() {})
                .onErrorResume(WebClientResponseException.class, e -> {
                    log.error("[GitLab] 获取MR建议失败: {}", e.getMessage());
                    return Flux.empty();
                });
    }
} 