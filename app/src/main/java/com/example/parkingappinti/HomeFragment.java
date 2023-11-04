package com.example.parkingappinti;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    Button registerVehicle, searchVehicle,callAdmin,reportVehicle;
    BottomNavigationView bottomNavigationView;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        return inflater.inflate(R.layout.fragment_home, container, false);
    }
    @Override
    public void  onViewCreated(@NonNull View view, @NonNull Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bottomNavigationView = view.findViewById(R.id.bottom_navigation_view);
        registerVehicle = view.findViewById(R.id.registerVehicle);
        searchVehicle = view.findViewById(R.id.search_Vehicle);
        reportVehicle = view.findViewById(R.id.reportVehicle);
        callAdmin = view.findViewById(R.id.callAdmin);

        VehicleFragment vehicleFragment = new VehicleFragment();
        ReportFragment reportFragment = new ReportFragment();
        SearchVehicleFragment searchVehicleFragment = new SearchVehicleFragment();
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();

        registerVehicle.setOnClickListener(v ->{
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.frame_layout, vehicleFragment)
                    .addToBackStack(null)
                    .commit();

        });
        searchVehicle.setOnClickListener(v -> {
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.frame_layout, reportFragment)
                    .addToBackStack(null)
                    .commit();
        });
        reportVehicle.setOnClickListener(v -> {
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.frame_layout, searchVehicleFragment)
                    .addToBackStack(null)
                    .commit();
        });
    }
}