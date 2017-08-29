package com.codepath.simplydo.activities;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.codepath.simplydo.BuildConfig;
import com.codepath.simplydo.Constants;
import com.codepath.simplydo.R;
import com.codepath.simplydo.model.Item;

import java.util.Calendar;
import java.util.Date;

public class EditItemActivity extends AppCompatActivity {

    EditText etEditItem;
    DatePicker dpDueDate;
    Spinner sPriority;
    int position;
    Item item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setUpActivity();
        setUpTextView();
        setUpDatePicker();
        setUpPriority();

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

        Calendar cal = Calendar.getInstance();
        cal.set(dpDueDate.getYear(), dpDueDate.getMonth(), dpDueDate.getDayOfMonth());
        java.sql.Date editedDate = new java.sql.Date(cal.getTimeInMillis());
        item.setDueDate(editedDate);

        item.setPriority(sPriority.getSelectedItem().toString());

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


    /**
     * This method handles the setup for the activity like content view and support bar.
     */
    private void setUpActivity() {
        setContentView(R.layout.activity_edit_item);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Edit to do item");

    }

    /**
     * This method initializes and sets up the Date Picker.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void setUpDatePicker() {

        Date savedDate = item.getDueDate();
        Calendar cal = Calendar.getInstance();

        if (savedDate != null) {
            cal.setTime(savedDate);
        }

        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        dpDueDate = ((DatePicker) findViewById(R.id.dpDueDateEdit));
        dpDueDate.init(year, month, day, null);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            dpDueDate.setCalendarViewShown(false);
        }
    }

    /**
     * This method initializes and sets up the TextView for to-do item description.
     */
    private void setUpTextView() {
        item = ((Item) getIntent().getExtras().getSerializable("item"));
        position = getIntent().getExtras().getInt(MainActivity.EXTRA_MESSAGE_POS);
        etEditItem = ((EditText) findViewById(R.id.etTaskNameEdit));
        etEditItem.setText(item.getDesc());
        etEditItem.requestFocus();
        etEditItem.append("");
    }



    /**
     * This method initializes and sets up the Spinner for to-do item priority.
     */
    private void setUpPriority() {
        sPriority = ((Spinner) findViewById(R.id.sPriorityEdit));

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.priority_array,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sPriority.setAdapter(adapter);

        String priority = item.getPriority() == null? "Medium" : item.getPriority();

        int position = priority.equalsIgnoreCase("High") ?
                0 : priority.equalsIgnoreCase("Medium") ? 1 : 2;
        sPriority.setSelection(position);

    }

}
