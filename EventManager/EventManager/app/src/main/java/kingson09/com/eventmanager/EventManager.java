package kingson09.com.eventmanager;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;

/**
 * Created by bjliuzhanyong on 2018/3/31.
 */

public class EventManager {
  private final static HashMap<Context, EventManager> managerMap = new HashMap<>();

  public static EventManager getEventManager(Context context) {
    EventManager eventManager = managerMap.get(context);
    if (eventManager == null) {
      eventManager = new EventManager();
      managerMap.put(context, eventManager);
    }
    return eventManager;
  }

  public static EventManager removeEventManager(Context context) {
    EventManager eventManager = managerMap.remove(context);
    return eventManager;
  }

  private HashMap<Class, EventProxy> eventMap = new HashMap();
  private HashMap<Class, Object> observablesMap = new HashMap();

  public <T> boolean registerSubscriber(Class<T> eventContract, T observer) {
    EventProxy proxy = eventMap.get(eventContract);
    if (proxy == null) {
      proxy = new EventProxy();
      eventMap.put(eventContract, proxy);
    }
    proxy.registerSubscriber(observer);
    return true;
  }

  public <T> boolean unRegisterSubscriber(Class<T> eventContract, T observer) {
    EventProxy proxy = eventMap.get(eventContract);
    if (proxy == null) {
      return true;
    }
    if (proxy.unRegisterSubscriber(observer)) {
      eventMap.remove(eventContract);
    }
    return true;
  }

  public <T> T registerPublisher(Class<T> eventContract) {

    EventProxy proxy = eventMap.get(eventContract);
    if (proxy == null) {
      proxy = new EventProxy();
      eventMap.put(eventContract, proxy);
    }
    Object observerHandler = observablesMap.get(eventContract);
    if (observerHandler == null) {
      observerHandler = Proxy.newProxyInstance(eventContract.getClassLoader(), new Class<?>[] { eventContract }, proxy);
      observablesMap.put(eventContract, observerHandler);
    }
    if (observerHandler == null) {
      return null;
    }
    return (T) observerHandler;
  }

  private static class EventProxy implements InvocationHandler {
    private ArrayList observers = new ArrayList<>();

    protected EventProxy() {
    }

    protected void registerSubscriber(Object observer) {
      if (observers.contains(observer)) {
        return;
      }
      observers.add(observer);
    }

    protected boolean unRegisterSubscriber(Object observer) {
      observers.remove(observer);
      return observers.isEmpty();
    }

    @Override
    public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
      for (Object observer : observers) {
        method.invoke(observer, objects);
      }
      return null;
    }
  }

}
