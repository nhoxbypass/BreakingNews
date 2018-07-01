package com.example.nhoxb.breakingnews.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;

/**
 * Created by nhoxb on 10/23/2016.
 */
public class UiUtils {
    public static float convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        return dp * (float) (displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    public static AlertDialog createAlertDialog(Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage("Connect to wifi or quit")
                .setCancelable(false)
                .setPositiveButton("Connect to WIFI", (dialog, id) -> {
                    activity.startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                })
                .setNegativeButton("Quit", (dialog, id) -> {
                    activity.finish();
                });
        return builder.create();
    }
}
