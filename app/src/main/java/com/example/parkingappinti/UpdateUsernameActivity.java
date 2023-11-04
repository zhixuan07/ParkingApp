package com.example.parkingappinti;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import es.dmoral.toasty.Toasty;

public class UpdateUsernameActivity extends AppCompatActivity {
    private EditText name;
    private Button updateName;
    private String userID;
    private final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_username_layout);
        Intent intent = getIntent();

        name = findViewById(R.id.name);
        name.setText(intent.getStringExtra("Name"));

        userID = user.getUid();
        updateName = findViewById(R.id.updateName);
        updateName.setOnClickListener(v -> {
            updateUsername();
        });
    }

    public void updateUsername(){
        // check name validation
        if(!name.getText().toString().isEmpty()){
            databaseReference.child("users").child(userID).child("name").setValue(name.getText().toString());
            Toasty.success(getApplicationContext(),"Information have been updated", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(),EditPersonal_Information.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

        }else {

            Toasty.warning(getApplicationContext(),"Please fill the name", Toast.LENGTH_SHORT).show();
        }


    }
}