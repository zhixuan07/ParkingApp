package com.example.parkingappinti;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class VehicleOwnerInfoActivity extends AppCompatActivity {
    TextView name,contact;
    Button call,notify,report;
    static final int PERMISSION_CODE=100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vehicle_owner_inforamtion_layout);
        name = findViewById(R.id.name);
        contact = findViewById(R.id.contact);
        call = findViewById(R.id.callOwner);
        notify = findViewById(R.id.notifyOwner);
        report = findViewById(R.id.reportVehicle);

        Intent intent =getIntent();
        name.setText(intent.getStringExtra("Owner_name"));
        contact.setText(intent.getStringExtra("Owner_contact"));

        call.setOnClickListener(v -> {
            callOwner();
        });
    }
    public void callOwner(){

        if (ActivityCompat.checkSelfPermission(VehicleOwnerInfoActivity.this,Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:"+contact.getText().toString()));
            startActivity(callIntent);
        }else {
            // Request the CALL_PHONE permission if it's not granted
            ActivityCompat.requestPermissions(VehicleOwnerInfoActivity.this, new String[]{Manifest.permission.CALL_PHONE},1);
        }
    }
}