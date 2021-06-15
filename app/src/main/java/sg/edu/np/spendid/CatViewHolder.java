package sg.edu.np.spendid;

import android.view.View;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

public class CatViewHolder extends RecyclerView.ViewHolder {
    ImageView image;
    public CatViewHolder(View itemView){
        super(itemView);
        image = itemView.findViewById(R.id.newRecordCat_imageView);
    }
}
