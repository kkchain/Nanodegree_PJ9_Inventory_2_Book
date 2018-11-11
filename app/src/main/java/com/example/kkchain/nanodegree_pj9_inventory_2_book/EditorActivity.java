package com.example.kkchain.nanodegree_pj9_inventory_2_book;

import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.kkchain.nanodegree_pj9_inventory_2_book.Data.BookContract.BookEntry;

/**
 * Allow user to create a new book or edit an existing one.
 */

public class EditorActivity extends AppCompatActivity {

    /**
     * EditText field to enter the book name
     */
    private EditText mBookNameEditText;

    /**
     * EditText field to enter the book price
     */
    private EditText mBookPriceEditText;

    /**
     * EditText field to enter the book quantity
     */
    private EditText mBookQuatityEditText;

    /**
     * EditText field to enter the book suuplier name
     */
    private EditText mBookSupplierNameEditText;

    /**
     * EditText field to enter the book name
     */
    private EditText mBookSupplierPhoneNoEditText;


    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.editor_activity);

        // Find all relevant views that we will need to read user input from
        mBookNameEditText = (EditText) findViewById(R.id.edit_bookName);
        mBookPriceEditText = (EditText) findViewById(R.id.edit_bookPrice);
        mBookQuatityEditText = (EditText) findViewById(R.id.edit_bookQuantity);
        mBookSupplierNameEditText = (EditText) findViewById(R.id.edit_bookSupplierName);
        mBookSupplierPhoneNoEditText = (EditText) findViewById(R.id.edit_bookSupplierphoneNo);
    }

    /**
     * Get User input from the editor activity and save pet into database.
     */
    private void savePet() {
        // Read from input fields
        // Use trim to eliminate leading or trailing white space
        String bookNameString = mBookNameEditText.getText().toString().trim();
        String bookPriceString = mBookPriceEditText.getText().toString().trim();
        String bookQuantityString = mBookQuatityEditText.getText().toString().trim();
        String bookSupplierNameString = mBookSupplierNameEditText.getText().toString().trim();
        String bookSupplierPhoneNoString = mBookSupplierPhoneNoEditText.getText().toString().trim();

        // Create a ContentValues object where column names are the keys,
        // and book  attributes from the editor are the values.
        ContentValues values = new ContentValues();
        values.put(BookEntry.COLUMN_BOOK_NAME, bookNameString);
        values.put(BookEntry.COLUMN_BOOK_PRICE, bookPriceString);
        values.put(BookEntry.COLUMN_BOOK_QUANTITY, bookQuantityString);
        values.put(BookEntry.COLUMN_BOOK_SUPPLIER_NAME, bookSupplierNameString);
        values.put(BookEntry.COLUMN_BOOK_SUPPLIER_PHONE_NO, bookSupplierPhoneNoString);

        // If the book quantity is not provided bt the user, don't parse the string into an
        // integer value. Use 0 as default.
        int quantity = 0;
        if (!TextUtils.isEmpty(bookQuantityString)) {
            quantity = Integer.parseInt(bookQuantityString);
        }
        values.put(BookEntry.COLUMN_BOOK_QUANTITY, quantity);

        Uri newUri = getContentResolver().insert(BookEntry.CONTENT_URI, values);

        // Show a toast message depending on whether or not the insertion was successful
        // If the new content URI is null, then there was an error with insertion.
        if (newUri == null) {
            Toast.makeText(this, getString(R.string.editor_insert_book_failed),
                    Toast.LENGTH_SHORT).show();
        } else {
            // Otherwise, the insertion was successful
            Toast.makeText(this, getString(R.string.editor_insert_book_successful),
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml.file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.editor_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            case R.id.action_save_data:
                // Save book to database
                savePet();
                finish();
                return true;
            case R.id.action_delete_data:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
