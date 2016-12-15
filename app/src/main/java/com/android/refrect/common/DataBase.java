package com.android.refrect.common;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import com.android.refrect.App;
import com.android.refrect.model.BaseEntity;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteOpenHelper;

import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * Created by yulong.liu on 2016/12/14.
 */

public abstract class DataBase {

    private ArrayList<Class<? extends BaseEntity>> klasss = getTables();

    protected abstract ArrayList<Class<? extends BaseEntity>> getTables();

    protected SQLiteOpenHelper sqlDataBase;

    public DataBase() {
        sqlDataBase = new OpenDataBase();
    }

    class OpenDataBase extends SQLiteOpenHelper {


        public OpenDataBase() {
            super(App.appContext, getDBName(), null, getVersion());
            SQLiteDatabase.loadLibs(App.appContext);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            createTables(db);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            upgrageDB(db, oldVersion, newVersion);
        }
    }

    public abstract int getVersion();

    public abstract String getDBName();

    private void createTables(SQLiteDatabase db) {
        for (Class<?> klass : klasss) {
            StringBuffer sb = new StringBuffer();
            sb.append("create table ");
            if (klass.isAnnotationPresent(Table.class)) {
                String TableName = klass.getAnnotation(Table.class).name();
                sb.append(TableName);
            }
            sb.append(" (");
            sb.append("_id integer primary key autoincrement,");
            Field[] fields = klass.getDeclaredFields();
            for (Field field : fields) {
                if (field.isAnnotationPresent(Colume.class)) {
                    if (field.getAnnotation(Colume.class).name().equals("_id")) {
                        continue;
                    }
                    field.setAccessible(true);
                    sb.append(field.getAnnotation(Colume.class).name());
                    if (field.getType() == String.class) {
                        sb.append(" text");
                    } else if (field.getType() == Integer.class) {
                        sb.append(" Integer");
                    }
                    sb.append(",");
                }
            }
            if (sb.charAt(sb.length() - 1) == ',') {
                sb.deleteCharAt(sb.length() - 1);
            }
            sb.append(");");
            Log.i("info", sb.toString());
            db.execSQL(sb.toString());

        }
    }

    protected <T extends BaseEntity> ArrayList<T> query(Class<T> klass) {
        ArrayList<T> list = new ArrayList<T>();
        String tabName = "";
        if (klass.isAnnotationPresent(Table.class)) {
            tabName = klass.getAnnotation(Table.class).name();
        }
        Field[] fields = klass.getFields();
        if (!tabName.isEmpty()) {
            Cursor cursor = sqlDataBase.getReadableDatabase("123456").query(tabName, null, null, null, null, null, null, null);
            while (cursor.moveToNext()) {
                try {
                    T t = klass.newInstance();
                    for (Field field : fields) {
                        if (field.isAnnotationPresent(Colume.class)) {
                            field.setAccessible(true);
                            int columeIndex = cursor.getColumnIndex(field.getAnnotation(Colume.class).name());
                            if (field.getType() == String.class) {
                                field.set(t, cursor.getString(columeIndex));
                            }
                        }
                    }
                    list.add(t);
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            Log.i("info", "cursor:" + cursor.getCount());
            return list;

        }
        return list;
    }

    public  <T extends BaseEntity> int del(Class<T> klass, String selectSql){
        int  isDelSuccessCount = 0;
        String tabName = "";
        if (klass.isAnnotationPresent(Table.class)) {
            tabName = klass.getAnnotation(Table.class).name();
        }
        isDelSuccessCount = sqlDataBase.getWritableDatabase("123456").delete(tabName,"name = ?",new String[]{selectSql}) ;
        return isDelSuccessCount;
    }

    public <T extends BaseEntity> int update(Class<T> klass,ContentValues values,String selectSql){
        int  isDelSuccessCount = 0;
        String tabName = "";
        if (klass.isAnnotationPresent(Table.class)) {
            tabName = klass.getAnnotation(Table.class).name();
        }
        isDelSuccessCount = sqlDataBase.getWritableDatabase("123456").update(tabName,values,"name = ?",new String[]{selectSql}) ;
        return isDelSuccessCount;
    }

    protected void upgrageDB(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
