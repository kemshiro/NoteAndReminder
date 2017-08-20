package com.k3mshiro.knotes.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.k3mshiro.knotes.dao.Constant;

/**
 * Created by k3mshiro on 7/24/17.
 */

public class Database extends SQLiteOpenHelper {

    public Database(Context context) {
        super(context, Constant.DB_NAME, null, Constant.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlCommand = "CREATE TABLE " + Constant.TABLE_NOTE
                + " ( " + Constant.COLUMN_NOTE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Constant.COLUMN_NOTE_TITLE + " TEXT, "
                + Constant.COLUMN_NOTE_DATE + " TEXT, "
                + Constant.COLUMN_NOTE_CONTENT + " TEXT, "
                + Constant.COLUMN_NOTE_COLOR + " TEXT ); ";

        db.execSQL(sqlCommand);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sqlCommand = "DROP TABLE IF EXISTS " + Constant.TABLE_NOTE;
        db.execSQL(sqlCommand);
        onCreate(db);
    }
}
