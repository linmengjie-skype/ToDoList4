package com.example.todolist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class TaskActivity extends AppCompatActivity {

    private ArrayList<String> mData = null;
    private ListViewAdapter mAdapter = null;
    private Context mContext;
    private ListView listView;
    private EditText editText;
    private Button btn;
    private DataBaseHelper dbHelper;
    private SQLiteDatabase db;
    private ArrayList<String> taskList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        mContext = TaskActivity.this;
        dbHelper = new DataBaseHelper(mContext);
        taskList=taskList = new ArrayList<>();

        editText = (EditText) findViewById(R.id.edit_text);
        btn = (Button) findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db=dbHelper.getWritableDatabase();
                switch (v.getId()){
                    case R.id.btn:
                        ContentValues values=new ContentValues();
                        values.put("TaskName",editText.getText().toString());
                }



            }
        });


        listView = (ListView) findViewById(R.id.list_view);
        mData = new ArrayList<String>();
        mData=getTaskList();
        mAdapter = new ListViewAdapter(mData, mContext);
        listView.setAdapter(mAdapter);


    }
    public ArrayList<String> getTaskList() {
        taskList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query( "task.db", new String[]{"TaskName"}, null, null, null, null, null);
        while (cursor.moveToNext()) {
            int index = cursor.getColumnIndex("TaskName");
            taskList.add(cursor.getString(index));
        }
        cursor.close();
        db.close();
        return taskList;
    }
    }


