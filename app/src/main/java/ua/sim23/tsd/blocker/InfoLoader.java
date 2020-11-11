package ua.sim23.tsd.blocker;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.ArraySet;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.Set;

public class InfoLoader {
    private static Set<String> checkedApps;
    private static SharedPreferences sp;
    private static String Password;
    private static Activity a;

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void Load(MainActivity activity){
        a = activity;
        sp = activity.getSharedPreferences(activity.getPackageName() , Context.MODE_PRIVATE);
        checkedApps = sp.getStringSet("saved apps",new ArraySet<String>());
        Password = sp.getString("password",null);

    }
    public static boolean passwordChack(String pass){
        return Password.equals(pass);
    }
    public static boolean isPassSet(){
        return Password != null;
    }
    public static void setPassword(String password){
        Password = password;
        boolean safe = sp.edit().putString("password",Password).commit();
        if(safe){
            Toast.makeText(a,"Password setted",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(a,"Error while saving password",Toast.LENGTH_SHORT).show();
        }
    }

    public static void addAppToList(String pack){
        checkedApps.add(pack);
        boolean safe = sp.edit().putStringSet("saved apps",checkedApps).commit();
        if(safe){
            Toast.makeText(a,"apps saved",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(a,"Error while saving apps",Toast.LENGTH_SHORT).show();
        }
    }
    public static void delAppToList(String pack){
        checkedApps.remove(pack);
        boolean safe = sp.edit().putStringSet("saved apps",checkedApps).commit();
        if(safe){
            Toast.makeText(a,"apps setted",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(a,"Error while saving apps",Toast.LENGTH_SHORT).show();
        }
    }

    public static boolean isIncheckedApps(String packname){
        return checkedApps.contains(packname);
    }
    public static Set<String> getCheckedApps() {
        return checkedApps;
    }
}
