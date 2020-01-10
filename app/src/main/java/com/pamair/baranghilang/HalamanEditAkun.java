package com.pamair.baranghilang;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import Model.UserUpdate;

public class HalamanEditAkun extends AppCompatActivity {

    /*Declare Variable*/
    private EditText txt_nickname;
    private EditText txt_nohp;
    private EditText txt_email;
    private Button btn_save;
    FirebaseFirestore db;

    /*Initialitation Variable*/
    public void init() {
        txt_nickname  = findViewById(R.id.txt_nickname);
        txt_nohp = findViewById(R.id.txt_nohp);
        txt_email = findViewById(R.id.txt_email);
        btn_save = findViewById(R.id.btn_save);
        db = FirebaseFirestore.getInstance();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_halaman_edit_akun);
        init();
        getUser();
        saveUser();
    }

    private void getUser() {
        //db.collection("userdata").document().get();
    }

    public void saveUser () {
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
       // UserUpdate data = new UserUpdate(txt_nickname.getText().toString(), txt_nohp.getText().toString(), txt_email.getText().toString());
        //Intent intent = new Intent(HalamanEditAkun.this, halamanDefault.class);
        //startActivity(intent);
    }
}
