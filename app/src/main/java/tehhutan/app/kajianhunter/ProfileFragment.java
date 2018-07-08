package tehhutan.app.kajianhunter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

import tehhutan.app.kajianhunter.Login_Register.Login;


public class ProfileFragment extends Fragment {
    private TextView nama, email, no_wa, biodata;
    private Button edit_profile;
    private LinearLayout logout;
    private String idUser;
    private ProgressDialog progress, progress1;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setTitle("Profile");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        setHasOptionsMenu(true);
        View v = inflater.inflate(R.layout.fragment_profile, null);
        /*
        progress = new ProgressDialog(getActivity());
        progress1 = new ProgressDialog(getActivity());
        idUser = FirebaseAuth.getInstance().getUid();
        nama = (TextView)v.findViewById(R.id.nama_profile);
        email = (TextView)v.findViewById(R.id.email_profile);
        no_wa = (TextView)v.findViewById(R.id.wa_profile);
        biodata = (TextView)v.findViewById(R.id.biodata_profile);
        edit_profile = (Button)v.findViewById(R.id.edit_profile);
        logout = (LinearLayout)v.findViewById(R.id.log_out);
        progress.setMessage("Memuat...");
        progress.show();

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress1.setMessage("Sedang keluar...");
                progress1.show();
                FirebaseAuth.getInstance().signOut();
                progress1.dismiss();
                Intent intent = new Intent(getActivity(), Login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), Edit_Profile.class));
            }
        });
        final DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference myRef = database.child("User");

        myRef.child(idUser).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //user = dataSnapshot.getValue(User.class);
                //loadFire.setText(dataSnapshot.getValue(String.class));
                Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                String nama_ = (String) map.get("Nama");
                String email_ = (String) map.get("Email");
                String wa_ = (String) map.get("Wa");
//                String bio_ = map.get("Biodata");
                nama.setText(nama_);
                email.setText(email_);
                no_wa.setText(wa_);
               if (!(myRef.child(idUser).child("Biodata").getDatabase()==null)) {
                 String bio_ = (String) map.get("Biodata");
                 biodata.setText(bio_);
                }
                progress.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}

        });
        */
        return v;
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
