package com.jiayou.githubsuser.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.jiayou.githubsuser.database.GithubHelper;

import java.util.Objects;

import static com.jiayou.githubsuser.database.DbContract.AUTHORITY;
import static com.jiayou.githubsuser.database.DbContract.UserColumns.CONTENT_URI;
import static com.jiayou.githubsuser.database.DbContract.UserColumns.TABLE_NAME;

public class GithubProvider extends ContentProvider {
    private static final int GITHUB = 1;
    private static final int GITHUB_ID = 2;
    private GithubHelper githubHelper;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {

        // content://com.jiayou.githubsuser/model/ItemResponse
        sUriMatcher.addURI(AUTHORITY, TABLE_NAME, GITHUB);

        // content://com.jiayou.githubsuser/model/ItemResponse/id
        sUriMatcher.addURI(AUTHORITY,
                TABLE_NAME + "/#",
                GITHUB_ID);
    }

    @Override
    public boolean onCreate() {
        githubHelper = GithubHelper.getInstance(getContext());
        githubHelper.open();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor cursor;
        switch (sUriMatcher.match(uri)) {
            case GITHUB:
                cursor = githubHelper.queryAll();
                break;
            case GITHUB_ID:
                cursor = githubHelper.queryById(uri.getLastPathSegment());
                break;
            default:
                cursor = null;
                break;
        }

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        long added;
        Uri contentUri = null;
        switch (sUriMatcher.match(uri)) {
            case GITHUB:
                added = githubHelper.insertProvider(values);
                if (added > 0) {
                    contentUri = ContentUris.withAppendedId(CONTENT_URI, added);
                }
                break;
            default:
                added = 0;
                break;
        }

        getContext().getContentResolver().notifyChange(CONTENT_URI, null);

        return Uri.parse(CONTENT_URI + "/" + added);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int deleted;
        switch (sUriMatcher.match(uri)) {
            case GITHUB_ID:
                deleted = githubHelper.deleteProvider(uri.getLastPathSegment());
                break;
            default:
                deleted = 0;
                break;
        }
        getContext().getContentResolver().notifyChange(CONTENT_URI, null);
        if (deleted > 0) {
            Objects.requireNonNull(getContext()).getContentResolver().notifyChange(uri, null);

        }
        return deleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        int updated;
        switch (sUriMatcher.match(uri)) {
            case GITHUB_ID:
                updated = githubHelper.updateProvider(uri.getLastPathSegment(), values);
                break;
            default:
                updated = 0;
                break;
        }
        getContext().getContentResolver().notifyChange(CONTENT_URI, null);
        if (updated > 0) {
            Objects.requireNonNull(getContext()).getContentResolver().notifyChange(uri, null);
        }

        return updated;
    }
}
