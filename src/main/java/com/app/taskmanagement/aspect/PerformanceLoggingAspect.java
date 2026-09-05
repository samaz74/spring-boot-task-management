package com.app.taskmanagement.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import com.app.taskmanagement.annotation.TrackExecutionTime;

@Component
@Aspect

public class PerformanceLoggingAspect {
    private static final Logger logger = LoggerFactory.getLogger(PerformanceLoggingAspect.class);
    @Around("@annotation(trackExecutionTime)")
    public Object performanceLoggingAspect(ProceedingJoinPoint proceedingJoinPoint,TrackExecutionTime trackExecutionTime) throws Throwable {
        long startTime = System.nanoTime();
        try {
            return proceedingJoinPoint.proceed();
        } finally {
            long endTime = System.nanoTime();
            long duration = (endTime - startTime)/1_000_000L;
            if (duration > trackExecutionTime.threshold()) {
                logger.warn("Slow method: {} took {} ms",
                        proceedingJoinPoint.getSignature().getName(),
                        duration);
            }
        }

    }


}
