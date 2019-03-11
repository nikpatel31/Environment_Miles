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
public class rankfragment extends Fragment {

    private RecyclerView blog_list_view_rank;
    private List<blogrank>rank_list;
    private DocumentSnapshot lastVisible;
    private FirebaseAuth mAuth;

    private FirebaseFirestore firebaseFirestore;
    private rankrecycleradapter rankrecycleradapter1;


    public rankfragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_rankfragment, container, false);

        rank_list=new ArrayList<>();
        blog_list_view_rank=view.findViewById(R.id.blog_list_view_rank);
        rankrecycleradapter1 = new rankrecycleradapter(rank_list);

        blog_list_view_rank.setLayoutManager(new LinearLayoutManager(getActivity()));
        blog_list_view_rank.setAdapter(rankrecycleradapter1);
        mAuth=FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser() != null) {

            firebaseFirestore = FirebaseFirestore.getInstance();

            blog_list_view_rank.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    boolean reachedbottom = !recyclerView.canScrollVertically(1);
                    if (reachedbottom) {
                        Toast.makeText(container.getContext(), "Loading More", Toast.LENGTH_SHORT).show();

                        loadmorerank();
                    }
                }
            });


            Query firstquery = firebaseFirestore.collection("Users").orderBy("points", Query.Direction.DESCENDING).limit(10);
            firstquery.addSnapshotListener(getActivity(), new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                    lastVisible = queryDocumentSnapshots.getDocuments()
                            .get(queryDocumentSnapshots.size() - 1);

                    for (DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()) {
                        if (doc.getType() == DocumentChange.Type.ADDED) {

                            blogrank blogrank1 = doc.getDocument().toObject(blogrank.class);
                            rank_list.add(blogrank1);
                            rankrecycleradapter1.notifyDataSetChanged();

                        }
                    }
                }
            });
        }
        return view;

    }

    public void loadmorerank(){


        Query nextquery = firebaseFirestore.collection("Users")
                .orderBy("points",Query.Direction.DESCENDING)
                .startAfter(lastVisible)
                .limit(10);

        nextquery.addSnapshotListener(getActivity(),new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                if (!queryDocumentSnapshots.isEmpty()) {


                    lastVisible = queryDocumentSnapshots.getDocuments()
                            .get(queryDocumentSnapshots.size() - 1);

                    for (DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()) {
                        if (doc.getType() == DocumentChange.Type.ADDED) {

                            blogrank blogrank1 = doc.getDocument().toObject(blogrank.class);
                            rank_list.add(blogrank1);
                            rankrecycleradapter1.notifyDataSetChanged();

                        }
                    }
                }
            }
        });


    }

}
