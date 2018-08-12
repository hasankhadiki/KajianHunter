package tehhutan.app.kajianhunter;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import tehhutan.app.kajianhunter.Interface.ItemClickListener;
import tehhutan.app.kajianhunter.KajianDetails.KajianDescription;
import tehhutan.app.kajianhunter.model.BookingList;

import static android.content.Context.LOCATION_SERVICE;


public class FindingFragment extends Fragment {



    private final int REQUEST_LOCATION_PERMISSION = 1;

    private FirebaseDatabase database;
    private DatabaseReference kajianlist, addKajian;
    private FloatingActionButton fab;

    private EditText editNamaPeminjam, editOrganisasi, editKegiatan, editJamMulai, editJamAkhir, editDeskripsi;
    private Button btnSubmit;
    private ImageView pickPlace;
    private RecyclerView recyclerBookingList;
    private RecyclerView.LayoutManager layoutManager;

    private MainActivity mainRef;
    private double latitude = 0.0;
    private double longtitude = 0.0;
    private final int PLACE_PICKER_REQUEST = 442;
    private boolean isPlaceButtonClicked = false;

    // Google Map
    private GoogleMap map;
    private SupportMapFragment mapFragment;

    Marker marker;
    List<KajianList> kajian = new ArrayList<>();

    private TabLayout tabLayout;
    private ViewPager viewPager;

