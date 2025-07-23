/**
 * JIRA MCP 客户端 - 前端 JavaScript 版本
 * 用于在前端项目中调用 JIRA MCP 服务
 */
class JiraMcpClient {
    constructor(options = {}) {
        this.baseUrl = options.baseUrl || 'http://localhost:8010/mcptools/api';
        this.timeout = options.timeout || 30000;
        this.retryAttempts = options.retryAttempts || 3;
        this.retryDelay = options.retryDelay || 1000;
    }

    /**
     * 通用请求方法
     */
    async request(url, options = {}) {
        const defaultOptions = {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json'
            },
            timeout: this.timeout
        };

        const mergedOptions = { ...defaultOptions, ...options };

        for (let attempt = 1; attempt <= this.retryAttempts; attempt++) {
            try {
                const controller = new AbortController();
                const timeoutId = setTimeout(() => controller.abort(), this.timeout);

                const response = await fetch(url, {
                    ...mergedOptions,
                    signal: controller.signal
                });

                clearTimeout(timeoutId);

                if (!response.ok) {
                    throw new Error(`HTTP ${response.status}: ${response.statusText}`);
                }

                return await response.json();
            } catch (error) {
                console.warn(`请求失败 (尝试 ${attempt}/${this.retryAttempts}):`, error.message);
                
                if (attempt === this.retryAttempts) {
                    throw new Error(`请求失败，已重试 ${this.retryAttempts} 次: ${error.message}`);
                }

                // 等待后重试
                await new Promise(resolve => setTimeout(resolve, this.retryDelay * attempt));
            }
        }
    }

    /**
     * 获取 JIRA 问题详细信息
     * @param {string} issueKey - JIRA 问题键值，如 "IOP-6269"
     * @returns {Promise<Object>} JIRA 问题对象
     */
    async getJiraIssue(issueKey) {
        const url = `${this.baseUrl}/jira/issue/${encodeURIComponent(issueKey)}`;
        return await this.request(url);
    }

    /**
     * 根据 JQL 搜索 JIRA 问题
     * @param {string} jql - JQL 查询语句
     * @returns {Promise<Array>} JIRA 问题列表
     */
    async searchJiraIssues(jql) {
        const url = `${this.baseUrl}/jira/search?jql=${encodeURIComponent(jql)}`;
        return await this.request(url);
    }

    /**
     * 根据项目搜索 JIRA 问题
     * @param {string} project - 项目键值
     * @returns {Promise<Array>} JIRA 问题列表
     */
    async getIssuesByProject(project) {
        return await this.searchJiraIssues(`project = ${project}`);
    }

    /**
     * 根据指派人搜索 JIRA 问题
     * @param {string} assignee - 指派人用户名
     * @returns {Promise<Array>} JIRA 问题列表
     */
    async getIssuesByAssignee(assignee) {
        return await this.searchJiraIssues(`assignee = ${assignee}`);
    }

    /**
     * 根据状态搜索 JIRA 问题
     * @param {string} status - 问题状态
     * @returns {Promise<Array>} JIRA 问题列表
     */
    async getIssuesByStatus(status) {
        return await this.searchJiraIssues(`status = '${status}'`);
    }

    /**
     * 根据 JIRA 问题生成代码
     * @param {string} issueKey - JIRA 问题键值
     * @param {string} codeType - 代码类型 (entity, service, controller, dto)
     * @returns {Promise<Object>} 生成的代码对象
     */
    async generateCodeFromJira(issueKey, codeType) {
        const url = `${this.baseUrl}/code/generate-from-jira`;
        const requestBody = {
            issueKey,
            codeType
        };

        return await this.request(url, {
            method: 'POST',
            body: JSON.stringify(requestBody)
        });
    }

    /**
     * 生成 Spring Boot Entity 代码
     * @param {string} issueKey - JIRA 问题键值
     * @returns {Promise<Object>} 生成的 Entity 代码
     */
    async generateEntity(issueKey) {
        return await this.generateCodeFromJira(issueKey, 'entity');
    }

    /**
     * 生成 Spring Boot Service 代码
     * @param {string} issueKey - JIRA 问题键值
     * @returns {Promise<Object>} 生成的 Service 代码
     */
    async generateService(issueKey) {
        return await this.generateCodeFromJira(issueKey, 'service');
    }

    /**
     * 生成 Spring Boot Controller 代码
     * @param {string} issueKey - JIRA 问题键值
     * @returns {Promise<Object>} 生成的 Controller 代码
     */
    async generateController(issueKey) {
        return await this.generateCodeFromJira(issueKey, 'controller');
    }

    /**
     * 调用 MCP 方法
     * @param {string} method - MCP 方法名
     * @param {Object} params - 方法参数
     * @returns {Promise<Object>} 方法执行结果
     */
    async callMcpMethod(method, params) {
        const url = `${this.baseUrl}/mcp/call`;
        const requestBody = {
            method,
            params
        };

        return await this.request(url, {
            method: 'POST',
            body: JSON.stringify(requestBody)
        });
    }

    /**
     * 检查 MCP 服务健康状态
     * @returns {Promise<Object>} 健康状态信息
     */
    async checkHealth() {
        const healthUrl = this.baseUrl.replace('/api', '') + '/actuator/health';
        return await this.request(healthUrl);
    }

    /**
     * 获取 MCP 服务信息
     * @returns {Promise<Object>} 服务信息
     */
    async getServiceInfo() {
        const infoUrl = this.baseUrl.replace('/api', '') + '/actuator/info';
        return await this.request(infoUrl);
    }

    /**
     * 批量操作：获取项目中的所有问题并生成代码
     * @param {string} project - 项目键值
     * @param {string} codeType - 代码类型
     * @param {Function} progressCallback - 进度回调函数
     * @returns {Promise<Object>} 批量生成结果
     */
    async batchGenerateCode(project, codeType = 'entity', progressCallback = null) {
        try {
            // 1. 获取项目中的所有问题
            const issues = await this.getIssuesByProject(project);
            
            if (progressCallback) {
                progressCallback({
                    stage: 'issues-loaded',
                    total: issues.length,
                    current: 0,
                    message: `已加载 ${issues.length} 个问题`
                });
            }

            // 2. 为每个问题生成代码
            const generatedCodes = {};
            const errors = {};

            for (let i = 0; i < issues.length; i++) {
                const issue = issues[i];
                
                try {
                    const code = await this.generateCodeFromJira(issue.key, codeType);
                    generatedCodes[issue.key] = code;
                    
                    if (progressCallback) {
                        progressCallback({
                            stage: 'generating',
                            total: issues.length,
                            current: i + 1,
                            issueKey: issue.key,
                            message: `已生成 ${i + 1}/${issues.length} 个代码`
                        });
                    }
                } catch (error) {
                    errors[issue.key] = error.message;
                    console.warn(`为问题 ${issue.key} 生成代码失败:`, error.message);
                }
            }

            const result = {
                success: true,
                project,
                codeType,
                totalIssues: issues.length,
                generatedCount: Object.keys(generatedCodes).length,
                errorCount: Object.keys(errors).length,
                generatedCodes,
                errors
            };

            if (progressCallback) {
                progressCallback({
                    stage: 'completed',
                    ...result,
                    message: '批量代码生成完成'
                });
            }

            return result;
        } catch (error) {
            if (progressCallback) {
                progressCallback({
                    stage: 'error',
                    message: `批量代码生成失败: ${error.message}`
                });
            }
            throw error;
        }
    }
}

