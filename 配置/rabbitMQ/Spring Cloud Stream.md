


Spring Cloud Stream 只绑定了第一个匹配的 function

即使你配置了：

spring:
cloud:
function:
definition: alarmDetect;alarmResolve;alarmDetectPush;alarmResolvePush



