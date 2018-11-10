package com.example.kkchain.nanodegree_pj9_inventory_2_book.Data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.example.kkchain.nanodegree_pj9_inventory_2_book.Data.BookContract.BookEntry;


public class BookProvider extends ContentProvider {
    /**
     * Tag for the log message
     */
    public static final String LOG_TAG = BookProvider.class.getSimpleName();

    /**
     * URI matcher code for the content URI for the books table
     */
    private static final int BOOKS = 100;

    /**
     * URI matcher code for the content URI for a single book in the books table
     */
    private static final int BOOK_ID = 101;

    /**
     * UriMatcher object to match a content URI to a corresponding code.
     * The input passed into the constructor represents the code to return for the root URI.
     * Use NO_MATCH as the input for this case
     */
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    // Static initializer. This is run the first time anything is called from the class.
    static {
        // The content URI of the form "content://com.example.kkchain.nanodegree_pj9_inventory_2_book/books"
        // will map to the integer code {@link #BOOKs}.
        // This URI is used to provide access to MULTIPLE rows of the books table.
        sUriMatcher.addURI(BookContract.CONTENT_AUTHORITY, BookContract.PATH_BOOKS, BOOKS);

        // This URI is used to provide access to ONE single row of the books table.
        // In this case, the "#" will be used to substitute for an integer.
        sUriMatcher.addURI(BookContract.CONTENT_AUTHORITY, BookContract.PATH_BOOKS + "/#", BOOK_ID);

    }

    /**
     * Database helper object
     */
    private BookDbHelper mDbHelper;

