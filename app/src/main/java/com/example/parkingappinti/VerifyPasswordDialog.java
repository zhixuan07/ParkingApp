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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import es.dmoral.toasty.Toasty;

public class VerifyPasswordDialog extends androidx.fragment.app.DialogFragment {
    DialogFragmentListener listener;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    @NonNull
    @Override
    public Dialog onCreateDialog(@NonNull Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.verify_password_dialog,null);
        EditText password = dialogView.findViewById(R.id.verifyPassword);
        builder.setView(dialogView).setTitle("Verify Password")
                .setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        if(!password.getText().toString().isEmpty()){
                            if(listener !=null){
                                listener.applyTexts(password.getText().toString());

                            }
                        }else {
                            Toasty.error(getActivity(),"Please enter the password",Toasty.LENGTH_SHORT).show();
                        }


                    }
                });
        return builder.create();
    }
    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        try{
            listener = (DialogFragmentListener) context;
        }catch(ClassCastException e) {
            e.printStackTrace();
        };

    }
    public void setListener(DialogFragmentListener listener) {
        this.listener = (DialogFragmentListener) listener;
    }


    public interface DialogFragmentListener {
        void apply(String password);
        void applyTexts(String password);
    }

}