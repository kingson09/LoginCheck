package kingson09.com.eventmanager;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by bjliuzhanyong on 2018/3/31.
 */

public class EventManager {
  private static HashMap<Class, EventProxy> eventMap = new HashMap();
  private static HashMap<Class, Object> observablesMap = new HashMap();

  public static <T> boolean registerSubscriber(Class<T> eventContract, T observer) {
    EventProxy proxy = eventMap.get(eventContract);
    if (proxy == null) {
      proxy = new EventProxy();
      eventMap.put(eventContract, proxy);
    }
    proxy.registerObserver(observer);
    return true;
  }

  public static <T> T registerPublisher(Class<T> eventContract) {

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

    protected void registerObserver(Object observer) {
      if (observers.contains(observer)) {
        return;
      }
      observers.add(observer);
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
