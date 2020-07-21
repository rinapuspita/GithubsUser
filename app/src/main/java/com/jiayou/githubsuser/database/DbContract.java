package com.jiayou.githubsuser.database;

import android.net.Uri;
import android.provider.BaseColumns;

public class DbContract {
    public static final String AUTHORITY = "com.jiayou.githubsuser";
    public static final String SCHEME = "content";

    public static final class UserColumns implements BaseColumns {
        public static final String TABLE_NAME = "listuser";
        public static final String ID = "id";
        public static final String USERNAME = "username";
        static final String AVATAR = "avatar";
        public static final Uri CONTENT_URI = new Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME)
                .build();
    }
}
