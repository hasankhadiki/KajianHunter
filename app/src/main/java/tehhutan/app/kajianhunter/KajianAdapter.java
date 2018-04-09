package tehhutan.app.kajianhunter;

import android.content.Context;
import android.view.View;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;

/**
 * Created by 5215100071 on 4/10/2018.
 */
public class KajianAdapter extends FirebaseRecyclerAdapter<KajianList, MenuViewHolder> {
    private static final String TAG = KajianAdapter.class.getSimpleName();
    private Context context;
    public KajianAdapter(Class<KajianList> modelClass, int modelLayout, Class<MenuViewHolder> viewHolderClass, DatabaseReference ref, Context context) {
        super(modelClass, modelLayout, viewHolderClass, ref);
        this.context = context;
    }
    @Override
    protected void populateViewHolder(MenuViewHolder viewHolder, KajianList model, int position) {

        viewHolder.txtNama.setText(model.getNama());
        viewHolder.txtOrganisasi.setText(model.getDepartemen());
        viewHolder.txtKegiatan.setText(model.getKegiatan());
        viewHolder.txtJamMulai.setText(model.getJamMulai());
        viewHolder.txtJamAkhir.setText(model.getJamAkhir());

        final KajianList clickItem = model;
        viewHolder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onCLick(View view, int position, boolean isLongClick) {

            }
        });
    }
}