package com.scale.hanwei.priceforscale;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

public class ModelActivity extends ActionBarActivity {

    String kindUsing, kindUsingEnglish;
    ListView mListViewForModel;

    private ModelDataSource mModelDataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_model);

        Bundle extras = getIntent().getExtras();
        if (extras != null){
            kindUsing = extras.getString("kindSelected");
            kindUsingEnglish = extras.getString("kindSelectedEnglish");
        }

        mListViewForModel = (ListView) findViewById(R.id.mListVIewForModel);

        mModelDataSource = new ModelDataSource(this);
        mModelDataSource.open();//開啟資料庫
        mModelDataSource.createTableInDatabase( "[" + kindUsingEnglish + "]");

        List<Model> values = mModelDataSource.getAllModels(kindUsingEnglish);
        setupListView(values);

        ActionBar actionBar = this.getSupportActionBar(); //設標題
        actionBar.setTitle(kindUsing);

    }

    private void setupListView(List<Model> models){
        final ArrayAdapter<Model> adapter = new ArrayAdapter<Model>(this,
                R.layout.item_style_for_activity_model, models);
        mListViewForModel.setAdapter(adapter);

        mListViewForModel.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //選擇到的字串
                        String item = String.valueOf(parent.getItemAtPosition(position));
                        //Model model = (Model) parent.getItemAtPosition(position);

                        Intent intentStartSpecActivity = new Intent(ModelActivity.this,SpecActivity.class);
                        intentStartSpecActivity.putExtra("ModelName",item);
                        intentStartSpecActivity.putExtra("KindUsing",kindUsing);
                        intentStartSpecActivity.putExtra("kindUsingEnglish",kindUsingEnglish);
                        ModelActivity.this.startActivity(intentStartSpecActivity);
                    }//the end of onItemClick
                });


        mListViewForModel.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                        Model model = (Model) parent.getItemAtPosition(position);
                            dialogForDelete(model, kindUsingEnglish);
                        return true;
                    }
                }
        ); // the end of mListViewForProductCategory.setOnItemLongClickListener(

    }// the end of setupListView

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_model, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add_model) {
            dialogForNewString(kindUsingEnglish);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }// the end of onOptionsItem....

    public void dialogForNewString(final String tableName) {
        AlertDialog.Builder editDialog = new AlertDialog.Builder(ModelActivity.this);
        editDialog.setTitle("--- Edit ---");
        //String newString;

        final EditText editText = new EditText(ModelActivity.this);
        editDialog.setView(editText);

        editDialog.setPositiveButton(R.string.buttonOK, new DialogInterface.OnClickListener() {
            // do something when the button is clicked
            public void onClick(DialogInterface arg0, int arg1) {
                //點擊到新增分類的時候
                //創建表格並將表格名稱加入products
                Model model;
                //新增項目至products
                model = mModelDataSource.createModel(editText.getText().toString(), tableName);
                //dataSource.createTableInDatabase(editText.getText().toString());//新增表格
                if (!(model.getModel().equals("重覆資料輸入"))) {
                    ArrayAdapter<Model> adapterListView = (ArrayAdapter<Model>) mListViewForModel.getAdapter();

                    //為tableName名稱的正確性
                    adapterListView.add(model);
                    adapterListView.notifyDataSetChanged();

                }// the end of  if (!(product.getProduct().equals(""重覆資料輸入"")){
                else {
                    Toast.makeText(getApplicationContext(), "已有相同資料", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //取消
        editDialog.setNegativeButton(R.string.buttonCancel, new DialogInterface.OnClickListener() {
            // do something when the button is clicked
            public void onClick(DialogInterface arg0, int arg1) {
                //...
            }
        });

        editDialog.show();
    }// the end of dialogForNewString

    protected void dialogForDelete(final Model model, final String tableName ){

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Title"); //設定dialog 的title顯示內容
        dialog.setIcon(android.R.drawable.ic_dialog_alert);//設定dialog 的ICON
        dialog.setCancelable(true); //關閉 Android 系統的主要功能鍵(menu,home等...)
        dialog.setPositiveButton("刪除", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // 按下"刪除"以後要做的事情
                //刪除存在list的表格名稱 和 database裡面
                ArrayAdapter<Model> adapterListView = (ArrayAdapter<Model>) mListViewForModel.getAdapter();

                adapterListView.remove(model);
                adapterListView.notifyDataSetChanged();

                mModelDataSource.deleteModel(model, tableName);

            }
        });
        dialog.show();
    }//the end of dialogForDelete

}// the end of ModelActivity
