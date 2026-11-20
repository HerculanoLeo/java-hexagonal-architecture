package com.lodh8.starter.cadastros.infra.configuration;

import com.lodh8.starter.shared.utils.MdcTaskDecorator;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class AsyncMdcConfiguration {

    @Bean
    static BeanPostProcessor mdcApplicationTaskExecutorCustomizer() {
        return new BeanPostProcessor() {
            @Override
            public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
                if ("applicationTaskExecutor".equals(beanName) && bean instanceof ThreadPoolTaskExecutor executor) {
                    executor.setTaskDecorator(new MdcTaskDecorator());
                }
                return bean;
            }
        };
    }
}
