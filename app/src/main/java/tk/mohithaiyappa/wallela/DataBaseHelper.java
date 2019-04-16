package tk.mohithaiyappa.wallela;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME="Favorites.db";
    private static final String TABLE_NAME="favorites_table";
    private static final String COL_1="ID";
    private static final String COL_2="LOW";
    private static final String COL_3="MID";
    private static final String COL_4="HIGH";

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 3);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL("CREATE TABLE "+TABLE_NAME+" (ID INTEGER PRIMARY KEY AUTOINCREMENT,LOW VARCHAR(50),MID VARCHAR(50),HIGH VARCHAR(50))");
        }catch (SQLException e){
            e.printStackTrace();
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }
    public boolean insertData(String lowResUrl,String midResUrl,String hiResUrl){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2,lowResUrl);
        contentValues.put(COL_3,midResUrl);
        contentValues.put(COL_4,hiResUrl);
        if(db.insert(TABLE_NAME,null ,contentValues)==-1) return false;
        else return true;
    }
    public Cursor getData(){
        SQLiteDatabase db = this.getWritableDatabase();
        int i =(int) DatabaseUtils.queryNumEntries(db,TABLE_NAME );
        if(i==0) {
                    return null;
                 }
        Cursor cursor = db.rawQuery("SELECT * FROM "+TABLE_NAME,null );
        return cursor;
    }
    public boolean dataExits(String lowResUrl){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+TABLE_NAME,null );
        while (cursor.moveToNext()){
            if(cursor.getString(1).equals(lowResUrl))
                return true;
        }
        return false;
    }
    public boolean dropEntry(String lowResUrl){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM favorites_table WHERE LOW='"+lowResUrl+"'");
        return true;
    }
}
