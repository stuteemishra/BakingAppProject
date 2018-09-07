package com.example.stutee.bakingappdemo.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class BakingAppWidgetProvider extends ContentProvider {

    public static final int CODE_INGREDIENT_DETAIL = 100;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private BakingAppWidgetDbHelper mOpenHelper;


    public static UriMatcher buildUriMatcher() {

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = BakingAppWidgetContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, BakingAppWidgetContract.PATH_INGREDIENT_DETAIL, CODE_INGREDIENT_DETAIL);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new BakingAppWidgetDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor cursor;

        switch (sUriMatcher.match(uri)) {
            case CODE_INGREDIENT_DETAIL: {
                cursor = mOpenHelper.getReadableDatabase().query(
                        BakingAppWidgetContract.IngredientDetailEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);

                break;

            }
            default:

                throw new UnsupportedOperationException("Unknown uri: " + uri);

        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
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
        return null;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {

        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        switch (sUriMatcher.match(uri)) {

            case CODE_INGREDIENT_DETAIL:
                db.beginTransaction();
                int rowsInserted = 0;
                try {
                    for (ContentValues value : values) {

                        long _id = db.insert(BakingAppWidgetContract.IngredientDetailEntry.TABLE_NAME,null, value);

                        if (_id != -1) {
                            rowsInserted++;
                        }
                    }

                    db.setTransactionSuccessful();

                } finally {

                    db.endTransaction();
                }

                if (rowsInserted > 0) {

                    getContext().getContentResolver().notifyChange(uri, null);
                }

                return rowsInserted;

            default:
                return super.bulkInsert(uri, values);

        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {

        int numRowsDeleted;

        if (null == selection) selection = "1";

        switch (sUriMatcher.match(uri)) {

            case CODE_INGREDIENT_DETAIL:
                numRowsDeleted = mOpenHelper.getWritableDatabase().delete(
                        BakingAppWidgetContract.IngredientDetailEntry.TABLE_NAME,
                        selection,
                        selectionArgs);
                break;



            default:

                throw new UnsupportedOperationException("Unknown uri: " + uri);

        }

        /* If we actually deleted any rows, notify that a change has occurred to this URI */

        if (numRowsDeleted != 0) {

            getContext().getContentResolver().notifyChange(uri, null);

        }

        return numRowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}

