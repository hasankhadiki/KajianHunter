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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import tehhutan.app.kajianhunter.MainActivity;
import tehhutan.app.kajianhunter.R;

public class Login extends AppCompatActivity {

    private EditText email, password;
    private Button loginBtn;
    private TextView register;
    private FirebaseAuth otentikasi;
    private ProgressDialog progress;
    private FirebaseAuth.AuthStateListener otentikasiListener;
    String saveIDuser;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        progress = new ProgressDialog(this);
        otentikasi = FirebaseAuth.getInstance();
        email=(EditText)findViewById(R.id.email_login);
        password=(EditText)findViewById(R.id.pass_login);
        loginBtn=(Button)findViewById(R.id.btn_login);
        register=(TextView) findViewById(R.id.register_login);
        otentikasiListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() != null){
                    progress.dismiss();
                    saveIDuser = FirebaseDatabase.getInstance().getReference().toString();
                    startActivity(new Intent(Login.this, MainActivity.class));
                }
            }

        };

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);
            }

        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(email.getText().toString().matches("") || password.getText().toString().matches("")){
                    Toast.makeText(Login.this, "Mohon isi dengan benar!", Toast.LENGTH_LONG).show();
                }else {
                    loginVerify();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        otentikasi.addAuthStateListener(otentikasiListener);
    }

    private void loginVerify(){
        String Email=email.getText().toString();
        String Pass=password.getText().toString();
        progress.setMessage("Sedang masuk...!");
        progress.show();
        otentikasi.signInWithEmailAndPassword(Email,Pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(!task.isSuccessful()){
                    AlertDialog.Builder builder_ = new AlertDialog.Builder(Login.this);
                    builder_.setMessage("Anda belum berhasil login.")
                            .setCancelable(false)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            });
                    AlertDialog alert = builder_.create();
                    alert.setTitle("Mohon maaf !");
                    alert.show();
                }
            }
        });
    }
}
