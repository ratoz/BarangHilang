package com.pamair.baranghilang.references;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.TimePicker;

import androidx.fragment.app.DialogFragment;

import java.text.DateFormat;
import java.util.Calendar;

/*public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
    private int hour;
    private int minute;

    public TimePickerFragment() {}

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        return new TimePickerDialog(getActivity(),this,hour,minute, true);
    }


    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        this.hour=hourOfDay;
        this.minute=minute;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }
}*/

public class TimePickerFragment extends DialogFragment {
    TimePickerDialog.OnTimeSetListener onTimeSet;
    private int minute, hour;

    public TimePickerFragment() {
    }

    public void setCallBack(TimePickerDialog.OnTimeSetListener ontime) {
        onTimeSet = ontime;
    }

    @SuppressLint("NewApi")
    @Override
    public void setArguments(Bundle argsT) {
        super.setArguments(argsT);
        minute = argsT.getInt("minute");
        hour = argsT.getInt("hour");

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //return new TimePickerDialog(getActivity(), onTimeSet, hour, minute);
        return new TimePickerDialog(getActivity(),onTimeSet,hour,minute,true);
    }
}