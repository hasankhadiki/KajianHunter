package tehhutan.app.kajianhunter;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

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
import java.util.List;

import static android.content.Context.LOCATION_SERVICE;

public class MapsFragment extends Fragment implements OnMapReadyCallback {

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

    public MapsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mainRef = (MainActivity) getActivity();
        database = FirebaseDatabase.getInstance();
        kajianlist = database.getReference("KajianList/Verified");
        addKajian = database.getReference("KajianList/Unverified");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_maps, container, false);

        return view;
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
                    final KajianList kajianList = s.getValue(KajianList.class);


                    kajian.add(kajianList);
                    for (int i = 0; i < kajian.size(); i++) {
                        final String tema = kajianList.getDepartemen();
                        final String tempat = kajianList.getNama();
                        final String deskripsi = kajianList.getDeskripsi();
                        final String namaUst = kajianList.getKegiatan();

                        LatLng latLng = new LatLng(kajianList.getLatitude(), kajianList.getLongitude());
                        if (map != null) {

                            marker = map.addMarker(new MarkerOptions().position(latLng)
                                    .title(kajianList.getDepartemen())
                                    .snippet(kajianList.getKegiatan()));

                        }

//                        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
//                            @Override
//                            public boolean onMarkerClick(Marker marker) {
//
//                                Intent intent = new Intent(getActivity(), KajianDescription.class);
//                                Bundle bundle = new Bundle();
//                                String url = "http://maps.google.com/maps?q=" + kajianList.getLatitude() + "," + kajianList.getLongitude();
//                                bundle.putString("tema",tema);
//                                bundle.putString("url",url);
//                                bundle.putString("namaUst",namaUst);
//                                bundle.putString("tempat",tempat);
//                                bundle.putString("deskripsi",deskripsi);
////                                bundle.putString("pict",pict);
//                                intent.putExtras(bundle);
//                                startActivity(intent);
//                                return false;
//                            }
//                        });

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
                            map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 14));
                            map.animateCamera(CameraUpdateFactory.zoomTo(13), 2000, null);

                            Circle circle = map.addCircle(new CircleOptions()
                                    .center(new LatLng(myLocation.getLatitude(), myLocation.getLongitude()))
                                    .radius(3000)
                                    .strokeColor(Color.parseColor("#009382"))
                                    .fillColor(Color.TRANSPARENT));
                        } else {
                            map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(29.9648943,-90.1095941)));
                            map.moveCamera(CameraUpdateFactory.zoomTo(14));
                        }
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
