package com.example.parkingappinti;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import es.dmoral.toasty.Toasty;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AccountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccountFragment extends Fragment implements VerifyPasswordDialog.DialogFragmentListener {
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    String userID ;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private TextView userName,useremail,contact,logout;
    NavigationView navigationView;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AccountFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AccountFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AccountFragment newInstance(String param1, String param2) {
        AccountFragment fragment = new AccountFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account, container, false);
    }
    @Override
    public void  onViewCreated(@NonNull View view,@NonNull Bundle savedInstanceState){
        super.onViewCreated(view,savedInstanceState);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        // Get user id from firebase
        userID = user.getUid();

        logout = view.findViewById(R.id.logout);
        logout.setOnClickListener(v -> {
            Signout();
        });
        ReportFragment reportFragment = new ReportFragment();
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        navigationView = view.findViewById(R.id.profile_navigation);
        // Set navigation item listener to navigate the user when click on it
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            switch (id) {
                case R.id.nav_account:
                    // open password dialog to verify the password from user if access account information
                    VerifyPasswordDialog dialog = new VerifyPasswordDialog();
                    dialog.setListener(this);
                    dialog.show(getChildFragmentManager(), "Password");

                    return true;

                case R.id.nav_report:
                    // open the fragment
                    fragmentManager
                            .beginTransaction()
                            .replace(R.id.frame_layout, reportFragment)

                            .addToBackStack(null)
                            .commit();
                    return true;
            }
            return false;
        });



    }



    public void Signout(){
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(getActivity(),MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public  void verifyPassword(String password){
        if(user!=null){
            // get email from current firebase instance
            String email = user.getEmail();
            //check the password validation
            mAuth.signInWithEmailAndPassword(email,password)
                    .addOnCompleteListener((getActivity()), task -> {
                        if(task.isSuccessful()){
                            openEditPersonalInformation();
                        }else{
                            Toasty.warning(getActivity(),"Password incorrect",Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
    private void openEditPersonalInformation() {
        startActivity(new Intent(getActivity(),EditPersonal_Information.class));
    }
    @Override
    public void apply(String password) {

    }

    @Override
    public void applyTexts(String password) {

        verifyPassword(password);
    }
}