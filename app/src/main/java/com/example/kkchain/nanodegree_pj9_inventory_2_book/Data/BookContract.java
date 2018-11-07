package com.example.kkchain.nanodegree_pj9_inventory_2_book.Data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public final class BookContract {

    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    private BookContract() {
    }

    // The "Content Authority" is a name for the entire content provider.
    public static final String CONTENT_AUTHORITY = "com.example.kkchain.nanodegree_pj9_inventory_2_book";

    /**
     * Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
     * the content provider.
     */
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    /**
     * Possible path (appended to base content URI for possible URI's)
     * content://com.example.kkchain.nanodegree_pj9_inventory_2_book/books is a valid
     * path for looking data.
     */
    public static final String PATH_BOOKS = "books";

    /**
     * Inner class that defines constant values for the Book Store Inventory database table
     * Each entry in the table represents a single book title
     */
    public static final class BookEntry implements BaseColumns {

        /**
         * The content URI to access the book data in the provider
         */
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_BOOKS);

        /**
         * MIME = Multipurpose Internet Mail Extensions. A way to identifying files on the internet.
         * MIME type of the {@link #CONTENT_URI} for a list of books.
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_BOOKS;

        /**
         * Name of database table for books
         */
        public final static String TABLE_NAME = "books";

        /**
         * Unique ID number for the book
         * Type INTEGER
         */
        public final static String _ID = BaseColumns._ID;

        /**
         * Book Name
         * Type: TEXT
         */
        public final static String COLUMN_BOOK_NAME = "book_name";

        /**
         * Book Price
         * Type: INTEGER
         */
        public final static String COLUMN_BOOK_PRICE = "book_price";

        /**
         * Book Quantity
         * Type: INTEGER
         */
        public final static String COLUMN_BOOK_QUANTITY = "book_quantity";

        /**
         * Book Supplier Name
         * Type: TEXT
         */
        public final static String COLUMN_BOOK_SUPPLIER_NAME = "book_supplier_name";

        /**
         * Book Supplier Phone No
         * Type: TEXT
         */
        public final static String COLUMN_BOOK_SUPPLIER_PHONE_NO = "book_supplier_phone_no";

    }

}