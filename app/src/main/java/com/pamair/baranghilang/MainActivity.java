package com.pamair.baranghilang;

import Model.User;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.errorprone.annotations.Var;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;


public class MainActivity extends AppCompatActivity {

    final FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button login = findViewById(R.id.loginclick);
        final EditText username = findViewById(R.id.txtbox_username);
        final EditText password = findViewById(R.id.txtbox_password);
        final ImageView warningicon = findViewById(R.id.warningicon);
        final TextView warningtext = findViewById(R.id.warningtext);

        login.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Boolean textempty= true;
                if(TextUtils.isEmpty(username.getText().toString())){
                    textempty = false;
                    username.setError("Tulis nama tolo!");
                }
                if(TextUtils.isEmpty(password.getText().toString())){
                    textempty = false;
                    password.setError("Tulis password juga tolo!");
                }
                if (textempty){
                DocumentReference loginref = db.collection("users").document(username.getText().toString());
                loginref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        Boolean validatefail = true;
                        if (task.isSuccessful()){
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()){
                                String pass = document.get("password").toString();
                                String passtxtbox = password.getText().toString();
                                if(pass.equals(passtxtbox)){
                                    Intent dashboard = new Intent(MainActivity.this, halamanDefault.class);
                                    startActivity(dashboard);

                                }
                                else{
                                     validatefail = false;
                                }
                            }
                            else {
                                Context context = getApplicationContext();
                                CharSequence text = "Tidak dapat menemukan user! ";
                                int duration = Toast.LENGTH_SHORT;

                                Toast toast = Toast.makeText(context, text, duration);
                                toast.show();
                                validatefail = false;
                            }

                            if (validatefail!=true) {
                                warningicon.setImageResource(R.drawable.draw_warning);
                                warningtext.setText("NIM atau Password salah!");
                            }
                        }
                        else{
                            Context context = getApplicationContext();
                            CharSequence text = "Anda sedang offline tolong ya ingat ini online tolo";
                            int duration = Toast.LENGTH_SHORT;
                            Toast toast = Toast.makeText(context, text, duration);
                            toast.show();
                        }
                    }
                });
                }
                else{
                    warningicon.setImageResource(R.drawable.draw_warning);
                    warningtext.setText("NIM atau Password salah!");
                }



            }
        });


    }
}
