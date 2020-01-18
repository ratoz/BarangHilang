package Model;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pamair.baranghilang.R;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

    public class PostJejakHolder extends RecyclerView.ViewHolder {

        public CardView cardView;
        public TextView txtSelesaipada;
        public TextView txtNama;
        public TextView txtDesc;
        public TextView txtKronologi;
        public TextView txtTypePost;
        public TextView txtIdPost;

        public PostJejakHolder(@NonNull View itemView) {
            super(itemView);
            txtNama = itemView.findViewById(R.id.j_namapost);
            txtTypePost = itemView.findViewById(R.id.j_typepost);
            txtIdPost = itemView.findViewById(R.id.idpost);
            cardView = itemView.findViewById(R.id.cv_postjejak);
            txtDesc = itemView.findViewById(R.id.j_desc);
            txtKronologi = itemView.findViewById(R.id.j_kronologi);
            txtSelesaipada = itemView.findViewById(R.id.j_doneat);

        }

    }



