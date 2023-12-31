package com.example.parkingappinti;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import es.dmoral.toasty.Toasty;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchVehicleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchVehicleFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    Button searchOwner;
    EditText plateNumber;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SearchVehicleFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchVehicleFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchVehicleFragment newInstance(String param1, String param2) {
        SearchVehicleFragment fragment = new SearchVehicleFragment();
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
        return inflater.inflate(R.layout.fragment_search_vehicle, container, false);
    }
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view,savedInstanceState);
        searchOwner = view.findViewById(R.id.searchProfile);
        plateNumber = view.findViewById(R.id.plate_number);
        searchOwner.setOnClickListener(v -> {
            searchOwner(plateNumber.getText().toString());
        });

    }
    public void searchOwner(String plate)
    {
        DatabaseReference vehiclesRef = FirebaseDatabase.getInstance().getReference();

        Query query = vehiclesRef.child("Vehicle").child(plate);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){


                    String userID = snapshot.child("userID").getValue(String.class);
                    System.out.println(userID);
                    databaseReference.child("users").child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                String name = snapshot.child("name").getValue(String.class);
                                String contact = snapshot.child("phone").getValue(String.class);
                                Intent intent = new Intent(getActivity(),VehicleOwnerInfoActivity.class);
                                intent.putExtra("Owner_name",name);
                                intent.putExtra("Owner_contact",contact);
                                startActivity(intent);
                            }else{
                                Toasty.error(getActivity(),"Error", Toasty.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }else {
                    Toasty.error(getActivity(),"Error,Vehicle not found", Toasty.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}