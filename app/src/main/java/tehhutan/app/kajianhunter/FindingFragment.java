package tehhutan.app.kajianhunter;

import android.*;
import android.Manifest;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import tehhutan.app.kajianhunter.Interface.ItemClickListener;
import tehhutan.app.kajianhunter.KajianDetails.KajianDescription;
import tehhutan.app.kajianhunter.model.BookingList;

import static android.content.Context.LOCATION_SERVICE;


public class FindingFragment extends Fragment implements OnMapReadyCallback{

    private boolean needsInit=false;

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

//        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
//        mapFragment.getMapAsync(new OnMapReadyCallback() {
//            @Override
//            public void onMapReady(GoogleMap googleMap) {
//                map = googleMap;
//                googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
//                kajianlist.addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        for (DataSnapshot s : dataSnapshot.getChildren()) {
//                            KajianList kajianList = s.getValue(KajianList.class);
//
//                            kajian.add(kajianList);
//                            for (int i = 0; i < kajian.size(); i++) {
//                                LatLng latLng = new LatLng(s.child("koordinatTempat").child("latitude").getValue(Long.class)
//                                        , s.child("koordinatTempat").child("longtitude").getValue(Long.class));
//                                if (map != null) {
//                                    marker = map.addMarker(new MarkerOptions().position(latLng).title(kajianList.getDepartemen()));
//                                }
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                    }
//                });
//            }
//        });


