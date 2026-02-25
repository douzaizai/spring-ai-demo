## Spring AI Demo
这是一个使用spring-ai框架开发的基于LLM的聊天机器人后端服务demo，实现了以下功能:
1. 使用redis缓存session和聊天历史记录，同时异步的在mysql中保存聊天历史记录。
2. 使用ZhipuAI作为Embedding model，在服务启动时对测试数据进行embedding。
3. 提供了登陆接口和token验证。
4. 提供了普通聊天接口和带有RAG功能的聊天接口。

### 运行环境
+ JDK: 17
+ Spring boot: 3.5.9
+ Spring AI: 1.1.2
+ LLM: Deepseek
+ Embedding model: ZhipuAI
+ Vector store: Elasticsearch
+ Chat memory: Redis + mysql

