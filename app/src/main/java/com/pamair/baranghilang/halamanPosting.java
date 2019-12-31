package com.pamair.baranghilang;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.pamair.baranghilang.references.DatePickerFragment;

import java.io.InputStream;
import java.util.Calendar;

public class halamanPosting extends Fragment {
    private View postView;
    private TextView tvDateFoundLost;
    private TextView calendar;
    private RadioButton radioButton;
    private EditText edtTitle;
    private EditText edtDescription;
    private EditText edtChronology;
    private Button btnUploadImage;
    private ImageView imgPreview;
    private RadioGroup rdgPost;
    private RadioButton rdbFound;
    private RadioButton rdbLost;
    private ImageView btnCalendar;
    private Calendar myCalendar;
    private DatePickerDialog.OnDateSetListener date;

    //private Uri imgPath;
    //private  final int PICK_IMAGE_REQUEST = 22;

    //FirebaseStorage storage;
    //StorageReference storageReference;

    public halamanPosting(){

    }

    public void init() {
        edtTitle =  postView.findViewById(R.id.edtTitle);
        edtDescription = postView.findViewById(R.id.edtDescription);
        edtChronology = postView.findViewById(R.id.edtChronology);
        btnUploadImage = postView.findViewById(R.id.btnChooseImage);
        imgPreview = postView.findViewById(R.id.imgPreview);
        tvDateFoundLost = postView.findViewById(R.id.tvDateFoundLostToggle);
        rdgPost = postView.findViewById(R.id.rdgPost);
        btnCalendar = postView.findViewById(R.id.btnDateFoundLost);
        calendar = postView.findViewById(R.id.tvDateFoundLost);

        //storage = FirebaseStorage.getInstance();
        //storageReference = storage.getReference();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        postView = inflater.inflate(R.layout.halaman_posting, container, false);
        // Inflate the layout for this fragment
        init();
        onSelectedPostType();
        onSelectedDateTime();
        return postView;
    }

    private void onSelectedDateTime() {
        btnCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment date = new DatePickerFragment();
                /**
                 * Set Up Current Date Into dialog
                 */
                Calendar calender = Calendar.getInstance();
                Bundle args = new Bundle();
                args.putInt("year", calender.get(Calendar.YEAR));
                args.putInt("month", calender.get(Calendar.MONTH));
                args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));
                date.setArguments(args);
                /**
                 * Set Call back to capture selected date
                 */
                date.setCallBack(ondate);
                date.show(getFragmentManager(), "Date Picker");
            }
        });
    }


    public void onSelectedPostType(){
        rdgPost.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.rdbFound:
                        tvDateFoundLost.setText("Tanggal Ditemukan");
                        break;
                    case R.id.rdbLost:
                        tvDateFoundLost.setText("Tanggal Kehilangan");
                        break;
                }
            }
        });
    }

    DatePickerDialog.OnDateSetListener ondate = new DatePickerDialog.OnDateSetListener() {
        String month;
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {

            monthOfYear++;
            switch(monthOfYear){
                case 1:
                    month = "Januari";
                    break;
                case 2:
                    month = "Februari";
                    break;
                case 3:
                    month = "Maret";
                    break;
                case 4:
                    month = "April";
                    break;
                case 5:
                    month = "Mei";
                    break;
                case 6:
                    month = "Juni";
                    break;
                case 7:
                    month = "Juli";
                    break;
                case 8:
                    month = "Agustus";
                    break;
                case 9:
                    month = "September";
                    break;
                case 10:
                    month = "Oktober";
                    break;
                case 11:
                    month = "November";
                    break;
                case 12:
                    month = "Desember";
                    break;
            }
            calendar.setText(dayOfMonth + " " + month + " " + year);
        }
    };
}
