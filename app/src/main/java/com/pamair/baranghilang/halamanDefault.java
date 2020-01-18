package com.pamair.baranghilang;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import android.view.MenuItem;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.pamair.baranghilang.halamanKetemu;


public class halamanDefault extends AppCompatActivity {

    halamanKetemu halKetemu;
    halamanPosting halPosting;
    halamanAkun halAkun;
    halamanKehilangan halKehilangan;
    BottomNavigationView botNav;

    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.halaman_default);

        halKetemu = new halamanKetemu();
        halPosting = new halamanPosting();
        halAkun = new halamanAkun();
        halKehilangan = new halamanKehilangan();
        botNav = findViewById(R.id.bnvMain);
        String state;
        /*Get userId From Login*/
        Bundle bundle = getIntent().getExtras();
        Bundle fragment = new Bundle();
        fragment.putString("user",bundle.getString("userId"));
        state = bundle.getString("state");
        halPosting.setArguments(fragment);
        halAkun.setArguments(fragment);
        halKehilangan.setArguments(fragment);
        halKetemu.setArguments(fragment);
        /*------------------------*/

        if(state.equals("backakun")){
            botNav.setSelectedItemId(R.id.AkunFrag);
            changeFragment(halAkun);
        }
        else
        changeFragment(halKehilangan);

        botNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.BarangHilang:
                        changeFragment(halKehilangan);
                        break;
                    case R.id.BarangKetemu:
                        changeFragment(halKetemu);
                        break;
                    case R.id.PostBarang:
                        changeFragment(halPosting);
                        break;
                    case R.id.AkunFrag:
                        changeFragment(halAkun);
                }
                return true;
            }
        });

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                AlertDialog.Builder builder = new AlertDialog.Builder(halamanDefault.this);
                builder.setTitle("Pemberitahuan");
                builder.setMessage("Anda yakin ingin keluar dari aplikasi?");
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
                builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        finish();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        };
        this.getOnBackPressedDispatcher().addCallback(this, callback);


    }

    private void changeFragment(Fragment fragment){
        getSupportFragmentManager().beginTransaction().replace(R.id.frameMain, fragment).commit();
    }
}
