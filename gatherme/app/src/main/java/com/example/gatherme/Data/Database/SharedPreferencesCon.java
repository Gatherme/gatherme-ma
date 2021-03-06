package com.example.gatherme.Data.Database;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.gatherme.Enums.Reference;

public class SharedPreferencesCon {

    private static String spName = "spFile";
    private static final String TAG = "SharedPreferencesCon";
    public static final String defValue = "empty";

    public static void save(Context ctx, Reference reference, String value) {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(spName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(reference.toString(), value);
        boolean commit = editor.commit();

    }

    public static String read(Context ctx, Reference reference) {
        String ans;
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(spName, Context.MODE_PRIVATE);
        ans = sharedPreferences.getString(reference.toString(), defValue);
        return ans;
    }

    public static void deleteValue(Context ctx, Reference reference) {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(spName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(reference.toString());
        boolean commit = editor.commit();
    }
}
