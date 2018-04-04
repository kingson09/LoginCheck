package kingson09.com.eventmanager.fragments;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import kingson09.com.eventmanager.EventManager;
import kingson09.com.eventmanager.R;

/**
 * Created by bjliuzhanyong on 2018/3/31.
 */

public class ItemListView extends ListView {
  private EventManager eventManager = EventManager.getEventManager(getContext());
  private IShowDetail containerPublisher;
  private SimpleAdapter adapter;
  private List<Map<String, Object>> list;

  public ItemListView(Context context, AttributeSet attrs) {
    super(context, attrs);
    containerPublisher = eventManager.registerPublisher(IShowDetail.class);
  }

  public void setData(List<Map<String, Object>> data) {
    list = data;
    adapter =
        new SimpleAdapter(getContext(), list, R.layout.list_item, new String[] { "article" }, new int[] { R.id.text });
    setAdapter(adapter);
  }

  @Override
  protected void onFinishInflate() {
    super.onFinishInflate();
    setOnItemClickListener(new OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Map<String, Object> item = (Map) adapter.getItem(position);
        containerPublisher.showItem((String) item.get("article"));
      }
    });
  }
}