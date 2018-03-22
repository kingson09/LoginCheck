package com.test;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import android.os.IBinder;
import android.os.Parcel;

/**
 * Created by bjliuzhanyong on 2018/3/22.
 */

public class HookAms {
  public static void hookAms() {

    try {
      Class<?> ActivityManagerNativeClass = Class.forName("android.app.ActivityManagerNative");
      Field gDefaultFiled = ActivityManagerNativeClass.getDeclaredField("gDefault");
      gDefaultFiled.setAccessible(true);
      Object gDefault = gDefaultFiled.get(null);

      Class<?> SingletonClass = Class.forName("android.util.Singleton");
      Field mInstanceField = SingletonClass.getDeclaredField("mInstance");
      mInstanceField.setAccessible(true);

      Object activityManagerObject = mInstanceField.get(gDefault);


      Class<?> ActivityManagerProxyClass = Class.forName("android.app.ActivityManagerProxy");
      Field mRemoteField = ActivityManagerProxyClass.getDeclaredField("mRemote");
      mRemoteField.setAccessible(true);
      Object mRemote = mRemoteField.get(activityManagerObject);

      Class<?> IBinderClass = Class.forName("android.os.IBinder");
      AmsInvocationHandler handler = new AmsInvocationHandler(mRemote);

      Object proxy = Proxy
          .newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class<?>[] { IBinderClass }, handler);


      mRemoteField.set(activityManagerObject, proxy);


    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private static class AmsInvocationHandler implements InvocationHandler {

    private Object mRemote;

    private AmsInvocationHandler(Object mRemote) {
      this.mRemote = mRemote;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
      if ((int) args[0] == IBinder.FIRST_CALL_TRANSACTION + 2) {
        System.out.println("bundle size:" + ((Parcel) args[1]).dataSize());
      }

      return method.invoke(mRemote, args);
    }
  }

}
