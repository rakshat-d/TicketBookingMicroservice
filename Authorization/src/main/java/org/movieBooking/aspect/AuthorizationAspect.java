package org.movieBooking.aspect;

import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.movieBooking.annotations.HasRoles;
import org.movieBooking.exceptions.AuthorizationFailedException;
import org.movieBooking.util.VerifyRole;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
public class AuthorizationAspect {

    @Around("@annotation(org.movieBooking.annotations.HasRoles)")
    public Object authorize(ProceedingJoinPoint joinPoint) throws Throwable {
        var request =  ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        var methodSignature = (MethodSignature) joinPoint.getSignature();
        var method = methodSignature.getMethod();
        var annotation = method.getAnnotation(HasRoles.class);
        if (!VerifyRole.verify(request.getHeader(VerifyRole.headerName), annotation.value()))
            throw new AuthorizationFailedException("User does not have enough permissions");
        return joinPoint.proceed();
    }
}
