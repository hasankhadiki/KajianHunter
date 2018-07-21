package tehhutan.app.kajianhunter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import tehhutan.app.kajianhunter.Interface.ItemClickListener;

/**
 * Created by 5215100071 on 7/21/2018.
 */

public class SavedViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


    public TextView txtNamaUstadz, txtTema, txtTempat, txtJamMulai, txtJamAkhir;


    private ItemClickListener itemClickListener;

    public SavedViewHolder(View itemView) {
        super(itemView);

        txtNamaUstadz = (TextView)itemView.findViewById(R.id.saved_nama);
        txtTempat = (TextView)itemView.findViewById(R.id.saved_tempat);
        txtTema = (TextView)itemView.findViewById(R.id.saved_tema);

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
