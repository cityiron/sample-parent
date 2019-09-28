package com.dbb.jobhandler;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 任务Handler示例（Bean模式）
 * <p>
 * 开发步骤： 1、继承"IJobHandler"：“com.xxl.job.core.handler.IJobHandler”； 2、注册到Spring容器：添加“@Component”注解，被Spring容器扫描为Bean实例；
 * 3、注册到执行器工厂：添加“@JobHandler(value="自定义jobhandler名称")”注解，注解value值对应的是调度中心新建任务的JobHandler属性的值。 4、执行日志：需要通过
 * "XxlJobLogger.log" 打印执行日志；
 *
 * @author xuxueli 2015-12-19 19:43:36
 */
@JobHandler(value = "demoJobHandler")
@Component
public class DemoJobHandler extends IJobHandler {

    @Value("${random.max:10000}")
    private String max;

    @Value("${random.min:200}")
    private String min;

    @Override
    public ReturnT<String> execute(String param) throws Exception {
        XxlJobLogger.log("XXL-JOB, Hello World.");

        int max = Integer.valueOf(getMax()).intValue(), min = Integer.valueOf(getMin()).intValue();
        int rand = (int)(Math.random() * (max - min) + min);

        TimeUnit.MILLISECONDS.sleep(rand);

        XxlJobLogger.log("This job run in _{}_ times", rand);

        return SUCCESS;
    }

    public String getMax() {
        return max;
    }

    public void setMax(String max) {
        this.max = max;
    }

    public String getMin() {
        return min;
    }

    public void setMin(String min) {
        this.min = min;
    }

}
