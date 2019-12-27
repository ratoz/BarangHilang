package com.pamair.baranghilang;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import android.view.MenuItem;

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


    }

    private void changeFragment(Fragment fragment){
        getSupportFragmentManager().beginTransaction().replace(R.id.frameMain, fragment).commit();
    }
}
