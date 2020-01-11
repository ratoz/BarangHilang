package com.pamair.baranghilang;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import Model.PostData;
import Model.PostDataHolder;
import Model.PostDataKehilanganHolder;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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

    public halamanKehilangan(){
        //empty constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
    }

    private void addData(RecyclerView recyclerView){
        Query query = FirebaseFirestore.getInstance()
                .collection("post").whereEqualTo("typePost","Lost");

        FirestoreRecyclerOptions<PostData> options = new FirestoreRecyclerOptions.Builder<PostData>()
                .setQuery(query, PostData.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<PostData, PostDataKehilanganHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final PostDataKehilanganHolder holder, int position, @NonNull PostData model) {
               holder.txtNama.setText(model.getTitle());

                holder.txtIdPost.setText(model.getIdPost());
                holder.txtDate.setText(String.format("%d-%d-%d", model.getDateDayLost(), model.getDateMonthLost(), model.getDateYearLost()));
                holder.txtClock.setText(String.format("%d:%d", model.getTimeHourLost(), model.getTimeMinuteLost()));
                holder.txtDesc.setText(model.getDescription());
                holder.txtIdPost.setText(model.getIdPost());

                Log.w("myAppKon", model.getPhoto());

                db.collection("userdata").document(model.getIdUser()).get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                DocumentSnapshot document = task.getResult();
                                holder.txtUser.setText(document.get("name").toString());
                            }
                        });

                StorageReference ref;
                if (!model.getPhoto().equals("undefined")) {
                    if (model.getTypePost().equals("Lost"))
                        ref = storageReference.child("images/lost/" + model.getPhoto());
                    else
                        ref = storageReference.child("images/found/" + model.getPhoto());

                    ;

                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            // Local temp file has been created
                            Glide.with(getContext()).load(uri).into(holder.imgBarang);

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

            }


            @NonNull
            @Override
            public PostDataKehilanganHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerviewpostkehilangan, parent, false);
                return new PostDataKehilanganHolder(view);
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
