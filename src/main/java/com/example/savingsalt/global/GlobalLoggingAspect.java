package com.example.savingsalt.global;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class GlobalLoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(GlobalLoggingAspect.class);

    // 포인트컷: 모든 컨트롤러 메서드
    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void restControllerMethods() {
    }

    // 포인트컷: 모든 서비스 메서드
    @Pointcut("within(@org.springframework.stereotype.Service *)")
    public void serviceMethods() {
    }

    // 메서드 실행 전 로그
    @Before("restControllerMethods() || serviceMethods()")
    public void logBefore(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        logger.info("메서드 시작: {}.{}() - args: {}",
            methodSignature.getDeclaringTypeName(),
            methodSignature.getName(),
            joinPoint.getArgs());
    }

    // 메서드 실행 후 로그
    @After("restControllerMethods() || serviceMethods()")
    public void logAfter(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        logger.info("메서드 종료: {}.{}()",
            methodSignature.getDeclaringTypeName(),
            methodSignature.getName());
    }

    // 메서드 실행 결과 로그
    @AfterReturning(pointcut = "restControllerMethods() || serviceMethods()", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        logger.info("메서드 반환: {}.{}() - result: {}",
            methodSignature.getDeclaringTypeName(),
            methodSignature.getName(),
            result);
    }

    // 메서드 실행 중 예외 로그
    @AfterThrowing(pointcut = "restControllerMethods() || serviceMethods()", throwing = "error")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable error) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        logger.error("메서드 예외: {}.{}() - error: {}",
            methodSignature.getDeclaringTypeName(),
            methodSignature.getName(),
            error.getMessage());
    }
}