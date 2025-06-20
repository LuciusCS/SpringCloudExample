

## Spring Cloud Gateway 限流

参考 gateway.src.main.java.com.example.config. Sentinel vs. Resilience4j.md


## Spring Cloud Gateway 需要使用 WebFlux


特性	          Servlet（Spring MVC）   	WebFlux
I/O 模型	            阻塞	                 非阻塞
线程模型      	每请求一个线程	    单线程事件驱动
性能	较低，       线程切换成本高	    较高，低线程开销
适合场景	       低并发、同步服务	    高并发、异步服务

## 在Gateway-Server中需要进行权限管理

gateway 作为资源服务器，代理校验请求权限，鉴权通过才转发请求