package com.example.order.component.xxljob;


import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.stereotype.Component;

@Component
public class XXLJobTask {

    @XxlJob("xxlJobTask")
    public void xxlJobTask(){
        // 获取 JobData（包含参数等信息）
        // 获取任务传递的参数
        String param = XxlJobHelper.getJobParam();
        System.out.println("Received param: " + param);
    }
}
