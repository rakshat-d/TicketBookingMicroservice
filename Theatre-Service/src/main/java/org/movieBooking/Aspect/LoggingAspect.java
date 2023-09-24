package org.movieBooking.Aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    ObjectMapper mapper;

    @Pointcut(value="execution(* com.movieBooking.Service.*.*(..) )")
    public void myPointcut() {
    }

    @Around("myPointcut()")
    public Object applicationLogger(ProceedingJoinPoint pjp) throws Throwable {
        String methodName = pjp.getSignature().getName();
        String className = pjp.getTarget().getClass().toString();
        Object[] array = pjp.getArgs();
        log.info("method_invoked " + "" + methodName + "class_Name " + " " + className + "arguments" + " "
                + mapper.writeValueAsString(array));
        Object object = pjp.proceed();
        log.info(methodName + "class_Name " + " " + className + "Response " + " " + mapper.writeValueAsString(object));
        return object;
    }

}
