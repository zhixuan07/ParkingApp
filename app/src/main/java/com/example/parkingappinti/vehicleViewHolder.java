package com.example.parkingappinti;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class vehicleViewHolder extends RecyclerView.ViewHolder {
    private static TextView plate;
    private static TextView brand;
    private  static ImageView deleteButton;

    public vehicleViewHolder(@NonNull View itemView) {
        super(itemView);
        plate = itemView.findViewById(R.id.plate_number);
        brand = itemView.findViewById(R.id.brand_text);
        deleteButton = itemView.findViewById(R.id.delete_vehicle);
    }

    public static void bind(Vehicle vehicle) {
        plate.setText(vehicle.getPlate());
        brand.setText(vehicle.getBrand());
    }
}