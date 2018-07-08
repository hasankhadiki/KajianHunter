package tehhutan.app.kajianhunter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import tehhutan.app.kajianhunter.Interface.ItemClickListener;

/**
 * Created by 5215100071 on 4/9/2018.
 */

public class MenuViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


    public TextView txtNamaUstadz, txtTema, txtTempat, txtJamMulai, txtJamAkhir;


    private ItemClickListener itemClickListener;

    public MenuViewHolder(View itemView) {
        super(itemView);

        txtNamaUstadz = (TextView)itemView.findViewById(R.id.txt_namaustadz);
        txtTempat = (TextView)itemView.findViewById(R.id.txt_tempat);
        txtTema = (TextView)itemView.findViewById(R.id.txt_tema);
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
