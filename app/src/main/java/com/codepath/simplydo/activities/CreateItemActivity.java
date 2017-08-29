package com.codepath.simplydo.activities;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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

public class CreateItemActivity extends AppCompatActivity {

    EditText etTaskName;
    DatePicker dpDueDate;
    Spinner sPriority;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (BuildConfig.DEBUG) Log.e(Constants.LOG, "CreateItemActivity::onCreate called.");

        setUpActivity();
        setUpTextView();
        setUpDatePicker();
        setUpPriority();

    }

    /**
     * This method saves the newly created item to the database and also sends it to the calling activity
     *
     * @param view The view that was pressed.
     */
    public void saveItem(View view) {

        Item item = new Item();
        item.setDesc(etTaskName.getText().toString());

        Calendar cal = Calendar.getInstance();
        cal.set(dpDueDate.getYear(), dpDueDate.getMonth(), dpDueDate.getDayOfMonth());
        java.sql.Date editedDate = new java.sql.Date(cal.getTimeInMillis());
        item.setDueDate(editedDate);

        String priority = sPriority.getSelectedItem().toString();
        item.setPriority(priority);

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

    /**
     * This method initializes and sets up the TextView for to-do item description.
     */
    private void setUpTextView() {
        etTaskName = ((EditText) findViewById(R.id.etTaskNameCreate));
    }


    /**
     * This method initializes and sets up the Date Picker.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void setUpDatePicker() {

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        dpDueDate = ((DatePicker) findViewById(R.id.dpDueDateCreate));
        dpDueDate.init(year, month, day, null);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            dpDueDate.setCalendarViewShown(false);
        }
    }


    /**
     * This method handles the setup for the activity like content view and support bar.
     */
    private void setUpActivity() {
        setContentView(R.layout.activity_create_item);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Create new to do item");
    }


    /**
     * This method initializes and sets up the Spinner for to-do item priority.
     */
    private void setUpPriority() {
        sPriority = ((Spinner) findViewById(R.id.sPriorityCreate));
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.priority_array,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sPriority.setAdapter(adapter);
    }

}
