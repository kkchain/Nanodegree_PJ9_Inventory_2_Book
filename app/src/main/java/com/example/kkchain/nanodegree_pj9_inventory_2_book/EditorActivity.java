package com.example.kkchain.nanodegree_pj9_inventory_2_book;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kkchain.nanodegree_pj9_inventory_2_book.Data.BookContract.BookEntry;

/**
 * Allow user to create a new book or edit an existing one.
 */

public class EditorActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    /**
     * Validate min phone number digit
     */
    private static final int MIN_PHONE_NO = 10;  // Example: 1234567890 (USA)

    /**
     * Identifier for the book data loader
     */
    private static final int EXISTING_BOOK_LOADER = 0;

    /**
     * Content URI for the existing Book (null if it's a new book)
     */
    private Uri mCurrentBookUri;

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

    /**
     * Boolean flag that keeps track of whether the book has been edited (true) or not (false)
     */
    private boolean mBookHasChanged = false;

    /**
     * OnTouchListener that listen for any user touches on a view, indicating that they are modifying
     * the view, and changed the mBookHasChanged boolean to true.
     */
    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mBookHasChanged = true;
            return false;
        }
    };


    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.editor_activity);

        // Examine the intent that was used to launch this activity,
        // figure out if we're creating a new book or editing an existing one.
        Intent intent = getIntent();
        mCurrentBookUri = intent.getData();


        // If the intent does not contain a book content URI, then this is creating a new book
        if (mCurrentBookUri == null) {
            // this is a new book, change the app bar to say " Add a Book"
            setTitle(getString(R.string.editor_title_bar_add_new_book));

            // Invalidate the options menu, so the "Delete" menu option can be hidden
            invalidateOptionsMenu();
        } else {
            // Otherwise this is an existing book, change app bar to sya "Edit Book"
            setTitle(getString(R.string.editor_title_bar_edit_book));

            // Initialize a loader to read the Book data from the database
            // and display the current values in the editor
            getLoaderManager().initLoader(EXISTING_BOOK_LOADER, null, this);
        }

        // Find all relevant views that we will need to read user input from
        mBookNameEditText = (EditText) findViewById(R.id.edit_bookName);
        mBookPriceEditText = (EditText) findViewById(R.id.edit_bookPrice);
        mBookQuatityEditText = (EditText) findViewById(R.id.edit_bookQuantity);
        mBookSupplierNameEditText = (EditText) findViewById(R.id.edit_bookSupplierName);
        mBookSupplierPhoneNoEditText = (EditText) findViewById(R.id.edit_bookSupplierphoneNo);

        // Setup OnTouchListener on all the input fields, so we can determine if the user has
        // touched or modified them. This will let us know if there are unsaved changes or not,
        // if the user tries to leave the editor without saving.
        mBookNameEditText.setOnTouchListener(mTouchListener);
        mBookPriceEditText.setOnTouchListener(mTouchListener);
        mBookQuatityEditText.setOnTouchListener(mTouchListener);
        mBookSupplierNameEditText.setOnTouchListener(mTouchListener);
        mBookSupplierPhoneNoEditText.setOnTouchListener(mTouchListener);

        // Button - Increase quantity
        Button increaseQuantity = (Button) findViewById(R.id.increase_quantity_button);
        // Button - Decrease quantity
        Button decreaseQuantity = (Button) findViewById(R.id.decrease_quantity_button);

        // Set on click listener, increase by one each time it click
        // Return early and alert user if quantity less than zero
        increaseQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String bookQuantityString = mBookQuatityEditText.getText().toString().trim();
                int quantity = 0;
                // Check if input field is not empty
                if (!bookQuantityString.isEmpty()) {
                    quantity = Integer.parseInt(bookQuantityString);
                } else {
                    Toast.makeText(EditorActivity.this, R.string.editor_quantity_greater_zero, Toast.LENGTH_SHORT).show();

                }

                if (quantity <= 0) {
                    Toast.makeText(EditorActivity.this, R.string.editor_quantity_greater_zero, Toast.LENGTH_SHORT).show();
                    return;
                }
                quantity = quantity + 1;

                // Update value in database
                ContentValues contentValues = new ContentValues();
                contentValues.put(BookEntry.COLUMN_BOOK_QUANTITY, quantity);
                getContentResolver().update(mCurrentBookUri, contentValues, null, null);
                mBookQuatityEditText.setText(String.valueOf(contentValues.getAsString(BookEntry.COLUMN_BOOK_QUANTITY)));
            }
        });

        // Set on click listener, decrease by one each time it click
        // Return early and alert user if quantity equal or less than zero
        decreaseQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String bookQuantityString = mBookQuatityEditText.getText().toString().trim();
                int quantity = 0;
                // Check if input field is not empty
                if (!bookQuantityString.isEmpty()) {
                    quantity = Integer.parseInt(bookQuantityString);
                } else {
                    Toast.makeText(EditorActivity.this, R.string.editor_quantity_greater_zero, Toast.LENGTH_SHORT).show();

                }

                if (quantity <= 0) {
                    Toast.makeText(EditorActivity.this, R.string.editor_quantity_greater_zero, Toast.LENGTH_SHORT).show();
                    return;
                }
                quantity = quantity - 1;

                // Update value in database
                ContentValues contentValues = new ContentValues();
                contentValues.put(BookEntry.COLUMN_BOOK_QUANTITY, quantity);
                getContentResolver().update(mCurrentBookUri, contentValues, null, null);
                mBookQuatityEditText.setText(String.valueOf(contentValues.getAsString(BookEntry.COLUMN_BOOK_QUANTITY)));
            }
        });

        // Set on click listener when call supplier button is clicked
        Button callSupplierPhoneNo = findViewById(R.id.call_supplier_button);
        callSupplierPhoneNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String bookSupplierPhoneNoString = mBookSupplierPhoneNoEditText.getText().toString().trim();
                if (bookSupplierPhoneNoString == null || TextUtils.isEmpty(bookSupplierPhoneNoString)) {
                    Toast.makeText(getApplicationContext(), R.string.editor_invalid_phone_no, Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    String uri = "tel:" + bookSupplierPhoneNoString;
                    intent.setData(Uri.parse(uri));
                    startActivity(intent);
                }
            }
        });
    }


    /**
     * Get User input from the editor activity and save book into database.
     */
    private void saveBook() {
        // Read from input fields
        // Use trim to eliminate leading or trailing white space
        String bookNameString = mBookNameEditText.getText().toString().trim();
        String bookPriceString = mBookPriceEditText.getText().toString().trim();
        String bookQuantityString = mBookQuatityEditText.getText().toString().trim();
        String bookSupplierNameString = mBookSupplierNameEditText.getText().toString().trim();
        String bookSupplierPhoneNoString = mBookSupplierPhoneNoEditText.getText().toString().trim();

        // Check if this is supposed to be new book
        // Check if all the fields in editor is blank
        if (mCurrentBookUri == null &&
                TextUtils.isEmpty(bookNameString) && TextUtils.isEmpty(bookPriceString) &&
                TextUtils.isEmpty(bookQuantityString) && TextUtils.isEmpty(bookSupplierNameString) &&
                TextUtils.isEmpty(bookSupplierPhoneNoString)) {
            // Since no field is modify, return early without creating a new book
            return;
        }

        // Check if book name or price or quantity or supplier name or phone no is empty
        // Stop saving and return early, Ask user to make sure to enter all inputs
        if (TextUtils.isEmpty(bookNameString) || TextUtils.isEmpty(bookPriceString) ||
                TextUtils.isEmpty(bookQuantityString) || TextUtils.isEmpty(bookSupplierNameString) ||
                TextUtils.isEmpty(bookSupplierPhoneNoString)) {
            Toast.makeText(this, getString(R.string.editor_no_empty_fields_allow),
                    Toast.LENGTH_SHORT).show();
            return;
        }

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

        // Determine if this is a new or existing pet by checking if mCurrentBookUri is null or not
        if (mCurrentBookUri == null) {
            // This is a new book, so insert a new book into the provider,
            // returning the content URI for the book.
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

        } else {

            // Otherwise this is an EXISTING book, so update the book with content URI: mCurrentBookUri
            int rowsAffected = getContentResolver().update(mCurrentBookUri, values, null, null);

            // Show a toast message depending on whether or not the update was successful
            // If no rows were affected, then there was an error with the update
            if (rowsAffected == 0) {
                Toast.makeText(this, getString(R.string.editor_failed_update_book),
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the update was successful
                Toast.makeText(this, getString(R.string.editor_update_book_successfully),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml.file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.editor_menu, menu);
        return true;
    }

    /**
     * This method is called after invalidateOptionsMenu(), so that the
     * menu can be updated
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        //If this is a new book, hide the "Delete" menu item.
        if (mCurrentBookUri == null) {
            MenuItem menuItem = menu.findItem(R.id.action_delete_data);
            menuItem.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            case R.id.action_save_data:
                // Save book to database
                saveBook();
                finish();
                return true;
            case R.id.action_delete_data:
                // Delete book from database
                // Pop up confirmation dialog for deletion
                showDeleteConfirmationDialog();
                return true;
            case R.id.home:
                // If the book has't changed, navigating up to parent activity {@link CatalogActivity)
                if (!mBookHasChanged) {
                    NavUtils.navigateUpFromSameTask(EditorActivity.this);
                    return true;
                }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {
                BookEntry._ID,
                BookEntry.COLUMN_BOOK_NAME,
                BookEntry.COLUMN_BOOK_PRICE,
                BookEntry.COLUMN_BOOK_QUANTITY,
                BookEntry.COLUMN_BOOK_SUPPLIER_NAME,
                BookEntry.COLUMN_BOOK_SUPPLIER_PHONE_NO};

        // This loader will execute the ContentProvider's query method on a background thread.
        return new CursorLoader(this, mCurrentBookUri, projection,
                null, null, null);
    }


    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        // Bail early if the cursor is null or there is less than 1 row in the cursor
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }

        // Proceed with moving to the first row of the cursor and reading data from it
        if (cursor.moveToFirst()) {
            int BookNameIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_NAME);
            int BookPriceIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_PRICE);
            int BookQuantityIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_QUANTITY);
            int BookSupplierNameIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_SUPPLIER_NAME);
            int BookSupplierPhoneNoIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_SUPPLIER_PHONE_NO);

            // Extract out the value from the Cursor for the given column index
            String name = cursor.getString(BookNameIndex);
            String price = cursor.getString(BookPriceIndex);
            int quantity = cursor.getInt(BookQuantityIndex);
            String supplierName = cursor.getString(BookSupplierNameIndex);
            String supplierPhoneNo = cursor.getString(BookSupplierPhoneNoIndex);

            // Update the views on the screen with the values from the database
            mBookNameEditText.setText(name);
            mBookPriceEditText.setText(price);
            mBookQuatityEditText.setText(Integer.toString(quantity)); // change input int to string
            mBookSupplierNameEditText.setText(supplierName);
            mBookSupplierPhoneNoEditText.setText(supplierPhoneNo);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // If the loader is invalidated, clear out all the data from the input fields.
        mBookNameEditText.setText("");
        mBookPriceEditText.setText("");
        mBookQuatityEditText.setText("");
        mBookSupplierNameEditText.setText("");
        mBookSupplierPhoneNoEditText.setText("");
    }

    /**
     * Prompt the user to confirm to delete this book
     */
    private void showDeleteConfirmationDialog() {
        // Create an AlertDialog.builder and set the message, and click listeners
        // for the positive and negative button on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog_message);
        builder.setPositiveButton(R.string.delete_message, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Delete" button, so delete the pet.
                deleteBook();
            }
        });
        builder.setNegativeButton(R.string.cancel_message, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    /**
     * Perform the deletion of the pet in the database.
     */
    private void deleteBook() {
        // Only perform the delete id this is an existing book.
        if (mCurrentBookUri != null) {
            // Call the ContentResolver().delete the book at the given URI.
            int rowsDeleted = getContentResolver().delete(mCurrentBookUri, null, null);

            //Show a toast message
            if (rowsDeleted == 0) {
                // If no rows were deleted
                Toast.makeText(this, getString(R.string.editor_error_delete_book), Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the delete was successful
                Toast.makeText(this, getString(R.string.editor_delete_book_successfully), Toast.LENGTH_SHORT).show();
            }
        }

        // Close the activity
        finish();
    }
}
