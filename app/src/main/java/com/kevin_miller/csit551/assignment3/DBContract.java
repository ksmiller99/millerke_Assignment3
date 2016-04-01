package com.kevin_miller.csit551.assignment3;

import android.provider.BaseColumns;

/**
 * Created by Kevin on 3/30/2016.
 */
public class DBContract {

        // To prevent someone from accidentally instantiating the contract class,
        // give it an empty constructor.
        public DBContract() {}

        /* Inner class that defines the table contents */
        public static abstract class FeedEntry implements BaseColumns {
            public static final String DB_NAME = "U5_Database.db";
            public static final String TABLE_NAME = "DB_Main_Table";
            public static final String COL_ID = "entryid";
            public static final String COL_USERNAME = "username";
            public static final String COL_FULLNAME = "fullname";
            public static final String COL_DOB = "dob";
            public static final String COL_MAJOR = "major";
            public static final String COL_EMAIL = "email";
            public static final String COL_PASS = "password";
            public static final int DB_VERSION = 2;
        }
    }


