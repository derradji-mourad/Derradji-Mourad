package AppTracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DataBaseHelperAll extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "AllApps.db";
    private static final String TABLE_NAME = "All_APPS";
    private static final String PACKAGE_NAME = "PACKAGE_NAME";
    private static final String APP_NAME = "APP_NAME";
    private static final String TIME_SPENT = "TIME_SPENT";

    private static final int VERSION = 1;
    public DataBaseHelperAll(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db2) {
        db2.execSQL("CREATE TABLE " + TABLE_NAME + "(PACKAGE_NAME TEXT PRIMARY KEY," +
                "TIME_SPENT INTEGER, APP_NAME TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db2, int oldVersion, int newVersion) {
        db2.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db2);
    }

    void insert(String packageName, int timeSpent,String appName) {
        SQLiteDatabase db2 = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PACKAGE_NAME, packageName);
        contentValues.put(TIME_SPENT, timeSpent);
        contentValues.put(APP_NAME,appName);
        long i = db2.insert(TABLE_NAME, null, contentValues);
        db2.close();
    }

    void delete(String packageName) {
        SQLiteDatabase db2 = this.getWritableDatabase();
        db2.delete(TABLE_NAME, PACKAGE_NAME + " = ?", new String[]{packageName});
        db2.close();
    }

   AllAppInfo getRow(String packageName) {
     AllAppInfo allAppInfo = null;
        SQLiteDatabase db2 = this.getReadableDatabase();
        Cursor cursor = db2.rawQuery("SELECT * FROM " +TABLE_NAME+ " WHERE " + PACKAGE_NAME + " = ?", new String[]{packageName});
        if(cursor.moveToFirst()) {
            int timeSpent = cursor.getInt(cursor.getColumnIndex(TIME_SPENT));
            String appName = cursor.getString(cursor.getColumnIndex(APP_NAME));
            allAppInfo = new AllAppInfo(packageName,appName, timeSpent );
        }
        cursor.close();
        db2.close();
        return allAppInfo;
    }

    public List<AllAppInfo> getAllRows() {
        List<AllAppInfo> allAppInfos = new ArrayList<>();
        SQLiteDatabase db2 = this.getReadableDatabase();
        Cursor cursor = db2.rawQuery("SELECT * FROM " +TABLE_NAME, null);
        while(cursor.moveToNext()) {
            String packageName = cursor.getString(cursor.getColumnIndex(PACKAGE_NAME));
            String appName= cursor.getString(cursor.getColumnIndex(APP_NAME));
            int timeSpent = cursor.getInt(cursor.getColumnIndex(TIME_SPENT));
            allAppInfos.add(new AllAppInfo(packageName, appName, timeSpent));
        }
        cursor.close();
        db2.close();
        return allAppInfos;
    }
    void setAllApp(String packageName, int timeSpent, String appName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TIME_SPENT, timeSpent);
        contentValues.put(APP_NAME,appName);
        db.update(TABLE_NAME, contentValues, PACKAGE_NAME + " = ?", new String[]{packageName});
        db.close();
    }
    public Cursor readAllData(){
        String query = "SELECT * FROM " +TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        if(db !=null){
            cursor=db.rawQuery(query,null);

        }
        return cursor;
    }


}
