package com.patelheggere.poacollection.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.patelheggere.poacollection.models.PAModel;
import com.patelheggere.poacollection.R;

import java.util.Date;

public class AddPAActivity extends AppCompatActivity {

    private double lat, lon;
    private EditText etHouseNo, etHouseName, etStreetName, etLocality, etSBL1, etSB2, etSB3, etPin, etLat, etLon;
    private EditText etPlaceName, Address, Phone, Email, website, comment;
    private Button mSubmit, mCancel;
    private Spinner Category;
    private String mName = "";
    private String mMoobile = "";
    private String mUid = "";
    private  boolean flag = false;
    private  PAModel paModel = new PAModel();
    private DatabaseReference mDBRef;
    private FirebaseAuth mAuth;
    private String mCategoryString;
    //test
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popupwindowfor_pa);
        lat = getIntent().getDoubleExtra("lat", 0.0f);
        lon = getIntent().getDoubleExtra("lon", 0.0f);
        mName = getIntent().getStringExtra("Name");
        mMoobile = getIntent().getStringExtra("mobile");
        mUid = getIntent().getStringExtra("uid");
        mAuth = FirebaseAuth.getInstance();
        initialiseUI();
    }

    private void initialiseUI()
    {
        getSupportActionBar().setTitle("Enter POA Details");
        etPlaceName = findViewById(R.id.placename);
        Category = findViewById(R.id.category);
        Address = findViewById(R.id.address);
        Phone = findViewById(R.id.phone);
        Email = findViewById(R.id.etEmail);
        website = findViewById(R.id.etWebsite);
        comment = findViewById(R.id.etComment);
        etPin = findViewById(R.id.etPIN);
        etLat = findViewById(R.id.etLat);
        etLon = findViewById(R.id.etLon);
        etLat.setText(String.valueOf(lat));
        etLon.setText(String.valueOf(lon));
        mSubmit = findViewById(R.id.btnSubmit);
        mCancel = findViewById(R.id.cancel);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.array_cat, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Applying the adapter to our spinner
        Category.setAdapter(adapter);
        Category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mCategoryString = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });

        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void submit()
    {
        /*
        if(!etLocality.getText().toString().equals(""))
            paModel.setEtLocality((etLocality.getText().toString()));
        else {
            etLocality.setError("Should not be empty");
            flag = false;
            return;
        }

       if(!etPin.getText().toString().equals("") || etPin.getText().length()==6)
            paModel.setEtPin((etPin.getText().toString()));
        else {
            etPin.setError("Should not be empty or PIN must be 6 digit");
            flag = false;
            return;
        }*/
        flag = true;
        paModel.setmPlaceName((etPlaceName.getText().toString()));
        paModel.setmPin((etPin.getText().toString()));
        paModel.setmCat(mCategoryString);
       // paModel.setEtHouseNo(etHouseNo.getText().toString());
       // paModel.setEtHouseName(etHouseName.getText().toString());
        paModel.setmAddress(Address.getText().toString());
        paModel.setmComment(comment.getText().toString());
        paModel.setmEmail(Email.getText().toString());
        paModel.setmLat(etLat.getText().toString());
        paModel.setWebsite(website.getText().toString());
        paModel.setmPhone(Phone.getText().toString());
        paModel.setmLon(etLon.getText().toString());
        paModel.setUserName(mAuth.getCurrentUser().getDisplayName());
        paModel.setUserPhone(mMoobile);



        if(flag)
            uploadData();
        else {
            Toast.makeText(AddPAActivity.this, "check all the feilds", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadData()
    {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        mDBRef = firebaseDatabase.getReference().child("POACollections").child(mMoobile);
        paModel.setmDate( new Date().getTime());
        mDBRef.push().setValue(paModel);
        etPlaceName.setText("");
        website.setText("");
        Email.setText("");
        comment.setText("");
        Phone.setText("");
        Address.setText("");
       // etSB3.setText("");
       etPin.setText("");
        //Toast.makeText(AddPAActivity.this, etHouseNo.getText().toString()+"\n"+ etHouseName.getText().toString()+"\n"+etStreetName.getText().toString()+"\n"+etLat.getText().toString()+"\n"+etLon.getText().toString(), Toast.LENGTH_SHORT).show();
    }

}
