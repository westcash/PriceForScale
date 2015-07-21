package com.scale.hanwei.priceforscale;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;


public class functionSort_Activity extends ActionBarActivity {

    String kindUsing, kindUsingEnglish;
    ListView mListView;
    ArrayAdapter<String> adapter;
    String[] function1 = { "計重", "計數", "計價"};
    String[] function1English = { "weight", "number", "price"};
    String[] function2 = { "多功能輪椅台秤", "身高體重秤", "電子體重秤", "電子座椅秤", "嬰兒秤", "動物秤"};
    String[] function2English = { "WheelChairScale", "HeightWeight", "weight", "chair", "baby", "animal"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_function_sort);

        Bundle extras = getIntent().getExtras();
        if (extras != null){
            kindUsing = extras.getString("kindSelected");
            kindUsingEnglish = extras.getString("kindSelectedEnglish");
        }


        ActionBar actionBar = this.getSupportActionBar(); //設標題
        actionBar.setTitle(kindUsing);

        mListView = (ListView) findViewById(R.id.listView);
        if(kindUsing.equals("醫療秤"))
            adapter = new ArrayAdapter<String>(this,
                    R.layout.item_style_for_activity_function_sort, function2);
        else
            adapter = new ArrayAdapter<String>(this,
                    R.layout.item_style_for_activity_function_sort, function1);

        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intentStartModelActivity = new Intent(functionSort_Activity.this, ModelActivity.class);
                String selected = (String) parent.getItemAtPosition(position);
                String kind = kindUsing + "  -  "+ selected;
                String kindEnglish;
                if(kindUsing.equals("醫療秤"))
                    kindEnglish = kindUsingEnglish + function2English[position];
                else
                    kindEnglish = kindUsingEnglish + function1English[position];
                intentStartModelActivity.putExtra("kindSelected", kind);
                intentStartModelActivity.putExtra("kindSelectedEnglish", kindEnglish);
                functionSort_Activity.this.startActivity(intentStartModelActivity);
            }
        });

    }// the end of onCreate


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_function_sort, menu);
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
