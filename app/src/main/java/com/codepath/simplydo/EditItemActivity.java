package com.codepath.simplydo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class EditItemActivity extends AppCompatActivity {

    EditText etEditItem;
    int position;
    String item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        item = getIntent().getExtras().getString(MainActivity.EXTRA_MESSAGE);
        position = getIntent().getExtras().getInt(MainActivity.EXTRA_MESSAGE_POS);
        etEditItem = ((EditText) findViewById(R.id.etEditItem));
        etEditItem.setText(item);
        etEditItem.requestFocus();
        etEditItem.append("");
        getSupportActionBar().setTitle("Edit to do item");
    }

    public void saveEditedItem(View view) {

        String item = etEditItem.getText().toString();
        System.out.println("Edited: " + item);

        Intent data = new Intent(this, MainActivity.class);
        data.putExtra(MainActivity.EXTRA_MESSAGE, item);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_delete) {
            System.out.println("deleting,,,");
            Intent data = new Intent(this, MainActivity.class);
            data.putExtra(MainActivity.EXTRA_MESSAGE_POS, position);
            data.putExtra(MainActivity.EXTRA_MESSAGE_ACTION, "EDIT_DELETE");
            setResult(RESULT_OK, data);

            finish();
        }

        return true;
    }
}
