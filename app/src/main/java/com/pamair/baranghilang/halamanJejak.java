package com.pamair.baranghilang;

import Model.PostData;
import Model.PostDataHolder;
import Model.PostDataKehilanganHolder;
import Model.PostJejakHolder;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class halamanJejak extends AppCompatActivity {

    private FirebaseFirestore db;
    private RecyclerView recyclerView;
    private String userid;
    private FirestoreRecyclerAdapter adapter;
    private ImageButton cross;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_halaman_jejak);
        db = FirebaseFirestore.getInstance();
        Intent intent = getIntent();
        userid = intent.getStringExtra("iduser");
        // ---------- Pengaturan Recycle View ----------
        recyclerView = findViewById(R.id.postJejak);
        recyclerView.setNestedScrollingEnabled(false);
        addData(recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        cross = findViewById(R.id.ep_btnBack);

        cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        // ---------- Pengaturan Recycle View ----------

        // Inflate the layout for this fragment
    }

    private void addData(RecyclerView recyclerView) {
        Query query = FirebaseFirestore.getInstance()
                .collection("post").orderBy("timestampUpdatePost", Query.Direction.DESCENDING).whereEqualTo("idUser", userid).whereEqualTo("acquire", true);

        FirestoreRecyclerOptions<PostData> options = new FirestoreRecyclerOptions.Builder<PostData>()
                .setQuery(query, PostData.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<PostData, PostJejakHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final PostJejakHolder holder, int position, @NonNull final PostData model) {
                String desc = model.getDescription();
                if (desc.length()>40){
                    desc = desc.substring(0,40)+"...";
                }
                String Kronologi = model.getChronology();
                if (Kronologi.length()>40)
                    Kronologi = Kronologi.substring(0,40)+"...";
                holder.txtNama.setText(model.getTitle());
                holder.txtTypePost.setText("Tipe Kasus: " + model.getTypePost());
                holder.txtDesc.setText("Deskripsi"+desc);
                holder.txtKronologi.setText("Kronologi"+Kronologi);
                holder.txtSelesaipada.setText(model.getTimestampUpdatePost());
                holder.txtIdPost.setText(model.getIdPost());
                holder.cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        popupRecycleView(view, holder, model);
                    }
                });
            }


            @NonNull
            @Override
            public PostJejakHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.recyclerviewjejak, parent, false);
                return new PostJejakHolder(view);
            }

            public void onDataChanged() {
                super.onDataChanged();
                TextView txtload1 = findViewById(R.id.txt_loadingpost1);
                TextView txtload2 = findViewById(R.id.txt_loadingpost2);
                if (getItemCount() == 0) {
                    txtload1.setText("Tidak menemukan Post yang aktif");
                } else {
                    txtload1.setVisibility(View.GONE);
                    txtload2.setVisibility(View.GONE);
                }
            }

        };

        recyclerView.setAdapter(adapter);

    }

    public void popupRecycleView(View view, final PostJejakHolder holder, PostData model) {
        LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(view.getContext().LAYOUT_INFLATER_SERVICE);
        final View popupView = inflater.inflate(R.layout.jejakpopup, null);

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
        TextView judulpost = popupView.findViewById(R.id.p_judulpost);
        final TextView kronologi = popupView.findViewById(R.id.p_kronologi);
        final TextView desc = popupView.findViewById(R.id.p_desc);
        final TextView tgllost = popupView.findViewById(R.id.p_tgllost);
        final TextView tglpost = popupView.findViewById(R.id.p_tglpost);
        final TextView tglselesai = popupView.findViewById(R.id.p_selesai);
        final RelativeLayout progressbar = popupView.findViewById(R.id.p_progressbar);
        final ScrollView viewpost = popupView.findViewById(R.id.p_viewpost);
        final TextView tipepost = popupView.findViewById(R.id.p_kategori);
        ImageButton btn_batal = popupView.findViewById(R.id.p_popupcancel);

        judulpost.setText("Post : " + holder.txtNama.getText());

        //------------------------------------------------------

        db.collection("post").document(holder.txtIdPost.getText().toString()).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot d) {
                        progressbar.setVisibility(View.GONE);
                        viewpost.setVisibility(View.VISIBLE);
                        PostData post = d.toObject(PostData.class);
                        kronologi.setText(d.getString("chronology"));
                        desc.setText(d.getString("description"));
                        tipepost.setText("Tipe Kasus: "+d.getString("typePost"));
                        tglselesai.setText("Tanggal Selesai: "+d.getString("timestampUpdatePost"));
                        tgllost.setText("Tanggal Hilang : " + post.getDateDayLost() + "-" +
                                post.getDateMonthLost() + "-" + post.getDateYearLost() + ", " + post.getTimeHourLost()
                                + ":" + post.getTimeMinuteLost()
                        );
                        tglpost.setText("Tanggal Posting : " + post.getDateDayPost() + "-" +
                                post.getDateMonthPost() + "-" + post.getDateYearPost() + ", " + post.getTimeHourPost()
                                + ":" + post.getTimeMinutePost()
                        );
                    }
                });
        btn_batal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });
        }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
