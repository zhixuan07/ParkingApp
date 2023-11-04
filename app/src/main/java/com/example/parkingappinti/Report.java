package com.example.parkingappinti;

public class Report {
    String plate_number;
    String issue;
    String issue_details;
    String user_id;
    String date;


    public Report (){

    }

    public Report(String plate_number,String issue,String issue_details,String user_id,String date){
        this.plate_number = plate_number;
        this.issue = issue;
        this.issue_details = issue_details;
        this.user_id = user_id;
        this.date = date;
    }



    public String getPlate_number() {
        return plate_number;
    }

    public String getIssue() {
        return issue;
    }

    public String getIssue_details() {
        return issue_details;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getDate() {
        return date;
    }
}