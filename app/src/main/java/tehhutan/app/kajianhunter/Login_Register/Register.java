package tehhutan.app.kajianhunter.Login_Register;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import tehhutan.app.kajianhunter.MainActivity;
import tehhutan.app.kajianhunter.R;
import tehhutan.app.kajianhunter.model.User;

public class Register extends AppCompatActivity {
    private EditText nama, email, no_wa, password,retype_pass, nama_unik;
    private Button register;
    private FirebaseAuth otentikasi;
    private ProgressDialog progress;
    private DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        otentikasi = FirebaseAuth.getInstance();
        progress = new  ProgressDialog(this);
        database = FirebaseDatabase.getInstance().getReference().child("User");
        nama = (EditText) findViewById(R.id.nama_reg);
        email = (EditText) findViewById(R.id.email_reg);
        no_wa = (EditText) findViewById(R.id.wa_reg);
        password = (EditText) findViewById(R.id.pass_reg);
        nama_unik = (EditText) findViewById(R.id.namaunik_reg);
//        retype_pass = (EditText) findViewById(R.id.retypepass_reg);
        register = (Button) findViewById(R.id.btn_register);

       /* register.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                registerVerify();
            }
        }); */
        email.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if(validateEmail(email.getText().toString())){
                    email.setError("Email is valid");
                }else{
                    email.setError("Invalid email format");
                }
                return false;
            }
        });



        //Initialize Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("User/Regular");


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkFieldsForEmptyValues();

                final ProgressDialog mDialog = new ProgressDialog(Register.this);
                mDialog.setMessage("Tunggu sebentar..");
                mDialog.show();


                table_user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //Cek apakah NRP sudah ada didalam database
                        if(dataSnapshot.child(nama_unik.getText().toString()).exists())
                        {
                            mDialog.dismiss();
                            Toast.makeText(Register.this, "No. Telp tersebut sudah ada di dalam database", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            mDialog.dismiss();
                            User user = new User(email.getText().toString(), nama.getText().toString(), password.getText().toString(), null, no_wa.getText().toString(), nama_unik.getText().toString());
                            table_user.child(nama_unik.getText().toString()).setValue(user);
                            Toast.makeText(Register.this, "Pendaftaran akun baru berhasil", Toast.LENGTH_SHORT).show();
                            finish();
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

    }
    private boolean validateEmail(String string){
        if (TextUtils.isEmpty(string)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(string).matches();
        }
    }

    private  void checkFieldsForEmptyValues(){
        String email_ = email.getText().toString();
        String nama_ = nama.getText().toString();
        String no_ = no_wa.getText().toString();
        String password_ = password.getText().toString();

        if (email_.length() > 0 && nama_.length() > 0 && no_.length() > 0 && password_.length() > 0) {
            register.setEnabled(true);
        } else {
            register.setEnabled(false);
        }

    }

    public void registerVerify(){
        final String Nama = nama.getText().toString().trim();
        final String Email = email.getText().toString().trim();
        final String Wa = no_wa.getText().toString().trim();
        final String Pass = password.getText().toString().trim();
        final String Re_Pass = retype_pass.getText().toString().trim();
        char[] cekEmail = Email.toCharArray();

        if(Nama.matches("") || Email.matches("") || Wa.matches("") || Pass.matches("") || Re_Pass.matches("")){
            Toast.makeText(Register.this, "Lengkapi form diatas!", Toast.LENGTH_LONG).show();
        }else if(!cekEmail(cekEmail)){
            Toast.makeText(Register.this, "Format email salah!", Toast.LENGTH_LONG).show();
        }else if(Pass.length()<6){
            Toast.makeText(Register.this, "Maaf, Anda memasukkan password kurang dari 6 karakter.", Toast.LENGTH_LONG).show();
        }else if(!Pass.equals(Re_Pass)){
            Toast.makeText(Register.this, "Password tidak cocok!", Toast.LENGTH_LONG).show();
        } else{
            progress.setMessage("Sedang mendaftar...!");
            progress.show();
            otentikasi.createUserWithEmailAndPassword(Email, Pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        AlertDialog.Builder builder_ = new AlertDialog.Builder(Register.this);
                        String id_user = otentikasi.getCurrentUser().getUid();
                        DatabaseReference idRef =  database.child(id_user);
                        idRef.child("Nama").setValue(Nama);
                        idRef.child("Email").setValue(Email);
                        idRef.child("Wa").setValue(Wa);
                        progress.dismiss();
                        /*
                        builder_.setMessage("Anda telah berhasil login.\nLogin sekarang ?")
                                .setCancelable(false)
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Intent intent = new Intent(Register.this, Login.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                    }
                                });
                        AlertDialog alert = builder_.create();
                        alert.setTitle("Selamat !");
                        alert.show();*/
                    }else {
                        progress.dismiss();
                        Toast.makeText(Register.this, "Register gagal!", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }

    }

    public Boolean cekEmail(char[] mail_char){
        int isEmail=0;
        Boolean isEmailBisa=false;
        int position=0;
        for(int i=0;i<mail_char.length;i++){
            if(mail_char[i]=='@'){
                isEmail++;
                position=i;
            }
            if(mail_char[i]=='!' || mail_char[i]=='#' || mail_char[i]=='$' || mail_char[i]=='%' || mail_char[i]=='^' ||
                    mail_char[i]=='&' || mail_char[i]=='*' || mail_char[i]=='(' || mail_char[i]==')' || mail_char[i]=='-' || mail_char[i]=='=' ||
                    mail_char[i]=='+' || mail_char[i]=='[' || mail_char[i]==']' || mail_char[i]=='{' || mail_char[i]=='}' || mail_char[i]==':' || mail_char[i]==';' || mail_char[i]=='/' ||
                    mail_char[i]=='?' || mail_char[i]=='>' || mail_char[i]=='<' || mail_char[i]==',' || mail_char[i]=='`' || mail_char[i]=='~'){
                isEmail=0;
                break;
            }
        }

        if(isEmail==1 && (mail_char.length-position-1)>4 && mail_char[position+1]!='.'){
            int pointChar=0;
            int[] titikPosisi = new int[mail_char.length-position-1];
            for(int i=position+1; i<mail_char.length;i++){
                if(mail_char[i]=='.'){
                    titikPosisi[pointChar]=i;
                    pointChar++;
                }
            }
            if(pointChar>0 && pointChar<3 && (titikPosisi[1]-titikPosisi[0]!=1) && titikPosisi[1]!=(mail_char.length-1)){
                isEmailBisa=true;
            }
        }
        return isEmailBisa;
    }
}