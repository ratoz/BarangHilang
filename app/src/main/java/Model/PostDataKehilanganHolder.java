package Model;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pamair.baranghilang.R;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class PostDataKehilanganHolder extends RecyclerView.ViewHolder {

    public CardView cardView;
    public LinearLayout backgroundimg;
    public TextView txtUser;
    public TextView txtNama;
    public TextView txtDesc;
    public TextView txtDate;
    public TextView txtClock;
    public TextView txtTypePost;
    public TextView txtIdPost;
    public ImageView imgBarang;

    public PostDataKehilanganHolder(@NonNull View itemView) {
        super(itemView);

            imgBarang = itemView.findViewById(R.id.pk_img_post);
            txtNama = itemView.findViewById(R.id.pk_tvTitle);
            txtUser = itemView.findViewById(R.id.pk_tvUser);
            txtDate = itemView.findViewById(R.id.pk_tvDate);
            txtClock = itemView.findViewById(R.id.pk_tvTime);
            txtDesc = itemView.findViewById(R.id.pk_tvDesc);
            txtIdPost= itemView.findViewById(R.id.pk_idpost);



    }


}

