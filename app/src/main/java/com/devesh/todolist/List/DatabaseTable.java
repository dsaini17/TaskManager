package com.devesh.todolist.List;

/**
 * Created by Devesh on 13-07-2016.
 */
public class DatabaseTable extends Table{

    public static final String TABLE_NAME = "Info";

    public static final String TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "+TABLE_NAME
            +LBR
            +Columns.ID + TYPE_INT_PK +COMMA
            +Columns.TASK + TYPE_TEXT +COMMA
            +Columns.DATE + TYPE_TEXT +COMMA
            +Columns.PRIORITY + TYPE_INT +COMMA
            +Columns.DONE + TYPE_INT
            +RBR
            +";";

    public interface Columns{
        String ID = "id";
        String TASK = "todotask";
        String DATE = "date";
        String PRIORITY = "priority";
        String DONE = "done";
    }

}
