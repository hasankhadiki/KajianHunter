package tehhutan.app.kajianhunter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;

import android.support.annotation.NonNull;

import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import com.firebase.ui.database.FirebaseRecyclerOptions;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import tehhutan.app.kajianhunter.Interface.ItemClickListener;
import tehhutan.app.kajianhunter.KajianDetails.KajianDescription;
import tehhutan.app.kajianhunter.Login_Register.Login;
import tehhutan.app.kajianhunter.R;
import tehhutan.app.kajianhunter.model.SavedList;
import tehhutan.app.kajianhunter.model.User;
import tehhutan.app.kajianhunter.utils.FirebaseUtils;
import tehhutan.app.kajianhunter.utils.GalleryUtil;


public class ProfileFragment extends Fragment {
    private TextView nama, email, namaunik, no_tlp;
    private Button edit_profile;
    private LinearLayout logout;
    private User pengguna;
    private MainActivity mainRef;
    private ProgressDialog progress, progress1;

    private FirebaseDatabase database;
    private DatabaseReference savedlist;

    private RecyclerView recyclerSavedList;
    private LinearLayoutManager layoutManager;
    private double latitude = 0.0;
    private double longtitude = 0.0;
    private static final int PICK_FROM_GALLERY = 532;
    private String URL_IMAGE = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setTitle("Profile");
//        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
//        setHasOptionsMenu(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        mainRef = (MainActivity)getActivity();
        View v = inflater.inflate(R.layout.fragment_profile, null);
        nama = (TextView)v.findViewById(R.id.profile_nama);
        namaunik = (TextView)v.findViewById(R.id.profile_namaunik);
        email = (TextView)v.findViewById(R.id.profile_email);
        no_tlp = (TextView)v.findViewById(R.id.profile_no_telp);
        mainRef.profilePhoto = (CircleImageView)v.findViewById(R.id.profile_photo);

        loadProfileInfo();
        database = FirebaseDatabase.getInstance();
        savedlist = database.getReference("SavedKajian");
        //Load Booking List
        recyclerSavedList = (RecyclerView) v.findViewById(R.id.recycler_saved);
        recyclerSavedList.computeVerticalScrollRange();
        recyclerSavedList.hasFixedSize();
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerSavedList.setLayoutManager(layoutManager);
        loadSavedList();
        mainRef.profilePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                //******call android default gallery
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                //******code for crop image
                intent.putExtra("crop", "true");
                intent.putExtra("aspectX", 0);
                intent.putExtra("aspectY", 0);
                try {
                    intent.putExtra("return-data", true);
                    getActivity().startActivityForResult(
                            Intent.createChooser(intent,"Complete action using"),
                            PICK_FROM_GALLERY);
                } catch (ActivityNotFoundException e) {}
            }
        });
        return v;
    }
    void loadProfileInfo(){
        FirebaseUtils.getUserRef(FirebaseUtils.getUserID(getActivity())).addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                pengguna = dataSnapshot.getValue(User.class);
                nama.setText(pengguna.getNama());
                email.setText(pengguna.getEmail());
                no_tlp.setText(pengguna.getWa());
                if(pengguna.getNamaUnik()!=null){
                    namaunik.setText(pengguna.getNamaUnik());
                }
               if(pengguna.getPhoto()!=null){
                    Glide.with(getActivity()).load(pengguna.getPhoto()).into(mainRef.profilePhoto);
                   mainRef.profilePhoto.setForeground(new ColorDrawable(ContextCompat.getColor(getActivity(), android.R.color.transparent)));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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

    public void loadSavedList() {

        FirebaseRecyclerOptions<SavedList> options =
                new FirebaseRecyclerOptions.Builder<SavedList>()
                        .setQuery(savedlist, SavedList.class)
                        .build();

        final FirebaseRecyclerAdapter adapter = new FirebaseRecyclerAdapter<SavedList, SavedViewHolder>(options) {
            @Override
            protected void onBindViewHolder(final SavedViewHolder viewHolder, final int position, final SavedList model) {

                viewHolder.txtNamaUstadz.setText(model.getNama());
                viewHolder.txtTema.setText(model.getTema());
                viewHolder.txtTempat.setText(model.getTempat());

//                DatabaseReference postRef= getRef(position);
//
//                String postKey = postRef.getKey();
//                postRef.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        latitude = dataSnapshot.child("koordinatTempat").child("latitude").getValue(Double.class);
//                        longtitude = dataSnapshot.child("koordinatTempat").child("longtitude").getValue(Double.class);
//                    }
//`
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//
//                    }
//                });


                final SavedList clickItem = model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onCLick(View view, int position, boolean isLongClick) {

                    }
                });

            }


            @NonNull
            @Override
            public SavedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.saved_kajian_item, parent, false);

                return new SavedViewHolder(view);
            }
        };

        adapter.startListening();
        adapter.notifyDataSetChanged();
        recyclerSavedList.setAdapter(adapter);

//        FirebaseRecyclerAdapter<SavedList, SavedViewHolder> adapter = new FirebaseRecyclerAdapter<SavedList, SavedViewHolder>(SavedList.class, R.layout.saved_kajian_item, SavedViewHolder.class, savedlist) {
//            @Override
//            protected void populateViewHolder(final SavedViewHolder viewHolder, final SavedList model, int position) {
//                viewHolder.txtNamaUstadz.setText(model.getNama());
//                viewHolder.txtTema.setText(model.getTema());
//                viewHolder.txtTempat.setText(model.getTempat());
//
//                final SavedList clickItem = model;
//                viewHolder.setItemClickListener(new ItemClickListener() {
//                    @Override
//                    public void onCLick(View view, int position, boolean isLongClick) {
//
//                    }
//                });
//            }
//        };

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
