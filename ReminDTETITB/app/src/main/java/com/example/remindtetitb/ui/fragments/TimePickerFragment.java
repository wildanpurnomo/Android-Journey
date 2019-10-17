package com.example.remindtetitb.ui.fragments;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
    private DialogTimeListener dialogTimeListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        dialogTimeListener = (DialogTimeListener) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (dialogTimeListener != null) {
            dialogTimeListener = null;
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final Calendar calendar = Calendar.getInstance();
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        boolean formatHour24 = true;
        return new TimePickerDialog(getActivity(), this, hourOfDay, minute, formatHour24);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        dialogTimeListener.onDialogTimeSet(getTag(), hourOfDay, minute);
    }

    public interface DialogTimeListener{
        void onDialogTimeSet(String tag, int hourOfDay, int minute);
    }
}
