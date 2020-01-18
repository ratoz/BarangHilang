package com.pamair.baranghilang;

import android.app.VoiceInteractor;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.firebase.ui.firestore.paging.LoadingState;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Objects;

import Model.PostData;
import Model.PostDataHolder;
import Model.PostDataKehilanganHolder;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class halamanKehilangan extends Fragment {

    private View view_fragment;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private FirebaseFirestore db;
    private RecyclerView recyclerView;
    private FirestoreRecyclerAdapter adapter;
    private String userid;

    public halamanKehilangan() {
        //empty constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        db = FirebaseFirestore.getInstance();
        userid = getArguments().getString("user");
        view_fragment = inflater.inflate(R.layout.halaman_kehilangan, container, false);

        // ---------- Pengaturan Recycle View ----------
        recyclerView = view_fragment.findViewById(R.id.postkehilangan);
        view_fragment.setNestedScrollingEnabled(false);
        addData(recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        // ---------- Pengaturan Recycle View ----------

        // Inflate the layout for this fragment
        return view_fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    /*private void addData(RecyclerView recyclerView){
        Query query = FirebaseFirestore.getInstance()
                .collection("post").orderBy("timestampUpdatePost", Query.Direction.DESCENDING).whereEqualTo("typePost", "Lost").whereEqualTo("acquire",false)
                ;
        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPrefetchDistance(10)
                .setPageSize(20)
                .build();

        FirestorePagingOptions<PostData> options = new FirestorePagingOptions.Builder<PostData>()
                .setLifecycleOwner(this)
                .setQuery(query, config, PostData.class)
                .build();

        FirestorePagingAdapter<PostData,PostDataKehilanganHolder> adapter = new FirestorePagingAdapter<PostData, PostDataKehilanganHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull PostDataKehilanganHolder holder, int position, @NonNull PostData model) {

            }

            @NonNull
            @Override
            public PostDataKehilanganHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return null;
            }

            protected void onLoadingStateChanged(@NonNull LoadingState state) {
                switch (state) {
                    case LOADING_INITIAL:
                        // The initial load has begun
                        // ...
                    case LOADING_MORE:
                        // The adapter has started to load an additional page
                        // ...
                    case LOADED:
                        // The previous load (either initial or additional) completed
                        // ...
                    case ERROR:
                        // The previous load (either initial or additional) failed. Call
                        // the retry() method in order to retry the load operation.
                        // ...
                }
            }
        };
    }*/

    private void addData(RecyclerView recyclerView) {
        Query query = FirebaseFirestore.getInstance()
                .collection("post").orderBy("timestampUpdatePost", Query.Direction.DESCENDING).whereEqualTo("typePost", "Lost").whereEqualTo("acquire",false)
                ;

        FirestoreRecyclerOptions<PostData> options = new FirestoreRecyclerOptions.Builder<PostData>()
                .setQuery(query, PostData.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<PostData, PostDataKehilanganHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final PostDataKehilanganHolder holder, int position, @NonNull final PostData model) {
                String desc = model.getDescription();
                if (desc.length()>40){
                    desc = desc.substring(0,40)+"...";
                }
                holder.txtNama.setText(model.getTitle());
                holder.txtIdPost.setText(model.getIdPost());
                holder.txtDate.setText(String.format("%d-%d-%d", model.getDateDayLost(), model.getDateMonthLost(), model.getDateYearLost()));
                holder.txtClock.setText(String.format("%d:%d", model.getTimeHourLost(), model.getTimeMinuteLost()));
                holder.txtDesc.setText(desc);
                holder.txtIdPost.setText(model.getIdPost());
                holder.timepost.setText(model.getTimestampUpdatePost());

                Log.w("myAppKon", model.getPhoto());

                db.collection("userdata").document(model.getIdUser()).get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                DocumentSnapshot document = task.getResult();
                                String nama = document.get("name").toString();
                                String arraynama[];
                                arraynama = nama.split(" ");
                                int count;
                                if (nama.length()>=15){
                                    count = 0;

                                    for (int i=0; i<arraynama.length ;i++){
                                        count += arraynama[i].length();
                                        if (count>15){
                                            arraynama[i] =  arraynama[i].substring(0,1).toUpperCase()+".";
                                        }
                                    }
                                }
                                nama = "";
                                for (int i=0; i<arraynama.length ;i++){
                                    nama += arraynama[i]+" ";
                                }
                                holder.txtUser.setText(nama);
                            }
                        });


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
                            holder.imgBarang.setBackgroundColor(getResources().getColor(R.color.colorBlack));
                            holder.imgBarang.setImageResource(R.drawable.fragfound);
                        }
                    });
                } else {
                    holder.imgBarang.setBackgroundColor(getResources().getColor(R.color.colorBlack));
                    holder.imgBarang.setImageResource(R.drawable.fragfound);
                }

                holder.cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        popupRecycleView(view, holder, model);
                    }
                });

            }


            @NonNull
            @Override
            public PostDataKehilanganHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerviewpostkehilangan, parent, false);
                return new PostDataKehilanganHolder(view);
            }

            public void onDataChanged() {
                super.onDataChanged();
                TextView txtload1 = view_fragment.findViewById(R.id.txt_loadingpost1);
                TextView txtload2 = view_fragment.findViewById(R.id.txt_loadingpost2);
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

    public void popupRecycleView(View view, final PostDataKehilanganHolder holder, PostData model) {
        LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(view.getContext().LAYOUT_INFLATER_SERVICE);
        final View popupView = inflater.inflate(R.layout.menupost, null);

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
        final TextView name = popupView.findViewById(R.id.p_nama);
        final RelativeLayout progressbar = popupView.findViewById(R.id.p_progressbar);
        final TextView nohp = popupView.findViewById(R.id.p_nohp);
        final TextView email = popupView.findViewById(R.id.p_gmail);
        final ScrollView viewpost = popupView.findViewById(R.id.p_viewpost);
        final ImageButton btnwa =popupView.findViewById(R.id.p_btnwa);
        final ImageButton btngmail =popupView.findViewById(R.id.p_btngmail);
        ImageView imgpost = popupView.findViewById(R.id.p_imgpost);
        ImageButton btn_batal = popupView.findViewById(R.id.p_popupcancel);

        Glide.with(holder.itemView.getContext()).load(holder.imgBarang.getDrawable()).into(imgpost);


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
                tgllost.setText("Tanggal Hilang : "+ post.getDateDayLost() + "-" +
                        post.getDateMonthLost() + "-" + post.getDateYearLost() + ", "+ post.getTimeHourLost()
                        +":" + post.getTimeMinuteLost()
                        );
                tglpost.setText("Tanggal Posting : "+ post.getDateDayPost() + "-" +
                        post.getDateMonthPost() + "-" + post.getDateYearPost() + ", "+ post.getTimeHourPost()
                        +":" + post.getTimeMinutePost()
                );
            }
        });
        db.collection("userdata").document(model.getIdUser()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot d) {
                name.setText("Pemilik Post: "+d.getString("name"));
                nohp.setText(d.getString("nohp"));
                email.setText(d.getString("email"));
                if (nohp.getText().toString().isEmpty()){
                    btnwa.setEnabled(false);
                    btnwa.setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN);
                }
                else{
                    btnwa.setEnabled(true);
                    btnwa.setColorFilter(null);
                }
                if (email.getText().toString().isEmpty()){
                    btngmail.setEnabled(false);
                }
                else{
                    btngmail.setEnabled(true);
                }
            }
        });

        btnwa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PackageManager pm = getActivity().getPackageManager();
                try {
                    Intent waIntent = new Intent(Intent.ACTION_VIEW);
                    String text = "Aku mendapatkan barang yang ada di post mu berjudul '"+holder.txtNama.getText().toString()+
                            "'. Jika ingin melanjutkan percakapan tentang barang ini tolong chat disini. Ini adalah pemberitahuan dari aplikasi Lo%26Fo Amikom terima kasih!";
                    waIntent.setData(Uri.parse("http://api.whatsapp.com/send?phone=62"+nohp.getText().toString() +"&text="+text));
                   // waIntent.setType("text/plain");
                    PackageInfo info=pm.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);
                    //Check if package exists or not. If not then code
                    //in catch block will be called
                    startActivity(waIntent);

                } catch (PackageManager.NameNotFoundException e) {
                    Toast.makeText(view_fragment.getContext(), "WhatsApp not Installed", Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });

        btngmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto",email.getText().toString(), null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Barang Hilang Ditemukan");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "Aku mendapatkan barang yang ada di post mu berjudul '"+holder.txtNama.getText().toString()+
                        "'. Jika ingin melanjutkan percakapan tentang barang ini tolong email disini. Ini adalah pemberitahuan dari aplikasi Lo&Fo Amikom terima kasih!");
                startActivity(Intent.createChooser(emailIntent, "Send email..."));
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


    public void onStart() {
        super.onStart();

        adapter.startListening();

    }


    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }


}
