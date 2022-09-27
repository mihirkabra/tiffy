package com.example.tiffy;

import static com.example.tiffy.Login.EMAIL;
import static com.example.tiffy.Login.Email;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class SQLiteHelper extends SQLiteOpenHelper {

    public static final String TABLE_NAME="UserTable";
    public static final String Table_Column_ID="id";
    public static final String Table_Column_1_Name="name";
    public static final String Table_Column_2_Email="email";
    public static final String Table_Column_3_Password="password";
    static String DATABASE_NAME="UserDataBase.db";


    public SQLiteHelper(Context context) {

        super(context, DATABASE_NAME, null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase database) {

        String CREATE_TABLE="CREATE TABLE IF NOT EXISTS "+TABLE_NAME+" ("+Table_Column_ID+" INTEGER PRIMARY KEY, "+Table_Column_1_Name+" VARCHAR, "+Table_Column_2_Email+" VARCHAR, "+Table_Column_3_Password+" VARCHAR)";
        database.execSQL(CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);

    }
    /*public String getUsername() throws SQLException {
        String username = "";
        Cursor cursor = this.getReadableDatabase().query(
                TABLE_NAME, new String[] { Table_Column_1_Name },
                null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                username = cursor.getString(0);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return username;
    }*/
    public String getUsername(){
        Cursor resultSet = this.getReadableDatabase().rawQuery("Select name from " +TABLE_NAME,null);
        resultSet.moveToFirst();
        String value = resultSet.getString(0);
        resultSet.close();
        return  value;

    }
    public String getDataEmail()
    {
        String userName ="";

        Cursor mCursor = this.getReadableDatabase().rawQuery("SELECT name FROM " + TABLE_NAME + " WHERE email = '"+Email+"'" , null);
        mCursor.moveToFirst();

        if(mCursor.getCount() > 0) {
        userName = mCursor.getString(0);
        }

        return userName;

    }
    public String getDataEMAIL()
    {
        String userName ="";

        Cursor mCursor = this.getReadableDatabase().rawQuery("SELECT name FROM " + TABLE_NAME + " WHERE email = '"+EMAIL+"'" , null);
        mCursor.moveToFirst();

        if(mCursor.getCount() > 0) {
            userName = mCursor.getString(0);
        }

        return userName;

    }

}

