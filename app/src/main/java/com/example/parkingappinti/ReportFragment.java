package com.example.parkingappinti;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReportFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReportFragment extends Fragment implements ReportDialog.DialogFragmentListener{
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String userID;
    FloatingActionButton fab;

    UUID randomUUID = UUID.randomUUID();
    String uuid = randomUUID.toString();
    Date currentDate = new Date();
    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy ");
    String formattedDate = sdf.format(currentDate);

    private FirebaseRecyclerAdapter<Report,reportViewHolder> adapter;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ReportFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ReportFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ReportFragment newInstance(String param1, String param2) {
        ReportFragment fragment = new ReportFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_report, container, false);

    }
    @Override
    public void  onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view,savedInstanceState);
        RecyclerView recyclerView = view.findViewById(R.id.report_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        userID = user.getUid();
        // retrieve the list of report from firebase and dump into recycle view
        FirebaseRecyclerOptions<Report> options = new FirebaseRecyclerOptions.Builder<Report>()
                .setQuery(databaseReference.child("Report").orderByChild("user_id").equalTo(userID), Report.class)
                .build();
        adapter = new FirebaseRecyclerAdapter<Report, reportViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull reportViewHolder holder, int position, @NonNull Report model) {
                holder.bind(model);
            }

            @NonNull
            @Override
            public reportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                // create the report card view bind with the recycle list to
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.report_card, parent, false);
                return new reportViewHolder(view);
            }
        };
        recyclerView.setAdapter(adapter);
        // floating action button to open report dialog
        fab = view.findViewById(R.id.fab);
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        fab.setOnClickListener(v -> {
            ReportDialog reportDialog = new ReportDialog();
            reportDialog.setListener(this);
            reportDialog.show(fragmentManager,"Report");

        });

    }
    public void submitReport(String plate_number,String issue,String issue_details){
        // insert the report information to firebase
        userID =  user.getUid();
        Report report = new Report(plate_number,issue,issue_details,userID,formattedDate);
        databaseReference.child("Report").child(uuid).setValue(report);
        Toasty.success(getContext(),"Report Submitted",Toasty.LENGTH_SHORT).show();


    }
    private  void validateVehicle(String plate_number){

    }
    // always listen to the adapter whether have new data or data is deleted
    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public void applyTexts(String plate, String issue, String issue_details) {
        // from the report dialog receive the inputs and apply on the logic
        databaseReference.child("Vehicle").child(plate).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    submitReport(plate,issue,issue_details);
                }else{
                    Toasty.error(getActivity(),"Vehicle not registered in system",Toasty.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}