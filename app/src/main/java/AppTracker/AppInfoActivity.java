package AppTracker;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.projetdememoire.R;

import java.util.Calendar;
import java.util.HashMap;
/*Cette activity a le but de gérer tout les function pour fonctionement de l'application  */
public class AppInfoActivity extends AppCompatActivity {
    private static final String TAG = "AppInfoActivity";
    private static final int LAUNCH_SETTINGS_ACTIVITY = 1;
    private DatabaseHelper dbHelper;
    private DataBaseHelperAll dbHelper2;
    private String packageName;
    private String appName;
    private TextView appNameView;
    private ImageView appIcon;
    private Spinner hour1;
    private Spinner minute1;
    private TextView hour2;
    private TextView minute2;
    private TextView second2;
    private Button saveButton;
    private Button stopButton;
    private ImageView warning;
    private TrackedAppInfo trackedAppInfo;
    private AllAppInfo allAppInfo;
    private int timeAllowed;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_info);
        if(!UtilsT.isUsageAccessAllowed(this)) {
            openUsageDialog();

        }

        packageName = getIntent().getStringExtra("packageName");
        appName = getIntent().getStringExtra("appName");
        dbHelper = new DatabaseHelper(this);
        dbHelper2=new DataBaseHelperAll(this);

        appNameView = findViewById(R.id.chart_app_name);
        appIcon = findViewById(R.id.list_app_icon);
        hour1 = findViewById(R.id.hour1);
        minute1 = findViewById(R.id.minute1);
        hour2 = findViewById(R.id.hour2);
        minute2 = findViewById(R.id.minute2);
        second2 = findViewById(R.id.second2);
        saveButton = findViewById(R.id.save);
        stopButton = findViewById(R.id.stop);

        warning = findViewById(R.id.isUsageExceeded);
        editAllApp();
        setSpinner();
        setAppNameAndImage();

/*ici quand l'utilisateur choisie une application pour tracker
l'application montre que le temps saisie est elle vas rendre le button stop visible si l'utilisateur veut arreter de tracker
 le button stop vas appler la fonction openTrackingDialog */
        trackedAppInfo = dbHelper.getRow(packageName);
        if(trackedAppInfo != null) {
            timeAllowed = trackedAppInfo.getTimeAllowed();
            showTimeAllowed(timeAllowed);
            stopButton.setVisibility(View.VISIBLE);
            saveButton.setText(R.string.save);
            stopButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openTrackingDialog();
                }
            });
        }
        /*le button save vas appler la fonction editTrackingInfo*/
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTrackingInfo();
            }
        });


    }
/*quand on resume l'application on vas recuperer le temps d'utilisation de l'application
et faire un comparison entre le temps d'utilisation et le temps permis
 si le temps d'utilisation a dépasser le temps permis
 on vas afficher l'icon warning et la coleur du text vas devenir rouge*/
    @Override
    protected void onResume() {
        super.onResume();
        int timeSpent = getTimeSpent();
        showTimeSpent(timeSpent);
        if(trackedAppInfo != null) {
            if(timeSpent > timeAllowed) {
                warning.setVisibility(ImageView.VISIBLE);
                hour2.setTextColor(getResources().getColor(R.color.warning, null));
                minute2.setTextColor(getResources().getColor(R.color.warning, null));
                second2.setTextColor(getResources().getColor(R.color.warning, null));
            }
        }
    }
/*Cette a le but de regler le spinner pour que l'utilisateur peut saisir le temps permis*/
    private void setSpinner() {
        Integer[] hourValues = new Integer[24];
        final Integer[] minuteValues = new Integer[60];
        for(int i=0; i<24; i++) {
            hourValues[i] = i;
        }
        for(int i=0; i<60; i++) {
            minuteValues[i] = i;
        }
        ArrayAdapter<Integer> hourAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, hourValues);
        ArrayAdapter<Integer> minuteAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, minuteValues);
        hour1.setAdapter(hourAdapter);
        minute1.setAdapter(minuteAdapter);
    }

