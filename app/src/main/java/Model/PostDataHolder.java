package Model;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pamair.baranghilang.R;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class PostDataHolder extends RecyclerView.ViewHolder {

    public CardView cardView;
    public LinearLayout backgroundimg;
    public TextView txtNama;
    public TextView txtTypePost;
    public TextView txtIdPost;
    public ImageView imgBarang;

    public PostDataHolder(@NonNull View itemView) {
        super(itemView);
        backgroundimg = itemView.findViewById(R.id.backgroundimg);
        txtNama = itemView.findViewById(R.id.namapost);
        txtTypePost = itemView.findViewById(R.id.typepost);
        txtIdPost = itemView.findViewById(R.id.idpost);
        imgBarang = itemView.findViewById(R.id.img_barang);
        cardView = itemView.findViewById(R.id.cv_postanda);

    }


}

