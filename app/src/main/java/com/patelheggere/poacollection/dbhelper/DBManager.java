package com.patelheggere.poacollection.dbhelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.patelheggere.poacollection.models.POIDetails;

public class DBManager {
    private DatabaseHelper dbHelper;

    private Context context;

    private SQLiteDatabase database;

    public DBManager(Context c) {
        context = c;
    }

    public DBManager open() throws SQLException {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public void insert(POIDetails object) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHelper.NAME, object.getName());
        contentValue.put(DatabaseHelper.CATEGORY, object.getCategory());
        contentValue.put(DatabaseHelper.SUB_CAT, object.getSubCat());
        contentValue.put(DatabaseHelper.BUILD_NAME, object.getbName());
        contentValue.put(DatabaseHelper.BUILD_NUMBER, object.getbNumber());
        contentValue.put(DatabaseHelper.NO_OF_FLOOR, object.getNoFloor());
        contentValue.put(DatabaseHelper.BRAND, object.getBrand());
        contentValue.put(DatabaseHelper.LAND_MARK, object.getLandMark());
        contentValue.put(DatabaseHelper.STREET, object.getStreet());
        contentValue.put(DatabaseHelper.LOCALITY, object.getLocality());
        contentValue.put(DatabaseHelper.PINCODE, object.getPincode());
        contentValue.put(DatabaseHelper.COMMENT, object.getComment());
        contentValue.put(DatabaseHelper.POI_NUMBER, object.getmPOINumber());
        contentValue.put(DatabaseHelper.LAT, object.getmLattitude());
        contentValue.put(DatabaseHelper.LON, object.getmLonggitude());
        contentValue.put(DatabaseHelper.PERSON_NAME, object.getmPersonName());
        contentValue.put(DatabaseHelper.PHONE, object.getmPhoneNumberr());
        contentValue.put(DatabaseHelper.DATE, object.getmDate());
        System.out.println("Inserted:"+database.insert(DatabaseHelper.TABLE_NAME, null, contentValue));
    }

    public Cursor fetch() {
        String[] columns = new String[] { DatabaseHelper._ID,  DatabaseHelper.NAME };
        Cursor cursor;// = database.query(DatabaseHelper.TABLE_NAME, columns, null, null, null, null, null);
        cursor = database.rawQuery("SELECT * FROM "+DatabaseHelper.TABLE_NAME, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }
/*
    public int update(long _id, String name, String desc) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.SUBJECT, name);
        contentValues.put(DatabaseHelper.DESC, desc);
        int i = database.update(DatabaseHelper.TABLE_NAME, contentValues, DatabaseHelper._ID + " = " + _id, null);
        return i;
    }*/

    public void delete(long _id) {
        database.delete(DatabaseHelper.TABLE_NAME, DatabaseHelper._ID + "=" + _id, null);
    }
}