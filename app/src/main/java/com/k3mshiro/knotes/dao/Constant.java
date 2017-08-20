package com.k3mshiro.knotes.dao;

/**
 * Created by k3mshiro on 8/19/17.
 */

public class Constant {
    /* CreateNoteFrg and EditNoteFrg */

    public static final CharSequence RED = "red";
    public static final CharSequence ORANGE = "orange";
    public static final CharSequence YELLOW = "yellow";
    public static final CharSequence GREEN = "green";
    public static final CharSequence BLUE = "blue";
    public static final CharSequence INDIGO = "indigo";
    public static final CharSequence VIOLET = "violet";

    /* Database*/
    public static final String DB_NAME = "NoteManagement";
    public static final int DB_VERSION = 2;
    public static final String TABLE_NOTE = "Note";

    public static final String COLUMN_NOTE_ID = "id";
    public static final String COLUMN_NOTE_TITLE = "title";
    public static final String COLUMN_NOTE_DATE = "date";
    public static final String COLUMN_NOTE_CONTENT = "content";
    public static final String COLUMN_NOTE_COLOR = "color";


    /*Main Activity*/
    public static final String KEY_EDIT_NOTE = "KEY_EDIT_NOTE";
}
