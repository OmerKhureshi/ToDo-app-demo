package com.codepath.simplydo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import com.codepath.simplydo.com.codepath.simplydo.model.Item;

import java.io.Serializable;

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

//        String item = etItem.getText().toString();
        Item item = new Item();
        item.setDesc(etItem.getText().toString());
        item.save();
        System.out.println("Type item:" + item.getDesc());

        Bundle bundle = new Bundle();
        bundle.putSerializable("item", ((Serializable) item));


        Intent data = new Intent(this, MainActivity.class);
        data.putExtras(bundle);
        data.putExtra(MainActivity.EXTRA_MESSAGE_ACTION, "CREATE");
        setResult(RESULT_OK, data);

        finish();

    }

}
