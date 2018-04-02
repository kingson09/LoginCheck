package kingson09.com.eventmanager.util;

/**
 * Created by bjliuzhanyong on 2018/4/2.
 */

public abstract class Singleton<T> {
  private T ourInstance;

  protected abstract T createInstance();

  public final T getInstance() {
    synchronized (this) {
      if (ourInstance == null) {
        ourInstance = createInstance();
      }
      return ourInstance;
    }
  }
}