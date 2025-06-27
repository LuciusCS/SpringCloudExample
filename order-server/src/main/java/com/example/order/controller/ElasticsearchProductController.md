

## 接口请求统一抛出异常

Spring Boot 默认使用 BasicErrorController + DefaultErrorAttributes 来处理未捕获异常。
发生运行时异常（如 NoSuchElementException）时，Spring 会返回一个 JSON 错误响应，状态码为 500，并自动包含如下字段：

```
{
    "timestamp": "...",
    "status": 500,
    "error": "Internal Server Error",
    "message": "No value present",
    "trace": "...",
    "path": "/order/product/findById"
}

```


方法	                                       说明	                 推荐
Optional.get()	                        简单，但容易抛出异常	   ❌ 不推荐
orElseThrow + @ControllerAdvice	        最优雅，统一异常管理	   ✅ 推荐
ResponseEntity 分支处理	                    简洁、无异常	       ✅ 推荐用于小项目