//        kajianlist.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
//                for(DataSnapshot child : dataSnapshot.getChildren()){
//
//                    final String tema = child.child("departemen").getValue().toString();
//                    final String tempat = child.child("kegiatan").getValue().toString();
//                    final String deskripsi = child.child("deskripsi").getValue().toString();
////                    final String pict = child.child("pict").getValue().toString();
//                    final String namaUst = child.child("nama").getValue().toString();
//                    final double latitude = child.child("koordinatTempat").child("latitude").getValue(double.class);
//                    final double longitude = child.child("koordinatTempat").child("longtitude").getValue(double.class);
//
//
//                    final LatLng latLng = new LatLng(latitude, longitude);
//                        mapFragment = SupportMapFragment.newInstance();
//                        mapFragment.getMapAsync(new OnMapReadyCallback() {
//                            @Override
//                            public void onMapReady(GoogleMap googleMap) {
//
//                                map = googleMap;
//
//                                map.addMarker(new MarkerOptions()
//                                        .position(latLng)
//                                        .title(tema));
//                                map.getUiSettings().setMyLocationButtonEnabled(true);
//
//                                // Zoom to device's current location
//                                if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
//                                        ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//                                    map.setMyLocationEnabled(true);
//                                    LocationManager locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
//                                    String provider = locationManager.getBestProvider(new Criteria(), true);
//                                    Location myLocation = locationManager.getLastKnownLocation(provider);
//                                    if(myLocation != null) {
//                                        LatLng currentLatLng = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
//                                        map.moveCamera(CameraUpdateFactory.newLatLng(currentLatLng));
//                                        map.moveCamera(CameraUpdateFactory.zoomTo(14));
//                                    } else {
//                                        Log.v(TAG, "Can't figure out current location :(");
//                                        map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(29.9648943,-90.1095941)));
//                                        map.moveCamera(CameraUpdateFactory.zoomTo(14));
//                                    }
//                                }
//
//                                map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
//                                    @Override
//                                    public boolean onMarkerClick(Marker marker) {
//                                        Intent intent = new Intent(getActivity(), KajianDescription.class);
//                                        Bundle bundle = new Bundle();
//                                        String url = "http://maps.google.com/maps?q=" + latitude + "," + longitude;
//                                        bundle.putString("namaUst",namaUst);
//                                        bundle.putString("tema",tema);
//                                        bundle.putString("tempat",tempat);
//                                        bundle.putString("deskripsi",deskripsi);
//                                        bundle.putString("url",url);
////                                        bundle.putString("pict",pict);
//                                        intent.putExtras(bundle);
//                                        startActivity(intent);
//                                        return false;
//                                    }
//                                });
//                            }
//                        });
//
//                    // R.id.map is a FrameLayout, not a Fragment
//                    getChildFragmentManager().beginTransaction().replace(R.id.map, mapFragment).commit();
//                }
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });


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
                                );
                                addKajian.child(mGroupId).setValue(newBooking);
                                addKajian.child(mGroupId).child("koordinatTempat").child("latitude").setValue(mainRef.plcLatitude);
                                addKajian.child(mGroupId).child("koordinatTempat").child("longtitude").setValue(mainRef.plcLongtitude);
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

        //Load Booking List
        recyclerBookingList = (RecyclerView) view.findViewById(R.id.recycler_finding);
        recyclerBookingList.hasFixedSize();
        recyclerBookingList.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerBookingList.setLayoutManager(layoutManager);
        loadKajianList();

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

    public void loadKajianList() {

        FirebaseRecyclerOptions<KajianList> options =
                new FirebaseRecyclerOptions.Builder<KajianList>()
                        .setQuery(kajianlist, KajianList.class)
                        .build();

        final FirebaseRecyclerAdapter adapter = new FirebaseRecyclerAdapter<KajianList, MenuViewHolder>(options) {
            @Override
            protected void onBindViewHolder(final MenuViewHolder viewHolder, final int position, final KajianList model) {

                viewHolder.txtNamaUstadz.setText(model.getNama());
                viewHolder.txtTema.setText(model.getDepartemen());
                viewHolder.txtTempat.setText(model.getKegiatan());
                viewHolder.txtJamMulai.setText(model.getJamMulai());
                viewHolder.txtJamAkhir.setText(model.getJamAkhir());
                DatabaseReference postRef= getRef(position);
                String postKey = postRef.getKey();
                postRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        latitude = dataSnapshot.child("koordinatTempat").child("latitude").getValue(Double.class);
                        longtitude = dataSnapshot.child("koordinatTempat").child("longtitude").getValue(Double.class);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                final KajianList clickItem = model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onCLick(View view, int position, boolean isLongClick) {
                        //String url = viewHolder.txtTempat.getText().toString();
//                        Intent intent = new Intent(Intent.ACTION_VIEW);
//                        intent.setData(Uri.parse(url));
                        Intent intent = new Intent(getActivity(), KajianDescription.class);
                        Bundle bundle = new Bundle();
                        String url = "http://maps.google.com/maps?q=" + latitude + "," + longtitude;
                        String namaUst = model.getNama();
                        String tema = model.getDepartemen();
                        String deskripsi = model.getDeskripsi();
                        String pict =  model.getPict();
                        String tempat = model.getKegiatan();
                        bundle.putString("namaUst",namaUst);
                        bundle.putString("tempat",tempat);
                        bundle.putString("tema",tema);
                        bundle.putString("deskripsi",deskripsi);
                        bundle.putString("url",url);
                        bundle.putString("pict",pict);
                        intent.putExtras(bundle);
                        startActivity(intent);

                    }
                });

            }


            @NonNull
            @Override
            public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.finding_item, parent, false);

                return new MenuViewHolder(view);
            }
        };

        adapter.startListening();
        adapter.notifyDataSetChanged();
        recyclerBookingList.setAdapter(adapter);

