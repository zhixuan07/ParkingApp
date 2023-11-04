package com.example.parkingappinti;


import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import es.dmoral.toasty.Toasty;

public class UpdateEmailActivity extends AppCompatActivity {
    private EditText email;
    private Button updateEmail;
    private String userID;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_email_layout);

        email = findViewById(R.id.email);
        updateEmail = findViewById(R.id.updateEmail);
        // after this activity start, get the value that have been passed to this intent
        Intent intent = getIntent();
        email.setText(intent.getStringExtra("Email"));

        updateEmail.setOnClickListener(v ->{
            UpdateEmail();
        });
    }

    public void UpdateEmail() {

        userID = user.getUid();
        // check the email input is empty or not // this is update user email in firebase database
        if (!email.getText().toString().isEmpty()) {
            mAuth.fetchSignInMethodsForEmail(email.getText().toString())
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            List<String> signInMethods = task.getResult().getSignInMethods();
                            if (signInMethods != null && !signInMethods.isEmpty()) {
                                // Email already exists, display an error message
                                Toast.makeText(UpdateEmailActivity.this, "Email already registered.",
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                databaseReference.child("users").child(userID).child("email").setValue(email).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toasty.success(getApplicationContext(), "Email have updated", Toasty.LENGTH_SHORT).show();
                                    }

                                });
                                // this is update the firebase authentication email
                                user.updateEmail(email.getText().toString())
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toasty.success(getApplicationContext(), "Email have updated", Toasty.LENGTH_SHORT).show();
                                                }
                                            }
                                        });      // after complete of email update and can back to previous page
                                Intent intent = new Intent(getApplicationContext(),EditPersonal_Information.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);


                            }
                        }
                    });


        }else{
            Toasty.warning(getApplicationContext(),"Please fill the blank",Toasty.LENGTH_SHORT).show();
        }
    }
}