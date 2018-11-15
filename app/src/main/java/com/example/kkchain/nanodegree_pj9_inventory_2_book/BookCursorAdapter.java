package com.example.kkchain.nanodegree_pj9_inventory_2_book;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

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
    public void bindView(View view, final Context context, Cursor cursor) {
        // Find individual views that we want to modify in the list item layout
        TextView bookNameTextView = (TextView) view.findViewById(R.id.bookName);
        TextView bookPriceTextView = (TextView) view.findViewById(R.id.bookPrice);
        final TextView bookQuantityTextView = (TextView) view.findViewById(R.id.bookQuantity);


        // Find the columns of book attributes that we're interested in.
        int idColumnIndex = cursor.getColumnIndex(BookEntry._ID);
        int bookNameColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_NAME);
        int bookPriceColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_PRICE);
        int bookQuantityColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_QUANTITY);

        // Read the book attributes from the Cursor for the current book
        final int id = cursor.getInt(idColumnIndex);
        String bookNames = cursor.getString(bookNameColumnIndex);
        String bookPrices = cursor.getString(bookPriceColumnIndex);
        String bookQuantity = cursor.getString(bookQuantityColumnIndex);

        // Update the TextViews with the attributes for the current book
        bookNameTextView.setText(bookNames);
        bookPriceTextView.setText(bookPrices);
        bookQuantityTextView.setText(bookQuantity);

        // Decrease the quantity by one each time it clicked
        Button sellButton = (Button) view.findViewById(R.id.sale_Button);
        sellButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String bookQuantityString = bookQuantityTextView.getText().toString().trim();
                int quantity = Integer.parseInt(bookQuantityString);

                // Check if quantity is equal or greater than zero
                if (quantity == 0 || quantity < 0) {
                    Toast.makeText(context, R.string.editor_quantity_less_than_zero, Toast.LENGTH_SHORT).show();
                } else {
                    // Decrease quantity by one
                    quantity--;

                    // update database
                    bookQuantityString = Integer.toString(quantity);
                    ContentValues values = new ContentValues();
                    values.put(BookEntry.COLUMN_BOOK_QUANTITY, bookQuantityString);
                    Uri mCurrentBookUri = ContentUris.withAppendedId(BookEntry.CONTENT_URI, id);

                    int rowsAffected = context.getContentResolver().update(mCurrentBookUri, values, null, null);
                    // Show a toast message depending on whether or not the update was successful
                    // If no rows were affected, then there was an error with the update
                    if (rowsAffected == 0) {
                        Toast.makeText(context, R.string.editor_failed_update_book,
                                Toast.LENGTH_SHORT).show();
                    } else {
                        // Otherwise, the update was successful
                        Toast.makeText(context, R.string.editor_update_book_successfully,
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

}
