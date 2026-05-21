package com.howord.backend.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class OrderAspect {
	
	@Before("execution(* com.howord.backend.order.OrderServiceImpl.*(..))")
    public void logBefore(JoinPoint joinPoint) {
        System.out.println("[AOP] 呼叫前: " + joinPoint.getSignature().getName());
    }
	
	@After("execution(* com.howord.backend.order.OrderServiceImpl.*(..))")
	public void logAfter(JoinPoint joinPoint) {
	    System.out.println("[AOP] 呼叫後：" + joinPoint.getSignature().getName());
	}
	
}