/*Cette fonction permis de recupérer le nom et icon de l'application */
    private void setAppNameAndImage() {
        appNameView.setText(appName);
        PackageManager packageManager = getPackageManager();
        try {
            appIcon.setImageDrawable(packageManager.getApplicationIcon(packageName));
        } catch (PackageManager.NameNotFoundException e) {
            Log.d(TAG, "package name not found");
        }
    }
/*Cette fonction convert le temps saisie en format que lutilisateur peut comprendre*/
    private void showTimeAllowed(int timeAllowed) {
        int[] timesAllowed = UtilsT.reverseProcessTime(timeAllowed);
        hour1.setSelection(timesAllowed[0]);
        minute1.setSelection(timesAllowed[1]);
    }
/*Cette fonction vas montre le temps d'utilisation de l'application*/
    private void showTimeSpent(int timeSpent) {
        int[] timesAllowed = UtilsT.reverseProcessTime(timeSpent);
        hour2.setText(String.valueOf(timesAllowed[0]));
        minute2.setText(String.valueOf(timesAllowed[1]));
        second2.setText(String.valueOf(timesAllowed[2]));
    }
/*Comment on vas recupérer le temps d'utilisation */
    private int getTimeSpent() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        long beginTime = calendar.getTimeInMillis();
        long endTime = beginTime + UtilsT.DAY_IN_MILLIS;
        HashMap<String, Integer> appUsageMap = UtilsT.getTimeSpent(this, packageName, beginTime, endTime);
        Integer usageTime = appUsageMap.get(packageName);
        if (usageTime == null) usageTime = 0;
        return usageTime;
    }
    /*Cette fonction vas ajouter on mettre a jour le nom d'application ,sont temps d'utilisation et son package que on veut tracker*/
    private void editAllApp(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        long beginTime = calendar.getTimeInMillis();
        long endTime = beginTime + UtilsT.DAY_IN_MILLIS;
        HashMap<String, Integer> appUsageMap = UtilsT.getTimeSpent(this, packageName, beginTime, endTime);
        Integer usageTime = appUsageMap.get(packageName);
        if (usageTime == null) usageTime = 0;
        int editTime = usageTime;
        appName = getIntent().getStringExtra("appName");
        String editAppName = appName;
        if(dbHelper2.getRow(packageName)==null){
            dbHelper2.insert(packageName,editTime,editAppName);
        }else{
            dbHelper2.setAllApp(packageName,editTime,editAppName);
        }
    }
/*ici on vas mettre a jour et voir si l'utilisateur a tracker lapplication et si il a depasser le temps permis ou nn*/
    private void editTrackingInfo() {
        Integer hour = Integer.parseInt(hour1.getSelectedItem().toString());
        Integer minute = Integer.parseInt(minute1.getSelectedItem().toString());
        int editedTimeAllowed = UtilsT.processTime(hour,minute,0);
        if(dbHelper.getRow(packageName) == null) {
            dbHelper.insert(packageName, editedTimeAllowed);
        }
        else {
            dbHelper.setTimeAllowed(packageName, editedTimeAllowed);
        }
        if(editedTimeAllowed > getTimeSpent()) {
            dbHelper.resetIsUsageExceeded(packageName);
        }
        Toast.makeText(getApplicationContext(),"Changes Saved!", Toast.LENGTH_LONG).show();
        finish();
    }
/*Cette function a le but de demander l'autorisation de recupiration des données */
    private void openUsageDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Usage Access Needed :(")
                .setMessage("You need to give usage access to this app to see usage data of your apps. " +
                        "Click \"Go To Settings\" and then give the access :)")
                .setPositiveButton("Go To Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent usageAccessIntent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
                        startActivityForResult(usageAccessIntent, LAUNCH_SETTINGS_ACTIVITY);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setCancelable(false);
        builder.show();
    }
/*Si l'utilisateur decide de arreter le trackage */
    private void openTrackingDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Do you want to stop tracking this app?")
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dbHelper.delete(packageName);
                        dialog.dismiss();
                        finish();
                    }
                })
                .setCancelable(false);
        builder.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == LAUNCH_SETTINGS_ACTIVITY) {
            if(UtilsT.isUsageAccessAllowed(this)) {
                Alarms.scheduleNotification(this);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbHelper.close();
    }
}