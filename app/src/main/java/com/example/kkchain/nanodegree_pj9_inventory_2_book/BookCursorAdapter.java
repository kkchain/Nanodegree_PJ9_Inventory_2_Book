package com.example.kkchain.nanodegree_pj9_inventory_2_book;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.kkchain.nanodegree_pj9_inventory_2_book.Data.BookContract.BookEntry;


public class BookCursorAdapter extends CursorAdapter {

    /**
     * Construct a new {@link BookCursorAdapter}.
     */
    public BookCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    /**
     * Makes a new blank list item view. No data is set to the views yet.
     *
     * @param context app context
     * @param cursor  The cursor from which to get data. The is already moved to correct position.
     * @param parent  The parent to which the new view is attached to
     * @return the newly created list item view
     */
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // Inflate a list view using the layout specified in list_item.xml
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    /**
     * This method binds the books data (in the current row pointed to by cursor) to the given list item layout.
     *
     * @param view    Existing view, returned earlier by newView() method
     * @param context app context
     * @param cursor  The cursor from which to get the data. Moved to current row.
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Find individual views that we want to modify in the list item layout
        TextView bookNameTextView = (TextView) view.findViewById(R.id.bookName);
        TextView bookPriceTextView = (TextView) view.findViewById(R.id.bookPrice);
        TextView bookQuantityTextView = (TextView) view.findViewById(R.id.bookQuantity);


        // Find the columns of book attributes that we're interested in.
        int bookNameColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_NAME);
        int bookPriceColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_PRICE);
        int bookQuantityColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_QUANTITY);

        // Read the book attributes from the Cursor for the current book
        String bookNames = cursor.getString(bookNameColumnIndex);
        String bookPrices = cursor.getString(bookPriceColumnIndex);
        String bookQuantity = cursor.getString(bookQuantityColumnIndex);

        // Update the TextViews with the attributes for the current book
        bookNameTextView.setText(bookNames);
        bookPriceTextView.setText(bookPrices);
        bookQuantityTextView.setText(bookQuantity);
    }

}
