#!/usr/bin/env node

/**
 * MCP 服务器代理
 * 将 Cursor 连接到我们的 Java MCP JIRA 服务
 */

const http = require('http');
const { URL } = require('url');

class McpProxy {
    constructor() {
        this.serverUrl = process.env.MCP_SERVER_URL || 'http://localhost:8010/mcp-jira/api/mcp';
        this.serverName = process.env.MCP_SERVER_NAME || 'JIRA Code Generator';
    }

    async handleRequest(request) {
        try {
            const response = await this.sendToJavaService(request);
            return response;
        } catch (error) {
            console.error('MCP 代理错误:', error);
            return {
                jsonrpc: "2.0",
                id: request.id,
                error: {
                    code: -32603,
                    message: "Internal error",
                    data: error.message
                }
            };
        }
    }

    async sendToJavaService(request) {
        return new Promise((resolve, reject) => {
            const url = new URL(this.serverUrl + '/request');
            const postData = JSON.stringify(request);

            const options = {
                hostname: url.hostname,
                port: url.port,
                path: url.pathname,
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Content-Length': Buffer.byteLength(postData)
                }
            };

            const req = http.request(options, (res) => {
                let data = '';
                res.on('data', (chunk) => {
                    data += chunk;
                });
                res.on('end', () => {
                    try {
                        const response = JSON.parse(data);
                        resolve(response);
                    } catch (error) {
                        reject(new Error(`解析响应失败: ${error.message}`));
                    }
                });
            });

            req.on('error', (error) => {
                reject(new Error(`请求失败: ${error.message}`));
            });

            req.write(postData);
            req.end();
        });
    }

    async initialize() {
        // 发送初始化请求
        const initRequest = {
            jsonrpc: "2.0",
            id: 1,
            method: "initialize",
            params: {
                protocolVersion: "2024-11-05",
                capabilities: {},
                clientInfo: {
                    name: "Cursor",
                    version: "1.0.0"
                }
            }
        };

        return await this.handleRequest(initRequest);
    }

    start() {
        // 监听标准输入
        process.stdin.setEncoding('utf8');
        
        let buffer = '';
        process.stdin.on('data', async (data) => {
            buffer += data;
            
            // 处理多个 JSON 消息
            const lines = buffer.split('\n');
            buffer = lines.pop(); // 保留不完整的行
            
            for (const line of lines) {
                if (line.trim()) {
                    try {
                        const request = JSON.parse(line);
                        const response = await this.handleRequest(request);
                        process.stdout.write(JSON.stringify(response) + '\n');
                    } catch (error) {
                        console.error('解析请求失败:', error);
                    }
                }
            }
        });

        process.stdin.on('end', () => {
            process.exit(0);
        });

        // 发送服务器信息
        const serverInfo = {
            jsonrpc: "2.0",
            method: "notifications/initialized",
            params: {
                serverInfo: {
                    name: this.serverName,
                    version: "1.0.0"
                }
            }
        };
        
        process.stdout.write(JSON.stringify(serverInfo) + '\n');
        console.error(`MCP 代理启动成功，连接到: ${this.serverUrl}`);
    }
}

// 启动代理
const proxy = new McpProxy();
proxy.start(); 