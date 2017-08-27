package com.codepath.simplydo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.codepath.simplydo.com.codepath.simplydo.model.Item;

public class EditItemActivity extends AppCompatActivity {

    EditText etEditItem;
    int position;
    Item item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Edit to do item");

        item = ((Item) getIntent().getExtras().getSerializable("item"));

        position = getIntent().getExtras().getInt(MainActivity.EXTRA_MESSAGE_POS);
        etEditItem = ((EditText) findViewById(R.id.etEditItem));
        etEditItem.setText(item.getDesc());
        etEditItem.requestFocus();
        etEditItem.append("");

        if (BuildConfig.DEBUG) Log.e(Constants.LOG, "EditItemActivity::onCreate called.");

    }

    /**
     * This method edits the Item object with the modified text, saves it database and sends it back to the calling activity.
     *
     * @param view The view that was pressed.
     */
    public void saveEditedItem(View view) {

        String desc = etEditItem.getText().toString();
        item.setDesc(desc);
        item.save();

        if (BuildConfig.DEBUG) Log.e(Constants.LOG, "saveEditedItem called. Item edited: " + item);

        Bundle bundle = new Bundle();
        bundle.putSerializable("item", item);

        Intent data = new Intent(this, MainActivity.class);
        data.putExtras(bundle);
        data.putExtra(MainActivity.EXTRA_MESSAGE_POS, position);
        data.putExtra(MainActivity.EXTRA_MESSAGE_ACTION, "EDIT_SAVE");
        setResult(RESULT_OK, data);

        finish();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_menu, menu);
        return true;
    }

    /**
     * This method is invoked when the delete button in the action bar is pressed.
     * This method deletes the Item object from the database and sends an appropriate message to the calling activity.
     *
     * @param item The item that was pressed.
     *
     * @return boolean
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_delete) {

            if (BuildConfig.DEBUG) Log.e(Constants.LOG, "Deleted Item: " + this.item);

            this.item.delete();

            Intent data = new Intent(this, MainActivity.class);
            data.putExtra(MainActivity.EXTRA_MESSAGE_POS, position);
            data.putExtra(MainActivity.EXTRA_MESSAGE_ACTION, "EDIT_DELETE");
            setResult(RESULT_OK, data);

            finish();
        }

        return true;
    }
}
