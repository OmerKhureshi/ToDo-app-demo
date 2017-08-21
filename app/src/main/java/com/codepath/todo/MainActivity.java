package com.codepath.todo;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String EXTRA_MESSAGE = "com.codepath.todo.MESSAGE";
    public static final String EXTRA_MESSAGE_ACTION = "com.codepath.todo.MESSAGE.ACTION";
    public static final String EXTRA_MESSAGE_POS = "com.codepath.todo.MESSAGE.POS";
    public static final int REQUEST_CODE_CREATE = 20;
    public static final int REQUEST_CODE_EDIT = 30;

    ArrayList<String> itemsArrayList;
    ArrayAdapter<String> itemsAdapter;
    ListView lvItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        lvItems = (ListView) findViewById(R.id.lvItems);
        itemsArrayList = new ArrayList<>();
        itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, itemsArrayList);
        lvItems.setAdapter(itemsAdapter);
        itemsArrayList.add("Welcome!");
        itemsArrayList.add("Click on me to edit");
        itemsArrayList.add("You can delete as well");
        itemsArrayList.add("Press on add to create new item");

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = itemsArrayList.get(position);
                System.out.println("Item clicked on: " + item);

                Intent intent = new Intent(MainActivity.this, EditItemActivity.class);
                intent.putExtra(EXTRA_MESSAGE, item);
                intent.putExtra(EXTRA_MESSAGE_POS, position);
                startActivityForResult(intent, REQUEST_CODE_EDIT);
            }
        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        getSupportActionBar().setTitle("Your to do list");
    }

    public void createItem(View view) {
        System.out.println("Clicked fab button");
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
        // as you specify a parent activity in AndroidManifest.xml.
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
        if (resultCode == RESULT_OK) {

            String action = data.getExtras().getString(EXTRA_MESSAGE_ACTION);

            if (requestCode == REQUEST_CODE_CREATE) {

                String item = data.getExtras().getString(EXTRA_MESSAGE);
                System.out.println("Created item: " + item);
                itemsArrayList.add(item);

            } else if (requestCode == REQUEST_CODE_EDIT) {

                if (action.equalsIgnoreCase("EDIT_SAVE")) {

                    String item = data.getExtras().getString(EXTRA_MESSAGE);
                    int pos = data.getExtras().getInt(EXTRA_MESSAGE_POS);
                    System.out.println("Edited item: " + item);
                    itemsArrayList.set(pos, item);

                } else if (action.equalsIgnoreCase("EDIT_DELETE")) {

                    int pos = data.getExtras().getInt(EXTRA_MESSAGE_POS);
                    System.out.println("deleting " + pos);
                    itemsArrayList.remove(pos);

                }
            }

            itemsAdapter.notifyDataSetChanged();
        }
    }
}