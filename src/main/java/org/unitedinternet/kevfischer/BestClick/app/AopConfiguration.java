package org.unitedinternet.kevfischer.BestClick.app;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.interceptor.PerformanceMonitorInterceptor;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import java.time.LocalDate;
import java.time.Month;

@Configuration
@EnableAspectJAutoProxy
@Aspect
public class AopConfiguration {

    @Pointcut(
            "execution(public org.unitedinternet.kevfischer.BestClick.model.database.Session org.unitedinternet.kevfischer.BestClick.controller.AuthentificationController.login(..))"
    )
    public void monitor() { }

    @Bean
    public PerformanceMonitorInterceptor performanceMonitorInterceptor() {
        return new PerformanceMonitorInterceptor(true);
    }

    @Bean
    public Advisor performanceMonitorAdvisor() {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression("org.unitedinternet.kevfischer.BestClick.app.AopConfiguration.monitor()");
        return new DefaultPointcutAdvisor(pointcut, performanceMonitorInterceptor());
    }

}