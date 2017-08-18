package com.k3mshiro.knotes.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import com.k3mshiro.knotes.dto.NoteDTO;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by k3mshiro on 8/18/17.
 */

public class NoteDAO {
    public static final String DB_PATH = Environment.getDataDirectory()
            + "/data/com.k3mshiro.knotes/databases";

    public static final String DB_NAME = "NoteManagement";
    public static final String TAG = NoteDAO.class.getName();
    public static final String TABLE_NAME = "Note";
    public static final String COLUMN_NOTE_TITLE = "title";
    public static final String COLUMN_NOTE_DATE = "date";
    public static final String COLUMN_NOTE_CONTENT = "content";
    public static final String COLUMN_NOTE_COLOR = "color";

    public Context mContext;
    public SQLiteDatabase mSQLiteDB;

    public NoteDAO(Context mContext) {
        this.mContext = mContext;
        copyDatabase();
    }

    public void copyDatabase() {
        new File(DB_PATH).mkdir();
        try {
            File file = new File(DB_PATH + "/" + DB_NAME);
            if (file.exists()) file.delete();

            InputStream input = mContext.getAssets().open(DB_NAME);

            file.createNewFile();
            FileOutputStream output = new FileOutputStream(file);

            int len;
            byte buff[] = new byte[1024];
            while ((len = input.read(buff)) > 0) {
                output.write(buff, 0, len);
            }
            output.close();
            input.close();
            Log.i(TAG, "Copy DB is done");
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

    public void openDB() {
        if (mSQLiteDB == null || !mSQLiteDB.isOpen()) {
            mSQLiteDB = SQLiteDatabase.openDatabase(DB_PATH + "/" + DB_NAME,
                    null,
                    SQLiteDatabase.OPEN_READWRITE);
        }
    }

    public void closeDB() {
        if (mSQLiteDB != null && mSQLiteDB.isOpen()) {
            mSQLiteDB.close();
        }
    }

    public boolean insert(String title, String date, String content, String color) {
        openDB();
        ContentValues values = new ContentValues();

        values.put(COLUMN_NOTE_TITLE, title);
        values.put(COLUMN_NOTE_DATE, date);
        values.put(COLUMN_NOTE_CONTENT, content);
        values.put(COLUMN_NOTE_COLOR, color);


        long result = mSQLiteDB.insert(TABLE_NAME, null, values);
        closeDB();
        return result > -1;
    }

    public List<NoteDTO> getListNotes() {
        openDB();

        List<NoteDTO> listNoteDTOs = new ArrayList<>();
        String sqlCommand = "SELECT * FROM " + TABLE_NAME;
        Cursor cursor = mSQLiteDB.rawQuery(sqlCommand, null);
        int titleIndex = cursor.getColumnIndex(COLUMN_NOTE_TITLE);
        int dateIndex = cursor.getColumnIndex(COLUMN_NOTE_DATE);
        int contentIndex = cursor.getColumnIndex(COLUMN_NOTE_CONTENT);
        int colorIndex = cursor.getColumnIndex(COLUMN_NOTE_COLOR);

        cursor.moveToFirst();

        while (cursor.isAfterLast() == false) {
            String title = cursor.getString(titleIndex);
            String date = cursor.getString(dateIndex);
            String content = cursor.getString(contentIndex);
            String color = cursor.getString(colorIndex);
            NoteDTO newNote = new NoteDTO(title, date, content, color);

            listNoteDTOs.add(newNote);

            cursor.moveToNext();
        }

        closeDB();

        for (int i = 0; i < listNoteDTOs.size(); i++) {
            Log.i(TAG, listNoteDTOs.get(i).getContent());
        }


        return listNoteDTOs;
    }
}