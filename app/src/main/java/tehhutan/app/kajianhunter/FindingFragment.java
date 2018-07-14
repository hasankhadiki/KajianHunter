package tehhutan.app.kajianhunter;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
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
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

import tehhutan.app.kajianhunter.Interface.ItemClickListener;
import tehhutan.app.kajianhunter.KajianDetails.KajianDescription;
import tehhutan.app.kajianhunter.model.BookingList;


public class FindingFragment extends Fragment {

    private FirebaseDatabase database;
    private DatabaseReference kajianlist, addKajian;
    private FloatingActionButton fab;

    private EditText editNamaPeminjam, editOrganisasi, editKegiatan, editJamMulai, editJamAkhir;
    private Button btnSubmit;
    private ImageView pickPlace;
    private RecyclerView recyclerBookingList;
    private RecyclerView.LayoutManager layoutManager;

    private MainActivity mainRef;
    private double latitude = 0.0;
    private double longtitude = 0.0;
    private final int PLACE_PICKER_REQUEST = 442;
    private boolean isPlaceButtonClicked = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setTitle("Finding");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        setHasOptionsMenu(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setElevation(0);


        mainRef = (MainActivity)getActivity();
        database = FirebaseDatabase.getInstance();
        kajianlist = database.getReference("KajianList/Verified");
        addKajian = database.getReference("KajianList/Unverified");
        View view = inflater.inflate(R.layout.fragment_finding, container, false);

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
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerBookingList.setLayoutManager(layoutManager);
        loadKajianList();
/*
        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadKajianList();
            }
        });
*/
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
        FirebaseRecyclerAdapter<KajianList, MenuViewHolder> adapter = new FirebaseRecyclerAdapter<KajianList, MenuViewHolder>(KajianList.class, R.layout.finding_item, MenuViewHolder.class, kajianlist) {
            @Override
            protected void populateViewHolder(final MenuViewHolder viewHolder, final KajianList model, int position) {
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
                        bundle.putString("namaUst",namaUst);
                        bundle.putString("tema",tema);
                        bundle.putString("deskripsi",deskripsi);
                        bundle.putString("url",url);
                        bundle.putString("pict",pict);
                        intent.putExtras(bundle);
                        startActivity(intent);

                    }
                });
            }
        };
        recyclerBookingList.setAdapter(adapter);
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

}
