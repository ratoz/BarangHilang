package com.pamair.baranghilang;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.InputStream;

public class halamanPosting extends Fragment {
    private View postView;
    private RadioButton radioButton;
    private EditText edtTitle;
    private EditText edtDescription;
    private EditText edtChronology;
    private Button btnUploadImage;
    private ImageView imgPreview;
    private RadioGroup rdgPost;
    private Uri imgPath;
    private  final int PICK_IMAGE_REQUEST = 22;

    FirebaseStorage storage;
    StorageReference storageReference;

    public halamanPosting(){

    }

    public void init() {
        edtTitle =  postView.findViewById(R.id.edtTitle);
        edtDescription = postView.findViewById(R.id.edtDescription);
        edtChronology = postView.findViewById(R.id.edtChronology);
        btnUploadImage = postView.findViewById(R.id.btnChooseImage);
        imgPreview = postView.findViewById(R.id.imgPreview);
        rdgPost = postView.findViewById(R.id.rdgPost);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        postView = inflater.inflate(R.layout.halaman_posting, container, false);
        // Inflate the layout for this fragment
        init();
        selectImage();
/*Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(
                        Intent.createChooser(
                                intent,"Pilih Gambar..."),PICK_IMAGE_REQUEST
                );*/
        return postView;
    }

    private void selectImage() {
        btnUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {/*
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(
                        Intent.createChooser(
                                intent,"Pilih Gambar..."),PICK_IMAGE_REQUEST
                );*/
  /*              Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,PICK_IMAGE_REQUEST);
  */          }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && data !=null && data.getData()!=null){
            imgPath = data.getData();

//            InputStream inputStream = getCo

        }
    }
}
