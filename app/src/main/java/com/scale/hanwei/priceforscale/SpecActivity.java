package com.scale.hanwei.priceforscale;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class SpecActivity extends ActionBarActivity {

    TextView mTextViewForModel;
    private String mStringForModel, kindUsing, kindUsingEnglish;
    Button ImagePick;
    ImageView Image;

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
            kindUsing = extras.getString("kindUsing");
            kindUsingEnglish = extras.getString("kindUsingEnglish");
        }

        mTextViewForModel = (TextView) findViewById(R.id.textView);
        mTextViewForModel.setText(mStringForModel);

        ActionBar actionBar = this.getSupportActionBar(); //設標題
        actionBar.setTitle(kindUsing);
    }

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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

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
