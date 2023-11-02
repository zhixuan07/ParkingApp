package com.example.parkingappinti;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {
    private Button resetPassword;
    private EditText email;
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password_layout);

        resetPassword = findViewById(R.id.sendReset);
        email = findViewById(R.id.emailInput);
        // set on click listener on button
        resetPassword.setOnClickListener(v ->{
            // send the reset email link
            auth.sendPasswordResetEmail(email.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(ForgotPassword.this,"Reset Password Link sent",Toast.LENGTH_SHORT).show();
                                // after sent link, redirect to login activity
                                startActivity(new Intent(getApplicationContext(),loginActivity.class));
                            }else {

                            }


                        }
                    });
        });

    }

}
