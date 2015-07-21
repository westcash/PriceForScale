package com.scale.hanwei.priceforscale;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;


public class MainActivity extends ActionBarActivity {

    GridView mGridView;
    TextView test;//test for version control

    static final String[] scaleKind = { "tablescale", "platformscale", "eletronicbalance", "hangscale",
                                        "groundscale", "weightdisplay", "mechscale", "loadcell",
                                        "wheelchairscale", "weights", "others"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mGridView = (GridView) findViewById(R.id.gridView);

        //資料來源
        ArrayList<HashMap<String, Object>> lstImageItem = new ArrayList<HashMap<String, Object>>();

        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("ItemImage", R.drawable.tablescale);//添加图像资源的ID
        map.put("ItemText", "電子桌秤");//按序号做ItemText
        lstImageItem.add(map);

        map = new HashMap<String, Object>();
        map.put("ItemImage", R.drawable.platformscale);//添加图像资源的ID
        map.put("ItemText", "電子台秤");//按序号做ItemText
        lstImageItem.add(map);

        map = new HashMap<String, Object>();
        map.put("ItemImage", R.drawable.eletronicbalance);//添加图像资源的ID
        map.put("ItemText", "電子天平");//按序号做ItemText
        lstImageItem.add(map);

        map = new HashMap<String, Object>();
        map.put("ItemImage", R.drawable.hangscale);//添加图像资源的ID
        map.put("ItemText", "電子吊秤");//按序号做ItemText
        lstImageItem.add(map);

        map = new HashMap<String, Object>();
        map.put("ItemImage", R.drawable.groundscale);//添加图像资源的ID
        map.put("ItemText", "地磅");//按序号做ItemText
        lstImageItem.add(map);

        map = new HashMap<String, Object>();
        map.put("ItemImage", R.drawable.weightdisplay);//添加图像资源的ID
        map.put("ItemText", "重量控制器");//按序号做ItemText
        lstImageItem.add(map);

        map = new HashMap<String, Object>();
        map.put("ItemImage", R.drawable.mechscale);//添加图像资源的ID
        map.put("ItemText", "機械秤");//按序号做ItemText
        lstImageItem.add(map);

        map = new HashMap<String, Object>();
        map.put("ItemImage", R.drawable.loadcell);//添加图像资源的ID
        map.put("ItemText", "荷重元");//按序号做ItemText
        lstImageItem.add(map);

        map = new HashMap<String, Object>();
        map.put("ItemImage", R.drawable.wheelchairscale);//添加图像资源的ID
        map.put("ItemText", "醫療秤");//按序号做ItemText
        lstImageItem.add(map);

        map = new HashMap<String, Object>();
        map.put("ItemImage", R.drawable.weights);//添加图像资源的ID
        map.put("ItemText", "砝碼");//按序号做ItemText
        lstImageItem.add(map);

        map = new HashMap<String, Object>();
        map.put("ItemImage", R.drawable.others);//添加图像资源的ID
        map.put("ItemText", "其它");//按序号做ItemText
        lstImageItem.add(map);

        //生成适配器的ImageItem <====> 动态数组的元素，两者一一对应
        SimpleAdapter saImageItems = new SimpleAdapter(this, //没什么解释
                lstImageItem,//数据来源
                R.layout.item_for_activity_main,//night_item的XML实现

                //动态数组与ImageItem对应的子项
                new String[] {"ItemImage","ItemText"},

                //ImageItem的XML文件里面的一个ImageView,两个TextView ID
                new int[] {R.id.ItemImage,R.id.ItemText});
        //添加并且显示
        mGridView.setAdapter(saImageItems);
        //添加消息处理
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intentStartActivity = new Intent(MainActivity.this, functionSort_Activity.class);
                Intent intentStartModelActivity = new Intent(MainActivity.this, ModelActivity.class);
                String kind = parent.getItemAtPosition(position).toString().split(",")[0].split("=")[1];
                String kindEnglish = scaleKind[position];

                if(kind.equals("電子桌秤") || kind.equals("電子台秤") || kind.equals("醫療秤")){
                    intentStartActivity.putExtra("kindSelected", kind);
                    intentStartActivity.putExtra("kindSelectedEnglish", kindEnglish);
                    MainActivity.this.startActivity(intentStartActivity);
                }
                else{
                    intentStartModelActivity.putExtra("kindSelected", kind);
                    intentStartModelActivity.putExtra("kindSelectedEnglish", kindEnglish);
                    MainActivity.this.startActivity(intentStartModelActivity);
                }

            }
            }
        );

    }// the end of onCreate

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
