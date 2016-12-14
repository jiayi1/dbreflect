package com.android.refrect;

import android.app.Activity;
import android.content.ContentValues;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.refrect.db.UserDB;

import java.util.ArrayList;

public class MainActivity extends Activity implements View.OnClickListener {

    private UserDB db;
    private MyAdapter mAdapter;
    private ArrayList<UserDB.Entity> list ;

    private EditText inputName;
    private EditText inputAddr;
    private EditText et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView tv = (TextView) findViewById(R.id.text);
        TextView add = (TextView) findViewById(R.id.btn2);
        final TextView del= (TextView) findViewById(R.id.del);
        TextView update = (TextView) findViewById(R.id.update);
        tv.setOnClickListener(this);
        add.setOnClickListener(this);
        del.setOnClickListener(this);
        update.setOnClickListener(this);

        ListView lv = (ListView) findViewById(R.id.listview);
        mAdapter = new MyAdapter();
        lv.setAdapter(mAdapter);

        et = (EditText) findViewById(R.id.et);
        inputName = (EditText) findViewById(R.id.et_name);
        inputAddr = (EditText) findViewById(R.id.et_addr);
    }

    private void update() {
        checkDb();
        String name = inputName.getText().toString().trim();
        String addr = inputAddr.getText().toString().trim();
        if (!name.isEmpty() && (!addr.isEmpty())) {
            ContentValues values = new ContentValues();
            values.put("addr",addr);
            int count = db.update(UserDB.Entity.class,values,name);
            if(count > 0){
                query();
            }
        }else {
            Toast.makeText(this,"姓名和地点都不能为空",Toast.LENGTH_SHORT).show();
        }
    }

    private void del() {
        checkDb();
        int count  = db.del(UserDB.Entity.class,et.getText().toString().replace(" ","").trim());
        if(count > 0){
            query();
        }
    }

    private void query() {
        checkDb();
        list = db.queryAll();
        mAdapter.notifyDataSetChanged();
    }

    private void testFun() {
        checkDb();
        String name = inputName.getText().toString().trim();
        String addr = inputAddr.getText().toString().trim();
        if (!name.isEmpty() && (!addr.isEmpty())) {
            db.insert(name,addr);
        }else {
            Toast.makeText(this,"姓名和地点都不能为空",Toast.LENGTH_SHORT).show();
        }
    }

    private void checkDb(){
        if(db == null){
            db = new UserDB();
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.text:
                testFun();
                break;
            case R.id.btn2:
                query();
                break;
            case R.id.del:
                del();
                break;
            case R.id.update:
                update();
                break;
        }
    }

    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            if(list != null ){
                return list.size();
            }
            return 0;
        }

        @Override
        public Object getItem(int position) {
            if(list != null ){
                return list.get(position);
            }
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Holder holder = null;
            if(convertView == null){
                holder = new Holder();
                convertView = getLayoutInflater().inflate(R.layout.item,null);
                holder.tv = (TextView) convertView.findViewById(R.id.item_tv);
                convertView.setTag(holder);
            }else {
                holder = (Holder) convertView.getTag();
            }
            UserDB.Entity enti = list.get(position);
            if(enti != null){
                holder.tv.setText(enti.useName+" , "+enti.addr);
            }
            return convertView;
        }
        class Holder {
            TextView tv;
        }
    }

}
