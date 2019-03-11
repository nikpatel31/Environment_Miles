package com.example.environment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class redeemfragment extends Fragment {

    RecyclerView recyclerView;
    redeemproductadapter productadapter;
    List<redeemlistview> productlist;

    public redeemfragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_redeemfragment, container, false);
        // Inflate the layout for this fragment

        productlist = new ArrayList<>();
        recyclerView=(RecyclerView)view.findViewById(R.id.redeemrecyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        productlist.add(
                new redeemlistview(
                        "Paytm",
                        "Paytm is an Indian e-commerce payment system and digital wallet company, based out of NOIDA SEZ, India.",
                        R.drawable.ic_launcher_background));

        productlist.add(
                new redeemlistview(
                        "Pay And Park",
                        "A person can pay for the Pay and Park via the points obtain through this app",
                        R.drawable.ic_launcher_foreground));
        productlist.add(
                new redeemlistview(
                        "Swiggy",
                        "Using Swiggy, you can order food & beverages online from restaurants near & around you. We deliver food from your neighborhood local joints",
                        R.drawable.ic_launcher_foreground));
        productlist.add(
                new redeemlistview(
                        "UBER",
                        "Uber is a transportation network company headquartered in San Francisco, California. Uber offers services including peer-to-peer ridesharing, taxi cab hailing, food delivery, and a bicycle-sharing system. ",
                        R.drawable.ic_launcher_background));

        productadapter = new redeemproductadapter(getActivity(),productlist);
        recyclerView.setAdapter(productadapter);


        return view;

    }

}
