package ua.sim23.tsd.blocker;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ListAllApps extends AppCompatActivity {


    PackageManager packageManager;

    public static List<AppInfo> apps;
    ListView AppListView;
    Button refresh;
    public static ArrayAdapter<AppInfo> adapter;
    MainActivity ma;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_list_all_apps);


        refresh = findViewById(R.id.RefreshButton);

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                apps = null;
                adapter = null;
                loadApps();
                loadListView();
                addGridListeners();
            }
        });

        apps = null;
        adapter = null;
        loadApps();
        loadListView();
        addGridListeners();
    }

    @Override
    public void onBackPressed() {

        setResult(1);
        super.onBackPressed();
    }

    public void addGridListeners() {
        try {

        } catch (Exception ex) {
            Toast.makeText(ListAllApps.this, ex.getMessage().toString() + " Grid", Toast.LENGTH_LONG).show();
            Log.e("Error Grid", ex.getMessage().toString() + " Grid");
        }

    }


    private void loadListView() {

        try {
            AppListView = (ListView) findViewById(R.id.AppListView);
            if (adapter == null) {
                adapter = new ArrayAdapter<AppInfo>(this, R.layout.item_element, apps) {

                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {

                        ViewHolderItem viewHolder = null;

                        if (convertView == null) {
                            convertView = getLayoutInflater().inflate(
                                    R.layout.item_element, parent, false
                            );
                            viewHolder = new ViewHolderItem();
                            viewHolder.icon = (ImageView) convertView.findViewById(R.id.appImageView);
                            viewHolder.name = (TextView) convertView.findViewById(R.id.appCodeNameTextView);
                            viewHolder.label = (TextView) convertView.findViewById(R.id.appNameTextView);
                            viewHolder.cb = (CheckBox) convertView.findViewById(R.id.isEnabledCheckBox);

                            convertView.setTag(viewHolder);
                        } else {
                            viewHolder = (ViewHolderItem) convertView.getTag();
                        }

                        final AppInfo appInfo = apps.get(position);

                        if (appInfo != null) {
                            viewHolder.icon.setImageDrawable(appInfo.icon);
                            viewHolder.label.setText(appInfo.label);
                            viewHolder.name.setText(appInfo.name);
                            viewHolder.cb.setChecked(InfoLoader.isIncheckedApps(appInfo.name.toString()));
                            viewHolder.cb.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if(!(InfoLoader.isIncheckedApps(appInfo.name.toString()))) {
                                        InfoLoader.addAppToList(appInfo.name.toString());

                                    }else{
                                        InfoLoader.delAppToList(appInfo.name.toString());
                                    }
                                }
                            });
                        }
                        return convertView;

                    }

                    final class ViewHolderItem {
                        ImageView icon;
                        TextView label;
                        TextView name;
                        CheckBox cb;
                    }
                };
            }

            AppListView.setAdapter(adapter);
        } catch (Exception ex) {
            Toast.makeText(ListAllApps.this, ex.getMessage().toString() + " loadListView", Toast.LENGTH_LONG).show();
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

                List<PackageInfo> availableApps = packageManager.getInstalledPackages(0);
                for (PackageInfo ri : availableApps) {
                    AppInfo appinfo = new AppInfo();
                    appinfo.label = ri.applicationInfo.loadLabel(packageManager);
                    appinfo.name = ri.packageName;
                    appinfo.icon = ri.applicationInfo.loadIcon(packageManager);
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
            Toast.makeText(ListAllApps.this, ex.getMessage().toString() + " loadApps", Toast.LENGTH_LONG).show();
            Log.e("Error loadApps", ex.getMessage().toString() + " loadApps");
        }

    }
}