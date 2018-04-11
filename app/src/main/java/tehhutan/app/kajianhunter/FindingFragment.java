package tehhutan.app.kajianhunter;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class FindingFragment extends Fragment {

    FirebaseDatabase database;
    DatabaseReference kajianlist;
    FloatingActionButton fab;

    RecyclerView recyclerBookingList;
    RecyclerView.LayoutManager layoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setTitle("Finding");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        setHasOptionsMenu(true);

        database = FirebaseDatabase.getInstance();
        kajianlist = database.getReference("KajianList");
        View view = inflater.inflate(R.layout.fragment_finding, container, false);

        //Load Booking List
        recyclerBookingList = (RecyclerView) view.findViewById(R.id.recycler_finding);
        recyclerBookingList.hasFixedSize();
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerBookingList.setLayoutManager(layoutManager);
        loadKajianList();

        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadKajianList();
            }
        });

        return view;
    }



    public void loadKajianList() {
        FirebaseRecyclerAdapter<KajianList, MenuViewHolder> adapter = new FirebaseRecyclerAdapter<KajianList, MenuViewHolder>(KajianList.class, R.layout.finding_item, MenuViewHolder.class, kajianlist) {
            @Override
            protected void populateViewHolder(MenuViewHolder viewHolder, KajianList model, int position) {
                viewHolder.txtNamaUstadz.setText(model.getNama());
                viewHolder.txtTema.setText(model.getDepartemen());
                viewHolder.txtTempat.setText(model.getKegiatan());
                viewHolder.txtJamMulai.setText(model.getJamMulai());
                viewHolder.txtJamAkhir.setText(model.getJamAkhir());

                final KajianList clickItem = model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onCLick(View view, int position, boolean isLongClick) {

                    }
                });
            }
        };
        recyclerBookingList.setAdapter(adapter);
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
