package com.devesh.todolist;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.devesh.todolist.List.DatabaseTable;
import com.devesh.todolist.List.Table;
import com.devesh.todolist.Models.Task;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";
    public static final int REQUEST_CODE = 121;
    Button add, clear;
    ArrayList<Task> myList;
    ListView myView;
    ListViewAdapter viewAdapter;
    SQLiteDatabase myDatabase;

    TextView tv1,tv2,tv3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myList = new ArrayList<>();
        myDatabase = Database.openWriteable(this);

        myView = (ListView) findViewById(R.id.listView);
        add = (Button) findViewById(R.id.add);
        clear = (Button) findViewById(R.id.clear);
        viewAdapter = new ListViewAdapter(myList);
        myView.setAdapter(viewAdapter);
//        viewAdapter.notifyDataSetChanged();

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Input.class);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });



        myView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Log.d(TAG,"ON_ITEM_CLICKED");

          /* //     Log.d(TAG,"Checkbox " +checkBox.isChecked());
                if(checkBox.isChecked()) {
                    checkBox.setChecked(false);
                }else{
                    checkBox.setChecked(true);
                }
             //       Log.d(TAG,"Checkbox " +checkBox.isChecked());
*/

                    Task task = myList.get(position);
                    int uniqueID = task.getId();
                    ContentValues values = new ContentValues();
                    String[] args = {
                            String.valueOf(uniqueID)
                    };
                int state = (task.getDone()+1)%2;
                    values.put(DatabaseTable.Columns.DONE,state);
          //          Log.d(TAG,"UPDATE CALLED on "+task.getDone()+" to "+state+" at "+task.getId());
                    int r=  myDatabase.update(DatabaseTable.TABLE_NAME, values, DatabaseTable.Columns.ID + "="+uniqueID, null);
            //        Log.d(TAG,"UPDATE AFFECTED ROWS : - "+r);

                    performTask();
                }



        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               int rp = myDatabase.delete(DatabaseTable.TABLE_NAME,DatabaseTable.Columns.DONE+"=1",null);
                Log.d(TAG,"ROWS AFFECTED "+rp);
                performTask();
            }
        });


        performTask();

  }

    public void performTask(){

        myList.clear();

        String[] proj = {
                DatabaseTable.Columns.ID,
            DatabaseTable.Columns.TASK,
                DatabaseTable.Columns.DATE,
                DatabaseTable.Columns.PRIORITY,
                DatabaseTable.Columns.DONE
        };



        Cursor c = myDatabase.query(DatabaseTable.TABLE_NAME,proj,null,null,null,null,DatabaseTable.Columns.DATE+" ASC");
        Log.d(TAG,""+c.getCount());
      //  c.moveToFirst();
        while(c.moveToNext()){
            int id = c.getInt(c.getColumnIndexOrThrow(DatabaseTable.Columns.ID));
            String todo=c.getString(c.getColumnIndexOrThrow(DatabaseTable.Columns.TASK));
            String time=c.getString(c.getColumnIndexOrThrow(DatabaseTable.Columns.DATE));
            int pr=c.getInt(c.getColumnIndexOrThrow(DatabaseTable.Columns.PRIORITY));
            int state=c.getInt(c.getColumnIndexOrThrow(DatabaseTable.Columns.DONE));

            myList.add(new Task(id,todo,time,pr,state));
      //      Log.d(TAG,""+"HI");

        //    Log.d(TAG,""+"Done Status "+myList.get(myList.size()-1).getDone());
        }
    //    Log.d(TAG,""+myList.size());

        viewAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ((requestCode == REQUEST_CODE)&&(resultCode==RESULT_OK)) {

            String text = data.getStringExtra("TEXT");
            String time = data.getStringExtra("DATE");
            int rating = data.getIntExtra("RATING", 1);
            int state = data.getIntExtra("STATE", 0);


            ContentValues values = new ContentValues();
   //         values.put(DatabaseTable.Columns.ID,k);
            values.put(DatabaseTable.Columns.TASK,text);
            values.put(DatabaseTable.Columns.DATE,time);
            values.put(DatabaseTable.Columns.PRIORITY,rating);
            values.put(DatabaseTable.Columns.DONE,state);



            myDatabase = Database.openWriteable(this);

      //      Log.d(TAG,"Values to be inserted "+text+" "+time+" "+rating+" "+state);
            Long id = myDatabase.insert(DatabaseTable.TABLE_NAME,null,values);
      //      Log.d(TAG,"Values Inserted at "+id);

            performTask();
        }

    }


    public class ListViewAdapter extends BaseAdapter {

        public class Holder{
            TextView tv1,tv2,tv3;
            CheckBox checkBox;
        }

        ArrayList<Task> mList;

        public ListViewAdapter(ArrayList<Task> mList) {
            this.mList = mList;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = getLayoutInflater();

            Holder holder = new Holder();
            if(convertView==null) {
                convertView=inflater.inflate(R.layout.list_item, null);

                holder.tv1 = (TextView) convertView.findViewById(R.id.task);
                holder.tv2 = (TextView) convertView.findViewById(R.id.date);
                holder.tv3 = (TextView) convertView.findViewById(R.id.priority);
                holder.checkBox = (CheckBox) convertView.findViewById(R.id.checkbox);

                convertView.setTag(holder);
                Log.d("List View","Create");

            }
            else{
                Log.d("List View","Recycle");
                holder = (Holder) convertView.getTag();
            }

            Task task = mList.get(position);

            holder.tv1.setText(task.getTodotask());
            holder.tv2.setText(task.getDate());
            int pr = task.getPriority();
            switch (pr){
                case 1 : holder.tv3.setText("LOW");
                        holder.tv3.setTextColor(Color.YELLOW);
                    break;
                case 2 :  holder.tv3.setText("MEDIUM");
                    holder.tv3.setTextColor(Color.GREEN);
                    break;
                case 3 :  holder.tv3.setText("HIGH");
                    holder.tv3.setTextColor(Color.RED);
                    break;
            }
            if(task.getDone()==1){
                holder.tv1.setPaintFlags(holder.tv1.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                holder.tv2.setPaintFlags(holder.tv2.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                holder.tv3.setPaintFlags(holder.tv3.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                holder.checkBox.setChecked(true);
            }else {
                    holder.tv1.setPaintFlags(holder.tv1.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
                holder.tv2.setPaintFlags(holder.tv2.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
                holder.tv3.setPaintFlags(holder.tv3.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
                holder.checkBox.setChecked(false);
            }



            return convertView;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public Task getItem(int position) {
            return mList.get(position);
        }

        @Override
        public int getCount() {
            return mList.size();
        }
    }

}
