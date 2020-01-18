package com.pamair.baranghilang;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
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
import androidx.core.app.NavUtils;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;

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
    private ImageButton btn_contact;
    FirebaseFirestore db;

    /*Initialitation Variable*/
    public void init() {

        txt_nama = findViewById(R.id.txt_namaakun);
        txt_prodi = findViewById(R.id.txt_prodiakun);
        txt_tahun_masuk = findViewById(R.id.txt_tahunmasuk);
        txt_nickname = findViewById(R.id.txt_nickname);
        txt_nohp = findViewById(R.id.txt_nohp);
        txt_email = findViewById(R.id.txt_email);
        btn_save = findViewById(R.id.btn_save);
        db = FirebaseFirestore.getInstance();
        btn_close = findViewById(R.id.backtoakun);
        progress = findViewById(R.id.ProgressWindow);
        btn_contact = findViewById(R.id.contact);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        userid = intent.getStringExtra("iduser");
        Log.w("EXTRAGET", userid);
        setContentView(R.layout.activity_halaman_edit_akun);
        init();
        //btn_save.setEnabled(false);
        getUser();
        saveUser();
        onClickContact();
        closeActivity();
    }

    private void getUser() {
        db.collection("userdata").document(userid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();
                if (!(document.get("nickname").toString() == null || document.get("nickname").toString().equals("")))
                    txt_nickname.setText(document.get("nickname").toString());
                if (!(document.get("email").toString() == null || document.get("email").toString().equals("")))
                    txt_email.setText(document.get("email").toString());
                if (!(document.get("nohp").toString() == null || document.get("nohp").toString().equals("")))
                    txt_nohp.setText(document.get("nohp").toString());
                txt_nama.setText(document.get("name").toString());
                txt_prodi.setText(document.get("prodi").toString());
                txt_tahun_masuk.setText("Masuk pada tahun " + document.get("tahun_masuk").toString());
            }
        });
    }


    public void saveUser() {
        btn_save.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Boolean errortext = true;
                if (isEmpty(txt_nickname)) {
                    txt_nickname.setError("Tidak boleh kosong!");
                    errortext = false;
                }
                if (txt_email.getText().toString().isEmpty()) {
                    txt_email.setError("Tidak boleh kosong!");
                    errortext = false;
                }
                if (isEmpty(txt_nohp)) {
                    txt_nohp.setError("Tidak boleh kosong!");
                    errortext = false;
                } else {
                    if (txt_nohp.getText().charAt(0) != '8' && txt_nohp.getText().length() <= 9) {
                        txt_nohp.setError("Format nomor tidak diketahui!");
                        errortext = false;
                    }
                }

                if (!errortext) {
                    Toast.makeText(HalamanEditAkun.this, "Tolong diisi dengan benar!", LENGTH_SHORT).show();
                } else {
                   /*UserUpdate userUpdate = new UserUpdate(txt_nickname.getText().toString(),txt_nohp.getText().toString(),
                           txt_email.getText().toString());*/
                    Map<String, Object> docData = new HashMap<>();
                    docData.put("nickname", txt_nickname.getText().toString());
                    docData.put("nohp", txt_nohp.getText().toString());
                    docData.put("email", txt_email.getText().toString());


                    progress.setVisibility(View.VISIBLE);
                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                    db.collection("userdata").document(userid).update(docData).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast toast = Toast.makeText(getBaseContext(), "Data telah di edit!", LENGTH_SHORT);
                            toast.show();
                            progress.setVisibility(View.GONE);
                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast toast = Toast.makeText(getBaseContext(), "Data tidak di edit!", LENGTH_SHORT);
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

    public void closeActivity() {
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                /*Bundle bundle = new Bundle();
                bundle.putString("userId", userid);
                bundle.putString("state", "backakun");
                Intent i = new Intent(HalamanEditAkun.this, halamanDefault.class);
                i.putExtras(bundle);
                startActivity(i);*/
                finish();
            }
        };
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Bundle bundle = new Bundle();
                bundle.putString("userId", userid);
                bundle.putString("state", "backakun");
                Intent i = new Intent(HalamanEditAkun.this, halamanDefault.class);
                i.putExtras(bundle);
                startActivity(i)*/

                finish();
            }
        });
        this.getOnBackPressedDispatcher().addCallback(this, callback);

    }

    public void onClickContact() {
        btn_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(Intent.ACTION_PICK,  ContactsContract.Contacts.CONTENT_URI);

                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        switch (reqCode) {
            case (1) :
                if (resultCode == Activity.RESULT_OK) {
                    Uri contactData = data.getData();
                    Cursor c = getContentResolver().query(contactData, null, null, null, null);
                    if (c.moveToFirst()) {
                        String contactId = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));
                        String hasNumber = c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                        String num = "";
                        String numfilter = "";
                        //String test="";
                        if (Integer.valueOf(hasNumber) == 1) {
                            Cursor numbers = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
                            while (numbers.moveToNext()) {
                                num = numbers.getString(numbers.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                if (num.charAt(0) == '+'){
                                    numfilter = num.substring(3).replaceAll("[^0-9]+","");
                                }
                                else {
                                    numfilter = num.substring(1).replaceAll("[^0-9]+","");
                                }
                                //Toast.makeText(HalamanEditAkun.this, "Number="+numfilter+" This is "+test, Toast.LENGTH_LONG).show();
                                txt_nohp.setText(numfilter);
                            }
                        }
                    }
                }
                break;
        }
    }

    public boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() == 0;
    }
}
