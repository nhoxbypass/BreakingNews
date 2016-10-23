package com.example.nhoxb.breakingnews.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;

/**
 * Created by nhoxb on 10/23/2016.
 */
public class UiUtils {
    public static float convertDpToPixel(float dp, Context context)
    {
        Resources resources = context.getResources();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        return dp * (float)(displayMetrics.densityDpi/DisplayMetrics.DENSITY_DEFAULT);
    }
}
