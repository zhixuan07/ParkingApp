package com.example.parkingappinti;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VehicleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VehicleFragment extends Fragment implements DialogFragment.DialogFragmentListener{


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    String userID;
    FloatingActionButton fab;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private FirebaseRecyclerAdapter<Vehicle, vehicleViewHolder> adapter;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public VehicleFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment VehicleFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static VehicleFragment newInstance(String param1, String param2) {
        VehicleFragment fragment = new VehicleFragment();
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
        return inflater.inflate(R.layout.fragment_vehicle, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view,savedInstanceState);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        // Get user id from firebase
        userID = user.getUid();
        // Display the recycle layout of list item
        RecyclerView recyclerView = view.findViewById(R.id.vehicle_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        // build the recycle list data by passing query of registered vehicle based on current user id
        FirebaseRecyclerOptions<Vehicle> options = new FirebaseRecyclerOptions.Builder<Vehicle>()
                .setQuery(databaseReference.child("Vehicle").orderByChild("userID").equalTo(userID), Vehicle.class)
                .build();
        adapter = new FirebaseRecyclerAdapter<Vehicle, vehicleViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull vehicleViewHolder holder, int position, @NonNull Vehicle model) {
                holder.bind(model);
                //set on click listener on the delete button
                holder.itemView.findViewById(R.id.delete_vehicle).setOnClickListener(view -> {
                    String plate = model.getPlate();
                    showAlertDialogConfirmation(plate);
                });
            }
            @NonNull
            @Override
            public vehicleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                // display the vehicle information in the card
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vehicle_card, parent, false);
                return new vehicleViewHolder(view);
            }
        };

        // Always notify adapter when data changed
        recyclerView.setAdapter(adapter);

        fab = view.findViewById(R.id.fab);
        userID =  user.getUid();
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        //Show Floating action button to open the dialog
        fab.setOnClickListener(v -> {
            DialogFragment dialog = new DialogFragment();
            dialog.setListener(this);
            dialog.show(fragmentManager,"Dialog");
        });


    }

    @Override
    public void apply(String plate, String brand) {

    }

    @Override
    public void applyTexts(String plate, String brand) {
        // get the value from dialog fragment and pass to the function
        addVehicle(plate,brand);
    }
    // Function add vehicle to database based on userID

    private void addVehicle(String plate,String brand){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();
        DatabaseReference vehicleRef = databaseReference.child("Vehicle");
        vehicleRef.child(plate).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    Toast.makeText(getContext(),"Plate number already exists",Toast.LENGTH_SHORT).show();
                }else{
                    Vehicle vehicle = new Vehicle(plate,userID,brand);
                    vehicleRef.child(plate).setValue(vehicle);
                    Toast.makeText(getContext(),"Plate number added",Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void showAlertDialogConfirmation(String plateNumber){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Delete Vehicle");
        builder.setMessage("Are you sure want to delete?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                deleteVehicleFromDatabase(plateNumber);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.show();
    }

    private void deleteVehicleFromDatabase(String plate) {
        DatabaseReference vehicleRef = databaseReference.child("Vehicle").child(plate);
        vehicleRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getContext(),"Vehicle deleted",Toast.LENGTH_SHORT).show();

                }else {
                    Toast.makeText(getContext(),"Failed to deleted",Toast.LENGTH_SHORT).show();

                }

            }
        });
    }

    // start listening on adapter data status
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

}