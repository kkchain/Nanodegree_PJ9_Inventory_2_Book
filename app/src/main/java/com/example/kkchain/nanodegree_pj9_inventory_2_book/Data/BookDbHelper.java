package com.example.kkchain.nanodegree_pj9_inventory_2_book.Data;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.kkchain.nanodegree_pj9_inventory_2_book.Data.BookContract.BookEntry;

/**
 * Database helper for Books store app. Manages database creation and version management
 */

public class BookDbHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = BookDbHelper.class.getSimpleName();

    /**
     * Name of the database file
     */
    private static final String DATABASE_NAME = "store.db";

    /**
     * Database version, if the database schema change, increment the database version
     */
    private static final int DATABASE_VERSION = 1;

    /**
     * Construct a new instance of {@link BookDbHelper}.
     *
     * @param context of the app
     */
    public BookDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * This is called when the database is created for the first time.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_BOOKS_TABLE = "CREATE TABLE " + BookEntry.TABLE_NAME + "("
                + BookEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + BookEntry.COLUMN_BOOK_NAME + " TEXT NOT NULL,"
                + BookEntry.COLUMN_BOOK_PRICE + " INTEGER NOT NULL,"
                + BookEntry.COLUMN_BOOK_QUANTITY + " INTEGER NOT NULL DEFAULT 0,"
                + BookEntry.COLUMN_BOOK_SUPPLIER_NAME + " TEXT NOT NULL,"
                + BookEntry.COLUMN_BOOK_SUPPLIER_PHONE_NO + " TEXT NOT NULL);";

        // Execute the SQL statement
        db.execSQL(SQL_CREATE_BOOKS_TABLE);
    }

    /**
     * This is called when the database need to be upgraded
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // The database is still at version 1, so there's nothing to do here
    }
}