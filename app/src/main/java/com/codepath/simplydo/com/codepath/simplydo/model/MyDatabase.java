package com.codepath.simplydo.com.codepath.simplydo.model;

import com.raizlabs.android.dbflow.annotation.Database;

/**
 * This class contains the database configuration properties.
 */

@Database(name = MyDatabase.NAME, version = MyDatabase.VERSION)
public class MyDatabase {
    public static final String NAME = "MyDataBase";

    public static final int VERSION = 1;
}