package com.example.common.utils;


import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
public class Snowflake {
    private static final int workerIdBits = 5; // 工作机器 ID 位数
    private static final int sequenceBits = 12; // 序列号位数

    private static final int workerIdShift = sequenceBits;
    private static final int timestampLeftShift = workerIdBits + sequenceBits;
    private static final int sequenceMask = (1 << sequenceBits) - 1;
    private static final long epoch = 1640995200000L; // 自定义起始时间戳（2022年1月1日）

    private long workerId; // 工作机器 ID
    private long sequence = 0L; // 序列号
    private long lastTimestamp = -1L; // 上一次时间戳

    private Lock lock = new ReentrantLock();

    // 默认构造函数，workerId 默认为 0
    public Snowflake() {
        this(0L);
    }

    public Snowflake(long workerId) {
        if (workerId < 0 || workerId >= (1 << workerIdBits)) {
            throw new IllegalArgumentException(String.format("worker Id must be between 0 and %d", (1 << workerIdBits) - 1));
        }
        this.workerId = workerId;
    }

    public synchronized long nextId() {
        long timestamp = System.currentTimeMillis() - epoch;

        if (timestamp == lastTimestamp) {
            sequence = (sequence + 1) & sequenceMask;
            if (sequence == 0) {
                while (timestamp <= lastTimestamp) {
                    timestamp = System.currentTimeMillis() - epoch;
                }
            }
        } else {
            sequence = 0;
        }

        lastTimestamp = timestamp;
        return ((timestamp << timestampLeftShift) | (workerId << workerIdShift) | sequence);
    }

    // 从 Nacos 获取微服务 ID（workerId）
//    public static long getWorkerIdFromNacos() throws NacosException {
//        String serverAddr = "127.0.0.1:8848"; // Nacos 服务器地址
//        String serviceName = "my-microservice"; // 微服务名称（假设服务注册在 Nacos 上）
//
//        NamingService namingService = NacosFactory.createNamingService(serverAddr);
//        List<Instance> instances = namingService.getAllInstances(serviceName);
//
//        if (instances.isEmpty()) {
//            throw new NacosException(NacosException.CLIENT_INVALID_PARAM, "No instances found for the service: " + serviceName);
//        }
//
//        // 获取服务实例的唯一 ID（假设使用服务名的哈希值作为 workerId）
//        String serviceInstanceId = instances.get(0).getInstanceId();
//        return Math.abs(serviceInstanceId.hashCode()) % (1 << workerIdBits); // 将服务实例 ID 的哈希值作为 workerId
//    }
//
//    public static void main(String[] args) {
//        try {
//            long workerId = getWorkerIdFromNacos();
//            Snowflake snowflake = new Snowflake(workerId);
//
//            long uniqueId = snowflake.nextId();
//            System.out.println("Generated Unique ID: " + uniqueId);
//        } catch (NacosException e) {
//            e.printStackTrace();
//        }
//    }
}