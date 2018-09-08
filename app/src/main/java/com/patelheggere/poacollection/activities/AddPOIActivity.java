package com.patelheggere.poacollection.activities;

import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.patelheggere.poacollection.dbhelper.DBManager;
import com.patelheggere.poacollection.models.CategoryModel;
import com.patelheggere.poacollection.models.PAModel;
import com.patelheggere.poacollection.R;
import com.patelheggere.poacollection.models.POIDetails;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AddPOIActivity extends AppCompatActivity {

    private double lat, lon;
    private EditText etName, etPOINumber, etSubCat, etBName, etBNumber, etNoOfFloor, etBrand, etLAndMark, etStreet, etLocality, etPinCode, etComment, etLat, etLon;
    private Button mSubmit, mCancel;
    private Spinner  SubCategory;
    private TextView Category;
    private String mName = "";
    private String mMoobile = "";
    private String mUid = "";
    private  boolean flag = false;
    private  PAModel paModel = new PAModel();
    private DatabaseReference mDBRef;
    private FirebaseAuth mAuth;
    private String mCategoryString;
    private String mSubCategoryString;
    private ArrayAdapter<String> subCategoryAd;

    //private String[] Sub
    private DBManager dbManager;
    private SharedPreferences sharedPreferences;

    private List<CategoryModel> categoryModelList = new ArrayList<>();
    private List<String> subCatList = new ArrayList<>();
    //test
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popupwindowfor_pa);
        dbManager = new DBManager(AddPOIActivity.this);
        lat = getIntent().getDoubleExtra("lat", 0.0f);
        lon = getIntent().getDoubleExtra("lon", 0.0f);
        //System.out.println("LAtscds:"+lat);
        mName = getIntent().getStringExtra("Name");
        mMoobile = getIntent().getStringExtra("mobile");
        mUid = getIntent().getStringExtra("uid");
        mAuth = FirebaseAuth.getInstance();
        CategoryModel ob = new CategoryModel();
        ob.setCategory("Amusement Park");
       // ob.setSubCat(new String[]{"Amusement Arcade", "Amusement Park"});
        categoryModelList.add(ob);

        sharedPreferences = getSharedPreferences("ADDPOI", MODE_PRIVATE);
        getCatDeatils2();
        initialiseUI();
    }

    private void initialiseUI()
    {
        getSupportActionBar().setTitle("Enter POI Details");
        Category = findViewById(R.id.category);
        SubCategory = findViewById(R.id.subcat);
        etBName = findViewById(R.id.etBName);
        etBNumber = findViewById(R.id.etBNumber);
        etNoOfFloor = findViewById(R.id.et_no_floor);
        etName = findViewById(R.id.placename);
        etBrand = findViewById(R.id.etBrand);
        etLAndMark = findViewById(R.id.et_land);
        etStreet = findViewById(R.id.et_street);
        etLocality = findViewById(R.id.etLocality);
        etComment = findViewById(R.id.etComment);
        etPinCode = findViewById(R.id.etPIN);
        etLat = findViewById(R.id.etLat);
        etLon = findViewById(R.id.etLon);
        etLat.setText(String.valueOf(lat));
        etLon.setText(String.valueOf(lon));
        mSubmit = findViewById(R.id.btnSubmit);
        mCancel = findViewById(R.id.cancel);
        etPOINumber = findViewById(R.id.et_poi_number);
        SubCategory.setBackgroundColor(getResources().getColor(R.color.brown));


        String land = sharedPreferences.getString("LANDMARK", null);
        if(land!=null)
        {
            etLAndMark.setText(land);
        }
        String localilty = sharedPreferences.getString("LOCALITY", null);
        if(localilty!=null)
        {
            etLocality.setText(localilty);
        }
        String street = sharedPreferences.getString("STREET", null);
        if(street!=null)
        {
            etStreet.setText(street);
        }

        String pincode = sharedPreferences.getString("PINCODE", null);
        if(pincode!=null)
        {
            etPinCode.setText(pincode);
        }



        final List<String> catList = new ArrayList<>();
        for (int i = 1;i<categoryModelList.size(); i++)
        {

            catList.add(categoryModelList.get(i).getCategory());
            Log.d("", "initialiseUI:category "+categoryModelList.get(i).getCategory());
            for (int j = 0;j<categoryModelList.get(i).getSubCat().size(); i++)
            {
                subCatList.add(categoryModelList.get(i).getSubCat().get(j));
                Log.d("Tag", "initialiseUI: "+categoryModelList.get(i).getSubCat().get(j));
            }
        }

        for (int i=1; i<categoryModelList.size(); i++)
        {
            Log.d("", "getCatDeatils2:cateogory:"+i+"-"+categoryModelList.get(i).getCategory());
            Log.d("", "getCatDeatils2: "+i+"-"+categoryModelList.get(i).getSubCat().size());
            if (categoryModelList.get(i).getSubCat().size() == 0)
            {
                subCatList.add(categoryModelList.get(i).getCategory());
            }
            else
            {
                for (int j = 0; j < categoryModelList.get(i).getSubCat().size(); j++)
                {
                    subCatList.add(categoryModelList.get(i).getSubCat().get(j));
                    // Log.d("", "getCatDeatils2:subcat: "+categoryModelList.get(i).getSubCat().get(j));
                }
            }

        }
        subCategoryAd = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, subCatList);
        subCategoryAd.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SubCategory.setAdapter(subCategoryAd);
        subCategoryAd.notifyDataSetChanged();

        SubCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mSubCategoryString = adapterView.getItemAtPosition(i).toString();

                for (int k=1; k<categoryModelList.size(); k++)
                {
                    if(categoryModelList.get(k).getCategory().equalsIgnoreCase(mSubCategoryString))
                    {
                        mCategoryString = categoryModelList.get(k).getCategory();
                    }
                    else {
                        for (int m=0; m<categoryModelList.get(k).getSubCat().size(); m++)
                        {
                            if(categoryModelList.get(k).getSubCat().get(m).equalsIgnoreCase(mSubCategoryString))
                            {
                               mCategoryString = categoryModelList.get(k).getCategory();
                            }
                        }
                    }
                }
                Category.setText(mCategoryString);
                Log.d("sdfds", "onItemSelected: category:"+mCategoryString);
                //Toast.makeText(getApplicationContext(), "Selecte Sub Cat:"+mSubCategoryString, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });





       // ArrayAdapter<String> Cate = ArrayAdapter.createFromResource(this)
        ArrayAdapter<String> CategoryAdapter = new ArrayAdapter<String>( this, android.R.layout.simple_spinner_item, catList);
       // ArrayAdapter<CharSequence> CategoryAdapter = ArrayAdapter.createFromResource(this, R.array.array_cat, android.R.layout.simple_spinner_item);
        CategoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Applying the adapter to our spinner
        /*Category.setAdapter(CategoryAdapter);
        Category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mCategoryString = adapterView.getItemAtPosition(i).toString();

              *//*
                //System.out.println("cate position:"+i);
               //subCategoryAd = new ArrayAdapter<String>( getApplicationContext(), android.R.layout.simple_spinner_item, catList);
                subCategoryAd = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, categoryModelList.get(i+1).getSubCat());
                subCategoryAd.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                SubCategory.setAdapter(subCategoryAd);
                subCategoryAd.notifyDataSetChanged();
              //  System.out.println("sub Size:"+categoryModelList.get(i+1).getSubCat().size());
               // Toast.makeText(getApplicationContext(), "Selecte Cat:"+catList.get(i).toString(), Toast.LENGTH_SHORT).show();
                SubCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        mSubCategoryString = adapterView.getItemAtPosition(i).toString();
                        //Toast.makeText(getApplicationContext(), "Selecte Sub Cat:"+mSubCategoryString, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

                *//*
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
*/

       /* ArrayAdapter<CharSequence> SubCategoryAdapter = ArrayAdapter.createFromResource(this, R.array.array_sub_cat, android.R.layout.simple_spinner_item);
        SubCategoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SubCategory.setAdapter(SubCategoryAdapter);
        */


        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
                finish();
            }
        });

        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void getCatDetails() {
        CategoryModel ob1 = new CategoryModel();
        ob1.setCategory("Amusement Park");
        List<String> subList1 = new ArrayList<>();
        subList1.add("Amusement Arcade");
        subList1.add("Amusement Park");
        ob1.setSubCat(subList1);
        categoryModelList.add(ob1);

        CategoryModel ob2 = new CategoryModel();
        ob2.setCategory("Automotive Dealer");
        List<String> subList2 = new ArrayList<>();
        subList2.add("Bus");
        subList2.add("Car");
        subList2.add("Motorcycle");
        subList2.add("Truck");
        ob2.setSubCat(subList2);
        categoryModelList.add(ob2);

        CategoryModel ob3 = new CategoryModel();
        ob3.setCategory("Cafe/Pub");
        List<String> subList3 = new ArrayList<>();
        subList3.add("Pub");
        subList3.add("Coffee Shop");
        subList3.add("Internet Cafe");
        //subList3.add("Truck");
        ob3.setSubCat(subList3);
        categoryModelList.add(ob3);


        CategoryModel ob4 = new CategoryModel();
        ob4.setCategory("Bank");
        List<String> subList4 = new ArrayList<>();
        subList4.add("Bank");
        ob4.setSubCat(subList4);
        categoryModelList.add(ob4);

        CategoryModel ob5 = new CategoryModel();
        ob5.setCategory("Beach");
        List<String> subList5 = new ArrayList<>();
        subList5.add("Beach");
        ob5.setSubCat(subList5);
        categoryModelList.add(ob5);

        CategoryModel ob6 = new CategoryModel();
        ob6.setCategory("Business Park");
        List<String> subList6 = new ArrayList<>();
        subList6.add("Business Park");
        ob6.setSubCat(subList6);
        categoryModelList.add(ob6);

        CategoryModel ob8 = new CategoryModel();
        ob8.setCategory("Casino");
        List<String> subList8 = new ArrayList<>();
        subList8.add("Casino");
        ob6.setSubCat(subList8);
        categoryModelList.add(ob8);

        CategoryModel ob9 = new CategoryModel();
        ob9.setCategory("Chowk/Local Landmark");
        List<String> subList9 = new ArrayList<>();
        subList9.add("Chowk");
        subList9.add("Local Landmark");
        subList9.add("permanent");
        //subList9.add("Truck");
        ob9.setSubCat(subList9);
        categoryModelList.add(ob9);

        CategoryModel ob10 = new CategoryModel();
        ob10.setCategory("Cinema");
        List<String> subList10 = new ArrayList<>();
        subList10.add("Cinema");
        subList10.add("Drive-In Cinema");
        //subList10.add("Truck");
        ob10.setSubCat(subList10);
        categoryModelList.add(ob10);

        CategoryModel ob11 = new CategoryModel();
        ob11.setCategory("College/University");
        List<String> subList11 = new ArrayList<>();
        subList11.add("College/University");
        subList11.add("Junior College/CommunityCollege");
        //subList11.add("Truck");
        ob11.setSubCat(subList11);
        categoryModelList.add(ob11);

        CategoryModel ob12 = new CategoryModel();
        ob12.setCategory("Commercial Building");
        List<String> subList12 = new ArrayList<>();
        //subList12.add("College/University");
        //subList12.add("Junior College/CommunityCollege");
        //subList12.add("Truck");
        ob12.setSubCat(subList12);
        categoryModelList.add(ob12);


        CategoryModel ob13 = new CategoryModel();
        ob13.setCategory("residential building");
        List<String> subList13 = new ArrayList<>();
        //subList13.add("College/University");
        //subList13.add("Junior College/CommunityCollege");
        //subList13.add("Truck");
        ob13.setSubCat(subList13);
        categoryModelList.add(ob13);

        CategoryModel ob14 = new CategoryModel();
        ob14.setCategory("Courthouse");
        List<String> subList14 = new ArrayList<>();
        //subList14.add("College/University");
        //subList14.add("Junior College/CommunityCollege");
        //subList14.add("Truck");
        ob14.setSubCat(subList14);
        categoryModelList.add(ob14);

        CategoryModel ob15 = new CategoryModel();
        ob15.setCategory("Dentist");
        List<String> subList15 = new ArrayList<>();
        //subList15.add("College/University");
        //subList15.add("Junior College/CommunityCollege");
        //subList15.add("Truck");
        ob15.setSubCat(subList15);
        categoryModelList.add(ob15);

        CategoryModel ob16 = new CategoryModel();
        ob16.setCategory("Doctor");
        List<String> subList16 = new ArrayList<>();
        subList16.add("General Practitioner");
        subList16.add("Specialist");
        //subList16.add("Truck");
        ob16.setSubCat(subList16);
        categoryModelList.add(ob16);

        CategoryModel ob17 = new CategoryModel();
        ob17.setCategory("Exchange");
        List<String> subList17 = new ArrayList<>();
        subList17.add("Currency Exchange");
        subList17.add("Stock Exchange");
        //subList17.add("Truck");
        ob17.setSubCat(subList17);
        categoryModelList.add(ob17);

        CategoryModel ob18 = new CategoryModel();
        ob18.setCategory("Exhibition & Convention Center");
        List<String> subList18 = new ArrayList<>();
        //subList18.add("Currency Exchange");
        //subList18.add("Stock Exchange");
        //subList18.add("Truck");
        ob18.setSubCat(subList18);
        categoryModelList.add(ob18);

        CategoryModel ob19 = new CategoryModel();
        ob19.setCategory("Fire Station/Brigade");
        List<String> subList19 = new ArrayList<>();
        //subList19.add("Currency Exchange");
        //subList19.add("Stock Exchange");
        //subList19.add("Truck");
        ob19.setSubCat(subList19);
        categoryModelList.add(ob19);

        CategoryModel ob20 = new CategoryModel();
        ob20.setCategory("Health Care Service");
        List<String> subList20 = new ArrayList<>();
        subList20.add("Unspecified");
        subList20.add("Personal Service");
        subList20.add("Personal Care Facility");
        subList20.add("Blood Bank");
        subList20.add("Ambulance Unit");
        ob20.setSubCat(subList20);
        categoryModelList.add(ob20);

        CategoryModel ob21 = new CategoryModel();
        ob21.setCategory("Hospital/Polyclinic");
        List<String> subList21 = new ArrayList<>();
        subList21.add("General");
        subList21.add("Hospital for Women and Children");
        subList21.add("optitian");
        subList21.add("clinic");
        subList21.add("dentist");
        subList21.add("nursung home");
        subList21.add("Special");
        ob21.setSubCat(subList21);
        categoryModelList.add(ob21);

        CategoryModel ob22 = new CategoryModel();
        ob22.setCategory("Hotel/Motel");
        List<String> subList22 = new ArrayList<>();
        subList22.add("Bed & Breakfast & Guest Houses");
        subList22.add("Cabins & Lodges");
        subList22.add("Hostel");
        subList22.add("Hotel");
        subList22.add("Motel");
        subList22.add("Resort");
        ob22.setSubCat(subList22);
        categoryModelList.add(ob22);

        CategoryModel ob23 = new CategoryModel();
        ob23.setCategory("Important Tourist Attraction");
        List<String> subList23 = new ArrayList<>();
        subList23.add("Bridge");
        subList23.add("Dam");
        subList23.add("Mausoleum/Grave");
        subList23.add("Monument");
        subList23.add("Natural Attraction");
        subList23.add("Observatory");
        subList23.add("Planetarium");
        subList23.add("Statue");
        ob23.setSubCat(subList23);
        categoryModelList.add(ob23);


        CategoryModel ob24 = new CategoryModel();
        ob24.setCategory("Library");
        List<String> subList24 = new ArrayList<>();
        ob24.setSubCat(subList24);
        categoryModelList.add(ob24);

        CategoryModel ob25 = new CategoryModel();
        ob25.setCategory("Market");
        List<String> subList25 = new ArrayList<>();
        subList25.add("Farmers");
        subList25.add("Public");
        subList25.add("Supermarkets & Hypermarkets");
        ob25.setSubCat(subList25);
        categoryModelList.add(ob25);



        CategoryModel ob26 = new CategoryModel();
        ob26.setCategory("Museum");
        List<String> subList26 = new ArrayList<>();
        //subList26.add("Farmers");
        //subList26.add("Public");
        //subList26.add("Supermarkets & Hypermarkets");
        ob26.setSubCat(subList26);
        categoryModelList.add(ob26);


        CategoryModel ob27 = new CategoryModel();
        ob27.setCategory("Nightlife");
        List<String> subList27 = new ArrayList<>();
        subList27.add("Bar");
        subList27.add("Discotheque");
        subList27.add("Private Club");
        subList27.add("Wine Bar");
        ob27.setSubCat(subList27);
        categoryModelList.add(ob27);

        CategoryModel ob28 = new CategoryModel();
        ob28.setCategory("Petrol and gasoline Station");
        List<String> subList28 = new ArrayList<>();
        subList28.add("petrol");
        subList28.add("gas");
        subList28.add("petrol and gas");
        //subList28.add("Wine Bar");
        ob28.setSubCat(subList28);
        categoryModelList.add(ob28);


        CategoryModel ob29 = new CategoryModel();
        ob29.setCategory("Post Office");
        List<String> subList29 = new ArrayList<>();
        subList29.add("General");
        subList29.add("Local");
        //subList29.add("petrol and gas");
        //subList29.add("Wine Bar");
        ob29.setSubCat(subList29);
        categoryModelList.add(ob29);

        CategoryModel ob30 = new CategoryModel();
        ob30.setCategory("Railway Station");
        List<String> subList30 = new ArrayList<>();
        subList30.add("(Sub) Urban");
        subList30.add("Metro");
        subList30.add("National");
        //subList30.add("Wine Bar");
        ob30.setSubCat(subList30);
        categoryModelList.add(ob30);
        CategoryModel ob31 = new CategoryModel();
        ob31.setCategory("Restaurant");
        List<String> subList31 = new ArrayList<>();
        subList31.add("(Barbecue");
        subList31.add("Chinese");
        subList31.add("Fast Food");
        subList31.add("Ice Cream Parlor");
        subList31.add("Indian");
        subList31.add("Italian");
        subList31.add("Pizza");
        subList31.add("Seafood");
        subList31.add("Thai");
        subList31.add("Vegetarian");
        subList31.add("Western & Continental");
        ob31.setSubCat(subList31);
        categoryModelList.add(ob31);


        CategoryModel ob32 = new CategoryModel();
        ob32.setCategory("School");
        List<String> subList32 = new ArrayList<>();
        subList32.add("(Art School");
        subList32.add("Child Care Facility");
        subList32.add("Driving School");
        subList32.add("Culinary School");
        subList32.add("Language School");
        subList32.add("High School");
        subList32.add("Middle School");
        subList32.add("Primary School");
        subList32.add("Pre School");
        subList32.add("Senior High School");
        subList32.add("Special School");
        subList32.add("Sport School");
        subList32.add("Technical School");
        subList32.add("Vocational Training");
        subList32.add("Technical School");
        subList32.add("Dance scholl");
        subList32.add("computer institution");
        ob32.setSubCat(subList32);
        categoryModelList.add(ob32);

        CategoryModel ob33 = new CategoryModel();
        ob33.setCategory("business services");
        List<String> subList33 = new ArrayList<>();
        subList33.add("(Advertising/Marketing");
        subList33.add("Automobile Manufacturing");
        subList33.add("Catering");
        subList33.add("Chemicals");
        subList33.add("Computer & Data Services");
        subList33.add("Computer Software");
        subList33.add("Construction");
        subList33.add("Diversified Finanacials");
        subList33.add("Electronics");
        subList33.add("Insurance");
        subList33.add("Legal Services");
        subList33.add("Mail/Package/Freight Delivery");
        subList33.add("Manufacturing");
        subList33.add("Mechanical Engineering");
        subList33.add("Pharmaceuticals");
        subList33.add("Service");
        subList33.add("Tax Services");
        subList33.add("Tele Communications");
        subList33.add("Transport");
        subList33.add("Wedding Services");
        subList33.add("Other services");
        ob33.setSubCat(subList33);
        categoryModelList.add(ob33);

        CategoryModel ob34 = new CategoryModel();
        ob34.setCategory("Rent-a-Car Facility");
        List<String> subList34 = new ArrayList<>();
        subList34.add("Rent-a-Car Facility");
        subList34.add("Rent-a-Bike Facility");
        ob34.setSubCat(subList34);
        categoryModelList.add(ob34);

        CategoryModel ob344 = new CategoryModel();
        ob344.setCategory("Shopping Center");
        List<String> subList344 = new ArrayList<>();
        //subList34.add("Rent-a-Car Facility");
        //subList34.add("Rent-a-Bike Facility");
        ob34.setSubCat(subList344);
        categoryModelList.add(ob344);

        CategoryModel ob35 = new CategoryModel();
        ob35.setCategory("Sports Center");
        List<String> subList35 = new ArrayList<>();
        subList35.add("Fitness Club & Center");
        subList35.add("Sports Center");
        ob35.setSubCat(subList35);
        categoryModelList.add(ob35);

        CategoryModel ob36 = new CategoryModel();
        ob36.setCategory("Stadium");
        List<String> subList36 = new ArrayList<>();
        subList36.add("Cricket Ground");
        subList36.add("Horse Racing");
        subList36.add("Football");
        subList36.add("Horse Racing");
        subList36.add("Other");
        subList36.add("Motor Sport");
        subList36.add("Tennis Court");
        ob36.setSubCat(subList36);
        categoryModelList.add(ob36);

        CategoryModel ob37 = new CategoryModel();
        ob37.setCategory("Toll Gate");
        List<String> subList37 = new ArrayList<>();
        //subList37.add("Cricket Ground");
        //subList37.add("Horse Racing");
        //subList37.add("Football");
        ob37.setSubCat(subList37);
        categoryModelList.add(ob37);

        CategoryModel ob38 = new CategoryModel();
        ob38.setCategory("Theater");
        List<String> subList38 = new ArrayList<>();
        subList38.add("Amphitheater");
        subList38.add("Concert Hall");
        subList38.add("Theater");
        ob38.setSubCat(subList38);
        categoryModelList.add(ob38);

        CategoryModel ob39 = new CategoryModel();
        ob39.setCategory("Zoo, Arboreta & Botanical Garden");
        List<String> subList39 = new ArrayList<>();
        subList39.add("Arboreta & Botanical Gardens");
        subList39.add("Wildlife Park");
        subList39.add("Zoo");
        ob39.setSubCat(subList39);
        categoryModelList.add(ob39);

        CategoryModel ob40 = new CategoryModel();
        ob40.setCategory("Car Wash");
        List<String> subList40 = new ArrayList<>();
        subList40.add("Car Wash");
        subList40.add("Truck Wash");
        //subList40.add("Zoo");
        ob40.setSubCat(subList40);
        categoryModelList.add(ob40);

        CategoryModel ob41 = new CategoryModel();
        ob41.setCategory("Club & Association");
        List<String> subList41 = new ArrayList<>();
        subList41.add("Beach Club");
        subList41.add("Club ");
        subList41.add("Association ");
        ob41.setSubCat(subList41);
        categoryModelList.add(ob41);


        CategoryModel ob42 = new CategoryModel();
        ob42.setCategory("Embassy");
        List<String> subList42 = new ArrayList<>();
        //subList42.add("Beach Club");
        //subList42.add("Club ");
        //subList42.add("Association ");
        ob42.setSubCat(subList42);
        categoryModelList.add(ob42);

        CategoryModel ob43 = new CategoryModel();
        ob43.setCategory("Ferry Terminal");
        List<String> subList43 = new ArrayList<>();
        //subList43.add("Beach Club");
        //subList43.add("Club ");
        //subList43.add("Association ");
        ob43.setSubCat(subList43);
        categoryModelList.add(ob43);


        CategoryModel ob44 = new CategoryModel();
        ob44.setCategory("Geographic Feature");
        List<String> subList44 = new ArrayList<>();
        subList44.add("Cave");
        subList44.add("Reservoir ");
        subList44.add("Valley ");
        subList44.add("moutain ");
        ob44.setSubCat(subList44);
        categoryModelList.add(ob44);



        CategoryModel ob45 = new CategoryModel();
        ob45.setCategory("Golf Course");
        List<String> subList45 = new ArrayList<>();
        //subList45.add("Cave");
        //subList45.add("Reservoir ");
        //subList45.add("Valley ");
        //subList45.add("moutain ");
        ob45.setSubCat(subList45);
        categoryModelList.add(ob45);


        CategoryModel ob46 = new CategoryModel();
        ob46.setCategory("Government Offic");
        List<String> subList46 = new ArrayList<>();
        subList46.add("National");
        subList46.add("Order 1 Area ");
        subList46.add("Order 2 Area ");
        subList46.add("Order 7 Area ");
        subList46.add("Order 8 Area ");
        ob46.setSubCat(subList46);
        categoryModelList.add(ob46);


        CategoryModel ob47 = new CategoryModel();
        ob47.setCategory("Industrial Building");
        List<String> subList47 = new ArrayList<>();
        ob47.setSubCat(subList47);
        categoryModelList.add(ob47);

        CategoryModel ob48 = new CategoryModel();
        ob48.setCategory("Leisure Center");
        List<String> subList48 = new ArrayList<>();
        subList48.add("Sauna, Solarium & Massage");
        ob48.setSubCat(subList48);
        categoryModelList.add(ob48);

        CategoryModel ob49 = new CategoryModel();
        ob49.setCategory("Marina");
        List<String> subList49 = new ArrayList<>();
        subList49.add("Marina");
        ob49.setSubCat(subList49);
        categoryModelList.add(ob49);

        CategoryModel ob50 = new CategoryModel();
        ob50.setCategory("Media Facility");
        List<String> subList50 = new ArrayList<>();
        // subList50.add("Marina");
        ob50.setSubCat(subList50);
        categoryModelList.add(ob50);

        CategoryModel ob51 = new CategoryModel();
        ob51.setCategory("Non Governmental Organization");
        List<String> subList51 = new ArrayList<>();
        subList51.add("orphanage");
        subList51.add("NGO");
        ob51.setSubCat(subList51);
        categoryModelList.add(ob51);

        CategoryModel ob52 = new CategoryModel();
        ob52.setCategory("Open Parking Area");
        List<String> subList52 = new ArrayList<>();
        //subList52.add("orphanage");
        //subList52.add("NGO");
        ob52.setSubCat(subList52);
        categoryModelList.add(ob52);

        CategoryModel ob53 = new CategoryModel();
        ob53.setCategory("Park and Recreation Area");
        List<String> subList53 = new ArrayList<>();
        //subList53.add("orphanage");
        //subList53.add("NGO");
        ob53.setSubCat(subList53);
        categoryModelList.add(ob53);

        CategoryModel ob533 = new CategoryModel();
        ob533.setCategory("Pharmacy");
        List<String> subList533 = new ArrayList<>();
        subList533.add("Pharmacy");
        //subList53.add("orphanage");
        //subList53.add("NGO");
        ob53.setSubCat(subList533);
        categoryModelList.add(ob533);

        CategoryModel ob54 = new CategoryModel();
        ob54.setCategory("Place of Worship");
        List<String> subList54 = new ArrayList<>();
        subList54.add("Ashram");
        subList54.add("Church");
        subList54.add("Gurudwara");
        subList54.add("Mosque");
        subList54.add("Pagoda");
        subList54.add("Synagogue");
        subList54.add("Temple");
        subList54.add("convent");
        ob54.setSubCat(subList54);
        categoryModelList.add(ob54);

        CategoryModel ob55 = new CategoryModel();
        ob55.setCategory("Port/Warehouse Facility");
        List<String> subList55 = new ArrayList<>();
        //subList55.add("orphanage");
        //subList55.add("NGO");
        ob55.setSubCat(subList55);
        categoryModelList.add(ob55);

        CategoryModel ob56 = new CategoryModel();
        ob56.setCategory("Primary Resource/Utility");
        List<String> subList56 = new ArrayList<>();
        //subList56.add("orphanage");
        //subList56.add("NGO");
        ob56.setSubCat(subList56);
        categoryModelList.add(ob56);

        CategoryModel ob57 = new CategoryModel();
        ob57.setCategory("Prison/Correctional Facility");
        List<String> subList57 = new ArrayList<>();
        //subList57.add("orphanage");
        //subList57.add("NGO");
        ob57.setSubCat(subList57);
        categoryModelList.add(ob57);

        CategoryModel ob58 = new CategoryModel();
        ob58.setCategory("Public Amenity");
        List<String> subList58 = new ArrayList<>();
        //subList58.add("orphanage");
        //subList58.add("NGO");
        ob58.setSubCat(subList58);
        categoryModelList.add(ob58);

        CategoryModel ob59 = new CategoryModel();
        ob59.setCategory("Public Transport Stop");
        List<String> subList59 = new ArrayList<>();
        subList59.add("Bus Stop");
        subList59.add("other");
        subList59.add("Taxi Stand");
        subList59.add("Tram Stop");
        ob59.setSubCat(subList59);
        categoryModelList.add(ob59);

        CategoryModel ob60 = new CategoryModel();
        ob60.setCategory("Repair Facility");
        List<String> subList60 = new ArrayList<>();
        subList60.add("Motor cycle");
        subList60.add("car");
        subList60.add("auto");
        subList60.add("electrical");
        subList60.add("other");
        ob60.setSubCat(subList60);
        categoryModelList.add(ob60);


        CategoryModel ob61 = new CategoryModel();
        ob61.setCategory("Restaurant Area");
        List<String> subList61 = new ArrayList<>();
        //subList61.add("orphanage");
        //subList61.add("NGO");
        ob61.setSubCat(subList61);
        categoryModelList.add(ob61);



        CategoryModel ob62 = new CategoryModel();
        ob62.setCategory("Road Name");
        List<String> subList62 = new ArrayList<>();
        //subList62.add("orphanage");
        //subList62.add("NGO");
        ob62.setSubCat(subList62);
        categoryModelList.add(ob62);

        CategoryModel ob63 = new CategoryModel();
        ob63.setCategory("Swimming Pool");
        List<String> subList63 = new ArrayList<>();
        //subList63.add("orphanage");
        //subList63.add("NGO");
        ob63.setSubCat(subList63);
        categoryModelList.add(ob63);

        CategoryModel ob64 = new CategoryModel();
        ob64.setCategory("Tourist Information Office");
        List<String> subList64 = new ArrayList<>();
        //subList64.add("orphanage");
        //subList64.add("NGO");
        ob64.setSubCat(subList64);
        categoryModelList.add(ob64);

        CategoryModel ob644 = new CategoryModel();
        ob644.setCategory("Transport Authority/Vehicle Registration");
        List<String> subList644 = new ArrayList<>();
        //subList64.add("orphanage");
        //subList64.add("NGO");
        ob644.setSubCat(subList644);
        categoryModelList.add(ob644);

        CategoryModel ob65 = new CategoryModel();
        ob65.setCategory("Veterinarian");
        List<String> subList65 = new ArrayList<>();
        //subList65.add("orphanage");
        //subList65.add("NGO");
        ob65.setSubCat(subList65);
        categoryModelList.add(ob65);

        CategoryModel ob66 = new CategoryModel();
        ob66.setCategory("Weigh Station");
        List<String> subList66 = new ArrayList<>();
        subList66.add("Weigh Scales");
        //subList66.add("NGO");
        ob66.setSubCat(subList66);
        categoryModelList.add(ob66);

        CategoryModel Ob67 = new  CategoryModel();
        Ob67.setCategory("shopping");
        List<String> subList67 = new ArrayList<>();
        subList67.add("Book Store");
        subList67.add("arts and crafts supplies");
        subList67.add("Bakery and baked goods Store");
        subList67.add("Barber");
        subList67.add("bicycle and bicycle accessories shop");
        subList67.add("butcher");
        subList67.add("children's apparel");
        subList67.add("clothing and Accessories");
        subList67.add("computer and software store");
        subList67.add("consumer electronics Store");
        subList67.add("convenience Store");
        subList67.add("Dairy goods");
        subList67.add("department store ");
        subList67.add(" pharmacy");
        subList67.add("drugstore -Pharmacy");

        subList67.add("entertainment electronics");
        subList67.add("floor and carpet");
        subList67.add("florist");
        subList67.add("flowers and jewellery");
        subList67.add("food beverage speciality Store");
        subList67.add("Garden centre");
        subList67.add("(furniture store");
        subList67.add("gift antique and art");
        subList67.add("glass and window");
        subList67.add("grocery");
        subList67.add("hair and beauty");
        subList67.add("hair and salon");
        subList67.add("hardware house and garden");
        subList67.add("hunting fishing shop");
        subList67.add("Lumber-Timber");
        subList67.add("jeweller");
        subList67.add("major appliance");
        subList67.add("men's apparels");
        subList67.add("mobile retailer");
        subList67.add("mobile service centre");
        subList67.add("Nail salon");
        subList67.add("office supply and service store");
        subList67.add("pet supply");
        subList67.add("Tele Communications");
        subList67.add("paint store");
        subList67.add("Pawnshop");
        subList67.add("power equipment dealers");
        subList67.add("record CD and video");
        subList67.add("shoes -footwear");
        subList67.add("shopping mall");
        subList67.add("specialty clothing store");
        subList67.add("specialty food store");
        subList67.add("speciality Stores");
        subList67.add("Sporting goods Store");
        subList67.add("sweet shop");
        subList67.add("toy store");
        subList67.add("used -second hand Merchandise Store");
        subList67.add("variety Store");
        subList67.add("warehouse and Wholesale Store");
        subList67.add("video and game rental");
        subList67.add("women's apparel");
        subList67.add("Tele Communications");
        subList67.add("home improvement Hardware Store");
        subList67.add("tack shop");
        subList67.add("electrical store");
        subList67.add("industrial equipment’s supply store");

        Ob67.setSubCat(subList67);
        categoryModelList.add(Ob67);

        CategoryModel Ob68 = new  CategoryModel();
        Ob68.setCategory("ATM");
        List<String> subList68 = new ArrayList<>();
        subList68.add("ATM");
        subList68.add("Cash deposit");
        Ob68.setSubCat(subList68);
        categoryModelList.add(Ob68);

        CategoryModel Ob69 = new CategoryModel();
        Ob69.setCategory("Facilities");
        List<String> subList69 = new ArrayList<>();
        subList69.add("Advertising/Marketing");
        subList69.add("Chemicals");
        subList69.add("Diversified Financial");
        subList69.add("Electronics");
        subList69.add("(Legal Services");
        subList69.add("Mail/Package/Freight Delivery-couriers");
        subList69.add("Mechanical Engineering");
        subList69.add(" Pharmaceuticals Arber");
        subList69.add("(Transport");
        subList69.add("(aviation");
        subList69.add("B2B restaurant service");
        subList69.add("B2B sales and service");
        subList69.add("Barber");
        subList69.add("bill payment service");
        subList69.add("boating services");
        subList69.add("body piercing and tattoos");
        subList69.add("business facility");
        subList69.add("computer and software service");
        subList69.add("business service");
        subList69.add("Catering and other food services");
        subList69.add("check cashing service currency exchange");
        subList69.add("commercial services");
        subList69.add("communication media");
        subList69.add("construction ");
        subList69.add("customer service ");
        subList69.add("customer care service center ");
        subList69.add("dry-cleaning and laundry");
        subList69.add("electrical ");
        subList69.add("emission testing ");
        subList69.add("engineering and scientific services ");
        subList69.add("(entertainment and Recreation ");
        subList69.add("farming ");
        subList69.add("finance and insurance ");

        subList69.add("financial investment firm ");
        subList69.add("fire department ");
        subList69.add("food production ");
        subList69.add("Funeral home ");
        subList69.add("human resources and recreating services ");
        subList69.add("interior and exterior design ");
        subList69.add("internet cafe ");
        subList69.add("IT and office equipment services ");
        subList69.add("landscaping services ");
        subList69.add("maid services ");
        subList69.add("management and Consulting Services ");
        subList69.add("manufacturing ");
        subList69.add("managing and matchmaking services ");
        subList69.add("mining ");
        subList69.add("money transferring service ");
        subList69.add("movers ");
        subList69.add("organization and societies ");
        subList69.add("pet care ");
        subList69.add("plumbing ");
        subList69.add("police services -security ");
        subList69.add("locksmiths and security system services");
        subList69.add("printing and publishing ");
        subList69.add("Property Management ");
        subList69.add("public administration ");
        subList69.add("real estate services ");
        subList69.add("rental and leasing");
        subList69.add("repairing and Maintenance Services");
        subList69.add("repair service ");
        subList69.add("road assistance");
        subList69.add("specialty trend contractors");
        subList69.add("storage");
        subList69.add("Tele Communications");
        subList69.add("tailor and alteration");
        subList69.add("tanning salon");
        subList69.add("Tax Services");
        subList69.add("telephone service");
        subList69.add("towing service");
        subList69.add("translation and interpretation services");
        subList69.add("travel agent ticketing");
        subList69.add("waste and sanitary");
        subList69.add("wedding services and bridal studio");
        subList69.add("Wellness Centre and services");
        Ob69.setSubCat(subList69);
        categoryModelList.add(Ob69);

        CategoryModel Ob70 = new CategoryModel();
        Ob70.setCategory("education facility");
        List<String> subList70 = new ArrayList<>();
//subList70.add("driving school");
//subList70.add("technical training");
//subList70.add("other training and development");
        Ob70.setSubCat(subList70);
        categoryModelList.add(Ob70);


        CategoryModel Ob71 = new CategoryModel();
        Ob71.setCategory("eat and drink");
        List<String> subList71 = new ArrayList<>();
        subList71.add("Coffee-Tea");
        subList71.add("Coffee Shop");
        Ob71.setSubCat(subList71);
        categoryModelList.add(Ob71);

        CategoryModel Ob72 = new CategoryModel();
        Ob72.setCategory("Banquet Hall");
        List<String> subList72 = new ArrayList<>();
//subList71.add(" Coffee-Tea");
//subList71.add("Coffee Shop");
        Ob72.setSubCat(subList72);
        categoryModelList.add(Ob72);

        Log.d("TAG", "getCatDetails: ");

    }

    private void submit()
    {
        POIDetails ob= new POIDetails();
        ob.setName(etName.getText().toString());
        ob.setCategory(mCategoryString);
        ob.setSubCat(mSubCategoryString);
        ob.setbName(etBName.getText().toString());
        ob.setbNumber(etBNumber.getText().toString());
        ob.setNoFloor(etNoOfFloor.getText().toString());
        ob.setBrand(etBrand.getText().toString());
        ob.setLandMark(etLAndMark.getText().toString());
        ob.setStreet(etStreet.getText().toString());
        ob.setLocality(etLocality.getText().toString());
        ob.setPincode(etPinCode.getText().toString());
        ob.setComment(etComment.getText().toString());
       // ob.setmLonggitude();
        ob.setmLattitude(""+String.valueOf(lat));
        ob.setmLonggitude(""+lon);
        ob.setmPOINumber(etPOINumber.getText().toString());
        ob.setmPhoneNumberr(mMoobile);
        ob.setmPersonName(mAuth.getCurrentUser().getDisplayName());
        ob.setmDate(new Date().getTime()+"");

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("LANDMARK", etLAndMark.getText().toString());
        editor.putString("LOCALITY", etLocality.getText().toString());
        editor.putString("STREET", etStreet.getText().toString());
        editor.putString("PINCODE", etPinCode.getText().toString());
        editor.commit();

        dbManager.open();
        dbManager.insert(ob, 1);
        dbManager.close();
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
        }
        flag = true;
        paModel.setmPlaceName((etPlaceName.getText().toString()));
        paModel.setmPin((etPin.getText().toString()));
        paModel.setmCat(mCategoryString);
       // paModel.setEtHouseNo(etHouseNo.getText().toString());
       // paModel.setEtHouseName(etHouseName.getText().toString());
        paModel.setmaddress(address.getText().toString());
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
            Toast.makeText(addPAActivity.this, "check all the feilds", Toast.LENGTH_SHORT).show();
        }
        */
    }

    private void getCatDeatils2()
    {
        CategoryModel Ob1 = new CategoryModel();
        Ob1.setCategory("Amusement Or Holiday Park");
        List<String> subList1 = new ArrayList<>();
//subList1.add("Amusement Arcade");
//subList1.add("Amusement Park");
        Ob1.setSubCat(subList1);
        categoryModelList.add(Ob1);
        CategoryModel Ob2 = new CategoryModel();
        Ob2.setCategory("Automobile Dealership");
        List<String> subList2 = new ArrayList<>();
        subList2.add("Automobile Dealership - Bus ");
        subList2.add("Automobile Dealership -new Car");
        subList2.add("Automobile Dealership -Used Car");
        subList2.add("Automobile Dealership- Used ");
        subList2.add("Automobile Dealership-Motorcycle");
        subList2.add("Automobile Dealership -Truck");
        subList2.add("Truck-Semi Dealer-Dervices");
        Ob2.setSubCat(subList2);
        categoryModelList.add(Ob2);
        CategoryModel Ob3 = new CategoryModel();
        Ob3.setCategory("City Hall");
        List<String> subList3 = new ArrayList<>();
//subList3.add("Pub");
//subList3.add("Coffee Shop");
//subList3.add("Truck");
        Ob3.setSubCat(subList3);
        categoryModelList.add(Ob3);

        CategoryModel Ob4 = new CategoryModel();
        Ob4.setCategory("Bank");
        List<String> subList4 = new ArrayList<>();
        subList4.add("Bank");
        Ob4.setSubCat(subList4);
        categoryModelList.add(Ob4);
        CategoryModel Ob5 = new CategoryModel();
        Ob5.setCategory("Beach");
        List<String> subList5 = new ArrayList<>();
        subList5.add("Beach");
        Ob5.setSubCat(subList5);
        categoryModelList.add(Ob5);
        CategoryModel Ob6 = new CategoryModel();
        Ob6.setCategory("Business Park");
        List<String> subList6 = new ArrayList<>();
        subList6.add("Business Park");
        Ob6.setSubCat(subList6);
        categoryModelList.add(Ob6);

        CategoryModel Ob7 = new CategoryModel();
        Ob7.setCategory("Coaching Institution");
        List<String> subList7 = new ArrayList<>();
        subList7.add("Competitive Exam Coaching");
        subList7.add("Tutorials");
        Ob7.setSubCat(subList7);
        categoryModelList.add(Ob7);

        CategoryModel Ob8 = new CategoryModel();
        Ob8.setCategory("Photography");
        List<String> subList8 = new ArrayList<>();
        subList8.add("Casino");
        Ob6.setSubCat(subList8);
        categoryModelList.add(Ob8);
        CategoryModel Ob9 = new CategoryModel();
        Ob9.setCategory("Named Intersection- Chowk ");
        List<String> subList9 = new ArrayList<>();
        subList9.add("Named Intersection- Chowk");
        subList9.add("Local Landmark");
//subList9.add("Truck");
        Ob9.setSubCat(subList9);
        categoryModelList.add(Ob9);
        CategoryModel Ob10 = new CategoryModel();
        Ob10.setCategory("Cinema");
        List<String> subList10 = new ArrayList<>();
        subList10.add("Cinema");
        subList10.add("Drive-In Cinema");
//subList10.add("Truck");
        Ob10.setSubCat(subList10);
        categoryModelList.add(Ob10);
        CategoryModel Ob11 = new CategoryModel();
        Ob11.setCategory("Higher Education");
        List<String> subList11 = new ArrayList<>();
        subList11.add("College/University");
        subList11.add("Junior College/Communitycollege");
//subList11.add("Truck");
        Ob11.setSubCat(subList11);
        categoryModelList.add(Ob11);
        CategoryModel Ob12 = new CategoryModel();
        Ob12.setCategory("Outdoor Area Complex");
        List<String> subList12 = new ArrayList<>();
//subList12.add("College/University");
//subList12.add("Junior College/Communitycollege");

//subList12.add("Truck");
        Ob12.setSubCat(subList12);
        categoryModelList.add(Ob12);

        CategoryModel Ob13 = new CategoryModel();
        Ob13.setCategory("Residential Area-Building");
        List<String> subList13 = new ArrayList<>();
//subList13.add("College/University");
//subList13.add("Junior College/Communitycollege");
//subList13.add("Truck");
        Ob13.setSubCat(subList13);
        categoryModelList.add(Ob13);
        CategoryModel Ob14 = new CategoryModel();
        Ob14.setCategory("Court House");
        List<String> subList14 = new ArrayList<>();
//subList14.add("College/University");
//subList14.add("Junior College/Communitycollege");
//subList14.add("Truck");
        Ob14.setSubCat(subList14);
        categoryModelList.add(Ob14);
        CategoryModel Ob15 = new CategoryModel();
        Ob15.setCategory("Dentist-Dental Office");
        List<String> subList15 = new ArrayList<>();
//subList15.add("College/University");
//subList15.add("Junior College/Communitycollege");
//subList15.add("Truck");
        Ob15.setSubCat(subList15);
        categoryModelList.add(Ob15);
        CategoryModel Ob16 = new CategoryModel();
        Ob16.setCategory("Doctor");
        List<String> subList16 = new ArrayList<>();
        subList16.add("General Practitioner");
        subList16.add("Specialist");
//subList16.add("Truck");
        Ob16.setSubCat(subList16);
        categoryModelList.add(Ob16);
        CategoryModel Ob17 = new CategoryModel();
        Ob17.setCategory("Check Cashing Service-Currency Exchange ");
        List<String> subList17 = new ArrayList<>();
        subList17.add("Check Cashing Service-Currency Exchange ");
        subList17.add("Stock Exchange");
//subList17.add("Truck");
        Ob17.setSubCat(subList17);
        categoryModelList.add(Ob17);
        CategoryModel Ob18 = new CategoryModel();

        Ob18.setCategory("Convention- Exhibition Center");
        List<String> subList18 = new ArrayList<>();
//subList18.add("Currency Exchange");
//subList18.add("Stock Exchange");
//subList18.add("Truck");
        Ob18.setSubCat(subList18);
        categoryModelList.add(Ob18);
        CategoryModel Ob19 = new CategoryModel();
        Ob19.setCategory("Fire Department");
        List<String> subList19 = new ArrayList<>();
//subList19.add("Currency Exchange");
//subList19.add("Stock Exchange");
//subList19.add("Truck");
        Ob19.setSubCat(subList19);
        categoryModelList.add(Ob19);
        CategoryModel Ob20 = new CategoryModel();
        Ob20.setCategory("Healthcare And Healthcare Support Services") ;
        List<String> subList20 = new ArrayList<>();
        subList20.add("Unspecified");
        subList20.add("Diagnostic And Other Health Services");
        subList20.add("Personal Care Facility");
        subList20.add("Blood Bank");
        subList20.add("Ambulance Services");
        Ob20.setSubCat(subList20);
        categoryModelList.add(Ob20);
        CategoryModel Ob21 = new CategoryModel();
        Ob21.setCategory("Hospital/Polyclinic");
        List<String> subList21 = new ArrayList<>();
        subList21.add("Family-General Practice Physicians");
        subList21.add("Hospital For Women And Children");
        subList21.add("Optician");
        subList21.add("Medical Services -Clinic");
        subList21.add("Nursing Home");
        subList21.add("Hospital");
        subList21.add("Hospital Emergency Room");
        subList21.add("Hospital Or Health Care Facility");
        Ob21.setSubCat(subList21);
        categoryModelList.add(Ob21);
        CategoryModel Ob22 = new CategoryModel();
        Ob22.setCategory("Accommodation");
        List<String> subList22 = new ArrayList<>();
        subList22.add("Bed & Breakfast");
        subList22.add("Guest Houses");
        subList22.add("Lodging");
        subList22.add("Hostel");
        subList22.add("Hotel");
        subList22.add("Hotel Or Motel");

        subList22.add("Other Accommodations");
        subList22.add("Resort");
        Ob22.setSubCat(subList22);
        categoryModelList.add(Ob22);
        CategoryModel Ob23 = new CategoryModel();
        Ob23.setCategory("Tourist Attraction");
        List<String> subList23 = new ArrayList<>();
        subList23.add("Bridge");
        subList23.add("Dam");
        subList23.add("Mausoleum/Grave");
        subList23.add("Monument");
        subList23.add("Natural Attraction");
        subList23.add("Observatory");
        subList23.add("Planetarium");
        subList23.add("Statue");
        Ob23.setSubCat(subList23);
        categoryModelList.add(Ob23);

        CategoryModel Ob24 = new CategoryModel();
        Ob24.setCategory("Library");
        List<String> subList24 = new ArrayList<>();
        Ob24.setSubCat(subList24);
        categoryModelList.add(Ob24);
        CategoryModel Ob25 = new CategoryModel();
        Ob25.setCategory("Market");
        List<String> subList25 = new ArrayList<>();
        subList25.add("Farmers");
        subList25.add("Public");
        subList25.add("Supermarkets & Hypermarkets");
        Ob25.setSubCat(subList25);
        categoryModelList.add(Ob25);

        CategoryModel Ob26 = new CategoryModel();
        Ob26.setCategory("Museum");
        List<String> subList26 = new ArrayList<>();
//subList26.add("Farmers");
//subList26.add("Public");
//subList26.add("Supermarkets & Hypermarkets");
        Ob26.setSubCat(subList26);
        categoryModelList.add(Ob26);

        CategoryModel Ob27 = new CategoryModel();
        Ob27.setCategory("Nightlife");
        List<String> subList27 = new ArrayList<>();
        subList27.add("Bar-Pub-Stube-Biergarten");

        subList27.add("Discotheque");
        subList27.add("Private Club");
        subList27.add("Wine And Liquor");
        subList27.add("Nightlife-Entertainment ");
        subList27.add("Night Club");
        Ob27.setSubCat(subList27);
        categoryModelList.add(Ob27);
        CategoryModel Ob28 = new CategoryModel();
        Ob28.setCategory("Petrol -Gasoline Station ");
        List<String> subList28 = new ArrayList<>();
        subList28.add("Petrol -Gasoline Station ");
        subList28.add("Gas");
        subList28.add("Petrol");
        subList28.add("Ev Charging Station");
        Ob28.setSubCat(subList28);
        categoryModelList.add(Ob28);

        CategoryModel Ob29 = new CategoryModel();
        Ob29.setCategory("Post Office");
        List<String> subList29 = new ArrayList<>();
//subList29.add("Petrol And Gas");
//subList29.add("Wine Bar");
        Ob29.setSubCat(subList29);
        categoryModelList.add(Ob29);
        CategoryModel Ob30 = new CategoryModel();
        Ob30.setCategory("Railway Station");
        List<String> subList30 = new ArrayList<>();
        subList30.add("(Sub) Urban Railway ");
        subList30.add("Metro Railway");
        subList30.add("National Railway");
//subList30.add("Wine Bar");
        Ob30.setSubCat(subList30);
        categoryModelList.add(Ob30);
        CategoryModel Ob31 = new CategoryModel();
        Ob31.setCategory("Restaurant");
        List<String> subList31 = new ArrayList<>();
        subList31.add("(Barbecue");
        subList31.add("Chinese");
        subList31.add("Fast Food");
        subList31.add("Ice Cream Parlor");
        subList31.add("Indian");
        subList31.add("Italian");
        subList31.add("Pizza");
        subList31.add("Seafood");
        subList31.add("Tea House");
        subList31.add("Thai");

        subList31.add("Vegetarian");
        subList31.add("Western & Continental");
        Ob31.setSubCat(subList31);
        categoryModelList.add(Ob31);

        CategoryModel Ob32 = new CategoryModel();
        Ob32.setCategory("School");
        List<String> subList32 = new ArrayList<>();
        subList32.add("(Fine Arts");
        subList32.add("Kindergarten And Child Care ");
        subList32.add("Culinary School");
        subList32.add("High School");
        subList32.add("Special School");
        subList32.add("Technical School (Iti& Diploma)");
        subList32.add("Middle School");
        subList32.add("Primary School");
        subList32.add("Pre School ");
        subList32.add("Senior High School");
        Ob32.setSubCat(subList32);
        categoryModelList.add(Ob32);
        CategoryModel Ob33 = new CategoryModel();
        Ob33.setCategory("Training And Development");
        List<String> subList33 = new ArrayList<>();
        subList33.add("Driving School");
        subList33.add("Technical Training");
        subList33.add("Other Training And Development");
        Ob33.setSubCat(subList33);
        categoryModelList.add(Ob33);
        CategoryModel Ob34 = new CategoryModel();
        Ob34.setCategory("Rental Car Agency");
        List<String> subList34 = new ArrayList<>();
        subList34.add("Rental Car Agency ");
        subList34.add("Rent-A-Bike Facility");
        Ob34.setSubCat(subList34);
        categoryModelList.add(Ob34);
        CategoryModel Ob35 = new CategoryModel();
        Ob35.setCategory("Fitness- Health Club ");
        List<String> subList35 = new ArrayList<>();
//subList35.add("Fitness Club & Center ");
//subList35.add("Sports Center");
        Ob35.setSubCat(subList35);
        categoryModelList.add(Ob35);
        CategoryModel Ob36 = new CategoryModel();
        Ob36.setCategory("Sports Activities");
        List<String> subList36 = new ArrayList<>();
        subList36.add("Sports Complex Or Stadium ");

        subList36.add("Sports Activities ");
        subList36.add("Sports Field");
        subList36.add("Indoor Sports");
        subList36.add("Other Sports Center");
//subList36.add("Motor Sport");
//subList36.add("Stadium");
// subList36.add("Tennis Court");
        Ob36.setSubCat(subList36);
        categoryModelList.add(Ob36);
        CategoryModel Ob37 = new CategoryModel();
        Ob37.setCategory("Tollboth");
        List<String> subList37 = new ArrayList<>();
//subList37.add("Cricket Ground");
//subList37.add("Horse Racing");
//subList37.add("Football");
        Ob37.setSubCat(subList37);
        categoryModelList.add(Ob37);
                CategoryModel Ob38 = new CategoryModel();
        Ob38.setCategory("Theater, Music And Culture");
        List<String> subList38 = new ArrayList<>();
        subList38.add("Theater, Music And Culture ");
        subList38.add("Concert Hall");
        subList38.add("Theater, Music And Culture ");
        subList38.add("Amphitheater");
        Ob38.setSubCat(subList38);
        categoryModelList.add(Ob38);
        CategoryModel Ob39 = new CategoryModel();
        Ob39.setCategory("Zoo, Arboreta & Botanical Garden");
        List<String> subList39 = new ArrayList<>();
        subList39.add("Arboreta & Botanical Gardens");
        subList39.add("Wild Animal Park");
        subList39.add("Zoo");
        Ob39.setSubCat(subList39);
        categoryModelList.add(Ob39);
        CategoryModel Ob40 = new CategoryModel();
        Ob40.setCategory("Car Wash -Detailing ");
        List<String> subList40 = new ArrayList<>();
        subList40.add("Car Wash -Detailing");
        subList40.add("Truck Wash");
//subList40.add("Zoo");
        Ob40.setSubCat(subList40);
        categoryModelList.add(Ob40);
        CategoryModel Ob41 = new CategoryModel();
        Ob41.setCategory("Collective Community");
        List<String> subList41 = new ArrayList<>();
        subList41.add("Beach Club");

        subList41.add("Club ");
        subList41.add("Association ");
        Ob41.setSubCat(subList41);
        categoryModelList.add(Ob41);

        CategoryModel Ob42 = new CategoryModel();
        Ob42.setCategory("Specialty Stores ");
        List<String> subList42 = new ArrayList<>();
        subList42.add("Specialty Stores ");
        subList42.add("Sweet Shop ");
        subList42.add("Butcher ");
        subList42.add("Dairy Goods ");
        subList42.add("Sweet Shop ");
        subList42.add("Food Beverage Specialty Store ");
        Ob42.setSubCat(subList42);
        categoryModelList.add(Ob42);
        CategoryModel Ob43 = new CategoryModel();
        Ob43.setCategory("Organization And Societies ");
        List<String> subList43 = new ArrayList<>();
//subList43.add("Organization And Societies ");
//subList43.add("Club ");
//subList43.add("Association ");
        Ob43.setSubCat(subList43);
        categoryModelList.add(Ob43);

        CategoryModel Ob44 = new CategoryModel();
        Ob44.setCategory("Geographic Feature");
        List<String> subList44 = new ArrayList<>();
        subList44.add("Cave");
        subList44.add("Reservoir ");
        subList44.add("Valley ");
        subList44.add("Moutain ");
        subList44.add("Water Fall ");
        Ob44.setSubCat(subList44);
        categoryModelList.add(Ob44);

        CategoryModel Ob45 = new CategoryModel();
        Ob45.setCategory("Home Improvement Hardware Store ");
        List<String> subList45 = new ArrayList<>();
        subList45.add("Furniture Store ");
        subList45.add("Paint Store ");
        subList45.add("Glass And Window ");
        subList45.add("Home Improvement Hardware Store ");
        subList45.add("Hardware House And Garden ");

        subList45.add("Lumber-Timber ");
        subList45.add("Floor And Carpet ");
        subList45.add("Major Appliance");
        subList45.add("Home Decor Store");
        Ob45.setSubCat(subList45);
        categoryModelList.add(Ob45);

        CategoryModel Ob46 = new CategoryModel();
        Ob46.setCategory("Government Office");
        List<String> subList46 = new ArrayList<>();
        subList46.add("National Government Office ");
        subList46.add("State Government Office ");
        subList46.add("Local Government Office ");
        subList46.add("Government Or Community Center");
        subList46.add("Registration Office ");
        Ob46.setSubCat(subList46);
        categoryModelList.add(Ob46);

        CategoryModel Ob47 = new CategoryModel();
        Ob47.setCategory("Industrial Zone -Industrial Building");
        List<String> subList47 = new ArrayList<>();
        Ob47.setSubCat(subList47);
        categoryModelList.add(Ob47);
        CategoryModel Ob48 = new CategoryModel();
        Ob48.setCategory("Cemetery");
        List<String> subList48 = new ArrayList<>();
//subList48.add("Sauna, Solarium & Massage");
        Ob48.setSubCat(subList48);
        categoryModelList.add(Ob48);
        CategoryModel Ob49 = new CategoryModel();
        Ob49.setCategory("Hair And Salon ");
        List<String> subList49 = new ArrayList<>();
        subList49.add("Hair And Beauty");
        subList49.add("Hair And Salon");
        subList49.add("Barber");
        Ob49.setSubCat(subList49);
        categoryModelList.add(Ob49);
        CategoryModel Ob50 = new CategoryModel();
        Ob50.setCategory("Communication-Media");
        List<String> subList50 = new ArrayList<>();
// subList50.add("Marina");
        Ob50.setSubCat(subList50);
        categoryModelList.add(Ob50);

        CategoryModel Ob51 = new CategoryModel();
        Ob51.setCategory("Social Services");
        List<String> subList51 = new ArrayList<>();
        subList51.add("Orphanage");
        subList51.add("Social Services ");
        Ob51.setSubCat(subList51);
        categoryModelList.add(Ob51);
        CategoryModel Ob52 = new CategoryModel();
        Ob52.setCategory("Parking Facility");
        List<String> subList52 = new ArrayList<>();
        subList52.add("Bike Park");
        subList52.add("Park And Ride");
        subList52.add("Parking Facility");
        subList52.add("Parking Garage- Parking House");
        subList52.add("Parking Lot");
        subList52.add("Truck Parking Lot");
        Ob52.setSubCat(subList52);
        categoryModelList.add(Ob52);
        CategoryModel Ob53 = new CategoryModel();
        Ob53.setCategory("Park -Recreation Area ");
        List<String> subList53 = new ArrayList<>();
        subList53.add("Park -Recreation Area ");
        subList53.add("Amusement Or Holiday Park");
        subList53.add("Wild Animal Park ");
        subList53.add("Animal Park");
        subList53.add("Water Park ");
        subList53.add("Theme Park");
        Ob53.setSubCat(subList53);
        categoryModelList.add(Ob53);
        CategoryModel Ob533 = new CategoryModel();
        Ob533.setCategory("Pharmacy");
        List<String> subList533 = new ArrayList<>();
//subList53.add("Orphanage");
//subList53.add("Ngo");
        Ob533.setSubCat(subList533);
        categoryModelList.add(Ob533);
        CategoryModel Ob54 = new CategoryModel();
        Ob54.setCategory("Place Of Worship");
        List<String> subList54 = new ArrayList<>();
        subList54.add("Ashram");
        subList54.add("Church");
        subList54.add("Gurudwara");
        subList54.add("Mosque");
        subList54.add("Pagoda");

        subList54.add("Synagogue");
        subList54.add("Temple");
        subList54.add("Religious Place");
        subList54.add("Other Place Of Worship");
        subList54.add("Convent");
        Ob54.setSubCat(subList54);
        categoryModelList.add(Ob54);
        CategoryModel Ob55 = new CategoryModel();
        Ob55.setCategory("Warehouse-Wholesale Store");
        List<String> subList55 = new ArrayList<>();
//subList55.add("Orphanage");
//subList55.add("Ngo");
        Ob55.setSubCat(subList55);
        categoryModelList.add(Ob55);
        CategoryModel Ob56 = new CategoryModel();
        Ob56.setCategory("Printing And Publishing");
        List<String> subList56 = new ArrayList<>();
//subList56.add("Orphanage");
//subList56.add("Ngo");
        Ob56.setSubCat(subList56);
        categoryModelList.add(Ob56);
        CategoryModel Ob57 = new CategoryModel();
        Ob57.setCategory("Internet Cafe ");
        List<String> subList57 = new ArrayList<>();
//subList57.add("Orphanage");
//subList57.add("Ngo");
        Ob57.setSubCat(subList57);
        categoryModelList.add(Ob57);
        CategoryModel Ob58 = new CategoryModel();
        Ob58.setCategory("Public Administration ");
        List<String> subList58 = new ArrayList<>();
//subList58.add("Orphanage");
//subList58.add("Ngo");
        Ob58.setSubCat(subList58);
        categoryModelList.add(Ob58);
        CategoryModel Ob59 = new CategoryModel();
        Ob59.setCategory("Public Transit Access");
        List<String> subList59 = new ArrayList<>();
        subList59.add("Bus Station");
        subList59.add("Bus Stop");
        subList59.add("Taxi Stand");
        subList59.add("Other Stop");
        Ob59.setSubCat(subList59);

        categoryModelList.add(Ob59);
        CategoryModel Ob60 = new CategoryModel();
        Ob60.setCategory("Auto Maintenances Facility");
        List<String> subList60 = new ArrayList<>();
        subList60.add("Motor Cycle Service And Maintenances ");
        subList60.add("Automobile Club");
        subList60.add("Bicycle Service And Maintenance");
        subList60.add("Car Repair");
        subList60.add("Car Repair- Service");
        subList60.add("Tire Repair");
        subList60.add("Truck Repair");
        subList60.add("Repair And Maintenance Service");
        Ob60.setSubCat(subList60);
        categoryModelList.add(Ob60);
        CategoryModel Ob61 = new CategoryModel();
        Ob61.setCategory("Auto Parts ");
        List<String> subList61 = new ArrayList<>();
//subList61.add("Motor Cycle Accessories ");
//subList61.add("Bicycle And Bicycle Accessories Shop ");
        Ob61.setSubCat(subList61);
        categoryModelList.add(Ob61);

        CategoryModel Ob62 = new CategoryModel();
        Ob62.setCategory("Public Rest Room- Toilet");
        List<String> subList62 = new ArrayList<>();
//subList62.add("Orphanage");
//subList62.add("Ngo");
        Ob62.setSubCat(subList62);
        categoryModelList.add(Ob62);
        CategoryModel Ob63 = new CategoryModel();
        Ob63.setCategory("Swimming Pool");
        List<String> subList63 = new ArrayList<>();
//subList63.add("Orphanage");
//subList63.add("Ngo");
        Ob63.setSubCat(subList63);
        categoryModelList.add(Ob63);
        CategoryModel Ob64 = new CategoryModel();
        Ob64.setCategory("Tourist Information ");
        List<String> subList64 = new ArrayList<>();
//subList64.add("Orphanage");
//subList64.add("Ngo");
        Ob64.setSubCat(subList64);

        categoryModelList.add(Ob64);
        CategoryModel Ob644 = new CategoryModel();
        Ob644.setCategory("Transport Authority/Vehicle Registration");
        List<String> subList644 = new ArrayList<>();
//subList64.add("Orphanage");
//subList64.add("Ngo");
        Ob644.setSubCat(subList644);
        categoryModelList.add(Ob644);
        CategoryModel Ob65 = new CategoryModel();
        Ob65.setCategory("Veterinary Medicine ");
        List<String> subList65 = new ArrayList<>();
        subList65.add("Veterinary Medicine ");
        subList65.add("Veterinary Hospital ");
        Ob65.setSubCat(subList65);
        categoryModelList.add(Ob65);
        CategoryModel Ob66 = new CategoryModel();
        Ob66.setCategory("Weigh Station");
        List<String> subList66 = new ArrayList<>();
        subList66.add("Weigh Scales");
//subList66.add("Ngo");
        Ob66.setSubCat(subList66);
        categoryModelList.add(Ob66);
        CategoryModel Ob67 = new CategoryModel();
        Ob67.setCategory("Shopping");
        List<String> subList67 = new ArrayList<>();
        subList67.add("(Book Store");
        subList67.add("Arts And Crafts Supplies");
        subList67.add("Bakery And Baked Goods Store");
        subList67.add("Children's Apparel");
        subList67.add("Clothing And Accessories");
        subList67.add("Computer And Software Store");
        subList67.add("Consumer Electronics Store");
        subList67.add("Convenience Store");
        subList67.add("Department Store ");
        subList67.add("Entertainment Electronics");
        subList67.add("Florist");
        subList67.add("Flowers And Jewelry");
        subList67.add("Garden Center");
        subList67.add("Gift Antique And Art");
        subList67.add("Grocery");
        subList67.add("Hunting Fishing Shop");
        subList67.add("Jeweler");
        subList67.add("Men's Apparels");
        subList67.add("Mobile Retailer");
        subList67.add("Mobile Service Center");

        subList67.add("Nail Salon");
        subList67.add("Office Supply And Service Store");
        subList67.add("Pet Supply");
        subList67.add("Tele Communications");
        subList67.add("Pawnshop");
        subList67.add("Power Equipment Dealers");
        subList67.add("Record Cd And Video");
        subList67.add("Shoes -Footwear");
        subList67.add("Shopping Mall");
        subList67.add("Specialty Clothing Store");
        subList67.add("Specialty Food Store");
        subList67.add("Sporting Goods Store");
        subList67.add("Toy Store");
        subList67.add("Used -Second Hand Merchandise Store");
        subList67.add("Variety Store");
        subList67.add("General Merchandise");
        subList67.add("Video And Game Rental");
        subList67.add("Women's Apparel");
        subList67.add("Tele Communications");
        subList67.add("Tack Shop");
        subList67.add("Electrical Store");
        subList67.add("Industrial Equipment’s Supply Store");
        Ob67.setSubCat(subList67);
        categoryModelList.add(Ob67);
        CategoryModel Ob68 = new CategoryModel();
        Ob68.setCategory("Atm");
        List<String> subList68 = new ArrayList<>();
        subList68.add("Atm");
        subList68.add("Cash Deposit");
        Ob68.setSubCat(subList68);
        categoryModelList.add(Ob68);
        CategoryModel Ob69 = new CategoryModel();
        Ob69.setCategory("Facilities");
        List<String> subList69 = new ArrayList<>();
        subList69.add("(Advertising/Marketing");
        subList69.add("Chemicals");
        subList69.add("Diversified Financial");
        subList69.add("Electronics");
        subList69.add("(Legal Services");
        subList69.add("Couriers");
        subList69.add("Mechanical Engineering");
        subList69.add(" Pharmaceuticals Arber");
        subList69.add("(Transport Service");
        subList69.add("(Aviation");
        subList69.add("B2b Restaurant Service");
        subList69.add("B2b Sales And Service");
        subList69.add("Bill Payment Service");
        subList69.add("Boating Services");
        subList69.add("Body Piercing And Tattoos");

        subList69.add("Business Facility");
        subList69.add("Computer And Software Service");
        subList69.add("Business Service");
        subList69.add("Catering And Other Food Services");
        subList69.add("Check Cashing Service Currency Exchange");
        subList69.add("Commercial Services");
        subList69.add("Construction ");
        subList69.add("Customer Service ");
        subList69.add("Customer Care Service Center ");
        subList69.add("Dry-Cleaning And Laundry");
                subList69.add("Electrical Services ");
        subList69.add("Emission Testing ");
        subList69.add("Engineering And Scientific Services ");
        subList69.add("(Entertainment And Recreation ");
        subList69.add("Farming ");
        subList69.add("Finance And Insurance ");
        subList69.add("Financial Investment Firm ");
        subList69.add("Food Production ");
        subList69.add("Funeral Home ");
        subList69.add("Human Resources And Recreating Services ");
        subList69.add("Interior And Exterior Design ");
        subList69.add("It And Office Equipment Services ");
        subList69.add("Landscaping Services ");
        subList69.add("Maid Services ");
        subList69.add("Management And Consulting Services ");
        subList69.add("Manufacturing ");
        subList69.add("Managing And Matchmaking Services ");
        subList69.add("Mining ");
        subList69.add("Movers ");
        subList69.add("Pet Care ");
        subList69.add("Plumbing ");
        subList69.add("Police Services -Security ");
        subList69.add("Locksmiths And Security System Services");
        subList69.add("Property Management ");
        subList69.add("Real Estate Services ");
        subList69.add("Rental And Leasing");
        subList69.add("Repair And Maintenance Services");
                subList69.add("Repair Service ");
        subList69.add("Modeling Agencies");
        subList69.add("Road Assistance");
        subList69.add("Specialty Trend Contractors");
        subList69.add("Storage");
        subList69.add("Cargo Center");
        subList69.add("Tele Communications");
        subList69.add("Tailor And Alteration");
        subList69.add("Tanning Salon");
        subList69.add("Tax Services");
        subList69.add("Telephone Service");
        subList69.add("Towing Service");
        subList69.add("Translation And Interpretation Services");
        subList69.add("Travel Agent Ticketing");

        subList69.add("Waste And Sanitary");
        subList69.add("Wedding Services And Bridal Studio");
        Ob69.setSubCat(subList69);
        categoryModelList.add(Ob69);

        CategoryModel Ob70 = new CategoryModel();
        Ob70.setCategory("Education Facility");
        List<String> subList70 = new ArrayList<>();
        subList70.add("Language Studies");
//subList70.add("Technical Training");
//subList70.add("Other Training And Development");
        Ob70.setSubCat(subList70);
        categoryModelList.add(Ob70);
        CategoryModel Ob71 = new CategoryModel();
        Ob71.setCategory("Coffee-Tea ");
        List<String> subList71 = new ArrayList<>();
        subList71.add(" Coffee-Tea");
        subList71.add("Coffee Shop");
        Ob71.setSubCat(subList71);
        categoryModelList.add(Ob71);
        CategoryModel Ob72 = new CategoryModel();
        Ob72.setCategory("Banquet Hall");
        List<String> subList72 = new ArrayList<>();
//subList71.add(" Coffee-Tea");
//subList71.add("Coffee Shop");
        Ob72.setSubCat(subList72);
        categoryModelList.add(Ob72);

    }

    private void uploadData()
    {
/*
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        mDBRef = firebaseDatabase.getReference().child("POACollections").child(mMoobile);
        paModel.setmDate( new Date().getTime());
        mDBRef.push().setValue(paModel);
        etPlaceName.setText("");
        website.setText("");
        Email.setText("");
        comment.setText("");
        Phone.setText("");
        address.setText("");
       // etSB3.setText("");
       etPin.setText("");
        //Toast.makeText(addPAActivity.this, etHouseNo.getText().toString()+"\n"+ etHouseName.getText().toString()+"\n"+etStreetName.getText().toString()+"\n"+etLat.getText().toString()+"\n"+etLon.getText().toString(), Toast.LENGTH_SHORT).show();
*/
    }

    private String getAddress(double latitude, double longitude) {
        try {
            Geocoder geo = new Geocoder(this.getApplicationContext(), Locale.getDefault());
            List<Address> addresses = geo.getFromLocation(latitude, longitude, 1);
            if (addresses.isEmpty()) {
            } else {
                if (addresses.size() > 0) {
                    etPinCode.setText(addresses.get(0).getPostalCode());
                    etStreet.setText(addresses.get(0).getSubLocality());
                    etLocality.setText(addresses.get(0).getLocality());
                    etPOINumber.setText(addresses.get(0).getPhone());
                    etName.setText(addresses.get(0).getFeatureName());
                }
            }
        } catch (Exception e) {
            e.printStackTrace(); // getFromLocation() may sometimes fail
        }
        return "";
    }


}
