


## 为什么 Sentinel 配置在服务调用者中，而不是在服务被调用者中？



有Sentinel的情况
A[用户请求] --> B(LoadBalancer)
B -->|选择实例| C[Service A]
C -->|调用依赖| D{Sentinel监控}
D -->|正常| E[Service B]
D -->|熔断| F[降级逻辑]