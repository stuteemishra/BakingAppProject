package com.example.stutee.bakingappdemo;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.stutee.bakingappdemo.data.BakingAppWidgetContract;

import static com.example.stutee.bakingappdemo.data.BakingAppWidgetContract.BASE_CONTENT_URI;
import static com.example.stutee.bakingappdemo.data.BakingAppWidgetContract.PATH_INGREDIENT_DETAIL;



public class WidgetRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory  {
    private Context mContext;
    private Cursor mCursor;

    public WidgetRemoteViewsFactory(Context applicationContext, Intent intent) {
        mContext = applicationContext;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {

        if (mCursor != null) {
            mCursor.close();
        }

        final long identityToken = Binder.clearCallingIdentity();
        Uri INGREDIENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_INGREDIENT_DETAIL).build();
        mCursor = mContext.getContentResolver().query(
                INGREDIENT_URI,
                null,
                null,
                null,
                BakingAppWidgetContract.IngredientDetailEntry._ID
        );
        Binder.restoreCallingIdentity(identityToken);

    }

    @Override
    public void onDestroy() {
        if (mCursor != null) {
            mCursor.close();
        }
    }

    @Override
    public int getCount() {
        return mCursor == null ? 0 : mCursor.getCount();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        if (position == AdapterView.INVALID_POSITION ||
                mCursor == null || !mCursor.moveToPosition(position)) {
            return null;
        }

        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.collection_widget_list_item);
        rv.setTextViewText(R.id.widgetItemTaskNameLabel, mCursor.getString(1));

        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return mCursor.moveToPosition(position) ? mCursor.getLong(0) : position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }


}
