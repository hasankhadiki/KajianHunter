package tehhutan.app.kajianhunter;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);

        FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
        tx.replace(R.id.fragmentContainer, new FindingFragment());
        tx.commit();
    }

    private boolean loadFragment(Fragment fragment){
        if(fragment != null){

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;

        switch (item.getItemId()) {
            case R.id.navigation_finding:
                fragment = new FindingFragment();
                break;
            case R.id.navigation_timeline:
                fragment = new TimelineFragment();
                break;
            case R.id.navigation_profile:
                fragment = new ProfileFragment();
                break;
        }
        return loadFragment(fragment);
    }

    boolean click_duaKali=false;
    public void onBackPressed() {
        //  super.onBackPressed();
        if(click_duaKali){
            Intent i = new Intent(Intent.ACTION_MAIN);
            i.addCategory(Intent.CATEGORY_HOME);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            finish();
            System.exit(0);
        }
        Toast.makeText(MainActivity.this, "Silahkan tekan kembali untuk keluar!", Toast.LENGTH_LONG).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                click_duaKali=false;
            }
        },3000);
        click_duaKali=true;
    }
}
