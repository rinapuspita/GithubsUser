package com.jiayou.githubsuser.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.jiayou.githubsuser.model.ItemResponse;

import java.util.ArrayList;

import static com.jiayou.githubsuser.database.DbContract.UserColumns.AVATAR;
import static com.jiayou.githubsuser.database.DbContract.UserColumns.ID;
import static com.jiayou.githubsuser.database.DbContract.UserColumns.TABLE_NAME;
import static com.jiayou.githubsuser.database.DbContract.UserColumns.USERNAME;

public class GithubHelper {
    private static final String DATABASE_TABLE = TABLE_NAME;
    private static DbHelper dbHelper;
    private static GithubHelper INST;
    private static SQLiteDatabase database;

    public GithubHelper(Context c) {
        dbHelper = new DbHelper(c);
    }

    public static GithubHelper getInstance(Context c) {
        if (INST == null) {
            synchronized (SQLiteOpenHelper.class) {
                if (INST == null) {
                    INST = new GithubHelper(c);
                }
            }
        }
        return INST;
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
        if (database.isOpen())
            database.close();
    }

    public Cursor queryAll() {
        return database.query(DATABASE_TABLE,
                null,
                null,
                null,
                null,
                null,
                ID+ " ASC");
    }

    public Cursor queryById(String id) {
        return database.query(DATABASE_TABLE, null
                , ID + " = ?"
                , new String[]{id}
                , null
                , null
                , null
                , null);
    }
    public ArrayList<ItemResponse> getDataUser(){
        ArrayList<ItemResponse> itemList = new ArrayList<>();
        Cursor cursor = database.query(DATABASE_TABLE,null,
                null,
                null,
                null,
                null,
                USERNAME + " ASC",
                null);
        cursor.moveToFirst();
        ItemResponse itemResponse;
        if (cursor.getCount() > 0){
            do {
                itemResponse = new ItemResponse();
                itemResponse.setId(cursor.getInt(cursor.getColumnIndexOrThrow(ID)));
                itemResponse.setLogin(cursor.getString(cursor.getColumnIndexOrThrow(USERNAME)));
                itemResponse.setAvatarUrl(cursor.getString(cursor.getColumnIndexOrThrow(AVATAR)));
                itemList.add(itemResponse);
                cursor.moveToNext();
            }while (!cursor.isAfterLast());
        }cursor.close();
        return itemList;
    }

    public long userInsert(ItemResponse itemResponse){
        ContentValues contentValues = new ContentValues();
        contentValues.put(ID, itemResponse.getId());
        contentValues.put(USERNAME, itemResponse.getLogin());
        contentValues.put(AVATAR, itemResponse.getAvatarUrl());

        return database.insert(DATABASE_TABLE,null, contentValues);
    }

    public int userDelete(String id){
        return database.delete(TABLE_NAME,ID + " = '" + id + "'", null);
    }

    public int deleteProvider(String id) {
        return database.delete(TABLE_NAME, ID + "=?",new String[]{id});
    }
    public int updateProvider(String id, ContentValues values) {
        return database.update(DATABASE_TABLE, values, ID + " =?", new String[]{id});
    }
    public long insertProvider(ContentValues values) {
        return database.insert(DATABASE_TABLE, null, values);
    }
}
