package com.example.parkingappinti;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.UUID;

public class ReportDialog extends androidx.fragment.app.DialogFragment implements AdapterView.OnItemSelectedListener{
    DialogFragmentListener listener;

    String[] items = {"Non-responsive","Illegal Parking","Reckless Driving","Vehicle Damage","Other"};
    String issue;
    UUID randomUUID = UUID.randomUUID();




    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder= new AlertDialog.Builder(getActivity());
        View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.report_vehicle_layout,null);
        // spinner is the drop down list item
        Spinner spinner = dialogView.findViewById(R.id.spinner);
        EditText plate_number = dialogView.findViewById(R.id.plate_number);
        EditText issue_details =dialogView.findViewById(R.id.issue_details);
        //set the array adapter with item to adapt with spinner
        ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item,items);
        stringArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(stringArrayAdapter);
        spinner.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);

        builder.setView(dialogView).setTitle("Report Vehicle")
                .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (listener != null) {
                            //pass the values to report fragment
                            listener.applyTexts(
                                    plate_number.getText().toString(),
                                    issue,
                                    issue_details.getText().toString()
                            );
                        }



                    }
                })
                .setNegativeButton("Cancel ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });


        return builder.create();
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        try{
            listener =(DialogFragmentListener) context;
        }catch(ClassCastException e) {
            e.printStackTrace();
        };

    }
    public void setListener(DialogFragmentListener listener) {
        this.listener = (DialogFragmentListener) listener;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        issue = items[position];
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public interface DialogFragmentListener {
        void applyTexts(String plate, String issue, String issue_details);
    }
}