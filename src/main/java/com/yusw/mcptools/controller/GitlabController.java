package com.yusw.mcptools.controller;

import com.yusw.mcptools.service.GitlabClientService;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

/**
 * GitLab MR建议接口
 * GitLab MR suggestion REST controller
 */
@RestController
@RequestMapping("/api/gitlab")
public class GitlabController {
    private final GitlabClientService gitlabClientService;

    public GitlabController(GitlabClientService gitlabClientService) {
        this.gitlabClientService = gitlabClientService;
    }

    /**
     * 获取所有开放的MR列表
     * Get all open Merge Requests
     */
    @GetMapping("/mrs")
    public Flux<Map<String, Object>> getOpenMergeRequests() {
        return gitlabClientService.getOpenMergeRequests();
    }

    /**
     * 获取指定MR的所有建议（discussions/notes）
     * Get all suggestions (discussions/notes) for a specific MR
     * @param mrIid MR内部ID
     */
    @GetMapping("/mr/{mrIid}/suggestions")
    public Flux<Map<String, Object>> getMergeRequestSuggestions(@PathVariable String mrIid) {
        return gitlabClientService.getMergeRequestDiscussions(mrIid);
    }

    /**
     * 只获取指定MR的PR Code Suggestions内容
     * Get only PR Code Suggestions for a specific MR
     * @param mrIid MR内部ID
     */
    @GetMapping("/mr/{mrIid}/code-suggestions")
    public Flux<Map<String, Object>> getMergeRequestCodeSuggestions(@PathVariable String mrIid) {
        return gitlabClientService.getMergeRequestDiscussions(mrIid)
            .flatMap(discussion -> {
                Object notesObj = discussion.get("notes");
                if (notesObj instanceof Iterable<?> notes) {
                    return Flux.fromIterable(notes)
                        .filter(noteObj -> {
                            if (noteObj instanceof Map<?, ?> noteMap) {
                                Object bodyObj = noteMap.get("body");
                                if (bodyObj instanceof String body) {
                                    // 过滤包含“PR Code Suggestions”关键字的内容
                                    return body.contains("PR Code Suggestions");
                                }
                            }
                            return false;
                        })
                        .map(noteObj -> (Map<String, Object>) noteObj);
                }
                return Flux.empty();
            });
    }

    /**
     * 结构化提取PR Code Suggestions表格内容，返回JSON
     * Extract PR Code Suggestions table as structured JSON
     * @param mrIid MR内部ID
     */
    @GetMapping("/mr/{mrIid}/code-suggestions/structured")
    public Flux<Map<String, Object>> getStructuredCodeSuggestions(@PathVariable String mrIid) {
        return gitlabClientService.getMergeRequestDiscussions(mrIid)
            .flatMap(discussion -> {
                Object notesObj = discussion.get("notes");
                if (notesObj instanceof Iterable<?> notes) {
                    List<Map<String, Object>> resultList = new java.util.ArrayList<>();
                    for (Object noteObj : notes) {
                        if (!(noteObj instanceof Map)) continue;
                        Map<String, Object> noteMap = (Map<String, Object>) noteObj;
                        Object bodyObj = noteMap.get("body");
                        if (!(bodyObj instanceof String)) continue;
                        String body = (String) bodyObj;
                        if (body.contains("<table")) {
                            org.jsoup.nodes.Document doc = org.jsoup.Jsoup.parse(body);
                            org.jsoup.nodes.Element table = doc.selectFirst("table");
                            if (table != null) {
                                org.jsoup.select.Elements rows = table.select("tbody tr");
                                for (org.jsoup.nodes.Element row : rows) {
                                    org.jsoup.select.Elements tds = row.select("td");
                                    if (tds.size() >= 3) {
                                        String category = tds.get(0).text().trim();
                                        String suggestion = tds.get(1).text().trim();
                                        String impact = tds.get(2).text().trim();
                                        // 跳过表头
                                        if ("Category".equalsIgnoreCase(category) && "Suggestion".equalsIgnoreCase(suggestion) && "Impact".equalsIgnoreCase(impact)) {
                                            continue;
                                        }
                                        // 提取Importance和Why
                                        String importance = "";
                                        String why = "";
                                        Map<String, Object> map = new java.util.HashMap<>();
                                        // 提取 Suggestion importance[1-10]: 数值
                                        java.util.regex.Matcher impMatcher = java.util.regex.Pattern.compile("(Suggestion importance\\[1-10\\]:)\\s*(\\d+)").matcher(suggestion);
                                        if (impMatcher.find()) {
                                            importance = impMatcher.group(2);
                                            // 新增原始key-value
                                            map.put(impMatcher.group(1), importance);
                                            suggestion = suggestion.replace(impMatcher.group(0), "");
                                        }
                                        // 提取 Why: ...
                                        int whyIdx = suggestion.indexOf("Why:");
                                        if (whyIdx >= 0) {
                                            why = suggestion.substring(whyIdx + 4).trim();
                                            suggestion = suggestion.substring(0, whyIdx).trim();
                                        }
                                        // 清理多余空白
                                        suggestion = suggestion.replaceAll("\\s+", " ").trim();
                                        map.put("Category", cleanMarkdownSymbols(category));
                                        map.put("Suggestion", cleanMarkdownSymbols(suggestion));
                                        map.put("Impact", cleanMarkdownSymbols(impact));
                                        if (!importance.isEmpty()) map.put("Importance", importance);
                                        if (!why.isEmpty()) map.put("Why", cleanMarkdownSymbols(why));
                                        resultList.add(map);
                                    }
                                }
                            }
                        }
                    }
                    return Flux.fromIterable(resultList);
                }
                return Flux.empty();
            });
    }

    /**
     * 清理常见Markdown符号，避免正则字符类转义问题
     */
    private static String cleanMarkdownSymbols(String text) {
        if (text == null) return "";
        return text.replace("#", "")
                   .replace("*", "")
                   .replace("`", "")
                   .replace("_", "")
                   .replace("[", "")
                   .replace("]", "")
                   .replace("(", "")
                   .replace(")", "")
                   .replace("-", "")
                   .replaceAll("\\s+", " ")
                   .trim();
    }
} 