//        FirebaseRecyclerAdapter<KajianList, MenuViewHolder> adapter = new FirebaseRecyclerAdapter<KajianList, MenuViewHolder>(KajianList.class, R.layout.finding_item, MenuViewHolder.class, kajianlist) {
//            @Override
//            protected void populateViewHolder(final MenuViewHolder viewHolder, final KajianList model, int position) {
//                viewHolder.txtNamaUstadz.setText(model.getNama());
//                viewHolder.txtTema.setText(model.getDepartemen());
//                viewHolder.txtTempat.setText(model.getKegiatan());
//                viewHolder.txtJamMulai.setText(model.getJamMulai());
//                viewHolder.txtJamAkhir.setText(model.getJamAkhir());
//                DatabaseReference postRef= getRef(position);
//                String postKey = postRef.getKey();
//                postRef.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        latitude = dataSnapshot.child("koordinatTempat").child("latitude").getValue(Double.class);
//                        longtitude = dataSnapshot.child("koordinatTempat").child("longtitude").getValue(Double.class);
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//
//                    }
//                });
//
//                final KajianList clickItem = model;
//                viewHolder.setItemClickListener(new ItemClickListener() {
//                    @Override
//                    public void onCLick(View view, int position, boolean isLongClick) {
//                        //String url = viewHolder.txtTempat.getText().toString();
////                        Intent intent = new Intent(Intent.ACTION_VIEW);
////                        intent.setData(Uri.parse(url));
//                        Intent intent = new Intent(getActivity(), KajianDescription.class);
//                        Bundle bundle = new Bundle();
//                        String url = "http://maps.google.com/maps?q=" + latitude + "," + longtitude;
//                        String namaUst = model.getNama();
//                        String tema = model.getDepartemen();
//                        String deskripsi = model.getDeskripsi();
//                        String pict =  model.getPict();
//                        String tempat = model.getKegiatan();
//                        bundle.putString("namaUst",namaUst);
//                        bundle.putString("tempat",tempat);
//                        bundle.putString("tema",tema);
//                        bundle.putString("deskripsi",deskripsi);
//                        bundle.putString("url",url);
//                        bundle.putString("pict",pict);
//                        intent.putExtras(bundle);
//                        startActivity(intent);
//
//                    }
//                });
//            }
//        };

    }

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

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (savedInstanceState == null) {
            needsInit=true;
        }

        SupportMapFragment fragment = (SupportMapFragment)getChildFragmentManager().findFragmentById(R.id.map);
        fragment.getMapAsync(this);
    }

    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        kajianlist.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot s : dataSnapshot.getChildren()) {
                    KajianList kajianList = s.getValue(KajianList.class);
                    final String tema = s.child("departemen").getValue().toString();
                    final String tempat = s.child("kegiatan").getValue().toString();
                    final String deskripsi = s.child("deskripsi").getValue().toString();
//                    final String pict = child.child("pict").getValue().toString();
                    final String namaUst = s.child("nama").getValue().toString();
                    final double latitude = s.child("koordinatTempat").child("latitude").getValue(double.class);
                    final double longitude = s.child("koordinatTempat").child("longtitude").getValue(double.class);

                    kajian.add(kajianList);
                    for (int i = 0; i < kajian.size(); i++) {
                        LatLng latLng = new LatLng(latitude, longitude);
                        if (map != null) {
                            marker = map.addMarker(new MarkerOptions().position(latLng).title(kajianList.getDepartemen()));
                        }
                    }

                    map.getUiSettings().setMyLocationButtonEnabled(true);

                    // Zoom to device's current location
                    if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                            ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        map.setMyLocationEnabled(true);
                        LocationManager locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
                        String provider = locationManager.getBestProvider(new Criteria(), true);
                        Location myLocation = locationManager.getLastKnownLocation(provider);
                        if(myLocation != null) {
                            LatLng currentLatLng = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
                            map.moveCamera(CameraUpdateFactory.newLatLng(currentLatLng));
                            map.moveCamera(CameraUpdateFactory.zoomTo(14));
                        } else {
                            map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(29.9648943,-90.1095941)));
                            map.moveCamera(CameraUpdateFactory.zoomTo(14));
                        }
                    }

                    map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                        @Override
                        public boolean onMarkerClick(Marker marker) {
                            Intent intent = new Intent(getActivity(), KajianDescription.class);
                            Bundle bundle = new Bundle();
                            String url = "http://maps.google.com/maps?q=" + latitude + "," + longitude;
                            bundle.putString("namaUst",namaUst);
                            bundle.putString("tema",tema);
                            bundle.putString("tempat",tempat);
                            bundle.putString("deskripsi",deskripsi);
                            bundle.putString("url",url);
//                                        bundle.putString("pict",pict);
                            intent.putExtras(bundle);
                            startActivity(intent);
                            return false;
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}