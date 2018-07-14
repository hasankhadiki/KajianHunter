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

import com.squareup.picasso.Picasso;

import tehhutan.app.kajianhunter.R;

public class KajianDescription extends AppCompatActivity {


    TextView txtNama, txtTema, txtDeskripsi, txtTitle;
    ImageView imgPict;
    FloatingActionButton fabPlace;
    Bundle bundle;
    CollapsingToolbarLayout ctlKajianDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kajian_description);

        txtNama = (TextView)findViewById(R.id.txt_kajiandesc_pemateri);
        txtTema = (TextView)findViewById(R.id.txt_kajiandesc_tema);
        txtTitle = (TextView)findViewById(R.id.txt_kajiandesc_title);
        txtDeskripsi = (TextView)findViewById(R.id.txt_kajiandesc_deskripsi);
        fabPlace = (FloatingActionButton)findViewById(R.id.fab_place);
        imgPict  = (ImageView) findViewById(R.id.img_kajiandesc);
        ctlKajianDesc = (CollapsingToolbarLayout)findViewById(R.id.collapsingtoolbar);


        bundle = getIntent().getExtras();
        String namaUst = bundle.getString("namaUst");
        String tema = bundle.getString("tema");
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
