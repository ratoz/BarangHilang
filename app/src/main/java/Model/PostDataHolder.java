package Model;

import android.view.View;
import android.widget.TextView;

import com.pamair.baranghilang.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PostDataHolder extends RecyclerView.ViewHolder {

    public TextView txtNama;
    public TextView txtKrono;

    public PostDataHolder(@NonNull View itemView) {
        super(itemView);
        txtNama = itemView.findViewById(R.id.namapost);
        txtKrono = itemView.findViewById(R.id.kronologipost);
    }


}

