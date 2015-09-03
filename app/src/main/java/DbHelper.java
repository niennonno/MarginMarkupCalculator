package com.mapplinks.calculator;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Aditya Vikram on 8/20/2015.
 */
public class DbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "taxes.db";
    private static final int DATABASE_VERSION = 1;
    public static String COLUMN_ID ="id";
    public static String COLUMN_TAX_NAME ="tax_name";
    public static String COLUMN_VALUE ="value";
    public static String TABLE_NAME="taxes";
    private static DbHelper singleton = null;

    public static DbHelper getInstance(Context context) {
        if(singleton == null) {
            singleton = new DbHelper(context);

        }
        return singleton;
    }


    private DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS "
                +TABLE_NAME + " ("
                + COLUMN_ID +" INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_TAX_NAME +"TEXT UNIQUE NOT NULL, "
                + COLUMN_VALUE +" DOUBLE NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