    public FindingFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setTitle("Finding");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        setHasOptionsMenu(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setElevation(0);

        mainRef = (MainActivity) getActivity();
        database = FirebaseDatabase.getInstance();
        kajianlist = database.getReference("KajianList/Verified");
        addKajian = database.getReference("KajianList/Unverified");
        View view = inflater.inflate(R.layout.fragment_finding, container, false);

        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        FloatingActionButton fab = (FloatingActionButton)view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
                mainRef.kajianDialog = getLayoutInflater().inflate(R.layout.add_booking, null);
                editNamaPeminjam = (EditText) mainRef.kajianDialog.findViewById(R.id.et_namapeminjam);
                editOrganisasi = (EditText) mainRef.kajianDialog.findViewById(R.id.et_organisasi);
                editKegiatan = (EditText) mainRef.kajianDialog.findViewById(R.id.et_deskripsikegiatan);
                editJamMulai = (EditText) mainRef.kajianDialog.findViewById(R.id.et_jammulai);
                editJamAkhir = (EditText) mainRef.kajianDialog.findViewById(R.id.et_jamakhir);
                editDeskripsi = (EditText) mainRef.kajianDialog.findViewById(R.id.et_deskripsikajian);
                btnSubmit = (Button) mainRef.kajianDialog.findViewById(R.id.btn_submit);
                pickPlace = (ImageView) mainRef.kajianDialog.findViewById(R.id.pickPlace);
                pickPlace.bringToFront();

                isPlaceButtonClicked = false;
                final String mGroupId = addKajian.push().getKey();

                mBuilder.setView(mainRef.kajianDialog);
                final AlertDialog dialog = mBuilder.create();
                dialog.show();
                //alertDialog=(getActivity())dialog.ge;

                pickPlace.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        isPlaceButtonClicked = true;
                        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();;
                        Intent intent;
                        try {
                            intent = builder.build(getActivity());
                            mainRef.startActivityForResult(intent, PLACE_PICKER_REQUEST);
                        } catch (GooglePlayServicesRepairableException e){
                            e.printStackTrace();
                        } catch (GooglePlayServicesNotAvailableException e){
                            e.printStackTrace();
                        }

                    }
                });

                editJamMulai.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        final Calendar c = Calendar.getInstance();
                        int hour = c.get(Calendar.HOUR_OF_DAY);
                        int minute = c.get(Calendar.MINUTE);

                        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),

                                new TimePickerDialog.OnTimeSetListener() {

                                    @Override
                                    public void onTimeSet(TimePicker view, int hourOfDay,
                                                          int minute) {

                                        editJamMulai.setText(hourOfDay + ":" + minute);
                                    }
                                }, hour, minute, false);
                        timePickerDialog.show();

                    }
                });


                editJamAkhir.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        final Calendar c = Calendar.getInstance();
                        int hour = c.get(Calendar.HOUR_OF_DAY);
                        int minute = c.get(Calendar.MINUTE);

                        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),
                                new TimePickerDialog.OnTimeSetListener() {

                                    @Override
                                    public void onTimeSet(TimePicker view, int hourOfDay,
                                                          int minute) {

                                        editJamAkhir.setText(hourOfDay + ":" + minute);
                                    }
                                }, hour, minute, false);
                        timePickerDialog.show();

                    }
                });


                // set listeners
                editNamaPeminjam.addTextChangedListener(mTextWatcher);
                editOrganisasi.addTextChangedListener(mTextWatcher);
                editKegiatan.addTextChangedListener(mTextWatcher);
                editJamMulai.addTextChangedListener(mTextWatcher);
                editJamAkhir.addTextChangedListener(mTextWatcher);
                editDeskripsi.addTextChangedListener(mTextWatcher);

                // run once to disable if empty
                checkFieldsForEmptyValues();

                btnSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ConnectivityManager connectivity = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
                        NetworkInfo activeNetwork = connectivity.getActiveNetworkInfo();
                        if(!isPlaceButtonClicked){
                            Toast.makeText(getActivity(), "Silahkan ambil lokasi dengan menekan ikon lokasi!", Toast.LENGTH_SHORT).show();
                        }  else
                        if (activeNetwork != null) { // connected to the internet
                            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                                // connected to wifi
                                BookingList newBooking = new BookingList(editOrganisasi.getText().toString()
                                        , editNamaPeminjam.getText().toString()
                                        , editKegiatan.getText().toString()
                                        , editJamMulai.getText().toString()
                                        , editJamAkhir.getText().toString()
                                        , editDeskripsi.getText().toString()
                                        ,mainRef.plcLatitude
                                        ,mainRef.plcLongtitude
                                );
                                addKajian.child(mGroupId).setValue(newBooking);
                                Toast.makeText(getActivity(), "Kajian akan ditampilkan jika disetujui!", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                                // connected to the mobile provider's data plan
                                BookingList newBooking = new BookingList(editOrganisasi.getText().toString()
                                        , editNamaPeminjam.getText().toString()
                                        , editKegiatan.getText().toString()
                                        , editJamMulai.getText().toString()
                                        , editJamAkhir.getText().toString()
                                        , editDeskripsi.getText().toString()
                                        ,mainRef.plcLatitude
                                        ,mainRef.plcLongtitude
                                );
                                addKajian.child(mGroupId).setValue(newBooking);
                                addKajian.child(mGroupId).child("koordinatTempat").child("latitude").setValue(mainRef.plcLatitude);
                                addKajian.child(mGroupId).child("koordinatTempat").child("longtitude").setValue(mainRef.plcLongtitude);
                                Toast.makeText(getActivity(), "Kajian akan ditampilkan jika disetujui!", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        } else {
                            Toast.makeText(getActivity(), "Internet Connection Not Available, Please Check Your Connection Setting", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }


        });

        return view;
    }


    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            // check Fields For Empty Values
            checkFieldsForEmptyValues();
        }
    };


    void checkFieldsForEmptyValues(){

        String s1 = editNamaPeminjam.getText().toString();
        String s2 = editOrganisasi.getText().toString();
        String s4 = editJamMulai.getText().toString();
        String s5 = editJamAkhir.getText().toString();

        if(s1.equals("")
                || s2.equals("")
                || s4.equals("")
                || s5.equals("")
                ){
            btnSubmit.setEnabled(false);
        } else {
            btnSubmit.setEnabled(true);
        }
    }

    public void setTitle(String title) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TextView textView = new TextView(getActivity());
        textView.setText(title);
        textView.setTextSize(18);
        textView.setTypeface(null, Typeface.BOLD);
        textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        textView.setGravity(Gravity.CENTER);


        textView.setTextColor(getResources().getColor(R.color.white));
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setCustomView(textView);
    }



    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new MapsFragment(), "MAPS");
        adapter.addFragment(new ListFragment(), "LIST");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

}