    @Override
    public boolean onCreate() {
        mDbHelper = new BookDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        // Get readable database
        SQLiteDatabase database = mDbHelper.getReadableDatabase();

        // This cursor will hold the result of th query.
        Cursor cursor;

        // Figure out if the URI matcher can match the URI tp a specific code
        int match = sUriMatcher.match(uri);
        switch (match) {
            case BOOKS:
                // Query the books table directly. The cursor could contain multiple rows of the books table.
                cursor = database.query(BookEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case BOOK_ID:
                // Extract out the ID from the URI.
                // For every "?" in the selection, we need to have an element in the
                // selection arguments that will fill in the "?".
                selection = BookEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};

                // This will perform a query on the books table where the _id equal to return
                // a Cursor containing that row of the table.
                cursor = database.query(BookEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }

        // Set notification URI on the Cursor, so we know what content URI the Cursor was created for.
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    /**
     * Insert new data into the provider with the given ContentValues.
     */
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case BOOKS:
                return insertBook(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    /**
     * Insert a book into the database with the given content values. Return the new content URI
     * for a specific row in the database.
     */
    private Uri insertBook(Uri uri, ContentValues values) {
        // Check that the book name is not null
        String name = values.getAsString(BookEntry.COLUMN_BOOK_NAME);
        if (name == null) {
            throw new IllegalArgumentException("Book requires a valid name");
        }

        // Check that book price greater than or equal to 0
        Integer price = values.getAsInteger(BookEntry.COLUMN_BOOK_PRICE);
        if (price != null && price < 0) {
            throw new IllegalArgumentException("Book Price need a value > $0");
        }

        // Check that book quantity greater than or equal to 0
        Integer quantiy = values.getAsInteger(BookEntry.COLUMN_BOOK_QUANTITY);
        if (quantiy != null && quantiy < 0) {
            throw new IllegalArgumentException("Book Price require a valid quantity");
        }

        // Check that book supplier name is not null
        String supplierName = values.getAsString(BookEntry.COLUMN_BOOK_SUPPLIER_NAME);
        if (supplierName == null) {
            throw new IllegalArgumentException("Book requires a valid supplier name");
        }

        // Check that book supplier phone no. is not null
        String supplierPhoneNo = values.getAsString(BookEntry.COLUMN_BOOK_SUPPLIER_PHONE_NO);
        if (supplierPhoneNo == null) {
            throw new IllegalArgumentException("Book requires a valid supplier phone no. ");
        }

        // Get writable database
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        // Insert the new book with the given values.
        long id = database.insert(BookEntry.TABLE_NAME, null, values);
        // If the ID is = -1, then the insertion failed. Log an error and return null.
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }
        // Notify all listeners that the data has changed for the book content URI
        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, id);
    }

    /**
     * Updates the data at the given selection and selection arguments, with the new ContentValues.
     */
    @Override
    public int update(Uri uri, ContentValues contentValues, String selection,
                      String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case BOOKS:
                return updatePet(uri, contentValues, selection, selectionArgs);
            case BOOK_ID:
                // Extract out the ID from the URI, so we know which row to update.
                // Selection will be be "_id=?" and selection arguments will be a String array
                // containing the actual ID.
                selection = BookEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updatePet(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    /**
     * Update books in the database with the given content values. Apply the changes to the rows
     * specified in the selection and selection arguments.
     * Return the number of rows that were successfully updated.
     */
    private int updatePet(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        // If the {@link BookEntry@COLUMN_BOOK_NAME} key is present,
        // Check that the book name is not null
        if (values.containsKey(BookEntry.COLUMN_BOOK_NAME)) {
            String name = values.getAsString(BookEntry.COLUMN_BOOK_NAME);
            if (name == null) {
                throw new IllegalArgumentException("Book requires a valid name");
            }
        }

        // If the {@link BookEntry@COLUMN_BOOK_PRICE} key is present,
        // Check that the book price is greater than 0.
        if (values.containsKey(BookEntry.COLUMN_BOOK_PRICE)) {
            Integer price = values.getAsInteger(BookEntry.COLUMN_BOOK_PRICE);
            if (price != null && price < 0) {
                throw new IllegalArgumentException("Book Price need a value > $0");
            }
        }

        // If the {@link BookEntry@COLUMN_BOOK_QUANTITY} key is present,
        // Check that book quantity greater than or equal to 0
        if (values.containsKey(BookEntry.COLUMN_BOOK_QUANTITY)) {
            Integer quantiy = values.getAsInteger(BookEntry.COLUMN_BOOK_QUANTITY);
            if (quantiy != null && quantiy < 0) {
                throw new IllegalArgumentException("Book Price require a valid quantity");
            }
        }

        // If the {@link BookEntry@COLUMN_BOOK_SUPPLIER_NAME} key is present,
        // Check that book supplier name is not null
        if (values.containsKey(BookEntry.COLUMN_BOOK_SUPPLIER_NAME)) {
            String supplierName = values.getAsString(BookEntry.COLUMN_BOOK_SUPPLIER_NAME);
            if (supplierName == null) {
                throw new IllegalArgumentException("Book requires a valid supplier name");
            }
        }

        // If the {@link BookEntry@COLUMN_BOOK_SUPPLIER_PHONE_NO} key is present,
        // Check that book supplier phone no. is not null
        if (values.containsKey(BookEntry.COLUMN_BOOK_SUPPLIER_PHONE_NO)) {
            String supplierPhoneNo = values.getAsString(BookEntry.COLUMN_BOOK_SUPPLIER_PHONE_NO);
            if (supplierPhoneNo == null) {
                throw new IllegalArgumentException("Book requires a valid supplier phone no. ");
            }
        }

        // If there are no values to update, then don't try to update the database.
        if (values.size() == 0) {
            return 0;
        }

        // Otherwise, get writeable database to update the data
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        // Perform the update on the database and get eh number of rows affected
        int rowsUpdated = database.update(BookEntry.TABLE_NAME, values, selection, selectionArgs);

        // If 1 or more rows were updated, then notify all listeners that data
        // at the given URI has changed.
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        // Return the number of rows updated
        return rowsUpdated;
    }


    /**
     * Delete the data at the given selection and selection arguments.
     */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Get writeable database
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        // Track the number of rows that were deleted
        int rowsDeleted;

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case BOOKS:
                // Delete all rows that match the selection and selectionArgs
                rowsDeleted = database.delete(BookEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case BOOK_ID:
                // Delete a single rows given by ID in the URI
                selection = BookEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = database.delete(BookEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }

        // If 1 or more rows were deleted, then notify all the listeners that hte data at the
        // given URI has changed
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        // Return the number of rows deleted
        return rowsDeleted;
    }

    /**
     * Returns the MIME type of data for the content URI.
     */
    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case BOOKS:
                return BookEntry.CONTENT_LIST_TYPE;
            case BOOK_ID:
                return BookEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Uknown URI " + uri + " with match " + match);
        }
    }
}
