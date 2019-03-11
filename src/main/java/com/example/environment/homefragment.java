package com.example.environment;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


/**
 * A simple {@link Fragment} subclass.
 */
public class homefragment extends Fragment {

    private CardView waterissuebutton;
    private CardView landissuebutton;
    private CardView natureissuebutton;
    private CardView sanitationissuebutton;
    private CardView infrastructureissuebutton;
    private CardView miscissuebutton;
    private String userid;
    private StorageReference storageReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private CardView userpointscard;
    private TextView usereditpoint;


    public homefragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_homefragment, container, false);


//
        firebaseAuth = FirebaseAuth.getInstance();

        userid = firebaseAuth.getCurrentUser().getUid();

        firebaseFirestore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        //hi this is me

        waterissuebutton = (CardView) view.findViewById(R.id.waterissuebutton);
        landissuebutton = (CardView) view.findViewById(R.id.landissuebutton);
        natureissuebutton = (CardView) view.findViewById(R.id.natureissuebutton);
        sanitationissuebutton = (CardView) view.findViewById(R.id.sanitationissuebutton);
        infrastructureissuebutton = (CardView) view.findViewById(R.id.infrastructureissuebutton);
        miscissuebutton = (CardView) view.findViewById(R.id.miscissuebutton);
        userpointscard = (CardView) view.findViewById(R.id.userpointscard);
        usereditpoint = (TextView) view.findViewById(R.id.usereditpoints);


        //

        firebaseFirestore.collection("Users").document(userid)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()) {

                    if (task.getResult().exists()) {
                        String name = task.getResult().getString("name");

                        String points = task.getResult().getString("points");


                        usereditpoint.setText(points);
                    }
                }
            }
        });


        //this is other edit


        waterissuebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), newwaterissue.class);
                startActivity(intent);
            }
        });

        landissuebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), newlandissue.class);
                startActivity(intent);
            }
        });

        natureissuebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), newnatureissue.class);
                startActivity(intent);
            }
        });
        sanitationissuebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), newsanitationissue.class);
                startActivity(intent);
            }
        });
        infrastructureissuebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), newinfrastructureissue.class);
                startActivity(intent);
            }
        });
        miscissuebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), newmiscissue.class);
                startActivity(intent);
            }
        });
        return view;
    }
}

