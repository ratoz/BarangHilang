package com.pamair.baranghilang;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class halamanEditPost extends AppCompatActivity {

    private String idPost;
    private Bundle bundle;
    private FirebaseFirestore db;
    private EditText edt_judul;
    private EditText edt_desc;
    private EditText edt_kronologi;
    private RadioButton rd_lost;
    private RadioButton rd_found;
    private Button btn_edit;
    private Button btn_done;
    private ImageButton btn_close;
    private String prev_judul,prev_kronologi,prev_desc,prev_type,checktype;
    private LinearLayout windowprogress;
    private RelativeLayout editpost;
    private TextView progressStatus, progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.halaman_edit_post);
        init();
        getDataProcess();
        onEditClicked();
        onSuccessClicked();
        onCloseClicked();
    }

    private void onCloseClicked() {
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void onSuccessClicked() {
        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = "Setelah anda mengubah post ini menjadi kasus yang selesai maka anda dan orang lain tidak bisa melihat post ini" +
                        ". Post ini akan di pindahkan ke Jejak Barang. Yakin dengan pilihan anda?";
                createAndShowAlertDialog("Pemberitahuan",message,"success");
            }
        });
    }

    private void init (){
        bundle = getIntent().getExtras();
        idPost = bundle.getString("idPost");
        db = FirebaseFirestore.getInstance();
        edt_judul = findViewById(R.id.edtTitle);
        edt_desc = findViewById(R.id.edtDescription);
        edt_kronologi = findViewById(R.id.edtChronology);
        rd_found = findViewById(R.id.rdbFound);
        rd_lost = findViewById(R.id.rdbLost);
        btn_edit = findViewById(R.id.btnPost);
        btn_done = findViewById(R.id.btnDone);
        btn_close = findViewById(R.id.ep_btnBack);
        windowprogress = findViewById(R.id.windowProgress);
        editpost = findViewById(R.id.layout_editpost);
        progressBar = findViewById(R.id.progressBarText);
        progressStatus = findViewById(R.id.progressStatus);

    }

    private void getDataProcess(){
        db.collection("post").document(idPost).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot d) {
                edt_judul.setText(d.getString("title"));
                edt_kronologi.setText("Kronologi: "+d.getString("chronology"));
                edt_desc.setText("Deskripsi:"+d.getString("description"));
                prev_judul = d.getString("title");
                prev_desc = d.getString("description");
                prev_kronologi = d.getString("chronology");
                prev_type = d.getString("typePost");
                if (d.getString("typePost").equals("Lost")){
                    rd_lost.setChecked(true);
                    rd_found.setChecked(false);
                }
                else {
                    rd_lost.setChecked(false);
                    rd_found.setChecked(true);
                }
            }
        });
    }

    private void onEditClicked(){
        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAndShowAlertDialog("Pemberitahuan","Apakah anda yakin dengan perubahan post anda?","edit");
            }
        });
    }

    private void createAndShowAlertDialog(String title, String message , final String type) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
                if (type.equals("edit"))
                    validateEdit();
                else
                    successPost();

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void successPost() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        editpost.setVisibility(View.GONE);
        windowprogress.setVisibility(View.VISIBLE);
        progressStatus.setText("Mengubah status post...");
        progressBar.setText("0%");
        Date date;
        date = Timestamp.now().toDate();
        String dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.US).format(date);
        Map<String, Object> docData = new HashMap<>();
        docData.put("acquire", true);
        docData.put("timestampUpdatePost", dateFormat);
        db.collection("post").document(idPost).update(docData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                windowprogress.setVisibility(View.GONE);
                editpost.setVisibility(View.VISIBLE);
                Toast.makeText(halamanEditPost.this,"Kasus pada post ini selesai!",Toast.LENGTH_SHORT).show();
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                windowprogress.setVisibility(View.GONE);
                editpost.setVisibility(View.VISIBLE);
                Toast.makeText(halamanEditPost.this,"Gagal melakukan perubahan. Pastikan anda online!",Toast.LENGTH_SHORT).show();
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            }
        });
    }

    private void validateEdit(){
        boolean valid;
        boolean notnull = true;
        if(edt_judul.getText().toString().isEmpty()){
            edt_judul.setError("Tidak boleh kosong!");
            notnull = false;
        }
        if(edt_desc.getText().toString().isEmpty()){
            edt_desc.setError("Tidak boleh kosong!");
            notnull = false;
        }
        if(edt_kronologi.getText().toString().isEmpty()){
            edt_kronologi.setError("Tidak boleh kosong!");
            notnull = false;
        }

        if (edt_judul.getText().toString().equals(prev_judul) || edt_kronologi.getText().toString().equals(prev_kronologi)
                || edt_desc.getText().toString().equals(prev_desc)){
            valid = false;
        }
        else
            valid = true;

        if (rd_found.isChecked()){
            checktype = "Found";
        }
        else
            checktype = "Lost";

        if (!prev_type.equals(checktype) || valid ){
            valid = true;
        }
        else valid = false;

        if (valid && notnull){
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            windowprogress.setVisibility(View.VISIBLE);
            editpost.setVisibility(View.GONE);
            progressStatus.setText("Mengedit Post...");
            progressBar.setText("0%");
            Date date;
            date = Timestamp.now().toDate();
            String dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.US).format(date);
            Map<String, Object> docData = new HashMap<>();
            docData.put("title", edt_judul.getText().toString());
            docData.put("description", edt_desc.getText().toString());
            docData.put("chronology", edt_kronologi.getText().toString());
            docData.put("typePost",checktype);
            docData.put("timestampUpdatePost", dateFormat);
            db.collection("post").document(idPost).update(docData).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    progressBar.setText("100%");
                    windowprogress.setVisibility(View.GONE);
                    editpost.setVisibility(View.VISIBLE);
                    Toast.makeText(halamanEditPost.this,"Post berhasil di edit!",Toast.LENGTH_SHORT).show();
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    windowprogress.setVisibility(View.GONE);
                    editpost.setVisibility(View.VISIBLE);
                    Toast.makeText(halamanEditPost.this,"Post gagal di edit. Pastikan anda online!",Toast.LENGTH_SHORT).show();
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                }
            });
        }
        else
            Toast.makeText(halamanEditPost.this,"Tidak ada perubahan atau ada yang kosong!",Toast.LENGTH_SHORT).show();
    }


}
