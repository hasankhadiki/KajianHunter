package tehhutan.app.kajianhunter;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class Edit_Profile extends AppCompatActivity {
    private EditText nama, email, wa, biodata;
    private Button simpan_profile;
    private ProgressDialog progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit__profile);
       // getSupportActionBar().setTitle("Ubah Profile");
        progress = new ProgressDialog(this);
        nama = (EditText) findViewById(R.id.nama_edit_profile);
        email = (EditText)findViewById(R.id.email_edit_profile);
        wa = (EditText) findViewById(R.id.wa_edit_profile);
        biodata = (EditText) findViewById(R.id.biodata_edit_profile);
        simpan_profile = (Button) findViewById(R.id.simpan_perubahan);
        final String idUser = FirebaseAuth.getInstance().getUid();
        final DatabaseReference idRef = FirebaseDatabase.getInstance().getReference().child("User").child(idUser);

        simpan_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress.setMessage("Menyimpan...");
                progress.show();
                String Nama = nama.getText().toString();
                String Email = email.getText().toString();
                String Wa = wa.getText().toString();
                String Biodata = biodata.getText().toString();

                idRef.child("Nama").setValue(Nama);
                idRef.child("Email").setValue(Email);
                idRef.child("Wa").setValue(Wa);

                if (!(idRef.child(idUser).child("Biodata").getDatabase()==null)) {
                    idRef.child("Biodata").setValue(Biodata);
                }
                progress.dismiss();
                Toast.makeText(Edit_Profile.this, "Profile tersimpan!", Toast.LENGTH_LONG).show();
                Intent i = new Intent(Edit_Profile.this, MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);

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
                wa.setText(wa_);
                if (!(myRef.child(idUser).child("Biodata").getDatabase()==null)) {
                    String bio_ = (String) map.get("Biodata");
                    biodata.setText(bio_);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}

        });


    }
}
