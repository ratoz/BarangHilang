package com.pamair.baranghilang;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.errorprone.annotations.Var;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.pamair.baranghilang.references.sessionLogin;


public class MainActivity extends AppCompatActivity {

    final FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final RelativeLayout windowprogress = findViewById(R.id.windowprogress);
        final Button login = findViewById(R.id.loginclick);
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
                    username.setError("Tulis nama dengan benar!");
                }
                if(TextUtils.isEmpty(password.getText().toString())){
                    textempty = false;
                    password.setError("Tulis password juga dengan benar!");
                }
                if (textempty){
                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    windowprogress.setVisibility(View.VISIBLE);
                   login.setBackgroundResource(R.color.colorBlackTrans);
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
                                    DissapearProgress(login,windowprogress);
                                    Bundle bundle = new Bundle();
                                    bundle.putString("userId",username.getText().toString());
                                    bundle.putString("state","init");
                                    Intent dashboard = new Intent(MainActivity.this, halamanDefault.class);
                                    dashboard.putExtras(bundle);
                                    startActivity(dashboard);
                                    finish();
                                }
                                else{
                                    DissapearProgress(login,windowprogress);
                                     validatefail = false;
                                }
                            }
                            else {
                                DissapearProgress(login,windowprogress);
                                Context context = getApplicationContext();
                                CharSequence text = "Tidak dapat menemukan user! ";
                                int duration = Toast.LENGTH_SHORT;

                                Toast toast = Toast.makeText(context, text, duration);
                                toast.show();
                                validatefail = false;
                            }

                            if (validatefail!=true) {
                                DissapearProgress(login,windowprogress);
                                warningicon.setImageResource(R.drawable.draw_warning);
                                warningtext.setText("NIM atau Password salah!");
                            }
                        }
                        else{
                            DissapearProgress(login,windowprogress);

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

    void DissapearProgress(Button login, RelativeLayout windowprogress){
        login.setBackground(ContextCompat.getDrawable(this, R.drawable.button));
        windowprogress.setVisibility(View.GONE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

    }
}
