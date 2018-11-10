package com.example.kkchain.nanodegree_pj9_inventory_2_book;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.example.kkchain.nanodegree_pj9_inventory_2_book.BookCursorAdapter;
import com.example.kkchain.nanodegree_pj9_inventory_2_book.EditorActivity;
import com.example.kkchain.nanodegree_pj9_inventory_2_book.R;
import com.example.kkchain.nanodegree_pj9_inventory_2_book.Data.BookContract.BookEntry;

/**
 * Display list of books that were entered and stored in the app
 */

public class CatalogActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    /**
     * Identifier for the book data loader
     */
    private static final int BOOK_LOADER = 0;

    /**
     * Adapter for the ListView
     */
    BookCursorAdapter mCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.catalog_activity);

        // Setup ADD Button to open EditorActivity
        Button editorButton = (Button) findViewById(R.id.addButton);
        editorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        // Find the ListView which will be populated with the book data.
        ListView bookListView = (ListView) findViewById(R.id.list);

        // Find and set empty view on the ListView, only shows when the list has 0 items.
        View emptyView = findViewById(R.id.empty_view);
        bookListView.setEmptyView(emptyView);

        // Set up a Adapter to create a list item for each row of book data in the cursor.
        // There is no book data yet (until the loader finishes) so pass in null for the cursor.
        mCursorAdapter = new BookCursorAdapter(this, null);
        bookListView.setAdapter(mCursorAdapter);

        // Kick off the Loader
        getLoaderManager().initLoader(BOOK_LOADER, null, this);
    }

    /**
     * Method to insert hardcoded data into database for debugging purposes
     */
    private void insertBook() {
        // Create a ContentValues object.
        ContentValues values = new ContentValues();
        values.put(BookEntry.COLUMN_BOOK_NAME, "Back to the Future");
        values.put(BookEntry.COLUMN_BOOK_PRICE, "$25");
        values.put(BookEntry.COLUMN_BOOK_QUANTITY, 3);
        values.put(BookEntry.COLUMN_BOOK_SUPPLIER_NAME, "Motion Picture");
        values.put(BookEntry.COLUMN_BOOK_SUPPLIER_PHONE_NO, "123-456-7890");

        Uri newUri = getContentResolver().insert(BookEntry.CONTENT_URI, values);
    }

    /**
     * Helper method to delete all books in the database
     */
    private void deleteAllBooks() {
        int rowsDeleted = getContentResolver().delete(BookEntry.CONTENT_URI, null, null);
        Log.v("CatalogActivity", rowsDeleted + " rows deleted from book database");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.catalog_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            case R.id.action_insert_dummy_data:
                insertBook();
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                // Do Nothing for now
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
        // Define a projection that specifies the columns from the table we care about.
        String[] projection = {
                BookEntry._ID,
                BookEntry.COLUMN_BOOK_NAME,
                BookEntry.COLUMN_BOOK_PRICE,
                BookEntry.COLUMN_BOOK_QUANTITY
        };

        // This loader will execute th ContentProvider's query method on a background thread.
        return new CursorLoader(this,       // Parent activity context
                BookEntry.CONTENT_URI,              // Provider content URI to query
                projection,                         // Columns to include in the resulting Cursor
                null,                      // No selection clause
                null,                  // No selection arguments
                null);                    // Default sortOrder
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // Update {@link BookCursorAdapter} with this new cursor containing updated book data
        mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // Callback called when the data needs to be deleted
        mCursorAdapter.swapCursor(null);
    }
}
