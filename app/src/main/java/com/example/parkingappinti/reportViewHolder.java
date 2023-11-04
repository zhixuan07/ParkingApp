package com.example.parkingappinti;


import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class reportViewHolder extends RecyclerView.ViewHolder {
    private static TextView plate;
    private static TextView issue;
    private  static TextView date;

    public reportViewHolder(@NonNull View itemView) {
        super(itemView);
        plate = itemView.findViewById(R.id.plate_number);
        issue= itemView.findViewById(R.id.issue);
        date = itemView.findViewById(R.id.report_date);
    }

    public static void bind(Report report) {
        plate.setText(report.getPlate_number());
        issue.setText(report.getIssue());
        date.setText(report.getDate());
    }
}