package com.example.parkingappinti;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class DialogFragment extends androidx.fragment.app.DialogFragment {

    DialogFragmentListener listener;



    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder= new AlertDialog.Builder(getActivity());
        View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_fragment_layout,null);
        EditText plate = dialogView.findViewById(R.id.plate_number_input);
        EditText brand = dialogView.findViewById(R.id.brand_input);

        builder.setView(dialogView).setTitle("Add vehicle")
                .setPositiveButton("Add ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (listener != null) {
                            listener.applyTexts(plate.getText().toString().toUpperCase(),brand.getText().toString().toUpperCase());
                            System.out.println("Success");
                        }else {
                            System.out.println("Error");
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

    public interface DialogFragmentListener {
        void apply(String plate,String brand);
        void applyTexts(String plate, String brand);
    }

}