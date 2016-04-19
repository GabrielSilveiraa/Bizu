package com.example.gabrielsilveira.bizu;

import android.animation.LayoutTransition;
import android.app.ActionBar;
import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.preference.PreferenceManager;
import android.os.Bundle;

import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;

import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gabrielsilveira.bizu.adapter.CompanyFeaturesCursorAdapter;
import com.example.gabrielsilveira.bizu.adapter.DrawerLayoutBaseAdapter;
import com.example.gabrielsilveira.bizu.adapter.SuggestionSimpleCursorAdapter;
import com.example.gabrielsilveira.bizu.db.SuggestionsDatabase;
import com.example.gabrielsilveira.bizu.model.Company;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;


public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener, SearchView.OnQueryTextListener, SearchView.OnSuggestionListener, View.OnClickListener {

    final Context context = this;

    private RelativeLayout parentLayout;
    private GoogleMap mMap;
    private Location currentLocation;
    private double longitude;
    private double latitude;
    private int actualRadiusSelected = 1000;

    private boolean isFirstLocationUpdate = true;

    private SuggestionsDatabase database;

    private SearchView searchView;

    private boolean isDialogExpanded = false;

    private ArrayList<Company> companiesWithTag = new ArrayList<>();
    private ArrayList<Company> testCompaniesList = new ArrayList<>();
    private ArrayList<Company> companiesToShow = new ArrayList<>();

    private CircleOptions circleOptions;

    private ListView mDrawerList;
    private DrawerLayout mDrawerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        parentLayout = (RelativeLayout) findViewById(R.id.mapsParentLayout);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        if (myToolbar != null){
            getSupportActionBar().setDisplayShowTitleEnabled(false);
       }

        ImageView burgerMenu = (ImageView) findViewById(R.id.burgerMenu);
        burgerMenu.setOnClickListener(this);

        mDrawerList = (ListView) findViewById(R.id.navList);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        setupDrawerLayout();


        this.setSearchView();
        this.dismissKeyboard(parentLayout);
        this.setFonts();
        this.setSuggestionsDatabase();
        this.createTestCompanies();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        circleOptions = new CircleOptions().radius(actualRadiusSelected);

        LocationManager locationManager =
                (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (Build.VERSION.SDK_INT >= 23 &&
                checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
    }

    private void setFonts() {
        Utilities.setFontNexa(this, parentLayout);
    }

    //MARK: DRAWER LAYOUT

    private void setupDrawerLayout() {
        int width = getResources().getDisplayMetrics().widthPixels*2/3;
        DrawerLayout.LayoutParams params = (android.support.v4.widget.DrawerLayout.LayoutParams) mDrawerList.getLayoutParams();
        params.width = width;
        mDrawerList.setLayoutParams(params);

        DrawerLayoutBaseAdapter drawerAdapter = new DrawerLayoutBaseAdapter(context);
        mDrawerList.setAdapter(drawerAdapter);

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(context, "" + position, Toast.LENGTH_LONG).show();
                switch (position) {
                    case 1:
                        // PERFIL
                        break;
                    case 2:
                        // CONTAR A UM AMIGO
                        break;
                    case 3:
                        // RELATAR UM PROBLEMA
                        break;
                    case 4:
                        // SOBRE
                        break;
                    case 5:
                        // TERMOS
                        break;
                    case 6:
                        // TUTORIAL
                        break;
                }
            }
        });
    }

    // MARK: GOOGLEMAPS

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
    }

    @Override
    public void onLocationChanged(Location location) {
        currentLocation = location;
        latitude = location.getLatitude();
        longitude = location.getLongitude();

        if(isFirstLocationUpdate) {
            LatLng currentLatLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 14));
            isFirstLocationUpdate= false;
        }
        this.refreshMap();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    private void refreshMap() {
        mMap.clear();
        LatLng currentLatLng = new LatLng(latitude, longitude);
        mMap.setMyLocationEnabled(true);
        if (actualRadiusSelected == 0) {
            showCompaniesOnMap(companiesWithTag);
        } else {
            this.circleConfiguration(currentLatLng);
            ArrayList<Company> companiesWithTagInRadius = getCompaniesInRadius(companiesWithTag, actualRadiusSelected);
            showCompaniesOnMap(companiesWithTagInRadius);
        }
    }

    private void circleConfiguration(LatLng currentLatLng){
        circleOptions.center(currentLatLng).radius(actualRadiusSelected);
        circleOptions.strokeWidth(4);
        circleOptions.strokeColor(Color.parseColor("#b7c426"));
        circleOptions.fillColor(Color.parseColor("#4D9eb333"));
        mMap.addCircle(circleOptions);
    }


    public void radioButtonClicked(View view){
        mMap.clear();
        LatLng currentLatLng = new LatLng(latitude, longitude);
        switch (view.getId()){
            case R.id.rdBt1km:
                actualRadiusSelected = 1000;
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 14));
                break;
            case R.id.rdBt2km:
                actualRadiusSelected = 2000;
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 13));
                break;
            case R.id.rdBt5km:
                actualRadiusSelected = 5000;
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 11.75f));
                break;
            case R.id.rdBtAll:
                actualRadiusSelected = 0;
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 11));
                break;
        }
        this.refreshMap();
    }

    // MARK: COMPANIES

    private ArrayList<Company> getCompaniesWithTag(ArrayList<Company> companies, String tag){
        ArrayList<Company> companiesWithTag = new ArrayList<>();
        for(Company company : companies) {
            if(company.getTags().contains(tag)){
                companiesWithTag.add(company);
            }
        }
        return companiesWithTag;
    }

    private ArrayList<Company> getCompaniesInRadius(ArrayList<Company> companies, int radius){
        ArrayList<Company> companiesInRadius = new ArrayList<>();
        for(Company company : companies) {
            Location companyLocation = new Location("");
            companyLocation.setLatitude(company.getLatitude());
            companyLocation.setLongitude(company.getLongitude());
            if(currentLocation.distanceTo(companyLocation) <= radius) {
                companiesInRadius.add(company);
            }
        }
        return companiesInRadius;
    }

    private void showCompaniesOnMap(ArrayList<Company> companies){
        companiesToShow = companies;
        for(Company company: companies){
            mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(company.getLatitude(), company.getLongitude()))
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.aberto)));

        }
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View v = inflater.inflate(R.layout.info_window_company, null);
                for(Company company: companiesToShow){
                    if(company.getLongitude() == marker.getPosition().longitude && company.getLatitude() == marker.getPosition().latitude){
                        v = setInfoWindow(v, company);
                        break;
                    }
                }
                return v;
            }
        });
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                for (Company company: companiesToShow){
                    if (company.getLongitude() == marker.getPosition().longitude && company.getLatitude() == marker.getPosition().latitude){
                        //setCompanyDialog(company);
                        setCompanyDialog(company).show();
                    }
                }

            }
        });
