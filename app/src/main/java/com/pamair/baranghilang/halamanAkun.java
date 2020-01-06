package com.pamair.baranghilang;

/**--------------------
 * 17.11.1228 Herlandro
 * Halaman Akun
 * --------------------**/

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;


import Model.PostData;
import Model.PostDataHolder;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class halamanAkun extends Fragment {

    //----- Inisialisasi Variable Global ----
    FirebaseFirestore db;
    RecyclerView recyclerView;
    FirestoreRecyclerAdapter adapter;
    View view_fragment;
    String userid;
    //----- Inisialisasi Variable Global ----

    public halamanAkun(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // ---------- Instansiasi ----------
        db = FirebaseFirestore.getInstance();
        userid = getArguments().getString("user");
        view_fragment = inflater.inflate(R.layout.halaman_akun, container, false);
        final ImageButton button = view_fragment.findViewById(R.id.menupopup);
        // ---------- Instansiasi ----------

        getProfil();

        // ---------- Button untuk pengaturan dan log out ----------
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
                        switch (item.getItemId()){
                            case R.id.settingakun:

                                return true;
                            case R.id.logout:
                                getActivity().finish();
                                return true;
                            default:
                                return true;
                        }
                    }
                });
                popup.show();//showing popup menu
            }
        });
        // ---------- Button untuk pengaturan dan log out ----------



        // ---------- Pengaturan Recycle View ----------
        recyclerView = view_fragment.findViewById(R.id.postandamain);
        addData(recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        // specify an adapter (see also next example)
        // ---------- Pengaturan Recycle View ----------


        // Inflate the layout for this fragment
        return view_fragment;
    }


    // ---------- Mengatur nama dan lain-lain ------------
    private void getProfil(){
        DocumentReference dataprofil = db.collection("userdata").document(userid);
        dataprofil.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>(){
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                TextView txt_nama = view_fragment.findViewById(R.id.txt_namaakun);
                TextView txt_prodi = view_fragment.findViewById(R.id.txt_prodiakun);
                TextView txt_tahunmasuk = view_fragment.findViewById(R.id.txt_tahunmasuk);
                if (task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        txt_nama.setText(document.get("name").toString());
                        txt_prodi.setText(document.get("prodi").toString());
                        txt_tahunmasuk.setText("Masuk pada tahun "+document.get("tahun_masuk").toString());
                    } else {
                        txt_nama.setText("Tidak Ditemukan 404");
                        txt_prodi.setText(document.get("Tidak Ditemukan 404").toString());
                        txt_prodi.setText(document.get("Tidak Ditemukan 404").toString());
                        Toast.makeText(getContext(),"Gagal mencari Data", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(),"Pastikan anda online!", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }


    // --------------Fungsi Adapter Recycle View------------------
    private void addData(RecyclerView recyclerView){
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

            public void onDataChanged(){
                super.onDataChanged();
                TextView txtload1 = view_fragment.findViewById(R.id.txt_loadingpost1);
                TextView txtload2 = view_fragment.findViewById(R.id.txt_loadingpost2);
                if (getItemCount()==0){
                    txtload1.setText("Tidak menemukan Post yang aktif");
                }
                else {
                    txtload1.setVisibility(View.GONE);
                    txtload2.setVisibility(View.GONE);
                }
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
