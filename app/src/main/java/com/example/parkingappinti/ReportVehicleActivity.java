package com.example.parkingappinti;


import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import es.dmoral.toasty.Toasty;

public class ReportVehicleActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    String[] items = {"Non-responsive","Illegal Parking","Reckless Driving","Vehicle Damage","Other"};
    String issue;
    EditText plate_number , issue_details;
    String userID;
    Button submit;
    UUID randomUUID = UUID.randomUUID();
    String uuid = randomUUID.toString();
    Date currentDate = new Date();
    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy ");
    String formattedDate = sdf.format(currentDate);
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_vehicle_layout);
        Spinner spinner = findViewById(R.id.spinner);
        ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,items);
        stringArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(stringArrayAdapter);
        spinner.setOnItemSelectedListener(this);
        plate_number = findViewById(R.id.plate_number);
        issue_details = findViewById(R.id.issue_details);


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selectedValue = items[position];
        issue = selectedValue;

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void submitReport(){

        userID =  user.getUid();
        Report report = new Report(plate_number.getText().toString(),issue,issue_details.getText().toString(),userID,formattedDate);
        databaseReference.child("Report").child(uuid).setValue(report);
        Toasty.success(getApplicationContext(),"Report Submitted",Toasty.LENGTH_SHORT).show();


    }
    private  void validateVehicle(String plate_number){
        databaseReference.child("Vehicle").child(plate_number).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    submitReport();
                }else{
                    Toasty.error(getApplicationContext(),"Vehicle not registered in system",Toasty.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}