//        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
//            @Override
//            public boolean onMarkerClick(Marker marker) {
//                for (Company company: companiesToShow){
//                    if (company.getLongitude() == marker.getPosition().longitude && company.getLatitude() == marker.getPosition().latitude){
//                        //setCompanyDialog(company);
//                        setCompanyDialog(company).show();
//                    }
//                }
//                return false;
//            }
//        });
    }

    private View setInfoWindow(View v, Company company){
        final ViewGroup infoWindowParentLayout = (ViewGroup) v.findViewById(R.id.infoWindowParentLayout);
        TextView companyName = (TextView) v.findViewById(R.id.infoWindowCompanyName);
        TextView companyAddress = (TextView) v.findViewById(R.id.infoWindowCompanyAddress);
        TextView companyDescription = (TextView) v.findViewById(R.id.infoWindowCompanyDescription);
        TextView companyPhone = (TextView) v.findViewById(R.id.infoWindowCompanyPhone);
        companyName.setText(company.getName());
        companyAddress.setText(company.getAddress());
        companyDescription.setText(company.getDescription());
        companyPhone.setText(" "+company.getPhone());
        Utilities.setFontNexa(context, infoWindowParentLayout);
        return v;
    }

    private Dialog setCompanyDialog(Company company){

        Dialog companyDialog = new Dialog(context);
        companyDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        companyDialog.setContentView(R.layout.dialog_company);

        final ViewGroup dialogCompanyLayout = (ViewGroup) companyDialog.findViewById(R.id.dialogCompanyLayout);
        TextView companyName = ((TextView)companyDialog.findViewById(R.id.companyName));
        companyName.setText(company.getName());
        TextView companyAddress = ((TextView)companyDialog.findViewById(R.id.companyAddress));
        companyAddress.setText(company.getAddress());
        TextView companyDescription = ((TextView)companyDialog.findViewById(R.id.companyDescription));
        companyDescription.setText(company.getDescription());
        TextView companyHorary = ((TextView)companyDialog.findViewById(R.id.companyHorary));
        String strHorary = " "+company.getHorary();
        companyHorary.setText(strHorary);
        TextView phone = (TextView) companyDialog.findViewById(R.id.companyPhone);
        phone.setText(" "+company.getPhone());

        companyDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        companyDialog.getWindow().setGravity(Gravity.TOP);

        final GridView featuresGrid = new GridView(context);
        featuresGrid.setNumColumns(5);
        featuresGrid.setAdapter(new CompanyFeaturesCursorAdapter(context));

        final Button btMoreInfo = (Button) companyDialog.findViewById(R.id.btMoreInfo);
        final ViewGroup.LayoutParams btMoreInfoOriginalParams = btMoreInfo.getLayoutParams();
        btMoreInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isDialogExpanded) {
                    dialogCompanyLayout.addView(featuresGrid, 5);
                    btMoreInfo.setBackgroundResource(R.drawable.subir);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    params.gravity = Gravity.CENTER;
                    params.topMargin = 20;
                    btMoreInfo.setLayoutParams(params);
                    btMoreInfo.setText("");
                    isDialogExpanded = true;
                } else {
                    dialogCompanyLayout.removeView(featuresGrid);
                    btMoreInfo.setBackgroundResource(R.drawable.button_yellow_border);
                    btMoreInfo.setLayoutParams(btMoreInfoOriginalParams);
                    btMoreInfo.setText(R.string.more_info);
                    isDialogExpanded = false;
                }
            }
        });
        Utilities.setFontNexa(context, dialogCompanyLayout);
        return companyDialog;
    }

    private void createTestCompanies(){
        Company c1 = new Company();
        c1.setName("Bourbon Ipiranga");
        c1.setLongitude(-51.1868);
        c1.setLatitude(-30.0567);
        ArrayList<String> tagsc1 = new ArrayList<>();
        tagsc1.add("shopping");
        c1.setTags(tagsc1);
        c1.setDescription("Blablbablabal Blablbab babal blablablabalba Blablbablabal Blablbab babal blablablabalba Blablbablabal Blablbab babal blablablabalba Blablbablabal Blablbab babal blablablabalba");
        c1.setAddress("Av. Ipiranga, 5200");
        c1.setHorary("10h até 24h");
        c1.setPhone("51 98222234");
        testCompaniesList.add(c1);

        Company c2 = new Company();
        c2.setName("PUCRS");
        c2.setLongitude(-51.1622);
        c2.setLatitude(-30.056);
        ArrayList<String> tagsc2 = new ArrayList<>();
        tagsc2.add("faculdade");
        c2.setTags(tagsc2);
        c2.setDescription("Blablbablabal Blablbab babal blablablabalba Blablbablabal Blablbab babal blablablabalba Blablbablabal Blablbab babal blablablabalba Blablbablabal Blablbab babal blablablabalba");
        c2.setAddress("Av. Ipiranga, 6681");
        c2.setHorary("08h até 23h");
        c2.setPhone("51 98222234");
        testCompaniesList.add(c2);
    }

    private void insertTagsInDatabase(){
        database.insertSuggestion("faculdade");
        database.insertSuggestion("shopping");
    }

    // MARK: SEARCHVIEW

    private void setSearchView(){
        searchView = (SearchView) findViewById(R.id.search);
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setBackgroundColor(Color.WHITE);
        searchView.setQueryHint("O que você procura?");
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(this);
        searchView.setOnSuggestionListener(this);
        searchView.setOnClickListener(this);
        searchView.setSubmitButtonEnabled(true);
        AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) searchView.findViewById(searchView.getResources().getIdentifier("android:id/search_src_text", null, null));
        autoCompleteTextView.setThreshold(1);
    }

    private void setSuggestionsDatabase(){
        database = new SuggestionsDatabase(this);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if(!prefs.getBoolean("firstTime", false)) {
            this.insertTagsInDatabase();
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("firstTime", true);
            editor.apply();
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        companiesWithTag = this.getCompaniesWithTag(testCompaniesList, query);
        this.refreshMap();
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        Cursor cursor = database.getSuggestions(newText);
        if(cursor!=null && cursor.getCount() > 0) {
            String[] columns = new String[] {SuggestionsDatabase.FIELD_SUGGESTION };
            int[] columnTextId = new int[] { android.R.id.text1};
                SuggestionSimpleCursorAdapter simple = new SuggestionSimpleCursorAdapter(getBaseContext(),
                    android.R.layout.simple_list_item_1, cursor,
                    columns, columnTextId, 0);
            searchView.setSuggestionsAdapter(simple);
            return true;
        } else {
            SuggestionSimpleCursorAdapter simple = new SuggestionSimpleCursorAdapter(getBaseContext(),
                    android.R.layout.simple_list_item_1, null,
                    null, null, 0);
            searchView.setSuggestionsAdapter(simple);
            return false;
        }
    }

    @Override
    public boolean onSuggestionSelect(int position) {
        return false;
    }

    @Override
    public boolean onSuggestionClick(int position) {

        SQLiteCursor cursor = (SQLiteCursor) searchView.getSuggestionsAdapter().getItem(position);
        int indexColumnSuggestion = cursor.getColumnIndex( SuggestionsDatabase.FIELD_SUGGESTION);

        searchView.setQuery(cursor.getString(indexColumnSuggestion), true);
        return false;
    }

    private void dismissKeyboard(View view){
        //Set up touch listener for non-text box views to hide keyboard.
        if(!(view instanceof SearchView)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    Utilities.hideSoftKeyboard(MapsActivity.this);
                    return false;
                }
            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                dismissKeyboard(innerView);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search:
                searchView.setIconified(false);
                break;
            case R.id.burgerMenu:
                if (mDrawerLayout.isDrawerOpen(Gravity.RIGHT)) {
                    mDrawerLayout.closeDrawer(Gravity.RIGHT);
                } else {
                    mDrawerLayout.openDrawer(Gravity.RIGHT);
                }
                break;
        }
    }
}
