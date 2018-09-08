package com.patelheggere.poacollection.activities;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.patelheggere.poacollection.R;
import com.patelheggere.poacollection.dbhelper.DBManager;
import com.patelheggere.poacollection.dbhelper.DatabaseHelper;
import com.patelheggere.poacollection.models.POIDetails;

import java.util.Date;

import static com.patelheggere.poacollection.dbhelper.DatabaseHelper.LAT;
import static com.patelheggere.poacollection.dbhelper.DatabaseHelper._ID;

public class UpLoadActivity extends AppCompatActivity {

    private Button btnUpload;
    private TextView mTvCount;
    private DBManager dbManager;
    private Cursor mCursor1, mCursor2;
    private DatabaseReference mDBRef, mDBRef2;
    private FirebaseAuth mAuth;
    private int nofItems1, nofItems2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_up_load);
        btnUpload = findViewById(R.id.btn_upload);
        mTvCount = findViewById(R.id.tv_no_of_items);
        dbManager = new DBManager(UpLoadActivity.this);
        dbManager.open();
        mCursor1 = dbManager.fetch(1);
        mCursor2 = dbManager.fetch(2);
        nofItems1=mCursor1.getCount();
        mAuth = FirebaseAuth.getInstance();
        mTvCount.setText("No of POI Details to upload:"+mCursor1.getCount()+"\n No of PA Details to upload:"+mCursor2.getCount());
        if(nofItems1>0 || nofItems2>0)
        {
            btnUpload.setEnabled(true);
        }
        else {
            btnUpload.setEnabled(false);
        }
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCursor1 = dbManager.fetch(1);
                mCursor2 = dbManager.fetch(2);
                POIDetails ob = new POIDetails();
                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                mDBRef = firebaseDatabase.getReference().child("POICollections").child(mAuth.getCurrentUser().getPhoneNumber());
                mDBRef2 = firebaseDatabase.getReference().child("PACollections").child(mAuth.getCurrentUser().getPhoneNumber());
                if (mCursor1.moveToFirst()) {
                    do {
                        ob.setmPersonName(mCursor1.getString(mCursor1.getColumnIndex(DatabaseHelper.PERSON_NAME)));
                        ob.setmPhoneNumberr(mCursor1.getString(mCursor1.getColumnIndex(DatabaseHelper.PHONE)));
                        ob.setName(mCursor1.getString(mCursor1.getColumnIndex(DatabaseHelper.NAME)));
                        ob.setmPOINumber(mCursor1.getString(mCursor1.getColumnIndex(DatabaseHelper.POI_NUMBER)));
                        ob.setCategory(mCursor1.getString(mCursor1.getColumnIndex(DatabaseHelper.CATEGORY)));
                        ob.setSubCat(mCursor1.getString(mCursor1.getColumnIndex(DatabaseHelper.SUB_CAT)));
                        ob.setbName(mCursor1.getString(mCursor1.getColumnIndex(DatabaseHelper.BUILD_NAME)));
                        ob.setbNumber(mCursor1.getString(mCursor1.getColumnIndex(DatabaseHelper.BUILD_NUMBER)));
                        ob.setNoFloor(mCursor1.getString(mCursor1.getColumnIndex(DatabaseHelper.NO_OF_FLOOR)));
                        ob.setBrand(mCursor1.getString(mCursor1.getColumnIndex(DatabaseHelper.BRAND)));
                        ob.setLandMark(mCursor1.getString(mCursor1.getColumnIndex(DatabaseHelper.LAND_MARK)));
                        ob.setStreet(mCursor1.getString(mCursor1.getColumnIndex(DatabaseHelper.STREET)));
                        ob.setLocality(mCursor1.getString(mCursor1.getColumnIndex(DatabaseHelper.LOCALITY)));
                        ob.setPincode(mCursor1.getString(mCursor1.getColumnIndex(DatabaseHelper.PINCODE)));
                        ob.setComment(mCursor1.getString(mCursor1.getColumnIndex(DatabaseHelper.COMMENT)));
                        ob.setmLattitude(mCursor1.getString(mCursor1.getColumnIndex(DatabaseHelper.LAT)));
                        ob.setmLonggitude(mCursor1.getString(mCursor1.getColumnIndex(DatabaseHelper.LON)));
                        ob.setmDate(mCursor1.getString(mCursor1.getColumnIndex(DatabaseHelper.DATE)));
                        mDBRef.push().setValue(ob);
                        dbManager.delete(mCursor1.getLong(mCursor1.getColumnIndex(DatabaseHelper._ID)), 1);
                        nofItems1 = nofItems1 - 1;
                        mTvCount.setText("No of POI Details to upload:" + nofItems1);
                        // do what ever you want here
                    } while (mCursor1.moveToNext());
                }

                    if (mCursor2.moveToFirst()){
                        do{
                            ob.setmPersonName(mCursor2.getString(mCursor2.getColumnIndex(DatabaseHelper.PERSON_NAME)));
                            ob.setmPhoneNumberr(mCursor2.getString(mCursor2.getColumnIndex(DatabaseHelper.PHONE)));
                            ob.setName(mCursor2.getString(mCursor2.getColumnIndex(DatabaseHelper.NAME)));
                            ob.setmPOINumber(mCursor2.getString(mCursor2.getColumnIndex(DatabaseHelper.POI_NUMBER)));
                            ob.setCategory(mCursor2.getString(mCursor2.getColumnIndex(DatabaseHelper.CATEGORY)));
                            ob.setSubCat(mCursor2.getString(mCursor2.getColumnIndex(DatabaseHelper.SUB_CAT)));
                            ob.setbName(mCursor2.getString(mCursor2.getColumnIndex(DatabaseHelper.BUILD_NAME)));
                            ob.setbNumber(mCursor2.getString(mCursor2.getColumnIndex(DatabaseHelper.BUILD_NUMBER)));
                            ob.setNoFloor(mCursor2.getString(mCursor2.getColumnIndex(DatabaseHelper.NO_OF_FLOOR)));
                            ob.setBrand(mCursor2.getString(mCursor2.getColumnIndex(DatabaseHelper.BRAND)));
                            ob.setLandMark(mCursor2.getString(mCursor2.getColumnIndex(DatabaseHelper.LAND_MARK)));
                            ob.setStreet(mCursor2.getString(mCursor2.getColumnIndex(DatabaseHelper.STREET)));
                            ob.setLocality(mCursor2.getString(mCursor2.getColumnIndex(DatabaseHelper.LOCALITY)));
                            ob.setPincode(mCursor2.getString(mCursor2.getColumnIndex(DatabaseHelper.PINCODE)));
                            ob.setComment(mCursor2.getString(mCursor2.getColumnIndex(DatabaseHelper.COMMENT)));
                            ob.setmLattitude(mCursor2.getString(mCursor2.getColumnIndex(DatabaseHelper.LAT)));
                            ob.setmLonggitude(mCursor2.getString(mCursor2.getColumnIndex(DatabaseHelper.LON)));
                            ob.setmDate(mCursor2.getString(mCursor2.getColumnIndex(DatabaseHelper.DATE)));
                            mDBRef2.push().setValue(ob);
                            dbManager.delete(mCursor2.getLong(mCursor2.getColumnIndex(DatabaseHelper._ID)), 2);
                            nofItems2 = nofItems2-1;
                            mTvCount.setText("No of POI Details to upload:"+nofItems1);
                            // do what ever you want here
                        }while(mCursor2.moveToNext());
                    Toast.makeText(UpLoadActivity.this, "Uploaded Successfully to server", Toast.LENGTH_LONG).show();

                }
                //mDBRef.push().setValue(paModel);
            }
        });
    }
}
