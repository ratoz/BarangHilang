package com.pamair.baranghilang;

/**--------------------
 * 17.11.1228 Herlandro
 * Halaman Akun
 * --------------------**/

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import Model.PostData;
import Model.PostDataHolder;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class halamanAkun extends Fragment {

    //----- Inisialisasi Variable Global ----
    FirebaseStorage storage;
    StorageReference storageReference;
    FirebaseFirestore db;
    RecyclerView recyclerView;
    FirestoreRecyclerAdapter adapter;
    View view_fragment;
    String userid;
    ImageButton btn_menutop;
    //----- Inisialisasi Variable Global ----

    public halamanAkun(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // ---------- Instansiasi ----------
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        db = FirebaseFirestore.getInstance();
        userid = getArguments().getString("user");
        view_fragment = inflater.inflate(R.layout.halaman_akun, container, false);
        btn_menutop = view_fragment.findViewById(R.id.menupopup);
        // ---------- Instansiasi ----------

        getProfil();

        // ---------- Button untuk pengaturan dan log out ----------
        onCreateMenuTopBar();
        // ---------- Button untuk pengaturan dan log out ----------

        // ---------- Pengaturan Recycle View ----------
        recyclerView = view_fragment.findViewById(R.id.postandamain);
        view_fragment.setNestedScrollingEnabled(false);
        addData(recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
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
                        txt_prodi.setText("Tidak Ditemukan 404");
                        txt_prodi.setText("Tidak Ditemukan 404");
                        Toast.makeText(getContext(),"Gagal mencari Data", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(),"Pastikan anda online!", Toast.LENGTH_SHORT).show();
                    txt_nama.setText("Tidak Ditemukan 404");
                    txt_prodi.setText("Tidak Ditemukan 404");
                    txt_prodi.setText("Tidak Ditemukan 404");
                    Toast.makeText(getContext(),"Gagal mencari Data", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }

    private void onCreateMenuTopBar(){
        btn_menutop.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Creating the instance of PopupMenu
                PopupMenu popup = new PopupMenu(getContext(), btn_menutop);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.menuakun, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.settingakun:
                                //Toast.makeText(halamanAkun.this.getContext(), halamanAkun.this.getActivity().toString(), Toast.LENGTH_LONG).show();
                                Intent i = new Intent(halamanAkun.this.getContext(),HalamanEditAkun.class);
                                i.putExtra("iduser", userid);
                                startActivity(i);
                                return true;
                            case R.id.logout:
                                Intent t = new Intent(halamanAkun.this.getContext(),MainActivity.class);
                                startActivity(t);
                                halamanAkun.this.getActivity().finish();
                                return true;
                            case R.id.history:
                                Intent e = new Intent(halamanAkun.this.getContext(),halamanJejak.class);
                                e.putExtra("iduser", userid);
                                startActivity(e);
                                return true;
                            default:
                                return true;
                        }
                    }
                });
                popup.show();//showing popup menu
            }
        });
    }

    // --------------Fungsi Click Recycle View------------------
    private void popupClickRecycleView(View view, final PostDataHolder holder){
        LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(view.getContext().LAYOUT_INFLATER_SERVICE);
        final View popupView = inflater.inflate(R.layout.menupostandaa, null);

        //Specify the length and width through constants
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.MATCH_PARENT;

        //Make Inactive Items Outside Of PopupWindow
        boolean focusable = true;

        //Create a window with our parameters
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        //Set the location of the window on the screen
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        //Initialize the elements of our window, install the handler

        TextView judulpost = popupView.findViewById(R.id.judulpost);
        judulpost.setText("Menu : "+holder.txtNama.getText());

        Button btn_edit = popupView.findViewById(R.id.editpost);
        Button btn_hapus = popupView.findViewById(R.id.hapuspost);
        Button btn_batal = popupView.findViewById(R.id.batalbutton);
        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                Intent i = new Intent(getActivity(),halamanEditPost.class);
                i.putExtra("idPost", holder.txtIdPost.getText().toString());
                startActivity(i);
            }
        });

        btn_hapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                popupWindow.dismiss();
                db.collection("post").document(holder.txtIdPost.getText().toString()).delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(v.getContext(), "Hapus Post Berhasil!", Toast.LENGTH_SHORT).show();

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(v.getContext(), "Hapus Post Gagal. Pastikan anda online!", Toast.LENGTH_SHORT).show();
                            }
                        });

            }
        });

        btn_batal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });



        //Handler for clicking on the inactive zone of the window

        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                //Close the window when clicked
                popupWindow.dismiss();
                return true;
            }
        });

    }

    // --------------Fungsi Adapter Recycle View------------------
    private void addData(RecyclerView recyclerView){
        Query query = FirebaseFirestore.getInstance()
                .collection("post").orderBy("timestampUpdatePost", Query.Direction.DESCENDING).whereEqualTo("idUser",userid).whereEqualTo("acquire",false)
                ;

        FirestoreRecyclerOptions<PostData> options = new FirestoreRecyclerOptions.Builder<PostData>()
                .setQuery(query, PostData.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<PostData, PostDataHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final PostDataHolder holder, int position, @NonNull PostData model) {
                holder.txtNama.setText(model.getTitle());
                holder.txtTypePost.setText(model.getTypePost());
                holder.txtIdPost.setText(model.getIdPost());
                Log.w("myAppKon", model.getPhoto());

                    StorageReference ref;
                    if (!model.getPhoto().equals("undefined")) {
                        if (model.getTypePost().equals("Lost"))
                            ref = storageReference.child("images/lost/" + model.getPhoto());
                        else
                            ref = storageReference.child("images/found/" + model.getPhoto());

                     ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                // Local temp file has been created
                                Log.w("myAppKon", holder.itemView.getContext().toString());
                                Glide.with(holder.itemView.getContext()).load(uri).into(holder.imgBarang);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle any errors
                            }
                        });
                    }
                    else{
                        holder.imgBarang.setBackgroundColor(getResources().getColor(R.color.colorBlack));
                        holder.imgBarang.setImageResource(R.drawable.fragfound);
                    }


                holder.cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        popupClickRecycleView(view,holder);
                    }
                });
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
