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
    public MenuViewHolder(View itemView) {
        super(itemView);

        txtNama = (TextView)itemView.findViewById(R.id.txt_namalengkap);
        txtKegiatan = (TextView)itemView.findViewById(R.id.txt_kegiatan);
        txtOrganisasi = (TextView)itemView.findViewById(R.id.txt_organisasi);
        txtJamMulai = (TextView)itemView.findViewById(R.id.txt_jammulai);
        txtJamAkhir = (TextView)itemView.findViewById(R.id.txt_jamakhir);

        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onCLick(view, getAdapterPosition(), false);
    }
}
