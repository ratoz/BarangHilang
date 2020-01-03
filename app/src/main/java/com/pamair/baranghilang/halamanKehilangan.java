package com.pamair.baranghilang;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class halamanKehilangan extends Fragment {

    private String user;
    private String deskripsi;
    private int time;
    private int date;

    public halamanKehilangan(){
        //empty constructor
    }

    public halamanKehilangan(String user, String deskripsi, int time, int date){
        this.user=user;
        this.deskripsi=deskripsi;
        this.time=time;
        this.date=date;
    }

    public String getUser() {
        return user;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public int getTime() {
        return time;
    }

    public int getDate() {
        return date;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        return inflater.inflate(R.layout.halaman_kehilangan, container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
    }
}
