package com.k3mshiro.knotes.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import com.k3mshiro.knotes.database.Database;
import com.k3mshiro.knotes.dto.NoteDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by k3mshiro on 7/24/17.
 */

public class NoteDAO {

    private SQLiteDatabase mSQLiteDB;
    private Database db;
    private Context mContext;

    public NoteDAO(Context mContext) {
        db = new Database(mContext);
    }


    public void openDatabase() {
        mSQLiteDB = db.getWritableDatabase();
    }

    public void closeDatabase() {
        db.close();
    }

    public boolean createNewNote(NoteDTO newNote) {

        ContentValues contentValues = new ContentValues();

        contentValues.put(Constant.COLUMN_NOTE_TITLE, newNote.getTitle().toString());
        contentValues.put(Constant.COLUMN_NOTE_DATE, newNote.getDate().toString());
        contentValues.put(Constant.COLUMN_NOTE_CONTENT, newNote.getContent().toString());
        contentValues.put(Constant.COLUMN_NOTE_COLOR, newNote.getColor().toString());

        long idNhanVien = mSQLiteDB.insert(Constant.TABLE_NOTE, null, contentValues);

        return idNhanVien != 0;
    }


    public List<NoteDTO> getListNotes() {

        List<NoteDTO> listNoteDTOs = new ArrayList<>();
        String sqlCommand = "SELECT * FROM " + Constant.TABLE_NOTE;
        Cursor cursor = mSQLiteDB.rawQuery(sqlCommand, null);
        int idIndex = cursor.getColumnIndex(Constant.COLUMN_NOTE_ID);
        int titleIndex = cursor.getColumnIndex(Constant.COLUMN_NOTE_TITLE);
        int dateIndex = cursor.getColumnIndex(Constant.COLUMN_NOTE_DATE);
        int contentIndex = cursor.getColumnIndex(Constant.COLUMN_NOTE_CONTENT);
        int colorIndex = cursor.getColumnIndex(Constant.COLUMN_NOTE_COLOR);

        cursor.moveToFirst();

        while (cursor.isAfterLast() == false) {
            int id = cursor.getInt(idIndex);
            String title = cursor.getString(titleIndex);
            String date = cursor.getString(dateIndex);
            String content = cursor.getString(contentIndex);
            String color = cursor.getString(colorIndex);
            NoteDTO newNote = new NoteDTO(id, title, date, content, color);

            listNoteDTOs.add(newNote);

            cursor.moveToNext();
        }


        return listNoteDTOs;
    }

    public boolean deleteNote(NoteDTO deletedNote) {

        int result = mSQLiteDB.delete(Constant.TABLE_NOTE, Constant.COLUMN_NOTE_ID + " = " + deletedNote.getId(),
                null);


        return result != 0;
    }

    public boolean editNote(NoteDTO editedNote) {

        ContentValues contentValues = new ContentValues();

        contentValues.put(Constant.COLUMN_NOTE_TITLE, editedNote.getTitle());
        contentValues.put(Constant.COLUMN_NOTE_DATE, editedNote.getDate());
        contentValues.put(Constant.COLUMN_NOTE_CONTENT, editedNote.getContent());
        contentValues.put(Constant.COLUMN_NOTE_COLOR, editedNote.getColor());

        int result = mSQLiteDB.update(Constant.TABLE_NOTE,
                contentValues,
                Constant.COLUMN_NOTE_ID + " = " + editedNote.getId(),
                null);

        return result != 0;
    }
}
