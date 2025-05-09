

一定要在父项目中添加，否则其他模块引用不到，而且common模块中也没有代码提示与报错


```xml

    <modules>
        <module>gateway</module>
        <module>common</module>
        <module>authorization-server</module>
        <module>authorization-resource</module>
        <module>seckill-server</module>
    </modules>
```