package com.example.nhoxb.breakingnews.ui.main;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.Spinner;

import com.example.nhoxb.breakingnews.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by nhoxb on 10/23/2016.
 */
public class FilterDialog extends Dialog {
    // Local
    DatePicker datePicker;
    Spinner spinner;
    CheckBox checkBoxA, checkBoxB, checkBoxC;
    Button btnSubmit;

    private OnResponseListener listener;

    public FilterDialog(@NonNull Context context) {
        this(context, null);
    }

    public FilterDialog(@NonNull Context context, OnResponseListener listener) {
        super(context);
        dialogContentInit();
        setOnResponseListener(listener);
    }

    public void setOnResponseListener(OnResponseListener listener) {
        this.listener = listener;
    }

    private void dialogContentInit() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCancelable(false);
        setContentView(R.layout.dialog_filter);

        //Find view
        datePicker = findViewById(R.id.dp_begindate);
        spinner = findViewById(R.id.spinner);
        checkBoxA = findViewById(R.id.checkbox_a);
        checkBoxB = findViewById(R.id.checkbox_b);
        checkBoxC = findViewById(R.id.checkbox_c);
        btnSubmit = findViewById(R.id.btn_submit);

        btnSubmit.setOnClickListener(view -> {
            //
            Map<String, String> map = new HashMap<>();

            String newsDesk = getNewsDeskString();
            if (!newsDesk.isEmpty()) {
                map.put("fq", getNewsDeskString());
            }
            map.put("sort", spinner.getSelectedItem().toString());
            map.put("begin_date", getDateString());

            if (listener != null)
                listener.onResponse(map);

            dismiss();
        });

        checkBoxA.setChecked(true);
    }

    private String getDateString() {
        String result = String.valueOf(datePicker.getYear());

        if (datePicker.getMonth() < 10) {
            result += '0';
        }
        result += String.valueOf(datePicker.getMonth());

        if (datePicker.getDayOfMonth() < 10) {
            result += '0';
        }
        result += String.valueOf(datePicker.getDayOfMonth());

        return result;
    }

    private String getNewsDeskString() {
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

    public interface OnResponseListener {
        void onResponse(Map<String, String> map);
    }
}
