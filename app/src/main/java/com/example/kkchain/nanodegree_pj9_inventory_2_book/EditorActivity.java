package com.example.kkchain.nanodegree_pj9_inventory_2_book;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.widget.EditText;

public class EditorActivity extends AppCompatActivity{

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml.file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.editor_menu, menu);
        return true;
    }

}
