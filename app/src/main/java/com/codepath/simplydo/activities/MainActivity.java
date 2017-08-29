package com.codepath.simplydo.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.codepath.simplydo.BuildConfig;
import com.codepath.simplydo.Constants;
import com.codepath.simplydo.R;
import com.codepath.simplydo.adapters.CustomAdapter;
import com.codepath.simplydo.model.Item;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    int currentVersion = BuildConfig.VERSION_CODE;
    public static final String EXTRA_MESSAGE_ACTION = "com.codepath.todo.MESSAGE.ACTION";
    public static final String EXTRA_MESSAGE_POS = "com.codepath.todo.MESSAGE.POS";
    public static final int REQUEST_CODE_CREATE = 20;

    public static final int REQUEST_CODE_EDIT = 30;
    ArrayList<Item> itemsArrayList;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    CustomAdapter customAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (BuildConfig.DEBUG) Log.e(Constants.LOG, "MainActivity::onCreate called.");

        setUpActivity();
        itemsArrayList = (ArrayList<Item>) readItemsFromDB();

        if (isFirstRun()) {
            if (BuildConfig.DEBUG) Log.e(Constants.LOG, "First run of the app.");
            runFirstTime();
        }

        setUpRecyclerView();
    }

    /**
     * This method is called when the add fab button is pressed.
     * Thie method calls card_view new Activity where new tasks can be added.
     *
     * @param view
     */
    public void createItem(View view) {
        if (BuildConfig.DEBUG) Log.e(Constants.LOG, "Clicked fab button.");

        Intent intent = new Intent(this, CreateItemActivity.class);
        startActivityForResult(intent, REQUEST_CODE_CREATE);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds itemsArrayList to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify card_view parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (BuildConfig.DEBUG)
            Log.e(Constants.LOG, "onActivityResult called. resultCode: " + resultCode + "; requestCode: " + requestCode);

        if (resultCode == RESULT_OK) {

            String action = data.getExtras().getString(EXTRA_MESSAGE_ACTION);

            if (requestCode == REQUEST_CODE_CREATE) {

                Item item = (Item) data.getExtras().getSerializable("item");
                itemsArrayList.add(item);
                customAdapter.notifyDataSetChanged();

                if (BuildConfig.DEBUG)
                    Log.e(Constants.LOG, "Created item: " + item);

            } else if (requestCode == REQUEST_CODE_EDIT) {

                if (BuildConfig.DEBUG) Log.e(Constants.LOG, "action: " + action);

                if (action.equalsIgnoreCase("EDIT_SAVE")) {

                    Item item = ((Item) data.getExtras().getSerializable("item"));
                    int pos = data.getExtras().getInt(EXTRA_MESSAGE_POS);
                    itemsArrayList.remove(pos);
                    itemsArrayList.add(pos, item);
                    customAdapter.notifyDataSetChanged();

                    if (BuildConfig.DEBUG) Log.e(Constants.LOG, "Edited item: " + item);

                } else if (action.equalsIgnoreCase("EDIT_DELETE")) {

                    int pos = data.getExtras().getInt(EXTRA_MESSAGE_POS);
                    itemsArrayList.remove(pos);
                    customAdapter.notifyDataSetChanged();

                    if (BuildConfig.DEBUG) Log.e(Constants.LOG, "Deleted item at position: " + pos);

                }
            }


        }
    }


    /**
     * This is card_view convenience method to read all items from database
     *
     * @return A list of Item objects stored in database.
     */
    private List<Item> readItemsFromDB() {
        return SQLite.select().
                from(Item.class).queryList();
    }

    /**
     * This method checks if the app is running for the first time or not.
     *
     * @return True if this app is running first time. Running first time after card_view version upgrade also return true. Other wise return false.
     */
    private boolean isFirstRun() {
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.SHARED_PREFS_NAME, MODE_PRIVATE);
        int lastVersion = sharedPreferences.getInt(Constants.SHARED_PREFS_VERSION, -1);
        sharedPreferences.edit().putInt(Constants.SHARED_PREFS_VERSION, currentVersion).apply();

        if (lastVersion == -1) {
            // This is the first run ever.
            return true;
        } else if (lastVersion < currentVersion) {
            // This is the first run for this version.
            return true;
        }

        // Not first run.
        return false;
    }


    /**
     * This method add items to ArrayList for the first time run.
     */
    private void runFirstTime() {
        newItem("Welcome to SimplyDo!", "High");
        newItem("You can create new tasks by pressing on add button.", "Medium");
        newItem("You can edit a task by pressing on it.", "Low");
        newItem("You can also delete tasks. Just press on a task and press delete.", "Medium");
    }


    /**
     * This is card_view convenience method to create card_view new item and dislplay on the UI
     */
    private void newItem(String desc, String priority) {
        Item item = new Item();
        item.setDesc(desc);
        item.setPriority(priority);
        item.setDueDate(new Date());
        item.save();

        itemsArrayList.add(item);
    }


    /**
     * This method initializes and sets up the Activity.
     */
    private void setUpActivity() {
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Your to do list");
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }


    /**
     * This method initializes and sets up the Recycler View for to-do items.
     */
    private void setUpRecyclerView() {
        recyclerView = ((RecyclerView) findViewById(R.id.rvItems));
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        customAdapter = new CustomAdapter(itemsArrayList, this);
        recyclerView.setAdapter(customAdapter);
        customAdapter.notifyDataSetChanged();

    }


}
