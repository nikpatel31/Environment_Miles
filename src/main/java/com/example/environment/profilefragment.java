package com.example.environment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;


/**
 * A simple {@link Fragment} subclass.

 */

public class profilefragment extends Fragment {

    private RecyclerView post_list_view;
    private List<blogpost> blog_list;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth mAuth;
    private DocumentSnapshot lastvisible;
    private boolean isfirstpagefirstview = true;

    private blogrecycleradapter blogrecycleradapters;


    public profilefragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_profilefragment, container, false);
        // Inflate the layout for this fragment
        blog_list = new ArrayList<>();
        post_list_view = view.findViewById(R.id.post_list_view);

        blogrecycleradapters= new blogrecycleradapter(blog_list);
        post_list_view.setLayoutManager(new LinearLayoutManager(getActivity()));
        post_list_view.setAdapter(blogrecycleradapters);

        firebaseFirestore = FirebaseFirestore.getInstance();
        mAuth=FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser() != null) {


            //<<IMPORTANT >>   starting retrieving the data from here so we can use this method to get data to form the ranklist
            post_list_view.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    Boolean reachedbottom = !recyclerView.canScrollVertically(-1);
                    if(reachedbottom){

                        Toast.makeText(container.getContext(),"Loading More",Toast.LENGTH_SHORT).show();
                        loadmorepost();
                    }

                }
            });

            Query firstquery = firebaseFirestore.collection("City_Ahmedabad")
                    .orderBy("timestamp",Query.Direction.DESCENDING);


            final String currentuser = mAuth.getCurrentUser().getUid();
            firstquery.addSnapshotListener(getActivity(),new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                    if(isfirstpagefirstview) {

                        lastvisible = queryDocumentSnapshots.getDocuments().get(queryDocumentSnapshots.size() - 1);
                    }

                    for (DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()) {

                        if (doc.getType() == DocumentChange.Type.ADDED) {

                            blogpost blogposts = doc.getDocument().toObject(blogpost.class);

                            if(isfirstpagefirstview) {
                                blog_list.add(blogposts);
                            }
                            else{
                                blog_list.add(0,blogposts);
                            }
                            blogrecycleradapters.notifyDataSetChanged();
                        }
                    }
                    isfirstpagefirstview =false;
                }
            });
        }



        return view;
    }

    public void loadmorepost(){
        Query nextquery =firebaseFirestore.collection("City_Ahmedabad")
                .orderBy("timestamp",Query.Direction.DESCENDING)
                .startAfter(lastvisible)
                .limit(5);

        nextquery.addSnapshotListener(getActivity(),new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {


                if (!queryDocumentSnapshots.isEmpty()) {
                    lastvisible = queryDocumentSnapshots.getDocuments().get(queryDocumentSnapshots.size() - 1);
                    for (DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()) {

                        if (doc.getType() == DocumentChange.Type.ADDED) {

                            blogpost blogposts = doc.getDocument().toObject(blogpost.class);
                            blog_list.add(blogposts);

                            blogrecycleradapters.notifyDataSetChanged();
                        }
                    }
                }
            }
        });

    }

}
