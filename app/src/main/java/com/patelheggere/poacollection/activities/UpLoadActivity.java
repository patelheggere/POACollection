package com.patelheggere.poacollection.activities;

import android.database.Cursor;
import androidx.appcompat.app.AppCompatActivity;
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

public class UpLoadActivity extends AppCompatActivity {

    private Button btnUpload;
    private TextView mTvCount;
    private DBManager dbManager;
    private Cursor mCursor;
    private DatabaseReference mDBRef;
    private FirebaseAuth mAuth;
    private int nofItems;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_up_load);
        btnUpload = findViewById(R.id.btn_upload);
        mTvCount = findViewById(R.id.tv_no_of_items);
        dbManager = new DBManager(UpLoadActivity.this);
        dbManager.open();
        mCursor = dbManager.fetch();
        nofItems=mCursor.getCount();
        mAuth = FirebaseAuth.getInstance();
        mTvCount.setText("No of  Details to upload:"+mCursor.getCount());
        if(nofItems>0)
        {
            btnUpload.setEnabled(true);
        }
        else {
            btnUpload.setEnabled(false);
        }
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCursor = dbManager.fetch();
                POIDetails ob = new POIDetails();
                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                mDBRef = firebaseDatabase.getReference().child("LandPoint").child(mAuth.getCurrentUser().getPhoneNumber());
                if (mCursor.moveToFirst()){
                    do{
                        ob.setmPersonName(mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.PERSON_NAME)));
                        ob.setmPhoneNumberr(mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.PHONE)));
                        ob.setName(mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.NAME)));
                        ob.setmPOINumber(mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.POI_NUMBER)));
                        ob.setCategory(mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.CATEGORY)));
                        ob.setSubCat(mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.SUB_CAT)));
                        ob.setbName(mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.BUILD_NAME)));
                        ob.setbNumber(mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.BUILD_NUMBER)));
                        ob.setNoFloor(mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.NO_OF_FLOOR)));
                        ob.setBrand(mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.BRAND)));
                        ob.setLandMark(mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.LAND_MARK)));
                        ob.setStreet(mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.STREET)));
                        ob.setLocality(mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.LOCALITY)));
                        ob.setPincode(mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.PINCODE)));
                        ob.setComment(mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.COMMENT)));
                        ob.setmLattitude(mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.LAT)));
                        ob.setmLonggitude(mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.LON)));
                        ob.setmDate(mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.DATE)));
                        mDBRef.push().setValue(ob);
                        dbManager.delete(mCursor.getLong(mCursor.getColumnIndex(DatabaseHelper._ID)));
                        nofItems = nofItems-1;
                        mTvCount.setText("No of Details to upload:"+nofItems);
                        // do what ever you want here
                    }while(mCursor.moveToNext());
                    Toast.makeText(UpLoadActivity.this, "Uploaded Successfully to server", Toast.LENGTH_LONG).show();
                }
                //mDBRef.push().setValue(paModel);
            }
        });
    }
}
