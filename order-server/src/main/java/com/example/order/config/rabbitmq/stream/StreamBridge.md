


# Spring Cloud Stream 只绑定了第一个匹配的 function

即使你配置了：

spring:
cloud:
function:
definition: alarmDetect;alarmResolve;alarmDetectPush;alarmResolvePush
但是 Spring Cloud Function 框架默认一个 destination 只绑定到一个函数，即便多个函数都声明了消费同一个 destination，只有第一个声明的有效，其余被忽略。