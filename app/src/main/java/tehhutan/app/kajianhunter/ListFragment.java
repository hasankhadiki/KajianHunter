package tehhutan.app.kajianhunter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Marker;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import tehhutan.app.kajianhunter.Interface.ItemClickListener;
import tehhutan.app.kajianhunter.KajianDetails.KajianDescription;

public class ListFragment extends Fragment {

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

    public ListFragment() {
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
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        //Load Booking List
        recyclerBookingList = (RecyclerView) view.findViewById(R.id.recycler_finding);
        recyclerBookingList.hasFixedSize();
        recyclerBookingList.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerBookingList.setLayoutManager(layoutManager);
        loadKajianList();

        return view;
    }

    public void loadKajianList() {

        FirebaseRecyclerOptions<KajianList> options =
                new FirebaseRecyclerOptions.Builder<KajianList>()
                        .setQuery(kajianlist, KajianList.class)
                        .build();

        final FirebaseRecyclerAdapter adapter = new FirebaseRecyclerAdapter<KajianList, MenuViewHolder>(options) {
            @Override
            protected void onBindViewHolder(final MenuViewHolder viewHolder, final int position, final KajianList model) {

                viewHolder.txtNamaUstadz.setText(model.getKegiatan());
                viewHolder.txtTema.setText(model.getDepartemen());
                viewHolder.txtTempat.setText(model.getNama());
                viewHolder.txtJamMulai.setText(model.getJamMulai());
                viewHolder.txtJamAkhir.setText(model.getJamAkhir());
                DatabaseReference postRef= getRef(position);
                String postKey = postRef.getKey();
                postRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        latitude = dataSnapshot.child("latitude").getValue(Double.class);
                        longtitude = dataSnapshot.child("longitude").getValue(Double.class);
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
                        String namaUst = model.getKegiatan();
                        String tema = model.getDepartemen();
                        String deskripsi = model.getDeskripsi();
                        String pict =  model.getPict();
                        String tempat = model.getNama();
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

}
