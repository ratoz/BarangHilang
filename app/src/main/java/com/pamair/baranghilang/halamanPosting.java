package com.pamair.baranghilang;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.pamair.baranghilang.references.DatePickerFragment;
import com.pamair.baranghilang.references.TimePickerFragment;

import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;

import Model.PostData;

public class halamanPosting extends Fragment {

    /*Declare Variable*/
    private View postView;
    private TextView tvDateFoundLost;
    private TextView tvTimeFoundLost;
    private TextView calendar,clock;
    private EditText edtTitle,edtDescription,edtChronology;
    private Button btnUploadImage;
    private Button btnPost;
    private Button btnTutup;
    private ImageView imgPreview;
    private RadioGroup rdgPost;
    private RadioButton rdbFound;
    private RadioButton rdbLost;
    private ImageView btnCalendar;
    private ImageView btnClock;
    private Calendar myCalendar;
    private DatePickerDialog.OnDateSetListener date;
    private PostData post;
    private LinearLayout progress;
    private LinearLayout success;
    FirebaseFirestore db;
    private boolean dateSet;
    private boolean timeSet;


    /*Constructor*/
    public halamanPosting(){

    }

    /*Initialitation Variable*/
    public void init() {
        edtTitle =  postView.findViewById(R.id.edtTitle);
        edtDescription = postView.findViewById(R.id.edtDescription);
        edtChronology = postView.findViewById(R.id.edtChronology);
        btnUploadImage = postView.findViewById(R.id.btnChooseImage);
        btnPost = postView.findViewById(R.id.btnPost);
        btnTutup = postView.findViewById(R.id.btnTutup);
        imgPreview = postView.findViewById(R.id.imgPreview);
        tvDateFoundLost = postView.findViewById(R.id.tvDateFoundLostToggle);
        tvTimeFoundLost = postView.findViewById(R.id.tvTimeFoundLost);
        rdgPost = postView.findViewById(R.id.rdgPost);
        btnCalendar = postView.findViewById(R.id.btnDateFoundLost);
        btnClock = postView.findViewById(R.id.btnTimeFoundLost);
        calendar = postView.findViewById(R.id.tvDateFoundLost);
        clock = postView.findViewById(R.id.tvTimeFoundLost);
        progress = postView.findViewById(R.id.windowProgress);
        success = postView.findViewById(R.id.windowSuccess);
        post = new PostData();
        db = FirebaseFirestore.getInstance();
        timeSet = false;
        dateSet = false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        postView = inflater.inflate(R.layout.halaman_posting, container, false);
        // Inflate the layout for this fragment

        return postView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
        onSelectedPostType();
        onSelectedDate();
        onSelectedTime();
        getDateTimeNow();
        onPostEntry();
        //tvDateFoundLost.setText(getArguments().getString("user"));
    }

    private void onPostEntry() {
        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                post.setIdUser(getArguments().getString("user"));
                post.setTitle(edtTitle.getText().toString());
                post.setDescription(edtDescription.getText().toString());
                post.setChronology(edtChronology.getText().toString());
                post.setPhoto("image/..."); //Sementara

                final CollectionReference newPost = db.collection("post");

                if (TextUtils.isEmpty(edtTitle.getText().toString())){
                    edtTitle.setError("Judul tidak boleh kosong");
                    edtTitle.requestFocus();
                } else if (TextUtils.isEmpty(edtChronology.getText().toString())){
                    edtChronology.setError("Kronologi tidak boleh kosong");
                    edtChronology.requestFocus();
                } else if (TextUtils.isEmpty(edtDescription.getText().toString())){
                    edtDescription.setError("Deskripsi tidak boleh kosong");
                    edtDescription.requestFocus();
                } else if (dateSet==false){
                    //Toast.makeText(getContext(),"Tanggal Tidak Boleh Kosong",Toast.LENGTH_SHORT).show();
                    calendar.setError("Tanggal tidak boleh kosong");
                    calendar.requestFocus();
                } else if (timeSet==false){
                    //Toast.makeText(getContext(),"Waktu Tidak Boleh Kosong",Toast.LENGTH_SHORT).show();
                    clock.setError("Waktu tidak boleh kosong");
                    clock.requestFocus();
                }else {
                    progress.setVisibility(View.VISIBLE);
                    newPost.add(post).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            String idPost = documentReference.getId().toString();
                            newPost.document(idPost).update("idPost", idPost);
                            progress.setVisibility(View.GONE);
                            edtTitle.setText("");
                            edtDescription.setText("");
                            edtChronology.setText("");
                            calendar.setText("...");
                            clock.setText("...");
                            success.setVisibility(View.VISIBLE);
                        }
                    });
                }
            }
        });

        btnTutup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                success.setVisibility(View.GONE);
            }
        });
    }

    private void getDateTimeNow() {
        Calendar now = Calendar.getInstance();
        int hour = now.get(Calendar.HOUR_OF_DAY);
        int minute = now.get(Calendar.MINUTE);
        int day = now.get(Calendar.DAY_OF_MONTH);
        int month = now.get(Calendar.MONTH)+1;
        int year = now.get(Calendar.YEAR);
        String datetime = Integer.toString(year)+Integer.toString(month)
                +Integer.toString(day)+Integer.toString(hour)
                +Integer.toString(minute);

        post.setTimePost(hour,minute);
        post.setDatePost(day,month,year);
        post.setDateTimePost(datetime); //For Sorting In Firestore
    }

    private void onSelectedDate() {
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

    private void onSelectedTime() {
        btnClock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerFragment time = new TimePickerFragment();
                /**
                 * Set Up Current Date Into dialog
                 */
                Calendar calender = Calendar.getInstance();
                Bundle argsT = new Bundle();
                argsT.putInt("hour", calender.get(Calendar.HOUR_OF_DAY));
                argsT.putInt("minute", calender.get(Calendar.MINUTE));

                time.setArguments(argsT);
                /**
                 * Set Call back to capture selected date
                 */
                time.setCallBack(ontime);
                time.show(getFragmentManager(), "Time Picker");
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
                        post.setTypePost("Found");
                        break;
                    case R.id.rdbLost:
                        tvDateFoundLost.setText("Tanggal Kehilangan");
                        post.setTypePost("Lost");
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
            post.setDateLost(dayOfMonth,monthOfYear,year);
            calendar.setText(dayOfMonth + " " + month + " " + year);
            dateSet = true;
        }
    };

    TimePickerDialog.OnTimeSetListener ontime = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minuteOfHour) {
            String hour = Integer.toString(hourOfDay);
            String minute = Integer.toString(minuteOfHour);

            post.setTimeLost(hourOfDay,minuteOfHour);
            tvTimeFoundLost.setText(hour+":"+minute);
            timeSet = true;
        }
    };
}
