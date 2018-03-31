package kingson09.com.eventmanager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import kingson09.com.eventmanager.fragments.ItemListView;

public class MainActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ((ItemListView) findViewById(R.id.article_list)).setData(buildListForSimpleAdapter());


  }

  private List<Map<String, Object>> buildListForSimpleAdapter() {
    List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("article", "金正日访华");
    listItems.add(map);

    map = new HashMap<String, Object>();
    map.put("article", "川普访华！");
    listItems.add(map);

    map = new HashMap<String, Object>();
    map.put("article", "全世界领导人都访华~");
    listItems.add(map);
    return listItems;
  }
}
