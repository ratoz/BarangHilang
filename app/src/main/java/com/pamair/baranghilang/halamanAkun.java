package com.pamair.baranghilang;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;


import Model.PostData;
import Model.PostDataHolder;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class halamanAkun extends Fragment {

    
    RecyclerView recyclerView;
    FirestoreRecyclerAdapter adapter;

    public halamanAkun(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view_fragment = inflater.inflate(R.layout.halaman_akun, container, false);

        final ImageButton button = view_fragment.findViewById(R.id.menupopup);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Creating the instance of PopupMenu
                PopupMenu popup = new PopupMenu(getContext(), button);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.menuakun, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        Toast.makeText(getContext(),"You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                        return true;
                    }
                });
                popup.show();//showing popup menu
            }
        });


        recyclerView = view_fragment.findViewById(R.id.postandamain);
        addData(recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        // specify an adapter (see also next example)

        // Inflate the layout for this fragment
        return view_fragment;
    }

    private void addData(RecyclerView recyclerView){
        String userid = getArguments().getString("user");
        Query query = FirebaseFirestore.getInstance()
                .collection("post").whereEqualTo("idUser",userid)
                .limit(50);

        FirestoreRecyclerOptions<PostData> options = new FirestoreRecyclerOptions.Builder<PostData>()
                .setQuery(query, PostData.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<PostData, PostDataHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull PostDataHolder holder, int position, @NonNull PostData model) {
                holder.txtNama.setText(model.getTitle());
                holder.txtKrono.setText(model.getChronology());
            }

            @NonNull
            @Override
            public PostDataHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.recyclerviewpostanda, parent, false);
                return new PostDataHolder(view);
            }

        };

        recyclerView.setAdapter(adapter);

    }

    public void onStart(){
        super.onStart();
        adapter.startListening();
    }


    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }




}
