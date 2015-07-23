package com.scale.hanwei.priceforscale;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class ModelDataSource {
    // data field
    private MySQLiteHelper dbHelper;
    private SQLiteDatabase database;

    public static final String COLUMN_MODEL = "model";
    public static final String COLUMN_ID = "_id";

    private String[] allColumns_model = { COLUMN_ID,
            COLUMN_MODEL };

    public ModelDataSource(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Model createModel(String model,String tableName) {
        Cursor cursor;
        ContentValues values = new ContentValues();

        String CorrectTableName = "[" + tableName + "]";

        values.put(COLUMN_MODEL, model);

        long insertId = database.insert(CorrectTableName, null,
                values);

        if(!(insertId == -1)) {

                cursor = database.query(CorrectTableName,
                        allColumns_model, COLUMN_ID + " = " + insertId, null,
                        null, null, null);
            cursor.moveToFirst();
            Model newModel = cursorToModel(cursor);
            cursor.close();
            return newModel;
        }else {
            Model errorModel = new Model();
            errorModel.setModel("重覆資料輸入");
            return errorModel;
        }// the end of else
    }// the end of  create Model

    //刪除物件
    public void deleteModel(Model model,String tableName) {
        long id = model.getId();
        String CorrectTableName = "[" + tableName + "]";
        database.delete(CorrectTableName, COLUMN_ID
                + " = " + id, null);
    }

    public List<Model> getAllModels(String tableName) {
        Cursor cursor;
        List<Model> models = new ArrayList<Model>();
        String CorrectTableName = "[" + tableName + "]";
        cursor = database.query(CorrectTableName,
                    allColumns_model, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Model model = cursorToModel(cursor);
            models.add(model);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return models;
    }

    private Model cursorToModel(Cursor cursor) {
        Model model = new Model();
        model.setId(cursor.getLong(0));
        model.setModel(cursor.getString(1));
        return model;
    }

    //新創造Table
    public void createTableInDatabase(String TABLE_NAME){
        // Database creation sql statement
        final String DATABASE_CREATE = "create table if not exists"
                + TABLE_NAME + "(" + COLUMN_ID
                + " integer primary key autoincrement, " + COLUMN_MODEL
                + " text unique);";
        database.execSQL(DATABASE_CREATE);
        //新增Default物件
        ContentValues values = new ContentValues();
    }

    //刪除Table
    public void deleteTableInDatabase(String tableName){
        String CorrectTableName = "[" + tableName + "]";
        database.execSQL("DROP TABLE IF EXISTS " + CorrectTableName);
    }

}
