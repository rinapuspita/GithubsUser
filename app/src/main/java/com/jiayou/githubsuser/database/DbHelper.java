package com.jiayou.githubsuser.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import static com.jiayou.githubsuser.database.DbContract.UserColumns.TABLE_NAME;

public class DbHelper extends SQLiteOpenHelper {
    private static final String USER_DB_NAME = "GithubUser";
    private static final int USER_DB_VERSION = 1;

    private static final String SQL_CREATE_TABLE_USER = String.format(
            "CREATE TABLE %s" +
                    "(%s INTEGER PRIMARY KEY," +
                    "%s TEXT NOT NULL," +
                    "%s TEXT NOT NULL)",
            TABLE_NAME,
            DbContract.UserColumns.ID,
            DbContract.UserColumns.USERNAME,
            DbContract.UserColumns.AVATAR
    );


    public DbHelper(Context context){
        super(context,USER_DB_NAME,null,USER_DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_USER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
