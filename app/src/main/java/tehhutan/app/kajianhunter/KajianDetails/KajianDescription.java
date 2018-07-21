package tehhutan.app.kajianhunter.KajianDetails;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import tehhutan.app.kajianhunter.R;
import tehhutan.app.kajianhunter.model.SavedList;

public class KajianDescription extends AppCompatActivity {


    TextView txtNama, txtTema, txtDeskripsi, txtTitle;
    FloatingActionButton fabPlace, fabSave;
    CollapsingToolbarLayout ctlKajianDesc;
    ImageView imgPict;

    Bundle bundle;

    private FirebaseDatabase database;
    private DatabaseReference addsaved;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kajian_description);

        txtNama = (TextView)findViewById(R.id.txt_kajiandesc_pemateri);
        txtTema = (TextView)findViewById(R.id.txt_kajiandesc_tema);
        txtTitle = (TextView)findViewById(R.id.txt_kajiandesc_title);
        txtDeskripsi = (TextView)findViewById(R.id.txt_kajiandesc_deskripsi);
        fabPlace = (FloatingActionButton)findViewById(R.id.fab_place);
        fabSave = (FloatingActionButton)findViewById(R.id.fab_save);
        imgPict  = (ImageView) findViewById(R.id.img_kajiandesc);
        ctlKajianDesc = (CollapsingToolbarLayout)findViewById(R.id.collapsingtoolbar);


        bundle = getIntent().getExtras();
        final String namaUst = bundle.getString("namaUst");
        final String tema = bundle.getString("tema");
        final String tempat = bundle.getString("tempat");
        String deskripsi = bundle.getString("deskripsi");


        String pict = bundle.getString("pict");

        txtNama.setText(namaUst);
        txtTema.setText(tema);
        txtTitle.setText(tema);
        txtDeskripsi.setText(deskripsi);
        ctlKajianDesc.setTitle(tema);
//        Picasso.get().load(pict).into(imgPict);


        fabPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = bundle.getString("url");
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        });

        database = FirebaseDatabase.getInstance();
        addsaved = database.getReference("SavedKajian");
        fabSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SavedList savedList = new SavedList(namaUst, tempat, tema);
                addsaved.push().setValue(savedList);
                Toast.makeText(KajianDescription.this, "Kajian Tersimpan", Toast.LENGTH_SHORT).show();
            }
        });

        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbarlayout);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

                TextView textKajianTitle = (TextView) findViewById(R.id.txt_kajiandesc_title);

                if (Math.abs(verticalOffset)-appBarLayout.getTotalScrollRange() == 0)
                {
                    //  Collapsed
                    textKajianTitle.setVisibility(View.GONE);


                }
                else
                {
                    //Expanded
                    textKajianTitle.setVisibility(View.VISIBLE);

                }
            }
        });
    }
}
