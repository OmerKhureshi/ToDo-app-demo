package com.codepath.simplydo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.codepath.simplydo.com.codepath.simplydo.model.Item;

public class CreateItemActivity extends AppCompatActivity {

    EditText etItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_item);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Create new to do item");

        etItem = ((EditText) findViewById(R.id.etItemName));

        if (BuildConfig.DEBUG) Log.e(Constants.LOG, "CreateItemActivity::onCreate called.");

    }

    /**
     * This method saves the newly created item to the database and also sends it to the calling activity
     *
     * @param view The view that was pressed.
     */
    public void saveItem(View view) {

        Item item = new Item();
        item.setDesc(etItem.getText().toString());
        item.save();
        if (BuildConfig.DEBUG) Log.e(Constants.LOG, "Saved item: " + item);

        Bundle bundle = new Bundle();
        bundle.putSerializable("item", item);

        Intent data = new Intent(this, MainActivity.class);
        data.putExtras(bundle);
        data.putExtra(MainActivity.EXTRA_MESSAGE_ACTION, "CREATE");
        setResult(RESULT_OK, data);

        finish();

    }

}
