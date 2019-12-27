package com.pamair.baranghilang;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class layoutKehilangan extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Buatlah sebuah intent yang digunakan untuk berpindah dari activity boarding ke activity main

    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(this,halamanDefault.class);
        startActivity(intent);
    }
}
