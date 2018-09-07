package com.example.stutee.bakingappdemo.data;

import android.net.Uri;
import android.provider.BaseColumns;

public class BakingAppWidgetContract {
    public static final String CONTENT_AUTHORITY = "com.example.stutee.bakingappdemo";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_INGREDIENT_DETAIL = "ingredient_detail";


    public static final class IngredientDetailEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_INGREDIENT_DETAIL)
                .build();

        /* Used internally as the name of our weather table. */
        public static final String TABLE_NAME = "ingredient_detail";

        /* Movie ID as returned by API */
        public static final String INGREDIENT_NAME = "ingredient_name";


        /* Movie title as returned by API */
        public static final String INGREDIENT_MEASURE = "ingredient_measure";


        /* Movie overview as returned by API */
        public static final String INGREDIENT_QUANTITY = "ingredient_quantity";

    }
}

