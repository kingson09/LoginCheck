package com.everyday.caipiao.weaver;

import java.util.concurrent.TimeUnit;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.CodeSignature;
import org.aspectj.lang.reflect.MethodSignature;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.os.Trace;
import android.util.Log;

import com.everyday.caipiao.common.context.AppContext;
import com.everyday.caipiao.common.context.AppLog;
import com.everyday.caipiao.common.types.UserSession;
import com.everyday.caipiao.publicserviceimpl.HostUIRouter;
import com.lede.tech.utils.Tools;
import com.everyday.caipiao.weaver.LoginCheck;

/**
 * Created by bjliuzhanyong on 2017/9/19.
 */

@Aspect
public class LoginWeaver {
  private ProceedingJoinPoint loginPoint;

  @Pointcut("execution(@LoginCheck * *(..))&&@annotation(loginCheck)")
  public void methodLoginCheck(LoginCheck loginCheck) {
  }

  @Around("methodLoginCheck(loginCheck)")
  public Object loginCheck(ProceedingJoinPoint joinPoint, LoginCheck loginCheck) throws Throwable {
    UserSession session = AppContext.getInstance().getSession();
    if (session != null && session.getState() == UserSession.LOG_IN) {
      Object result = joinPoint.proceed();
      AppLog.debug("LoginCheck", "methodLoginCheck haslogin ");
      return result;
    } else {
      if (!loginCheck.autoNext() || !isMainThread()) {
        AppLog.debug("LoginCheck", "methodLoginCheck noLogin autoNext false");
        Tools.openUri(HostUIRouter.HOST_LOGIN, null);
        return null;
      } else {
        loginPoint = joinPoint;
        CodeSignature codeSignature = (CodeSignature) joinPoint.getSignature();
        Class<?> cls = codeSignature.getDeclaringType();
        String methodName = codeSignature.getName();
        Uri url = new Uri.Builder().scheme(HostUIRouter.LOTTERY_NEW_SCHEME).path("//check_denglu_status")
            .appendQueryParameter("classname", cls.getSimpleName()).appendQueryParameter("method", methodName).build();
        HostUIRouter.login(url);
        AppLog.debug("LoginCheck", "methodLoginCheck noLogin autoNext true:" + url);
        return null;
      }
    }
  }


  @Pointcut("execution(@com.everyday.caipiao.weaver.AfterLoginCheck * *(..))")
  public void methodAfterLoginCheck() {
  }

  @Around("methodAfterLoginCheck()")
  public Object afterLoginCheck(ProceedingJoinPoint joinPoint) throws Throwable {
    CodeSignature afterCodeSignature = (CodeSignature) joinPoint.getSignature();
    String[] parameterNames = afterCodeSignature.getParameterNames();
    Object[] parameterValues = joinPoint.getArgs();
    if (parameterNames != null && parameterNames.length == 2 && loginPoint != null) {
      CodeSignature loginCodeSignature = (CodeSignature) loginPoint.getSignature();
      Class<?> cls = loginCodeSignature.getDeclaringType();
      String methodName = loginCodeSignature.getName();
      int i = 0;
      for (; i < parameterValues.length; i++) {
        if ("classname".equals(parameterNames[i]) && cls.getSimpleName().equals(parameterValues[i]) ||
            "method".equals(parameterNames[i]) && methodName.equals(parameterValues[i])) {
          continue;
        } else {
          break;
        }
      }
      if (i == 2) {
        Object result = loginPoint.proceed();
        loginPoint = null;
        AppLog.debug("LoginCheck", "methodAfterLoginCheck success");
        return result;
      }
    }
    return null;
  }

  public boolean isMainThread() {
    return Looper.getMainLooper() == Looper.myLooper();
  }
}
