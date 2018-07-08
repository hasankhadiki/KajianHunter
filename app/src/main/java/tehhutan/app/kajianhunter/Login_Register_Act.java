package tehhutan.app.kajianhunter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import tehhutan.app.kajianhunter.Login_Register.Login;
import tehhutan.app.kajianhunter.Login_Register.Register;

public class Login_Register_Act extends AppCompatActivity {
    private Button signIn, signUp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register_);

        signIn = (Button)findViewById(R.id.btn_signin);
        signUp = (Button)findViewById(R.id.btn_signup);

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login_Register_Act.this, Login.class));
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login_Register_Act.this, Register.class));
            }
        });
    }
}
