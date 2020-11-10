package ua.sim23.tsd.blocker;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    PackageManager packageManager;

    public static List<AppInfo> apps;
    ListView AppListView;
    Button openList;
    public static ArrayAdapter<AppInfo> adapter;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        InfoLoader.Load(this);
        setContentView(R.layout.activity_main);

        openList = findViewById(R.id.OpenListButton);

        openList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,ListAllApps.class);
                startActivityForResult(i,1);

            }
        });

        apps = null;
        adapter = null;
        loadApps();
        loadListView();
        addGridListeners();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==1){
            if(resultCode==1){
                update();
            }
        }
        super.onActivityResult(requestCode,resultCode,data);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void update(){

        apps = null;
        adapter = null;
        loadApps();
        loadListView();
        addGridListeners();
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBackPressed() {

        update();
    }

    @Override
    public void onTrimMemory(int level) {
        if(level == TRIM_MEMORY_UI_HIDDEN){

        }
    }

    public void addGridListeners() {
        try {
            AppListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent = packageManager.getLaunchIntentForPackage(apps.get(i).name.toString());
                    //assert intent != null;
                    //intent.addCategory(Intent.CATEGORY_LAUNCHER);
                    MainActivity.this.startActivity(intent);
                    //MainActivity.this.finish();
                }
            });
        } catch (Exception ex) {
            Toast.makeText(MainActivity.this, ex.getMessage().toString() + " Grid", Toast.LENGTH_LONG).show();
            Log.e("Error Grid", ex.getMessage().toString() + " Grid");
        }

    }


    private void loadListView() {

        try {
            AppListView = (ListView) findViewById(R.id.AppListView);
            if (adapter == null) {
                adapter = new ArrayAdapter<AppInfo>(this, R.layout.main_item_element, apps) {

                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {

                        ViewHolderItem viewHolder = null;

                        if (convertView == null) {
                            convertView = getLayoutInflater().inflate(
                                    R.layout.main_item_element, parent, false
                            );
                            viewHolder = new ViewHolderItem();
                            viewHolder.icon = (ImageView) convertView.findViewById(R.id.appImageView);
                            viewHolder.name = (TextView) convertView.findViewById(R.id.appCodeNameTextView);
                            viewHolder.label = (TextView) convertView.findViewById(R.id.appNameTextView);

                            convertView.setTag(viewHolder);
                        } else {
                            viewHolder = (ViewHolderItem) convertView.getTag();
                        }

                        AppInfo appInfo = apps.get(position);

                        if (appInfo != null) {
                            viewHolder.icon.setImageDrawable(appInfo.icon);
                            viewHolder.label.setText(appInfo.label);
                            viewHolder.name.setText(appInfo.name);
                        }
                        return convertView;

                    }

                    final class ViewHolderItem {
                        ImageView icon;
                        TextView label;
                        TextView name;
                    }
                };
            }

            AppListView.setAdapter(adapter);
        } catch (Exception ex) {
            Toast.makeText(MainActivity.this, ex.getMessage().toString() + " loadListView", Toast.LENGTH_LONG).show();
            Log.e("Error loadListView", ex.getMessage().toString() + " loadListView");
        }

    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void loadApps() {
        try {

            packageManager = getPackageManager();
            if (apps == null) {
                apps = new ArrayList<AppInfo>();

                Intent i = new Intent(Intent.ACTION_MAIN, null);
                i.addCategory(Intent.CATEGORY_LAUNCHER);
                Set<String> appList = InfoLoader.getCheckedApps();
                //List<ApplicationInfo> availableApps = packageManager.getInstalledApplications(PackageManager.GET_META_DATA);
                for (String app : appList) {
                    AppInfo appinfo = new AppInfo();
                    ApplicationInfo ri = packageManager.getApplicationInfo(app,0);
                    appinfo.label = ri.loadLabel(packageManager);
                    appinfo.name = ri.packageName;
                    appinfo.icon = ri.loadIcon(packageManager);
                    apps.add(appinfo);

                }
                apps.sort(new Comparator<AppInfo>() {
                    @Override
                    public int compare(AppInfo o1, AppInfo o2) {
                        return o1.label.toString().compareTo(o2.label.toString());
                    }
                });
            }

        } catch (Exception ex) {
            Toast.makeText(MainActivity.this, ex.getMessage().toString() + " loadApps", Toast.LENGTH_LONG).show();
            Log.e("Error loadApps", ex.getMessage().toString() + " loadApps");
        }

    }
}