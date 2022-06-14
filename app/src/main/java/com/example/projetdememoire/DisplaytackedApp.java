package com.example.projetdememoire;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import AppTracker.AllAppInfo;
import AppTracker.DataBaseHelperAll;
import AppTracker.UsageState;

public class DisplaytackedApp extends AppCompatActivity {
RecyclerView recyclerView;
FloatingActionButton add_button;
DataBaseHelperAll myDB;
SQLiteDatabase sq;
ArrayList<String> PackageName,AppName,TimeSpent;
CustomAdapter customAdapter;
Button btn;
    String[]permission;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_displaytacked_app);
        permission=new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
        recyclerView=findViewById(R.id.recyclerView);
        add_button=findViewById(R.id.add_button);
        btn=findViewById(R.id.btnEx);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(VerifierStoragePermission()){
                    exportUserCSV();
                }else{
                    DemandPermission();
                }
            }
        });
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DisplaytackedApp.this, UsageState.class));
            }
        });
        myDB= new DataBaseHelperAll(this);
        PackageName = new ArrayList<>();
        AppName = new ArrayList<>();
        TimeSpent = new ArrayList<>();
        displayData();
        customAdapter = new CustomAdapter(DisplaytackedApp.this,PackageName,AppName,TimeSpent);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(DisplaytackedApp.this));
    }
    void displayData(){
        myDB=new DataBaseHelperAll(this);
        sq=myDB.getReadableDatabase();
        Cursor cursor = myDB.readAllData();
        if(cursor.getCount()==0){
            Toast.makeText(this,"no data.",Toast.LENGTH_SHORT).show();
        }else{
            while (cursor.moveToNext()){
                PackageName.add(cursor.getString(0));
                AppName.add(cursor.getString(2));
                TimeSpent.add(String.valueOf(cursor.getInt(1)));
            }
        }
    }
    private boolean VerifierStoragePermission(){
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)==(PackageManager.PERMISSION_GRANTED);
        return result;
    }
    private void DemandPermission(){
        ActivityCompat.requestPermissions(this,permission,1);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch(requestCode){
            case 1:
            {
                if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    exportUserCSV();
                }else {
                    Toast.makeText(this,"Permission needed",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void exportUserCSV(){
        File folder = new File(Environment.getExternalStorageDirectory()+"/"+"UserCSV");
        boolean folderCree=false;
        if(!folder.exists()){
            folderCree=folder.mkdir();
        }
        Log.d("CSV_TAG","EXPORT CSV"+folderCree);
        String csvFile="User.csv";
        String path =folder+"/"+csvFile;
        List<AllAppInfo> lsApp = new ArrayList<>();
        lsApp= myDB.getAllRows();
       try {
            FileWriter fw = new FileWriter(path);
            for(int i=0;i<lsApp.size();i++){
                fw.append(lsApp.get(i).getPackageName());
                fw.append(",");
                fw.append(lsApp.get(i).getAppName());
                fw.append(",");
                fw.append((char) lsApp.get(i).getTimeSpent());
                fw.append("\n");
            }
            fw.flush();
            fw.close();
            Toast.makeText(this,"Exporting Finished",Toast.LENGTH_SHORT).show();
        }catch (Exception ex){
            Toast.makeText(this,""+ex.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }
}