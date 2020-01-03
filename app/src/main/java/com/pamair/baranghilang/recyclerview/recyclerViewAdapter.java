package com.pamair.baranghilang.recyclerview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

//import com.google.firebase.firestore.core.View;


import com.pamair.baranghilang.R;

import java.util.ArrayList;

import Model.PostData;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class recyclerViewAdapter extends RecyclerView.Adapter<recyclerViewAdapter.PostViewHolder> {

    private ArrayList<PostData> dataList;

    public recyclerViewAdapter(ArrayList<PostData> dataList){
        this.dataList = dataList;
    }

    public class PostViewHolder extends RecyclerView.ViewHolder{
        private TextView txtNama;

        public PostViewHolder(View itemView) {
            super(itemView);
            txtNama = itemView.findViewById(R.id.namapost);

        }
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Membuat View untuk Menyiapkan dan Memasang Layout yang Akan digunakan pada RecyclerView
        View V = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerviewpostanda, parent, false);
        return new PostViewHolder(V);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        holder.txtNama.setText(dataList.get(position).getTitle());

    }

    @Override
    public int getItemCount() {
        return (dataList != null) ? dataList.size() : 0;
    }

    void setFilter(ArrayList<PostData> filterlist){
        dataList = new ArrayList<>();
        dataList.addAll(filterlist);
        notifyDataSetChanged();
    }


}

