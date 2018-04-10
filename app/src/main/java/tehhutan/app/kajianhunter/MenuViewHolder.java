package tehhutan.app.kajianhunter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by 5215100071 on 4/9/2018.
 */

public class MenuViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


    public TextView txtNama, txtOrganisasi, txtKegiatan, txtJamMulai, txtJamAkhir;


    private ItemClickListener itemClickListener;
    public MenuViewHolder(View view) {
        super(view);

        txtNama = (TextView)view.findViewById(R.id.txt_namalengkap);
        txtKegiatan = (TextView)view.findViewById(R.id.txt_kegiatan);
        txtOrganisasi = (TextView)view.findViewById(R.id.txt_organisasi);
        txtJamMulai = (TextView)view.findViewById(R.id.txt_jammulai);
        txtJamAkhir = (TextView)view.findViewById(R.id.txt_jamakhir);

        view.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onCLick(view, getAdapterPosition(), false);
    }
}
