package edu.cczu.table.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.NumberPicker;

import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import edu.cczu.table.BasicApp;

public class NumberPickerDialog extends DialogFragment {
    private NumberPicker.OnValueChangeListener valueChangeListener;
    private BasicApp app;
    private LiveData<Integer> curWeek;

    public NumberPickerDialog(Activity context) {
        app = (BasicApp) context.getApplication();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        curWeek = app.getCurWeek();
        final NumberPicker numberPicker = new NumberPicker(getActivity());
        int weekNum = 20;

        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(weekNum - 1);
        numberPicker.setValue(curWeek.getValue() - 1);

        String[] displayedValues = new String[weekNum];
        for (int i = 0; i < weekNum; i++) {
            displayedValues[i] = "第" + (i + 1) + "周";
        }
        numberPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        numberPicker.setDisplayedValues(displayedValues);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("周数");
        builder.setMessage("查看第几周");

        builder.setPositiveButton("SHOW", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                valueChangeListener.onValueChange(numberPicker,
                        numberPicker.getValue(), numberPicker.getValue());
            }
        });

        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // doNothing
            }
        });

        builder.setView(numberPicker);
        return builder.create();
    }

    public NumberPicker.OnValueChangeListener getValueChangeListener() {
        return valueChangeListener;
    }

    public void setValueChangeListener(NumberPicker.OnValueChangeListener valueChangeListener) {
        this.valueChangeListener = valueChangeListener;
    }
}
