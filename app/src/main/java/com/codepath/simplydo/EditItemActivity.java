package com.codepath.simplydo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.codepath.simplydo.com.codepath.simplydo.model.Item;

import java.io.Serializable;

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

        item = ((Item) getIntent().getExtras().getSerializable("item"));

        position = getIntent().getExtras().getInt(MainActivity.EXTRA_MESSAGE_POS);
        etEditItem = ((EditText) findViewById(R.id.etEditItem));
        etEditItem.setText(item.getDesc());
        etEditItem.requestFocus();
        etEditItem.append("");
        getSupportActionBar().setTitle("Edit to do item");
    }

    public void saveEditedItem(View view) {

        String desc = etEditItem.getText().toString();
        item.setDesc(desc);
        item.save();
        System.out.println("Edited: " + item);

        Bundle bundle = new Bundle();
        bundle.putSerializable("item", ((Serializable) item));


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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_delete) {
            System.out.println("deleting,,,");

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
