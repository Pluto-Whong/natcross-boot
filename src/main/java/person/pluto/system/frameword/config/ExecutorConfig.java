package person.pluto.system.frameword.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * <p>
 * 异步线程池配置
 * </p>
 *
 * @author wangmin1994@qq.com
 * @since 2019-04-02 13:36:41
 */
@Configuration
public class ExecutorConfig {

    @Bean("commonExecutor")
    public Executor webSocketExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(8);
        taskExecutor.setMaxPoolSize(16);
        // 缓冲执行任务的队列
        taskExecutor.setQueueCapacity(Integer.MAX_VALUE);
        // 线程池关闭的时候等待所有任务都完成再继续销毁其他的Bean
        taskExecutor.setWaitForTasksToCompleteOnShutdown(true);
        // 设置线程池中任务的等待时间，如果超过这个时候还没有销毁就强制销毁，以确保应用最后能够被关闭，而不是阻塞住
        taskExecutor.setAwaitTerminationSeconds(60);
        // 当超过了核心线程出之外的线程在空闲时间到达之后会被销毁
        taskExecutor.setKeepAliveSeconds(3600);
        taskExecutor.setThreadNamePrefix("common-thread-");
        return taskExecutor;
    }

}
