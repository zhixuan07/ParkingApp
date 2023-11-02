package com.example.parkingappinti;


import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditPersonal_Information extends AppCompatActivity {
    // get the firebase database reference that has connected
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    private String userID;
    private TextView username,useremail,usercontact;
    private LinearLayout userLayout,emailLayout;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_personal_information);
        //get the current user uuid for later use
        userID = user.getUid();

        username = findViewById(R.id.name);
        useremail = findViewById(R.id.email);
        usercontact = findViewById(R.id.phone);

        userLayout = findViewById(R.id.updateName);
        emailLayout = findViewById(R.id.updateEmail);
        // get the current user information to display
        databaseReference.child("users").child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    // pass the user information to variable and set the text to display
                    String name = snapshot.child("name").getValue(String.class);
                    String email = snapshot.child("email").getValue(String.class);
                    String contact = snapshot.child("phone").getValue(String.class);
                    username.setText(name);
                    useremail.setText(email);
                    usercontact.setText(contact);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        // set on click listener when user click on the layout
        userLayout.setOnClickListener(v -> {
            UpdateName();
        });

        emailLayout.setOnClickListener(v -> {
            UpdateEmail();
        });
    }
    public void UpdateName(){
        // declare the intent to start new activity
        Intent intent = new Intent(getApplicationContext(),UpdateUsernameActivity.class);
        // pass the username to new intent activity so can use this value on that activity
        intent.putExtra("Name",username.getText().toString());
        startActivity(intent);
    }
    public void UpdateEmail(){
        Intent intent = new Intent(getApplicationContext(),UpdateEmailActivity.class);
        // pass the email to new intent activity so can use this value on that activity
        intent.putExtra("Email",useremail.getText().toString());
        startActivity(intent);
    }
}