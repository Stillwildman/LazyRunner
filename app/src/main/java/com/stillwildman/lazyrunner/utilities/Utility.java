package com.stillwildman.lazyrunner.utilities;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.widget.Toast;

import com.stillwildman.lazyrunner.AppController;

/**
 * Created by vincent.chang on 2017/4/12.
 */

public class Utility {

    public static void forceCloseTask() {
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    public static void toastShort(String msg) {
        Toast.makeText(AppController.getInstance().getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    public static void toastLong(String msg) {
        Toast.makeText(AppController.getInstance().getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }

    public static void logLongInfo(String tagName, String str) {
        if(str.length() > 4000) {
            Log.i(tagName, str.substring(0, 4000));
            logLongInfo(tagName, str.substring(4000));
        }
        else
            Log.i(tagName, str);
    }

    public static int getScreenWidth() {
        DisplayMetrics dm = AppController.getInstance().getApplicationContext().getResources().getDisplayMetrics();
        return dm.widthPixels;
    }

    public static int getScreenHeight() {
        DisplayMetrics dm = AppController.getInstance().getApplicationContext().getResources().getDisplayMetrics();
        return dm.heightPixels;
    }

    public static int getActionbarHeight() {
        Context mContext = AppController.getInstance().getApplicationContext();

        TypedValue tv = new TypedValue();

        int actionBarHeight = 0;
        int statusBarHeight = 0;

        if (mContext.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true))
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, mContext.getResources().getDisplayMetrics());

        int resourceId = mContext.getResources().getIdentifier("status_bar_height", "dimen", "android");

        if (resourceId > 0)
            statusBarHeight = mContext.getResources().getDimensionPixelSize(resourceId);

        Log.i("ActionBarHeight", "ActionBarHeight: " + actionBarHeight + " StatusBarHeight: " + statusBarHeight);

        return actionBarHeight + statusBarHeight;
    }

    public static int getStatusBarHeight() {
        int resourceId = AppController.getInstance().getResources().getIdentifier("status_bar_height", "dimen", "android");

        int statusBarHeight = 0;

        if (resourceId > 0)
            statusBarHeight = AppController.getInstance().getResources().getDimensionPixelSize(resourceId);

        Log.i("ActionBarHeight", "StatusBarHeight: " + statusBarHeight);

        return statusBarHeight;
    }

    /**
     * @return The height of Actionbar or Toolbar from the default android attributes.
     */
    public static int getActionbarSize() {
        final TypedArray styledAttributes = AppController.getInstance().getApplicationContext().getTheme()
                .obtainStyledAttributes(new int[] {android.R.attr.actionBarSize});

        int actionBarSize = (int) styledAttributes.getDimension(0, 0);
        styledAttributes.recycle();

        return actionBarSize;
    }
}
