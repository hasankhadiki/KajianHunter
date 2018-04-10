package tehhutan.app.kajianhunter.Login_Register;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import tehhutan.app.kajianhunter.R;

public class Register extends AppCompatActivity {
    private EditText nama, email, no_wa, password;
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
        register = (Button) findViewById(R.id.btn_register);
        register.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                registerVerify();
            }
        });

    }


    public void registerVerify(){
        final String Nama = nama.getText().toString().trim();
        String Email = email.getText().toString().trim();
        String Wa = no_wa.getText().toString().trim();
        String Pass = password.getText().toString().trim();
        char[] cekEmail = Email.toCharArray();

        if(Nama.matches("") || Email.matches("") || Wa.matches("") || Pass.matches("")){
            Toast.makeText(Register.this, "Lengkapi form diatas!", Toast.LENGTH_LONG).show();
        }else if(!cekEmail(cekEmail)){
            Toast.makeText(Register.this, "Format email salah!", Toast.LENGTH_LONG).show();
        }else{
            progress.setMessage("Sedang mendaftar...!");
            progress.show();
            otentikasi.createUserWithEmailAndPassword(Email, Pass). addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        String id_user = otentikasi.getCurrentUser().getUid();
                        DatabaseReference current_user =  database.child(id_user);
                        current_user.child("Nama").setValue(Nama);
                        current_user.child("Email").setValue(Nama);
                        current_user.child("Wa").setValue(Nama);
                        progress.dismiss();
                        AlertDialog.Builder builder_ = new AlertDialog.Builder(Register.this);
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
                        alert.show();
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