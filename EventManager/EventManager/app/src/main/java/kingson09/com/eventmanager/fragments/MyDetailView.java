package kingson09.com.eventmanager.fragments;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import kingson09.com.eventmanager.EventManager;

/**
 * Created by bjliuzhanyong on 2018/3/31.
 */

public class MyDetailView extends TextView implements IShowDetail, IBackPressed {
  private EventManager eventManager = EventManager.getEventManager(getContext());

  public MyDetailView(Context context) {
    super(context);
    eventManager.registerSubscriber(IShowDetail.class, this);
  }

  public MyDetailView(Context context, AttributeSet attrs) {
    super(context, attrs);
    eventManager.registerSubscriber(IShowDetail.class, this);
  }

  @Override
  public void showItem(String item) {
    setText(item);
  }

  @Override
  public boolean onBackPressed() {
    setText("");
    return false;
  }
}
