package com.example.stutee.bakingappdemo.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BakingAppWidgetDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "ingredient_detail.db";

    private static final int DATABASE_VERSION = 1;

    public BakingAppWidgetDbHelper(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_WEATHER_TABLE =



                "CREATE TABLE " + BakingAppWidgetContract.IngredientDetailEntry.TABLE_NAME + " (" +

                        BakingAppWidgetContract.IngredientDetailEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+

                        BakingAppWidgetContract.IngredientDetailEntry.INGREDIENT_NAME + " TEXT NOT NULL, " +

                        BakingAppWidgetContract.IngredientDetailEntry.INGREDIENT_MEASURE + " TEXT NOT NULL, " +

                        BakingAppWidgetContract.IngredientDetailEntry.INGREDIENT_QUANTITY + " TEXT NOT NULL );";


        db.execSQL(SQL_CREATE_WEATHER_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + BakingAppWidgetContract.IngredientDetailEntry.TABLE_NAME);
        onCreate(db);

    }
}
