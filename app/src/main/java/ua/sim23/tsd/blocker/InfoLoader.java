package ua.sim23.tsd.blocker;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.ArraySet;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.Set;

public class InfoLoader {
    private static Set<String> checkedApps;
    private static SharedPreferences sp;

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void Load(MainActivity activity){
        sp = activity.getSharedPreferences(activity.getPackageName() , Context.MODE_PRIVATE);
        checkedApps = sp.getStringSet("saved apps",new ArraySet<String>());

    }

    public static void addAppToList(String pack){
        checkedApps.add(pack);
        sp.edit().putStringSet("saved apps",checkedApps).apply();
    }
    public static void delAppToList(String pack){
        checkedApps.remove(pack);
        sp.edit().putStringSet("saved apps",checkedApps).apply();
    }

    public static boolean isIncheckedApps(String packname){
        return checkedApps.contains(packname);
    }
    public static Set<String> getCheckedApps() {
        return checkedApps;
    }
}
