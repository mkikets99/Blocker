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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
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
    public static List<AppInfo> vapps;
    ListView AppListView;
    Button refresh;
    EditText nameOrPack;
    public static ArrayAdapter<AppInfo> adapter;
    MainActivity ma;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_list_all_apps);
        nameOrPack = findViewById(R.id.nameOrPack);
        nameOrPack.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //Toast.makeText(ListAllApps.this,"before",Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Toast.makeText(ListAllApps.this,"Changed",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void afterTextChanged(Editable s) {
                //
                String text = s.toString();
//                Toast.makeText(ListAllApps.this,text,Toast.LENGTH_SHORT).show();
                vapps.clear();
                for(AppInfo app : apps){
                    if(app.name.toString().contains(text)||app.label.toString().contains(text)){
                        vapps.add(app);
                    }
                }
                vapps.sort(new Comparator<AppInfo>() {
                    @Override
                    public int compare(AppInfo o1, AppInfo o2) {
                        return o1.label.toString().compareTo(o2.label.toString());
                    }
                });
                adapter.notifyDataSetChanged();
            }
        });
        /**/

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
                adapter = new ArrayAdapter<AppInfo>(this, R.layout.item_element, vapps) {

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

                        final AppInfo appInfo = vapps.get(position);

                        if (appInfo != null) {
                            viewHolder.icon.setImageDrawable(appInfo.icon);
                            viewHolder.label.setText(appInfo.label);
                            viewHolder.name.setText(appInfo.name);
                            viewHolder.cb.setChecked(InfoLoader.isIncheckedApps(appInfo.name.toString()));
                            viewHolder.cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                    try {
                                        if (isChecked) {
                                            InfoLoader.addAppToList(appInfo.name.toString());

                                        } else {
                                            InfoLoader.delAppToList(appInfo.name.toString());
                                        }
                                    }catch(Exception e){
                                        //Toast.makeText(ListAllApps.this,appInfo.name,Toast.LENGTH_LONG).show();
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
                vapps = new ArrayList<AppInfo>();

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
                vapps.clear();
                vapps.addAll(apps);
                vapps.sort(new Comparator<AppInfo>() {
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