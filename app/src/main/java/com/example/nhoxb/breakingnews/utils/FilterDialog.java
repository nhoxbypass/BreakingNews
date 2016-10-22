package com.example.nhoxb.breakingnews.utils;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.DatePicker;
import android.widget.Spinner;

import com.example.nhoxb.breakingnews.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by nhoxb on 10/23/2016.
 */
public abstract class FilterDialog {
    Activity mActivity;
    Dialog mDialog;

    //Local
    DatePicker datePicker;
    Spinner spinner;
    CheckBox checkBoxA, checkBoxB, checkBoxC;
    Button btnSubmit;

    public abstract void onResponse(Map<String, String> map);

    public FilterDialog(Activity activity) {
        this.mActivity = activity;
        this.mDialog = new Dialog(mActivity);
        dialogContentInit();
    }

    public void show()
    {
        mDialog.show();
    }

    private void dialogContentInit()
    {
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setCancelable(false);
        mDialog.setContentView(R.layout.dialog_filter);

        //Find view
        datePicker = (DatePicker) mDialog.findViewById(R.id.dp_begindate);
        spinner = (Spinner) mDialog.findViewById(R.id.spinner);
        checkBoxA = (CheckBox) mDialog.findViewById(R.id.checkbox_a);
        checkBoxB = (CheckBox) mDialog.findViewById(R.id.checkbox_b);
        checkBoxC = (CheckBox) mDialog.findViewById(R.id.checkbox_c);
        btnSubmit = (Button) mDialog.findViewById(R.id.btn_submit);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //
                Map<String, String> map = new HashMap<>();

                String newsDesk = getNewsDeskString();
                if (newsDesk != "" || !newsDesk.equals(""))
                {
                    map.put("fq", getNewsDeskString());
                }
                map.put("sort", spinner.getSelectedItem().toString());
                map.put("begin_date", getDateString());
                onResponse(map);

                mDialog.dismiss();
            }
        });

        checkBoxA.setChecked(true);
    }

    String getDateString()
    {
        String result =  String.valueOf(datePicker.getYear());

        if (datePicker.getMonth() < 10)
        {
            result += '0';
        }
        result += String.valueOf(datePicker.getMonth());

        if (datePicker.getDayOfMonth() < 10)
        {
            result += '0';
        }
        result += String.valueOf(datePicker.getDayOfMonth());

        return result;
    }

    String getNewsDeskString()
    {
        if (!checkBoxA.isChecked() && !checkBoxB.isChecked() && !checkBoxC.isChecked())
            return "";

        //Else
        String result = "news_desk:(";

        result += (checkBoxA.isChecked() ? ("\"" + checkBoxA.getText().toString() + "\"%20") : "");
        result += (checkBoxB.isChecked() ? ("\"" + checkBoxB.getText().toString() + "\"%20") : "");
        result += (checkBoxC.isChecked() ? ("\"" + checkBoxC.getText().toString() + "\"") : "");

        result += ")";

        return result;
    }
}
