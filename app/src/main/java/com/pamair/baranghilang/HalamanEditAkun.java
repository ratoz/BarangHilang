package com.pamair.baranghilang;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import Model.UserUpdate;

import static android.widget.Toast.LENGTH_SHORT;

public class HalamanEditAkun extends AppCompatActivity {

    /*Declare Variable*/
    private TextView txt_nama, txt_prodi, txt_tahun_masuk;
    private EditText txt_nickname;
    private EditText txt_nohp;
    private EditText txt_email;
    private Button btn_save;
    private String userid;
    private ImageButton btn_close;
    private RelativeLayout progress;
    FirebaseFirestore db;

    /*Initialitation Variable*/
    public void init() {

        txt_nama = findViewById(R.id.txt_namaakun);
        txt_prodi = findViewById(R.id.txt_prodiakun);
        txt_tahun_masuk = findViewById(R.id.txt_tahunmasuk);
        txt_nickname  = findViewById(R.id.txt_nickname);
        txt_nohp = findViewById(R.id.txt_nohp);
        txt_email = findViewById(R.id.txt_email);
        btn_save = findViewById(R.id.btn_save);
        db = FirebaseFirestore.getInstance();
        btn_close = findViewById(R.id.backtoakun);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        userid = intent.getStringExtra("iduser");
        Log.w("EXTRAGET",userid);
        setContentView(R.layout.activity_halaman_edit_akun);
        init();
        getUser();
        saveUser();
        closeActivity();
    }

    private void getUser() {
        db.collection("userdata").document(userid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();
                if (!(document.get("nickname").toString()==null||document.get("nickname").toString()==""))
                txt_nickname.setText(document.get("nickname").toString());
                if (!(document.get("email").toString()==null||document.get("email").toString()==""))
                txt_email.setText(document.get("email").toString());
                if (!(document.get("nohp").toString()==null||document.get("nohp").toString()==""))
                txt_nohp.setText(document.get("nohp").toString());
                txt_nama.setText(document.get("name").toString());
                txt_prodi.setText(document.get("prodi").toString());
                txt_tahun_masuk.setText("Masuk pada tahun "+document.get("tahun_masuk").toString());
            }
        });
    }

    public void saveUser () {
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txt_email.getText().equals(null)){
                    txt_email.setError("Tidak boleh kosong!");
                }
                else if (txt_nickname.getText().equals(null)){
                    txt_nickname.setError("Tidak boleh kosong!");
                }
                else if (txt_nohp.getText().equals(null)){
                    txt_nohp.setError("Tidak boleh kosong!");
                }
                else{
                Map<String, Object> data = new HashMap<>();
                data.put("nickname", txt_nickname.getText());
                data.put("email", txt_email.getText());
                data.put("nohp",txt_nohp.getText());
                progress.setVisibility(View.VISIBLE);
                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                db.collection("cities")
                        .add(data)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Toast toast = Toast.makeText(getBaseContext(), "Data telah di edit!", LENGTH_SHORT);
                                toast.show();
                                progress.setVisibility(View.GONE);
                                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast toast = Toast.makeText(getBaseContext(), "Data gagal di edit!", LENGTH_SHORT);
                                toast.show();
                                progress.setVisibility(View.GONE);
                                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                            }
                        });
                }
            }
        });
       // UserUpdate data = new UserUpdate(txt_nickname.getText().toString(), txt_nohp.getText().toString(), txt_email.getText().toString());
        //Intent intent = new Intent(HalamanEditAkun.this, halamanDefault.class);
        //startActivity(intent);
    }

    public void closeActivity () {
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }
}
