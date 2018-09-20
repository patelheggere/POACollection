package com.patelheggere.poacollection.dbhelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    // Table Name
    public static final String TABLE_NAME_POI = "POI";
    public static final String TABLE_NAME_PA = "PA";

    // Table columns
    public static final String _ID = "_id";
    public static final String POI_NUMBER = "poi_no";
    public static final String NAME = "name";
    public static final String CATEGORY = "category";
    public static final String SUB_CAT = "sub_cat";
    public static final String BUILD_NAME = "b_name";
    public static final String BUILD_NUMBER = "b_number";
    public static final String NO_OF_FLOOR = "no_floor";
    public static final String BRAND = "brand";
    public static final String LAND_MARK = "land_mark";
    public static final String STREET = "street";
    public static final String LOCALITY = "locality";
    public static final String PINCODE = "pin_code";
    public static final String COMMENT = "comment";
    public static final String LAT = "lat";
    public static final String LON = "lon";
    public static final String PHONE = "phone";
    public static final String PERSON_NAME = "person_name";
    public static final String DATE = "date";

    // Database Information
    static final String DB_NAME = "POI3.DB";

    // database version
    static final int DB_VERSION = 1;

    // Creating table query
    private static final String CREATE_TABLE_POI = "create table " + TABLE_NAME_POI + "(" + _ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + NAME + " TEXT , " + CATEGORY + " TEXT , "+ SUB_CAT +" TEXT ,"
            +BUILD_NAME +" TEXT , "+BUILD_NUMBER+" TEXT ,"+NO_OF_FLOOR+" TEXT , "+BRAND+" TEXT , "+LAND_MARK+" TEXT, "
            +STREET+" TEXT , "+LOCALITY+" TEXT , "+PINCODE+" TEXT , "+COMMENT+" TEXT ,"+POI_NUMBER+" TEXT ,"+LAT+" TEXT , "+LON+" TEXT ,"+PHONE+" TEXT ,"+PERSON_NAME+" TEXT ,"+DATE+" );";

    // Creating table query
    private static final String CREATE_TABLE_PA = "create table " + TABLE_NAME_PA + "(" + _ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + NAME + " TEXT , " + CATEGORY + " TEXT , "+ SUB_CAT +" TEXT ,"
            +BUILD_NAME +" TEXT , "+BUILD_NUMBER+" TEXT ,"+NO_OF_FLOOR+" TEXT , "+BRAND+" TEXT , "+LAND_MARK+" TEXT, "
            +STREET+" TEXT , "+LOCALITY+" TEXT , "+PINCODE+" TEXT , "+COMMENT+" TEXT ,"+POI_NUMBER+" TEXT ,"+LAT+" TEXT , "+LON+" TEXT ,"+PHONE+" TEXT ,"+PERSON_NAME+" TEXT ,"+DATE+" );";


    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_POI);
        db.execSQL(CREATE_TABLE_PA);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_POI);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_PA);
        onCreate(db);
    }
}
