package com.codepath.todo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

public class CreateItemActivity extends AppCompatActivity {

    EditText etItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_item);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        etItem = ((EditText) findViewById(R.id.etItemName));

        getSupportActionBar().setTitle("Create new to do item");
    }

    public void saveItem(View view) {

        String item = etItem.getText().toString();
        System.out.println("Type item:" + item);

        Intent data = new Intent(this, MainActivity.class);
        data.putExtra(MainActivity.EXTRA_MESSAGE, item);
        data.putExtra(MainActivity.EXTRA_MESSAGE_ACTION, "CREATE");
        setResult(RESULT_OK, data);

        finish();

    }

}
