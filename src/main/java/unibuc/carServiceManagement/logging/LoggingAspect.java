package unibuc.carServiceManagement.logging;

import java.util.Arrays;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Aspect
@Component
public class LoggingAspect {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    /**
     * Pointcut that matches all repositories, services and Web REST endpoints.
     */
    @Pointcut("within(@org.springframework.stereotype.Repository *)" +
            " || within(@org.springframework.stereotype.Service *)" +
            " || within(@org.springframework.web.bind.annotation.RestController *)")
    public void springBeanPointcut() {
        // Method is empty as this is just a Pointcut, the implementations is in the advices.
    }


    /**
     * Advice that logs methods throwing exceptions.
     */
    @AfterThrowing(pointcut = "springBeanPointcut()", throwing = "e")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable e) {
        log.error("Exception in {}.{}() with cause = {}", joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(), e.getCause() != null ? e.getCause() : "NULL");
    }

    /**
     * Advice that logs when a method is entered and exited.
     */
    @Around("springBeanPointcut()")
    // Alternative: use regex for joinPoint
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
//  Enable logging only when debug enabled
//            if (log.isDebugEnabled()) {}
//  Alternative:
//            MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
//
//            //Get intercepted method details
//            String className = methodSignature.getDeclaringType().getSimpleName();
//            String methodName = methodSignature.getName();

        log.info("Call method: {}.{}() with argument[s] = {}", joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(), Arrays.toString(joinPoint.getArgs()));

        final StopWatch stopWatch = new StopWatch();

        //Measure method execution time
        stopWatch.start();

        try {
            Object result = joinPoint.proceed();
            log.info("Exit method: {}.{}() with result = {}", joinPoint.getSignature().getDeclaringTypeName(),
                    joinPoint.getSignature().getName(), result);

            //Log method execution time
            stopWatch.stop();
            log.info("Execution time of: {} ms", stopWatch.getTotalTimeMillis());

            return result;
        } catch (IllegalArgumentException e) {
            log.error("Illegal argument: {} in {}.{}()", Arrays.toString(joinPoint.getArgs()),
                    joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());
            throw e;
        }

    }
}