// 使用示例
const examples = {
    /**
     * 基本使用示例
     */
    async basicUsage() {
        const client = new JiraMcpClient({
            baseUrl: 'http://localhost:8010/mcptools/api',
            timeout: 30000,
            retryAttempts: 3
        });

        try {
            // 获取问题
            const issue = await client.getJiraIssue('IOP-6269');
            console.log('JIRA 问题:', issue);

            // 生成代码
            const entity = await client.generateEntity('IOP-6269');
            console.log('生成的 Entity 代码:', entity.code);

            // 搜索问题
            const issues = await client.searchJiraIssues('project = IOP AND status = "In Progress"');
            console.log('搜索结果:', issues);

        } catch (error) {
            console.error('操作失败:', error);
        }
    },

    /**
     * 批量代码生成示例
     */
    async batchGeneration() {
        const client = new JiraMcpClient();

        try {
            const result = await client.batchGenerateCode(
                'IOP', 
                'entity',
                (progress) => {
                    console.log('进度更新:', progress);
                }
            );

            console.log('批量生成结果:', result);
        } catch (error) {
            console.error('批量生成失败:', error);
        }
    },

    /**
     * 健康检查示例
     */
    async healthCheck() {
        const client = new JiraMcpClient();

        try {
            const health = await client.checkHealth();
            console.log('服务健康状态:', health);

            const info = await client.getServiceInfo();
            console.log('服务信息:', info);
        } catch (error) {
            console.error('健康检查失败:', error);
        }
    }
};

// 导出模块 (Node.js)
if (typeof module !== 'undefined' && module.exports) {
    module.exports = { JiraMcpClient, examples };
}

// 浏览器全局变量
if (typeof window !== 'undefined') {
    window.JiraMcpClient = JiraMcpClient;
    window.JiraMcpExamples = examples;
} 