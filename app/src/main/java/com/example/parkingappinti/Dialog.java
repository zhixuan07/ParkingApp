package com.example.parkingappinti;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

public class Dialog {

    private Activity activity;
    private AlertDialog dialog;

    Dialog(Activity myActivity){
        activity = myActivity;
    }

    @SuppressLint("InflateParams")
    void startLoadingdialog() {

        // adding ALERT Dialog builder object and passing activity as parameter
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        // layoutinflater object and use activity to get layout inflater
        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.loading_dialog, null));
        builder.setCancelable(false);

        dialog = builder.create();
        dialog.show();
    }

    // dismiss method
    void dismissdialog() {
        dialog.dismiss();
    }
}