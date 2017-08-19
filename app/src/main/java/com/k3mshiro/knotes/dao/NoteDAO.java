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

        contentValues.put(Database.COLUMN_NOTE_TITLE, newNote.getTitle().toString());
        contentValues.put(Database.COLUMN_NOTE_DATE, newNote.getDate().toString());
        contentValues.put(Database.COLUMN_NOTE_CONTENT, newNote.getContent().toString());
        contentValues.put(Database.COLUMN_NOTE_COLOR, newNote.getColor().toString());

        long idNhanVien = mSQLiteDB.insert(Database.TABLE_NOTE, null, contentValues);

        return idNhanVien != 0;
    }


    public List<NoteDTO> getListNotes() {

        List<NoteDTO> listNoteDTOs = new ArrayList<>();
        String sqlCommand = "SELECT * FROM " + Database.TABLE_NOTE;
        Cursor cursor = mSQLiteDB.rawQuery(sqlCommand, null);
        int idIndex = cursor.getColumnIndex(Database.COLUMN_NOTE_ID);
        int titleIndex = cursor.getColumnIndex(Database.COLUMN_NOTE_TITLE);
        int dateIndex = cursor.getColumnIndex(Database.COLUMN_NOTE_DATE);
        int contentIndex = cursor.getColumnIndex(Database.COLUMN_NOTE_CONTENT);
        int colorIndex = cursor.getColumnIndex(Database.COLUMN_NOTE_COLOR);

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

        int result = mSQLiteDB.delete(Database.TABLE_NOTE, Database.COLUMN_NOTE_ID + " = " + deletedNote.getId(),
                null);


        return result != 0;
    }

    public boolean editNote(NoteDTO editedNote) {

        ContentValues contentValues = new ContentValues();

        contentValues.put(Database.COLUMN_NOTE_TITLE, editedNote.getTitle());
        contentValues.put(Database.COLUMN_NOTE_DATE, editedNote.getDate());
        contentValues.put(Database.COLUMN_NOTE_CONTENT, editedNote.getContent());
        contentValues.put(Database.COLUMN_NOTE_COLOR, editedNote.getColor());

        int result = mSQLiteDB.update(Database.TABLE_NOTE,
                contentValues,
                Database.COLUMN_NOTE_ID + " = " + editedNote.getId(),
                null);

        return result != 0;
    }
}
