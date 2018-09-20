package com.patelheggere.poacollection.activities;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.patelheggere.poacollection.dbhelper.DBManager;
import com.patelheggere.poacollection.dbhelper.DatabaseHelper;
import com.patelheggere.poacollection.models.LocationTrack;
import com.patelheggere.poacollection.models.POIDetails;
import com.patelheggere.poacollection.services.LocationService;
import com.patelheggere.poacollection.R;

import java.util.Date;

public class MapLocationActivity extends AppCompatActivity
        implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener, GoogleMap.OnMarkerClickListener, GoogleMap.OnCameraIdleListener,
        GoogleMap.OnMapLoadedCallback,
        GoogleMap.OnCameraMoveListener,
        GoogleMap.OnCameraMoveStartedListener, GoogleMap.OnCameraChangeListener, GoogleMap.OnCameraMoveCanceledListener, GoogleMap.OnMapLongClickListener {

    GoogleMap mGoogleMap;
    SupportMapFragment mapFrag;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    private Button btnAddPA, btncancel, btnsubmit, btnSignOut;
    private PopupWindow mPopupWindow;
    private View customView;
    private double lat;
    private double lon ;
    private static final String TAG = "MapLocation";
    private String mName = "";
    private String mMoobile = "";
    private String mUid = "";
    private TextView mUserName;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseReference;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private Button uploadBtn, btnPOI;

    private DBManager dbManager;
    private Cursor mCursor;
    private ImageView imageViewCurrLocation;
    private  ConstraintLayout myContainer;
    private int type = 1;
    private SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_location);
        mName = getIntent().getStringExtra("Name");
        mMoobile = getIntent().getStringExtra("mobile");
        mUid = getIntent().getStringExtra("uid");
        uploadBtn = findViewById(R.id.btnUploadPOI);
        LayoutInflater layoutInflater = (LayoutInflater) MapLocationActivity.this.getSystemService(LAYOUT_INFLATER_SERVICE);
        customView = layoutInflater.inflate(R.layout.popupwindowfor_pa, (ViewGroup) findViewById(R.id.poplyt));
        initialiseaddbtn();
        signOut();
        cancelbtn();
        submitbtn();
        myContainer = findViewById(R.id.root_view);
        dbManager = new DBManager(MapLocationActivity.this);
        dbManager.open();
        imageViewCurrLocation = findViewById(R.id.iv_curr);
        btnPOI = findViewById(R.id.btnAddPOI);

        getSupportActionBar().setTitle("My Location");
        mapFrag =  (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
        mapFrag.getMapAsync(this);

        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent intent =  new Intent(MapLocationActivity.this, UpLoadActivity.class);
               intent.putExtra("TYPE", type);
                startActivity(intent);
            }
        });

        btnPOI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MapLocationActivity.this, AddPOIActivity.class);
                intent.putExtra("lat", lat);
                intent.putExtra("lon", lon);
                intent.putExtra("name", mName);
                intent.putExtra("mobile", mMoobile);
                intent.putExtra("uid", mUid);
                startActivity(intent);
            }
        });

        btnSignOut.setText("POI click here to chnage");
        btnPOI.setClickable(true);
        btnPOI.setEnabled(true);

        btnAddPA.setEnabled(false);
        btnAddPA.setClickable(false);
        type = 1;
        LoadPOACollections();
        sharedPreferences = getSharedPreferences("TRACKING", MODE_PRIVATE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        LoadPOACollections();
    }

    private void initialiseaddbtn()
    {
        mAuth = FirebaseAuth.getInstance();
        //System.out.println("moble:"+mAuth.getCurrentUser().getPhoneNumber());
        mUserName = findViewById(R.id.userName);
        mUserName.setText("Logged in as:"+mName);
        btnAddPA = findViewById(R.id.btnAddPa);
        btnAddPA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MapLocationActivity.this, AddPAActivity.class);
                intent.putExtra("lat", lat);
                intent.putExtra("lon", lon);
                intent.putExtra("name", mName);
                intent.putExtra("mobile", mMoobile);
                intent.putExtra("uid", mUid);
                startActivity(intent);
               // Toast.makeText(MapLocationActivity.this, "btn", Toast.LENGTH_SHORT).show();
                mPopupWindow = new PopupWindow(customView, 300,300, true);
              // mPopupWindow.isShowing();
            }
        });


    }

    private void cancelbtn()
    {
        btncancel = customView.findViewById(R.id.cancel);
        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow.dismiss();
            }
        });

    }

    private void signOut()
    {
        //stopService(new Intent(MapLocationActivity.this, LocationService.class));
        btnSignOut = findViewById(R.id.btnSignout);
        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btnSignOut.getText().toString().contains("POI"))
                {
                    btnSignOut.setText("PA click here to chnage");
                    type=2;
                    btnPOI.setClickable(false);
                    btnPOI.setEnabled(false);

                    btnAddPA.setEnabled(true);
                    btnAddPA.setClickable(true);
                    mGoogleMap.clear();

                    LoadPOACollections();
                }
                else if(btnSignOut.getText().toString().contains("PA"))
                {
                    btnSignOut.setText("POI click here to chnage");
                    btnPOI.setClickable(true);
                    btnPOI.setEnabled(true);

                    btnAddPA.setEnabled(false);
                    btnAddPA.setClickable(false);
                    type = 1;
                    mGoogleMap.clear();

                    LoadPOACollections();
                }
                //mAuth.signOut();
                //startActivity(new Intent(MapLocationActivity.this, PhoneAuthActivity.class));
                //finish();
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopService(new Intent(MapLocationActivity.this, LocationService.class));
    }

    @Override
    protected void onStart() {
        super.onStart();
        boolean land = sharedPreferences.getBoolean("TRACKING_ENABLE", false);
        if(land)
        {
            startService(new Intent(MapLocationActivity.this, LocationService.class));
        }
    }

    private void submitbtn()
    {
        btnsubmit = customView.findViewById(R.id.btnSubmit);
    }

    @Override
    public void onPause() {
        super.onPause();

        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        mGoogleMap=googleMap;
        mGoogleMap.setOnMarkerClickListener(this);
        mGoogleMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                Log.d(TAG, "onCameraChange: cam:"+cameraPosition.target.latitude);
                Log.d(TAG, "onCameraChange: "+cameraPosition.target.longitude);
            }
        });

        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        mGoogleMap.setOnCameraIdleListener(this);
        mGoogleMap.setOnCameraMoveStartedListener(this);
        mGoogleMap.setOnCameraMoveListener(this);
        mGoogleMap.setOnCameraMoveCanceledListener(this);
        mGoogleMap.setOnMapLongClickListener(this);

        //Initialize Google Play Services
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                buildGoogleApiClient();
                mGoogleMap.setMyLocationEnabled(true);

            } else {
                //Request Location Permission
                checkLocationPermission();
            }
        }
        else {
            buildGoogleApiClient();
            mGoogleMap.setMyLocationEnabled(true);
        }
    }
    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        mLocationRequest = new LocationRequest();
        //mLocationRequest.setInterval(1000);
        //mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    private void LoadPOACollections()
    {
        Log.d(TAG, "LoadPOACollections type:  "+type);
        mCursor = dbManager.fetch(type);
        POIDetails ob = new POIDetails();
        if(mCursor.getCount()>0) {
            if (mCursor.moveToFirst()) {
                do {
                    try {
                        Log.d(TAG, "LoadPOACollections: "+mCursor.getInt(mCursor.getColumnIndex(DatabaseHelper._ID)));
                        LatLng latLng = new LatLng(Double.parseDouble(mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.LAT))), Double.parseDouble(mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.LON))));
                        MarkerOptions markerOptions = new MarkerOptions();
                        markerOptions.position(latLng);
                        if (mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.BUILD_NAME)) != null)
                            markerOptions.title(mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.BUILD_NAME)));
                        else
                            markerOptions.title("No Name");
                        //markerOptions.snippet(markerOptions.getPosition().toString());
                        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                        markerOptions.draggable(false);
                        mGoogleMap.addMarker(markerOptions).setTag(mCursor.getInt(mCursor.getColumnIndex(DatabaseHelper._ID)));
                    }catch (Exception e)
                    {
                        //LoadPOACollections();
                    }
                } while (mCursor.moveToNext());

            }
        }

        /*
        mDatabaseReference = firebaseDatabase.getReference().child("POACollections");
        mDatabaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren())
                {
             PAModel post = postSnapshot.getValue(PAModel.class);
             if(post.getmLon()!=null && post.getmLat()!=null)
             {
                 LatLng latLng = new LatLng(Double.parseDouble(post.getmLat().toString()), Double.parseDouble(post.getmLon().toString()));
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(latLng);
                    markerOptions.title(post.getmPlaceName());
                    markerOptions.snippet(markerOptions.getPosition().toString());
                   // Bitmap mark =  BitmapFactory.decodeResource(getResources(), android.R.drawable.btn_star );
                   // markerOptions.icon(BitmapDescriptorFactory.fromBitmap(mark));
                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                    markerOptions.draggable(false);
                    mGoogleMap.addMarker(markerOptions);
             }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren())
                {
                    PAModel post = postSnapshot.getValue(PAModel.class);
                    if(post.getmLon()!=null && post.getmLat()!=null)
                    {
                        LatLng latLng = new LatLng(Double.parseDouble(post.getmLat().toString()), Double.parseDouble(post.getmLon().toString()));
                        MarkerOptions markerOptions = new MarkerOptions();
                        markerOptions.position(latLng);
                        markerOptions.title(post.getmPlaceName());
                        markerOptions.snippet(markerOptions.getPosition().toString());
                        // Bitmap mark =  BitmapFactory.decodeResource(getResources(), android.R.drawable.btn_star );
                        // markerOptions.icon(BitmapDescriptorFactory.fromBitmap(mark));
                        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                        markerOptions.draggable(false);
                        mGoogleMap.addMarker(markerOptions);
                    }
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        */



    }

    @Override
    public void onConnectionSuspended(int i) {}


    @Override
    public void onLocationChanged(Location location)
    {
        System.out.println("on location changed");
        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }
        //Place current location marker
       // lat = location.getLatitude();
       // lon = location.getLongitude();
        LocationTrack ob = new LocationTrack();
        ob.setmLatitude(lat);
        ob.setmLongitude(lon);
        ob.setmElevation(location.getAltitude());
        ob.setmTime(new Date().getTime());
        //System.out.println("altitude:"+location.getAltitude());
        mDatabaseReference = firebaseDatabase.getReference().child("LocationTrack").child(mMoobile);
        //mDatabaseReference.push().setValue(ob);
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        final MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.snippet(markerOptions.getPosition().toString());
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        markerOptions.draggable(true);
        //textView.setText(markerOptions.getPosition().toString());
        //mCurrLocationMarker = mGoogleMap.addMarker(markerOptions);

        //move map camera
        float zoomLevel = mGoogleMap.getMaxZoomLevel(); //20.0f; //This goes up to 21
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,zoomLevel));
        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(zoomLevel));
        mGoogleMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {
                Log.d(TAG, "onMarkerDragStart: "+marker.getPosition());
            }

            @Override
            public void onMarkerDrag(Marker marker) {
                Log.d(TAG, "onMarkerDragStart: "+marker.getPosition());
            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                Log.d(TAG, "onMarkerDragStart: "+marker.getPosition());
                //btnAddPA.setText(marker.getPosition().toString());
                lat = marker.getPosition().latitude;
                lon = marker.getPosition().longitude;
                markerOptions.snippet(marker.getPosition().toString());
            }
        });
    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(MapLocationActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION );
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION );
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mGoogleMap.setMyLocationEnabled(true);
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
       /* int mapHeight = myContainer.getHeight();
        int mapWidth = myContainer.getWidth();
        Projection projection = mGoogleMap.getProjection();
        LatLng markerPosition = marker.getPosition();
        Point markerPoint = projection.toScreenLocation(markerPosition);
        Point targetPoint = new Point(markerPoint.x, markerPoint.y - myContainer.getHeight() / 2);
        LatLng targetPosition = projection.fromScreenLocation(targetPoint);
        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLng(targetPosition), 1000, null);*/
        showDetailsOfPartner(marker.getTitle(), (int)marker.getTag());
        return false;
    }

    private void showDetailsOfPartner(String details, final int id)
    {
        Log.d(TAG, "showDetailsOfPartner: "+id);
        final LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View customView = inflater.inflate(R.layout.pop_layout, null, false);
        LinearLayout constraintLayout = customView.findViewById(R.id.cl_details);
        mPopupWindow = new PopupWindow(
                customView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT, true
        );

        TextView textView = customView.findViewById(R.id.tv_title);
        textView.setText(details);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setFocusable(true);
        // Removes default background.
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        if (Build.VERSION.SDK_INT >= 21) {
            mPopupWindow.setElevation(5.0f);
        }

       // Animation animation = AnimationUtils.loadAnimation(MapLocationActivity.this, R.anim.zoom_in_one_sec);
       // constraintLayout.startAnimation(animation);

        Button closeButton =  customView.findViewById(R.id.iv_close);
        Button delete = customView.findViewById(R.id.iv_delete);
        // Set a click listener for the popup window close button
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Dismiss the popup window
                mPopupWindow.dismiss();
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbManager.delete(id, type);
                mPopupWindow.dismiss();
                mGoogleMap.clear();
                LoadPOACollections();
            }
        });
        mPopupWindow.showAtLocation(myContainer, Gravity.CENTER, 0, 0);
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
    }

    @Override
    public void onCameraIdle() {
        LatLng latLng = mGoogleMap.getCameraPosition().target;
        Log.d(TAG, "onCameraIdle: "+latLng.latitude +" "+latLng.longitude);
        lat = latLng.latitude;
        lon = latLng.longitude;
    }

    @Override
    public void onCameraMoveCanceled() {

    }

    @Override
    public void onCameraMove() {

    }

    @Override
    public void onCameraMoveStarted(int i) {

    }

    @Override
    public void onMapLoaded() {

    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        imageViewCurrLocation.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.updload:
                boolean land = sharedPreferences.getBoolean("TRACKING_ENABLE", false);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                if(land)
                {
                    editor.putBoolean("TRACKING_ENABLE", false);
                    editor.commit();
                    stopService(new Intent(MapLocationActivity.this, LocationService.class));
                }
                else {
                    editor.putBoolean("TRACKING_ENABLE", true);
                    editor.commit();
                    startService(new Intent(MapLocationActivity.this, LocationService.class));
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}