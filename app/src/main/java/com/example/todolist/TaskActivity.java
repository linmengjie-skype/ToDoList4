package com.example.todolist;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Calendar;

public class TaskActivity extends AppCompatActivity {

    private ArrayList<String> mData = null;
    private ListViewAdapter mAdapter = null;
    private Context mContext;
    private ListView listView;
    private EditText editText;
    private Button btn;
    private DataBaseHelper dbHelper;
    private SQLiteDatabase db;
    private TextView time;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        mContext = TaskActivity.this;
        dbHelper = new DataBaseHelper(getApplicationContext());
        time=(TextView)findViewById(R.id.time);


        editText = (EditText) findViewById(R.id.edit_text);
        btn = (Button) findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("TaskName", editText.getText().toString());
                db.insert("task", null, values);
            }

        });
        listView = (ListView) findViewById(R.id.list_view);
        initListNoteListener();
    }

    protected void onResume() {
        super.onResume();
        mData = getTaskList();
        mAdapter = new ListViewAdapter(mData, mContext);
        listView.setAdapter(mAdapter);
    }




    public ArrayList<String> getTaskList() {
        ArrayList<String> taskList = new ArrayList<String>();
        db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("task", new String[]{"TaskName"}, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            while (cursor.moveToNext()) {
                int index = cursor.getColumnIndex("TaskName");
                taskList.add(cursor.getString(index));
            }
        }
        cursor.close();
        db.close();
        return taskList;
    }
    private  void initListNoteListener() {
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, final long id) {
                new AlertDialog.Builder(TaskActivity.this)
                        .setTitle("提示框")
                        .setMessage("确认删除该笔记？？")
                        .setPositiveButton("确定",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        dbHelper.deleteTask((int) id);

                                        //删除后刷新列表
                                        TaskActivity.this.onResume();
                                        Toast.makeText(
                                                TaskActivity.this,
                                                "删除成功！！",
                                                Toast.LENGTH_LONG)
                                                .show();
                                    }
                                }).setNegativeButton("取消", null).show();
                return true;
            }
        });
    }


}

