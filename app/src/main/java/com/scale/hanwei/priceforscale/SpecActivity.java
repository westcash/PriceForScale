package com.scale.hanwei.priceforscale;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class SpecActivity extends ActionBarActivity {

    TextView mTextViewForModel;
    private String mStringForModel, kindUsing, kindUsingEnglish,modelName;
    Button ImagePick;// Used to select image now invisible
    ImageView Image;
    private ModelDataSource mModelDataSource;
    ListView mListViewForSpec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spec);

        ImagePick = (Button) findViewById(R.id.button);
        ImagePick.setOnClickListener(new View.OnClickListener(){
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                // 建立 "選擇檔案 Action" 的 Intent
                Intent intent = new Intent( Intent.ACTION_PICK );

                // 過濾檔案格式
                intent.setType( "image/*" );

                // 建立 "檔案選擇器" 的 Intent  (第二個參數: 選擇器的標題)
                Intent destIntent = Intent.createChooser( intent, "選擇檔案" );

                // 切換到檔案選擇器 (它的處理結果, 會觸發 onActivityResult 事件)
                startActivityForResult( destIntent, 0 );
            }
        });

        Bundle extras = getIntent().getExtras();
        if (extras != null){
            mStringForModel = "型號:" + extras.getString("ModelName");
            modelName = extras.getString("ModelName");
            kindUsing = extras.getString("kindUsing");
            kindUsingEnglish = extras.getString("kindUsingEnglish");
        }

        mListViewForSpec = (ListView) findViewById(R.id.mListViewForSpec);

        mModelDataSource = new ModelDataSource(this);
        mModelDataSource.open();//開啟資料庫
        mModelDataSource.createTableInDatabase( "[" + kindUsingEnglish + modelName + "]");

        List<Model> values = mModelDataSource.getAllModels(kindUsingEnglish + modelName);
         setupListView(values);

        mTextViewForModel = (TextView) findViewById(R.id.textView);
        mTextViewForModel.setText(mStringForModel);

        ActionBar actionBar = this.getSupportActionBar(); //設標題
        actionBar.setTitle(kindUsing);
    }// the end of on create

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_spec, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add_info) {
            dialogForNewString(kindUsingEnglish + modelName);
            return true;
        }

        if (id == R.id.action_add_spec_price) {
            dialogForSpecPrice(kindUsingEnglish + modelName);
            return true;
        }

        if (id == R.id.action_add_spec) {
            dialogForSpec(kindUsingEnglish + modelName);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupListView(List<Model> models){
        final ArrayAdapter<Model> adapter = new ArrayAdapter<Model>(this,
                R.layout.item_style_for_activity_model, models);
        mListViewForSpec.setAdapter(adapter);

        /*
        mListViewForSpec.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //選擇到的字串
                        String item = String.valueOf(parent.getItemAtPosition(position));
                        //Model model = (Model) parent.getItemAtPosition(position);

                        Intent intentStartSpecActivity = new Intent(ModelActivity.this,SpecActivity.class);
                        intentStartSpecActivity.putExtra("ModelName",item);
                        intentStartSpecActivity.putExtra("kindUsing",kindUsing);
                        intentStartSpecActivity.putExtra("kindUsingEnglish",kindUsingEnglish);
                        ModelActivity.this.startActivity(intentStartSpecActivity);
                    }//the end of onItemClick
                });
        */

        mListViewForSpec.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                        Model model = (Model) parent.getItemAtPosition(position);
                        dialogForDelete(model, kindUsingEnglish + modelName);
                        return true;
                    }
                }
        ); // the end of mListViewForProductCategory.setOnItemLongClickListener(

    }// the end of setupListView

    public void dialogForNewString(final String tableName) {
        AlertDialog.Builder editDialog = new AlertDialog.Builder(SpecActivity.this);
        editDialog.setTitle("--- Edit ---");
        //String newString;

        final EditText editText = new EditText(SpecActivity.this);
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
                    ArrayAdapter<Model> adapterListView = (ArrayAdapter<Model>) mListViewForSpec.getAdapter();

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
                ArrayAdapter<Model> adapterListView = (ArrayAdapter<Model>) mListViewForSpec.getAdapter();

                adapterListView.remove(model);
                adapterListView.notifyDataSetChanged();

                mModelDataSource.deleteModel(model, tableName);

            }
        });
        dialog.show();
    }//the end of dialogForDelete

    public void dialogForSpecPrice(final String tableName){
        LayoutInflater factory = LayoutInflater.from(this);

        final View textEntryView = factory.inflate(R.layout.custom_dialog_for_spec_price, null);

        final EditText MaxCapacity = (EditText) textEntryView.findViewById(R.id.editTextForMaxCapacity);
        final EditText MiniCapacity = (EditText) textEntryView.findViewById(R.id.editTextForMiniCapacity);
        final EditText Price = (EditText) textEntryView.findViewById(R.id.editTextForPrice);

        MaxCapacity.setHint("----------秤量----------");
        MiniCapacity.setHint("----------感量----------");
        Price.setHint("----------價錢----------");

        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("輸入資料").setView(textEntryView)
                .setPositiveButton("確定",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                 /* User clicked OK so do some stuff */

                                //點擊到新增分類的時候
                                //創建表格並將表格名稱加入products
                                Model model;
                                //新增項目至products
                                model = mModelDataSource.createModel(MaxCapacity.getText().toString() + "\n"+
                                                                       MiniCapacity.getText().toString() + "\n"+
                                                                       Price.getText().toString(), tableName);
                                //dataSource.createTableInDatabase(editText.getText().toString());//新增表格
                                if (!(model.getModel().equals("重覆資料輸入"))) {
                                    ArrayAdapter<Model> adapterListView = (ArrayAdapter<Model>) mListViewForSpec.getAdapter();

                                    //為tableName名稱的正確性
                                    adapterListView.add(model);
                                    adapterListView.notifyDataSetChanged();

                                }// the end of  if (!(product.getProduct().equals(""重覆資料輸入"")){
                                else {
                                    Toast.makeText(getApplicationContext(), "已有相同資料", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                .setNegativeButton("取消",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                /*
                            * User clicked cancel so do some stuff
                            */
                            }
                        });
        alert.show();

    }// the end of dialogForSpecPrice

    public void dialogForSpec(final String tableName){
        LayoutInflater factory = LayoutInflater.from(this);

        final View textEntryView = factory.inflate(R.layout.custom_dialog_for_spec, null);

        final EditText MaxCapacity = (EditText) textEntryView.findViewById(R.id.editTextForMaxCapacity);
        final EditText MiniCapacity = (EditText) textEntryView.findViewById(R.id.editTextForMiniCapacity);

        MaxCapacity.setHint("----------秤量----------");
        MiniCapacity.setHint("----------感量----------");

        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("輸入資料").setView(textEntryView)
                .setPositiveButton("確定",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                 /* User clicked OK so do some stuff */

                                //點擊到新增分類的時候
                                //創建表格並將表格名稱加入products
                                Model model;
                                //新增項目至products
                                model = mModelDataSource.createModel(MaxCapacity.getText().toString() + "\n"+
                                        MiniCapacity.getText().toString(), tableName);
                                //dataSource.createTableInDatabase(editText.getText().toString());//新增表格
                                if (!(model.getModel().equals("重覆資料輸入"))) {
                                    ArrayAdapter<Model> adapterListView = (ArrayAdapter<Model>) mListViewForSpec.getAdapter();

                                    //為tableName名稱的正確性
                                    adapterListView.add(model);
                                    adapterListView.notifyDataSetChanged();

                                }// the end of  if (!(product.getProduct().equals(""重覆資料輸入"")){
                                else {
                                    Toast.makeText(getApplicationContext(), "已有相同資料", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                .setNegativeButton("取消",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                /*
                            * User clicked cancel so do some stuff
                            */
                            }
                        });
        alert.show();

    }// the end of dialogForNewWork

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        // 有選擇檔案
        if ( resultCode == RESULT_OK )
        {
            // 取得檔案的 Uri
            Uri uri = data.getData();
            if( uri != null )
            {
                // 利用 Uri 顯示 ImageView 圖片
                Image = (ImageView)this.findViewById(R.id.imageView);
                Image.setImageURI( uri );

            }
            else
            {
                setTitle("無效的檔案路徑 !!");
            }
        }
        else
        {
            setTitle("取消選擇檔案 !!");
        }
    }

}
