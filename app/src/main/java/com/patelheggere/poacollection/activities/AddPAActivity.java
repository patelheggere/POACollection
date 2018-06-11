package com.patelheggere.poacollection.activities;

import android.location.Address;
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
import com.patelheggere.poacollection.dbhelper.DBManager;
import com.patelheggere.poacollection.models.CategoryModel;
import com.patelheggere.poacollection.models.PAModel;
import com.patelheggere.poacollection.R;
import com.patelheggere.poacollection.models.POIDetails;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AddPAActivity extends AppCompatActivity {

    private double lat, lon;
    private EditText etName, etPOINumber, etSubCat, etBName, etBNumber, etNoOfFloor, etBrand, etLAndMark, etStreet, etLocality, etPinCode, etComment, etLat, etLon;
    private Button mSubmit, mCancel;
    private Spinner Category, SubCategory;
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
    private String[] CatArray = {"Amusement Park", "Automotive Dealer", "Bank", "Beach", "Business Park", "Cafe/Pub", "Cash Dispenser", "Casino",
    "Chowk/Local Landmark", "Cinema"};
    //private String[] Sub
    private DBManager dbManager;

    private List<CategoryModel> categoryModelList = new ArrayList<>();
    //test
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popupwindowfor_pa);
        dbManager = new DBManager(AddPAActivity.this);
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
        initialiseUI();
    }

    private void initialiseUI()
    {
        getSupportActionBar().setTitle("Enter POA Details");
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

        getCatDetails();

        final List<String> catList = new ArrayList<>();
        for (int i = 1;i<categoryModelList.size(); i++)
        {
            catList.add(categoryModelList.get(i).getCategory());
        }


       // ArrayAdapter<String> Cate = ArrayAdapter.createFromResource(this)
        ArrayAdapter<String> CategoryAdapter = new ArrayAdapter<String>( this, android.R.layout.simple_spinner_item, catList);
       // ArrayAdapter<CharSequence> CategoryAdapter = ArrayAdapter.createFromResource(this, R.array.array_cat, android.R.layout.simple_spinner_item);
        CategoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Applying the adapter to our spinner
        Category.setAdapter(CategoryAdapter);
        Category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mCategoryString = adapterView.getItemAtPosition(i).toString();
                System.out.println("cate position:"+i);
               // subCategoryAd = new ArrayAdapter<String>( getApplicationContext(), android.R.layout.simple_spinner_item, catList);
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
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


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
        ob.setmLattitude(""+String.valueOf(lon));
        ob.setmLonggitude(""+lon);
        System.out.println("GDfg:"+ob.getmLattitude()+"\n dfsdfg:"+ob.getmLonggitude());
        ob.setmPOINumber(etPOINumber.getText().toString());
        ob.setmPhoneNumberr(mMoobile);
        ob.setmPersonName(mAuth.getCurrentUser().getDisplayName());
        dbManager.open();
        dbManager.insert(ob);
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
        */
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
        Address.setText("");
       // etSB3.setText("");
       etPin.setText("");
        //Toast.makeText(AddPAActivity.this, etHouseNo.getText().toString()+"\n"+ etHouseName.getText().toString()+"\n"+etStreetName.getText().toString()+"\n"+etLat.getText().toString()+"\n"+etLon.getText().toString(), Toast.LENGTH_SHORT).show();
*/
    }


}
