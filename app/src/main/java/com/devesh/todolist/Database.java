package com.devesh.todolist;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.devesh.todolist.List.DatabaseTable;
import com.devesh.todolist.List.Table;

/**
 * Created by Devesh on 13-07-2016.
 */
public class Database extends SQLiteOpenHelper {

    public static final String DATABASE_NAME="myDatabase";
    public static final int DB_VERSION=1;
    private static Database myDBopener = null;

    public static SQLiteDatabase openReadable(Context c){
        if(myDBopener==null){
            myDBopener = new Database(c);
        }
        return myDBopener.getReadableDatabase();
    }

    public static SQLiteDatabase openWriteable(Context c){
        if(myDBopener==null){
            myDBopener = new Database(c);
        }
        return myDBopener.getWritableDatabase();
    }

    public Database(Context context) {
        super(context, DATABASE_NAME , null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DatabaseTable.TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


}

