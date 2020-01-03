package com.pamair.baranghilang;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.pamair.baranghilang.recyclerview.recyclerViewAdapter;

import java.util.ArrayList;

import Model.PostData;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class halamanAkun extends Fragment {

    final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private recyclerViewAdapter adapter;
    private ArrayList<PostData> postDataList;

    public halamanAkun(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view_fragment = inflater.inflate(R.layout.halaman_akun, container, false);

        final Button button = view_fragment.findViewById(R.id.menupopup);
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


        RecyclerView recyclerView = view_fragment.findViewById(R.id.postandamain);
        adapter = new recyclerViewAdapter(postDataList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        // specify an adapter (see also next example)
        recyclerView.setAdapter(adapter);
        addData();

        // Inflate the layout for this fragment
        return view_fragment;


    }

    void addData(){
        db.collection("post").whereEqualTo("idUser","admin").get().
                addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            postDataList = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()){
                                postDataList.add(new PostData(document.get("title").toString(),
                                        "",""));
                                //Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                        }
                        else {
                            postDataList.add(new PostData(task.getException().toString(),"",""));
                        }
                    }

                });

    }
}
