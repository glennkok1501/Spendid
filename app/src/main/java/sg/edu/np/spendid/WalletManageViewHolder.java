package sg.edu.np.spendid;


import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class WalletManageViewHolder extends RecyclerView.ViewHolder {
    TextView name;
    TextView currency;
    ImageView fav;

    public WalletManageViewHolder(View itemView){
        super(itemView);
        name = itemView.findViewById(R.id.manageWalletName_textView);
        currency = itemView.findViewById(R.id.manageWalletCur_textView);
        fav = itemView.findViewById(R.id.manageWalletFav_imageView);

    }
}
