package ua.sim23.tsd.blocker;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.ArraySet;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class InfoLoader {
    private static List<String> checkedApps;
    //private static SharedPreferences sp;
    private static Activity a;
    private static String Password;

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void Load(MainActivity activity){
        //sp = activity.getSharedPreferences(activity.getPackageName() , Context.MODE_PRIVATE);
        a = activity;
        try {
            String Apps = FileRead("savedapps");
            checkedApps = Arrays.asList(Apps.split("#"));
            if(checkedApps==null) checkedApps = new ArrayList<String>();
            Password = FileRead("password");
        }catch(Exception e){
            e.printStackTrace();
            checkedApps = new ArrayList<String>();
        }
    }
    private static void FileWrite(String file, String data) throws IOException {
        FileOutputStream fileOutputStream = a.openFileOutput(file, Context.MODE_PRIVATE);
        fileOutputStream.write(data.getBytes());
        fileOutputStream.close();
    }
    private static String FileRead(String file) throws IOException {
        FileInputStream fileInputStream = a.openFileInput(file);
        int read = -1;
        StringBuffer buffer = new StringBuffer();
        while((read =fileInputStream.read())!= -1){
            buffer.append((char)read);
        }
        return buffer.toString();
    }
    public static boolean passwordChack(String pass){
        return Password.equals(pass);
    }
    public static boolean isPassSet(){
        return Password != null;
    }
    public static void setPassword(String password){
        Password = password;
        try {
            FileWrite("password",password);
        }catch(Exception e){
            e.printStackTrace();
            Toast.makeText(a,e.getMessage(),Toast.LENGTH_LONG).show();
        }
        //sp.edit().putString("password",Password).apply();
    }

    public static void addAppToList(String pack){
        try {
            checkedApps.add(pack);
            String delim = "#";

            StringBuilder sb = new StringBuilder();

            int i = 0;
            while (i < checkedApps.size() - 1) {
                sb.append(checkedApps.get(i));
                sb.append(delim);
                i++;
            }
            sb.append(checkedApps.get(i));

            String res = sb.toString();
            FileWrite("savedapps",res);
        }catch(Exception e){
            e.printStackTrace();
            Toast.makeText(a,e.getMessage(),Toast.LENGTH_LONG).show();
        }
        //sp.edit().putStringSet("saved apps",checkedApps).apply();
    }
    public static void delAppToList(String pack){
        checkedApps.remove(pack);
        try {
            String delim = "#";

            StringBuilder sb = new StringBuilder();

            int i = 0;
            while (i < checkedApps.size() - 1) {
                sb.append(checkedApps.get(i));
                sb.append(delim);
                i++;
            }
            sb.append(checkedApps.get(i));

            String res = sb.toString();
            FileWrite("savedapps",res);
        }catch(Exception e){
            e.printStackTrace();
            Toast.makeText(a,e.getMessage(),Toast.LENGTH_LONG).show();
        }
        //sp.edit().putStringSet("saved apps",checkedApps).apply();
    }

    public static boolean isIncheckedApps(String packname){
        return checkedApps.contains(packname);
    }
    public static List<String> getCheckedApps() {
        return checkedApps;
    }
}
